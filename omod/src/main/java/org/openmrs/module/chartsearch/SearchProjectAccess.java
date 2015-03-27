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

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.SolrUtils;
import org.openmrs.module.chartsearch.solr.nonPatient.AddCustomFieldsToSchema;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataIndexer;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;

/**
 * Provides read, write, create, indexing and searching it's data, save etc ability to a
 * SearchProject
 */
public class SearchProjectAccess {
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	/**
	 * Creates and saves a new SearchProject into the database and returns its's uuid
	 * 
	 * @param request
	 * @return
	 */
	public String createAndSaveANewSearchProject(HttpServletRequest request) {
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
		NonPatientDataIndexer indexer = new NonPatientDataIndexer();
		SearchProject project;
		String existingColumns = chartSearchService.getAllColumnNamesFromAllProjectsSeperatedByCommaAndSpace();
		
		if (StringUtils.isBlank(databaseName)) {//is openmrs database
			project = checkNonExistingColumnsAndCreateBasicSearchProject(projectName, projectDescription, mysqlQuery,
			    columns, indexer, existingColumns);
		} else {
			project = checkNonExistingColumnsAndCreateBasicSearchProject(projectName, projectDescription, mysqlQuery,
			    columns, indexer, existingColumns);
			project.setDatabase(databaseName);
			project.setDatabaseUser(databaseUser);
			project.setDatabaseUSerPassword(databaseUserPassword);
			project.setServerName(databaseServer);
			project.setDbms(databaseManager);
			project.setPortNumber(databasePortNumber);
		}
		if (project != null) {
			//saves the new search project into the chartsearch database
			chartSearchService.saveSearchProject(project);
			return project.getUuid();
		} else
			return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection indexSearchProjectData(String uuid, boolean isAnonOpenmrsDatabase) {
		SearchProject project = chartSearchService.getSearchProjectByUuid(uuid);
		NonPatientDataIndexer indexer = new NonPatientDataIndexer();
		List<String> fields = indexer.removeSpacesAndSplitLineUsingComma(project.getColumnNames());
		String copyFieldsEntries = AddCustomFieldsToSchema.generateWellWrittenCopyFieldEntries(fields);
		String fieldsEntries = AddCustomFieldsToSchema.generateAWellWrittenFieldEntry(fields, "text_general", true, true,
		    false);
		String currentSchemaLocation = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "collection1"
		        + File.separator + "conf" + File.separator + "schema.xml";
		String newSchemaLocation = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "new-schema.xml";
		
		OpenmrsUtil.getApplicationDataDirectory();
		AddCustomFieldsToSchema.readSchemaFileLineByLineAndWritNewFieldEntries(currentSchemaLocation, newSchemaLocation,
		    fieldsEntries, copyFieldsEntries);
		
		Collection docs = indexer
		        .generateDocumentsAndAddFieldsAndCommitToSolr(project.getProjectId(), isAnonOpenmrsDatabase);
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
		if (StringUtils.isNotEmpty(projectName) && StringUtils.isNotEmpty(mysqlQuery) && StringUtils.isNotEmpty(newColumns)) {
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
		return project;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
