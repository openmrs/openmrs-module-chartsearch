/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocumentList;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.SolrSingleton;
import org.openmrs.module.chartsearch.solr.SolrUtils;
import org.openmrs.module.chartsearch.solr.nonPatient.CustomFieldsToAndFromSchema;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataIndexer;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataSearcher;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;
import org.openmrs.util.OpenmrsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides read, write, create, save, delete, indexing and searching SearchProject data et-cetera.
 */
public class SearchProjectAccess {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchProjectAccess.class);
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	/**
	 * Creates and saves a new SearchProject into the database and returns its's uuid, it must NOT
	 * have an existing name
	 * 
	 * @param request
	 * @return
	 */
	public String createAndSaveANewSearchProjectOrReturnPrevious(HttpServletRequest request) {
		String projectName = request.getParameter("projectName");
		String projectDescription = request.getParameter("projectDesc");
		String mysqlQuery = request.getParameter("mysqlQuery");
		String columns = request.getParameter("columns");
		String databaseName = request.getParameter("databaseName");
		String databaseUser = request.getParameter("databaseUser");
		String databaseUserPassword = request.getParameter("databaseUserPassword");
		String databaseServer = request.getParameter("databaseServer");
		String databaseManager = request.getParameter("databaseManager");
		String databasePortNumber = request.getParameter("databasePortNumber");
		String databaseType = request.getParameter("databaseType");
		
		NonPatientDataIndexer indexer = new NonPatientDataIndexer();
		SearchProject project;
		String existingColumns = chartSearchService.getAllColumnNamesFromAllProjectsSeperatedByCommaAndSpace();
		String projectUuid = null;
		boolean isADefaultDb = StringUtils.isBlank(databaseName) && "default".equals(databaseType);
		boolean isImportedDB = StringUtils.isNotBlank(databaseName) && "imported".equals(databaseType);
		
		if (isADefaultDb || isImportedDB) {//is openmrs or imported database
			project = checkNonExistingColumnsAndCreateBasicSearchProject(projectName, projectDescription, mysqlQuery,
			    columns, indexer, existingColumns);
			project.setDatabaseType(databaseType);
			if (isImportedDB) {
				project.setDatabase(databaseName);
			} else {
				project.setDatabase(OpenmrsConstants.DATABASE_NAME);
			}
		} else {
			project = checkNonExistingColumnsAndCreateBasicSearchProject(projectName, projectDescription, mysqlQuery,
			    columns, indexer, existingColumns);
			project.setDatabaseType("remote");
			project.setDatabase(databaseName);
			project.setDatabaseUser(databaseUser);
			project.setDatabaseUSerPassword(databaseUserPassword);
			project.setServerName(databaseServer);
			project.setDbms(databaseManager);
			project.setPortNumber(databasePortNumber);
		}
		List<SearchProject> allExistingProjects = chartSearchService.getAllSearchProjects();
		String allExistingProjectNames = "Existing Project(s): ";
		for (int i = 0; i < allExistingProjects.size(); i++) {
			if (i != allExistingProjects.size() - 1) {
				allExistingProjectNames += allExistingProjects.get(i).getProjectName() + ", ";
			} else {
				allExistingProjectNames += allExistingProjects.get(i).getProjectName();
			}
		}
		logger.info(allExistingProjectNames);
		if (project != null && !allExistingProjectNames.contains(project.getProjectName())) {//project name must not exist or be null to save
			//saves the new search project into the chartsearch database
			chartSearchService.saveSearchProject(project);
			projectUuid = project.getUuid();
		}
		
		return projectUuid;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection indexSearchProjectData(String projectUuid, boolean isAnonOpenmrsDatabase) {
		SearchProject project = chartSearchService.getSearchProjectByUuid(projectUuid);
		NonPatientDataIndexer indexer = new NonPatientDataIndexer();
		List<String> fields = indexer.removeSpacesAndSplitLineUsingComma(project.getColumnNames());
		boolean fieldsExistInSchemaAlready = project.fieldsExistInSchema();
		String copyFieldsEntries = CustomFieldsToAndFromSchema.generateWellWrittenCopyFieldEntries(fields,
		    chartSearchService, fieldsExistInSchemaAlready);
		String fieldsEntries = CustomFieldsToAndFromSchema.generateWellWrittenFieldEntries(fields, "text_general", true,
		    true, false, chartSearchService, fieldsExistInSchemaAlready);
		String currentSchemaLocation = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "collection1"
		        + File.separator + "conf" + File.separator + "schema.xml";
		String newSchemaLocation = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "schema.xml";
		boolean fieldsExistInSchema = true;
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		
		if (!project.fieldsExistInSchema()) {
			fieldsExistInSchema = false;
		}
		if (!StringUtils.isBlank(fieldsEntries) && !StringUtils.isBlank(copyFieldsEntries) && !fieldsExistInSchema) {
			CustomFieldsToAndFromSchema.readSchemaFileLineByLineAndWritNewFieldEntries(currentSchemaLocation,
			    newSchemaLocation, fieldsEntries, copyFieldsEntries, solrServer, chartSearchService, project);
			project.setFieldsExistInSchema(true);//now solr fields have been added to schema file
			chartSearchService.saveSearchProject(project);
		}
		Collection docs = indexer
		        .generateDocumentsAndAddFieldsAndCommitToSolr(project.getProjectId(), isAnonOpenmrsDatabase);
		if (null != docs) {
			project.setInitiallyIndexed(true);
			chartSearchService.saveSearchProject(project);
		}
		return docs;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection onlyUpdateIndexOfSearchProject(String searchProject) {
		Integer projectId = fetchProjectIdThatMatchesName(searchProject);
		NonPatientDataIndexer indexer = new NonPatientDataIndexer();
		SearchProject project = chartSearchService.getSearchProject(projectId);
		Collection docs = indexer.generateDocumentsAndAddFieldsAndCommitToSolr(project.getProjectId(),
		    !OpenmrsConstants.DATABASE_NAME.equals(project.getDatabase()) && !"default".equals(project.getDatabaseType()));
		return docs;
	}
	
	private SearchProject checkNonExistingColumnsAndCreateBasicSearchProject(String projectName, String projectDescription,
	                                                                         String mysqlQuery, String newColumns,
	                                                                         NonPatientDataIndexer indexer,
	                                                                         String existingColumns) {
		List<String> newColumnNamesList = new ArrayList<String>();
		List<String> existingColumnNamesList = new ArrayList<String>();
		List<String> nonExistingColumns = new ArrayList<String>();
		SearchProject project = null;
		List<SearchProject> existingProjects = chartSearchService.getAllSearchProjects();
		
		for (int k = 0; k < existingProjects.size(); k++) {
			if (StringUtils.isNotEmpty(projectName) && StringUtils.isNotEmpty(mysqlQuery)
			        && StringUtils.isNotEmpty(newColumns) && !existingProjects.get(k).getProjectName().equals(projectName)) {
				newColumnNamesList.addAll(indexer.removeSpacesAndSplitLineUsingComma(newColumns));
				existingColumnNamesList.addAll(indexer.removeSpacesAndSplitLineUsingComma(existingColumns));
				
				for (int i = 0; i < newColumnNamesList.size(); i++) {
					for (int j = 0; j < existingColumnNamesList.size(); j++) {
						if (!newColumnNamesList.get(i).equals(existingColumnNamesList.get(j))
						        && !nonExistingColumns.contains(newColumnNamesList.get(i))) {
							nonExistingColumns.add(newColumnNamesList.get(i));
						}
						
					}
				}
				project = new SearchProject(projectName, mysqlQuery, nonExistingColumns, OpenmrsConstants.DATABASE_NAME);
				project.setProjectDescription(projectDescription);
				
			}
		}
		return project;
	}
	
	public List<String> getExistingSearchProjectNames() {
		List<String> names = new ArrayList<String>();
		List<SearchProject> existingSearchProjects = chartSearchService.getAllSearchProjects();
		for (int i = 0; i < existingSearchProjects.size(); i++) {
			names.add(existingSearchProjects.get(i).getProjectName());
		}
		return names;
	}
	
	public JSONObject fetchSearchProjectDetails(String selectedProjectName) {
		JSONObject projectJson = new JSONObject();
		
		List<SearchProject> existingSearchProjects = chartSearchService.getAllSearchProjects();
		for (int i = 0; i < existingSearchProjects.size(); i++) {
			SearchProject project = existingSearchProjects.get(i);
			String projectName = project.getProjectName();
			
			if (projectName.equals(selectedProjectName)) {
				projectJson.put("projectId", project.getProjectId());
				projectJson.put("projectName", project.getProjectName());
				projectJson.put("projectDescription", project.getProjectDescription());
				projectJson.put("projectUuid", project.getUuid());
				projectJson.put("projectDB", project.getDatabase());
				projectJson.put("projectFieldsExistInSchema", project.fieldsExistInSchema());
				projectJson.put("projectDatabaseQuery", project.getSqlQuery());
				projectJson.put("projectSolrFields", project.getColumnNames());
				projectJson.put("projectDBUser", project.getDatabaseUser());
				projectJson.put("projectDBUserPassword", project.getDatabaseUSerPassword());
				projectJson.put("projectServerName", project.getServerName());
				projectJson.put("projectDBPortNumber", project.getPortNumber());
				projectJson.put("projectDBManager", project.getDbms());
				projectJson.put("projectDBType", project.getDatabaseType());
			}
		}
		return projectJson;
	}
	
	@SuppressWarnings("rawtypes")
	public List getAllExistingDatabases() {
		List listDAtabases = chartSearchService.getAllExistingDabases();
		return listDAtabases;
	}
	
	public boolean createAndDumpToNonExistingDatabase(String dbName, String sqlSourcePath) {
		return chartSearchService.createAndDumpToNonExistingDatabase(dbName, sqlSourcePath);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getPersonalDatabases() {
		List allDbs = getAllExistingDatabases();
		List personalDBs = new ArrayList<String>();
		
		for (int i = 0; i < allDbs.size(); i++) {
			String curDb = (String) allDbs.get(i);
			
			if (!curDb.equals("information_schema") && !curDb.equals("performance_schema") && !curDb.equals("mysql")
			        && !curDb.equals(OpenmrsConstants.DATABASE_NAME)) {
				personalDBs.add(curDb);
			}
		}
		return personalDBs;
	}
	
	public JSONObject getTablesAndColumnsOfDatabase(String databaseName) {
		return chartSearchService.getAllTablesAndColumnNamesOfADatabase(databaseName);
	}
	
	public boolean deleteImportedDatabase(String dbName) {
		return chartSearchService.deleteImportedDatabase(dbName);
	}
	
	public String getAllFieldsSetInSchemaByDefault() {
		return chartSearchService.getAllFieldsSetInSchemaByDefault();
	}
	
	public List<String> getInitiallyIndexedSearchProjectNames() {
		List<String> names = new ArrayList<String>();
		List<SearchProject> allSProjects = chartSearchService.getAllSearchProjects();
		
		for (int i = 0; i < allSProjects.size(); i++) {
			SearchProject sp = allSProjects.get(i);
			if (sp.wasInitiallyIndexed()) {
				names.add(sp.getProjectName());
			}
		}
		
		return names;
	}
	
	public SolrDocumentList searchNonPatientSpecificDataForAlreadyIndexed(String searchText, String selectedProject) {
		NonPatientDataSearcher searcher = new NonPatientDataSearcher();
		SolrDocumentList solrDocList = null;
		Integer projectId = fetchProjectIdThatMatchesName(selectedProject);
		
		if (null != projectId) {
			solrDocList = searcher.getNonPatientDocumentList(searchText, projectId);
		}
		return solrDocList;
	}
	
	private Integer fetchProjectIdThatMatchesName(String selectedProject) {
		List<SearchProject> allSProjs = chartSearchService.getAllSearchProjects();
		Integer projectId = null;
		SearchProject proj;
		
		for (int i = 0; i < allSProjs.size(); i++) {
			proj = allSProjs.get(i);
			if (selectedProject.equals(proj.getProjectName())) {
				projectId = proj.getProjectId();
			}
		}
		return projectId;
	}
	
	public String[] getAllFieldsOfASearchProject(String projectName) {
		Integer projectId = fetchProjectIdThatMatchesName(projectName);
		SearchProject project = chartSearchService.getSearchProject(projectId);
		NonPatientDataIndexer indexer = new NonPatientDataIndexer();
		String[] fields = null;
		
		if (null != project) {
			List<String> fieldsList = indexer.removeSpacesAndSplitLineUsingComma(project.getColumnNames());
			fields = new String[fieldsList.size()];
			fields = (String[]) fieldsList.toArray(fields);
		}
		
		return fields;
	}
	
	/**
	 * @param searchProjectName
	 */
	public boolean deleteSearchProjectTotally(String searchProjectName) {
		Integer pId = fetchProjectIdThatMatchesName(searchProjectName);
		SearchProject project = chartSearchService.getSearchProject(pId);
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		boolean deleted = false;
		
		chartSearchService.removeFieldsFromAlreadyExistingInSchema(project.getColumnNames());
		String pathToBackUpDir = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "backup";
		String pathToSchemaBackUp = pathToBackUpDir + File.separator + "schema.xml";
		File shemaBackup = new File(pathToSchemaBackUp);
		String pathToBackUpDirSchema = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "schema.xml";
		
		if (shemaBackup.exists() && project.fieldsExistInSchema()) {
			boolean fieldsRemoved = CustomFieldsToAndFromSchema.removeCustomAndCopyFieldsFromSchema(pathToSchemaBackUp,
			    pathToBackUpDirSchema, project, chartSearchService, solrServer);
			if (fieldsRemoved) {
				project.setFieldsExistInSchema(false);//useless after the next delete line is executed
				chartSearchService.deleteSearchProject(project);
				deleted = true;
			}
		}
		
		return deleted;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
