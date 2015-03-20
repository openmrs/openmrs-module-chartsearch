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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;

public class NonPatientDataIndexer {
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	private AdministrationService adminService = Context.getAdministrationService();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generateDocumentsAndAddFieldsAndCommitToSolr(SolrServer solrServer, int projectId) {
		//testing chartsearch in the first place which is id = 1
		SearchProject project = chartSearchService.getSearchProject(projectId);
		String sql = project.getSqlQuery();
		List<List<Object>> rowsByColumns = adminService.executeSQL(sql, false);
		Collection docs = new ArrayList();
		if (!rowsByColumns.isEmpty()) {
			for (int i = 0; i < rowsByColumns.size(); i++) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", i);
				doc.addField("project_id", i);
				List<Object> columns = rowsByColumns.get(i);
				String columnNamesWithCommas = project.getColumnNames();
				
				if (StringUtils.isNotBlank(columnNamesWithCommas) && !columns.isEmpty()) {
					//contains column names obtained from chart search module data
					List<String> columnNamesFromCS = removeSpacesAndSplitLineUsingComma(columnNamesWithCommas);
					for (int j = 0; j < columns.size();) {
						for (int k = 0; k < columnNamesFromCS.size(); k++) {
							if (j == k) {//this logically implies the column names match
								//obtained from running SQL query and not necessarily chart search module data
								Object currentColumnFromDB = columns.get(j);
								String currentColumnNameFromCS = columnNamesFromCS.get(k);
								
								if (currentColumnFromDB.getClass().equals(int.class)
								        || currentColumnFromDB.getClass().equals(Integer.class)) {
									int currentColumnValueFromDB = (Integer) currentColumnFromDB;
									doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
								} else if (currentColumnFromDB.getClass().equals(String.class)) {
									String currentColumnValueFromDB = (String) currentColumnFromDB;
									doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
									
								} else if (currentColumnFromDB.getClass().equals(boolean.class)
								        || currentColumnFromDB.getClass().equals(Boolean.class)) {
									boolean currentColumnValueFromDB = (Boolean) currentColumnFromDB;
									doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
								} else {
									Object currentColumnValueFromDB = currentColumnFromDB;
									doc.addField(currentColumnNameFromCS, currentColumnValueFromDB);
								}
							}
							j++;
						}
					}
				}
				docs.add(doc);
			}
		}
		try {
			if (docs.size() > 1000) {
				// Commit within 5 minutes.
				UpdateResponse resp = solrServer.add(docs, 300000);
				printFailureIfResponseIsNotZeroAndClearDocs(docs, resp);
			} else {
				UpdateResponse resp = solrServer.add(docs);//TODO fix: https://groups.google.com/a/openmrs.org/forum/#!topic/dev/N4l1Hj77j98
				printFailureIfResponseIsNotZeroAndClearDocs(docs, resp);
			}
		}
		catch (SolrServerException e) {
			System.out.println("Error generated" + e);
		}
		catch (IOException e) {
			System.out.println("Error generated" + e);
		}
	}
	
	private void printFailureIfResponseIsNotZeroAndClearDocs(Collection docs, UpdateResponse resp) {
		if (resp.getStatus() != 0) {
			System.out.println("Some error has occurred, status is: " + resp.getStatus());
		}
		docs.clear();
	}
	
	/**
	 * Removes space(s) either at the start or end or line and then split it using commas
	 * 
	 * @param line
	 * @return list of words split from the line
	 */
	public static List<String> removeSpacesAndSplitLineUsingComma(String line) {
		//remove space(s) either at the start or end or line and then split it using commas
		String[] result = line.replaceAll(" ", "").split(",");
		List<String> words = new ArrayList<String>();
		words.addAll(Arrays.asList(result));
		return words;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}

}
