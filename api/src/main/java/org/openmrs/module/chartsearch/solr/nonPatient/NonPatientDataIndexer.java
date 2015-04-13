/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr.nonPatient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.SolrSingleton;

public class NonPatientDataIndexer {//TODO may need to add and access this as a bean

	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	private AdministrationService adminService = Context.getAdministrationService();
	
	/**
	 * generates the solr documents from the data fetched from a database, adds it to SolrServer for
	 * indexing
	 * 
	 * @param solrServer
	 * @param projectId
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection generateDocumentsAndAddFieldsAndCommitToSolr(int projectId, boolean isAnonOpenmrsDatabase) {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		SearchProject project = chartSearchService.getSearchProject(projectId);
		String sql = project.getSqlQuery();
		String columnNamesWithCommas = project.getColumnNames();
		List<String> columnNamesFromCS = removeSpacesAndSplitLineUsingComma(columnNamesWithCommas);
		Collection docs = new ArrayList();
		
		if (!isAnonOpenmrsDatabase && "default".equals(project.getDatabaseType())) {
			List<List<Object>> rowsByColumns = adminService.executeSQL(sql, false);
			if (!rowsByColumns.isEmpty()) {
				for (int i = 0; i < rowsByColumns.size(); i++) {
					SolrInputDocument doc = new SolrInputDocument();
					addBasicFieldsToSolrDoc(project, i + 1, doc);
					List<Object> currentRow = rowsByColumns.get(i);//looks like:[1, Outpatient, Out-patient care setting, OUTPATIENT, 2013-12-27 00:00:00.0, null] 
					
					if (StringUtils.isNotBlank(columnNamesWithCommas) && !currentRow.isEmpty()) {
						//contains column names obtained from chart search module data etc
						for (int j = 0; j < currentRow.size();) {
							for (int k = 0; k < columnNamesFromCS.size(); k++) {
								if (j == k) {//this logically implies the column names match
									//obtained from running SQL query and not necessarily chart search module data
									Object currentColumnValue = currentRow.get(j);//TODO should be column value
									String currentColumnNameFromCS = columnNamesFromCS.get(k);
									
									if (null == currentColumnValue) {//works well, excluding fields whose values are blank
										currentColumnValue = "";
									} else {
										if (currentColumnValue.getClass().equals(int.class)
										        || currentColumnValue.getClass().equals(Integer.class)) {
											int currentColumnValueFromDB = (Integer) currentColumnValue;
											doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
										} else if (currentColumnValue.getClass().equals(String.class)) {
											String currentColumnValueFromDB = (String) currentColumnValue;
											doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
											
										} else if (currentColumnValue.getClass().equals(boolean.class)
										        || currentColumnValue.getClass().equals(Boolean.class)) {
											boolean currentColumnValueFromDB = (Boolean) currentColumnValue;
											doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
										} else {
											Object currentColumnValueFromDB = currentColumnValue;
											doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
										}
									}
								}
								j++;
							}
						}
					}
					docs.add(doc);
				}
			}
		} else {//Non OpenMRS database
			String dbName = project.getDatabase();
			if ("imported".equals(project.getDatabaseType())) {
				List rowResults = chartSearchService.getResultsFromSQLRunOnANonDefaultOpenMRSDatabase(dbName, sql);
				
				for (int i = 0; i < rowResults.size(); i++) {
					SolrInputDocument doc = new SolrInputDocument();
					
					addBasicFieldsToSolrDoc(project, i + 1, doc);
					
					Object currentRow = rowResults.get(i);
					if (null != currentRow) {
						if (currentRow instanceof Object[]) {
							Object[] currentObjs = (Object[]) currentRow;
							for (int j = 0; j < currentObjs.length;) {
								for (int k = 0; k < columnNamesFromCS.size(); k++) {
									if (j == k) {
										Object currentColumnValue = currentObjs[j];
										if (null == currentColumnValue) {
											currentColumnValue = "";
										}
										String currentColumnNameFromCS = columnNamesFromCS.get(k);
										doc.addField(currentColumnNameFromCS, currentColumnValue);
									}
									j++;
								}
							}
						} else {//This means that the search project has only one column
							Object obj = rowResults.get(i);
							if (null == obj) {
								obj = "";
							}
							if (columnNamesFromCS.size() == 1) {
								doc.addField(columnNamesFromCS.get(0), obj);
							}
						}
					}
					docs.add(doc);
				}
			} else {
				//TODO needs to fix, NOT YET SUPPORTED
				String dbUser = project.getDatabaseUser();
				String dbPassword = project.getDatabaseUSerPassword();
				String serverName = project.getServerName();
				String dbms = project.getDbms();
				String portNumber = project.getPortNumber();
				try {
					Connection connection = getConnection(serverName, dbms, dbUser, dbPassword, dbName, portNumber);
					Statement statement = connection.createStatement();
					ResultSet rs = statement.executeQuery(sql);
					
					while (rs.next()) {
						SolrInputDocument doc = new SolrInputDocument();
						addBasicFieldsToSolrDoc(project, rs.getRow(), doc);
						for (int i = 0; i < columnNamesFromCS.size(); i++) {
							//Object object = rs.getString(columnNamesFromCS.get(i));
							doc.addField(columnNamesFromCS.get(i), rs.getString(columnNamesFromCS.get(i)));
						}
						docs.add(doc);
					}
				}
				catch (SQLException e) {
					System.out.println("Error generated" + e);
				}//connection.url
			}
		}
		try {
			if (docs.size() > 1000) {
				// Commit within 5 minutes.
				UpdateResponse resp = solrServer.add(docs, 300000);
				printFailureIfResponseIsNotZero(resp);
			} else {
				UpdateResponse resp = solrServer.add(docs);//TODO fix: https://groups.google.com/a/openmrs.org/forum/#!topic/dev/N4l1Hj77j98
				printFailureIfResponseIsNotZero(resp);
			}
			solrServer.commit();
		}
		catch (SolrServerException e) {
			System.out.println("Error generated" + e);
		}
		catch (IOException e) {
			System.out.println("Error generated" + e);
		}
		return docs;
	}
	
	private void addBasicFieldsToSolrDoc(SearchProject project, int i, SolrInputDocument doc) {
		doc.addField("id", i);
		doc.addField("project_id", i);
		doc.addField("project_name", project.getProjectName());
		doc.addField("project_db_name", project.getDatabase());
		doc.addField("project_description", project.getProjectDescription());
		doc.addField("project_uuid", project.getUuid());
	}
	
	private void printFailureIfResponseIsNotZero(UpdateResponse resp) {
		if (resp.getStatus() != 0) {
			System.out.println("Some error has occurred, status is: " + resp.getStatus());
		}
	}
	
	/**
	 * Removes space(s) either at the start or end of a sentence/line and then split it using commas
	 * 
	 * @param line
	 * @return list of words split from the line
	 */
	public List<String> removeSpacesAndSplitLineUsingComma(String line) {
		//remove space(s) either at the start or end or line and then split it using commas
		String[] result = line.replaceAll(" ", "").split(",");
		List<String> words = new ArrayList<String>();
		words.addAll(Arrays.asList(result));
		return words;
	}
	
	/**
	 * Only to be used when indexing data outside openmrs current running database, Remotely
	 * connected to databases
	 */
	public Connection getConnection(String serverName, String dbms, String dbUser, String dbPassword, String dbName,
	                                String portNumber) throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbUser);
		connectionProps.put("password", dbPassword);
		
		//TODO may be modified to support several other database managers
		if (dbms.equals("mysql")) {
			conn = (Connection) DriverManager.getConnection("jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/",
			    connectionProps);
		} else if (dbms.equals("derby")) {
			conn = (Connection) DriverManager.getConnection("jdbc:" + dbms + ":" + dbName + ";create=true", connectionProps);
		}
		System.out.println("Connected to database: " + dbName);
		return conn;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
	
}
