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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;

public class NonPatientDataIndexer {
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generateDocumentsAndAddFieldsAndCommitToSolr(SolrServer solrServer, int projectId) {
		//testing chartsearch in the first place which is id = 1
		SearchProject project = chartSearchService.getSearchProject(projectId);
		
		String sql = project.getSqlQuery();
		
		if (StringUtils.isNotBlank(sql) && project != null) {
			ResultSet rs = chartSearchService.executeSQL(sql);
			Collection docs = new ArrayList();
			try {
				while (rs.next()) {
					String columnNamesWithCommas = project.getColumnNames();
					if (StringUtils.isNotBlank(columnNamesWithCommas)) {
						SolrInputDocument doc = new SolrInputDocument();
						List<String> columnNames = new ArrayList<String>();
						columnNames.addAll(splitLineUsingComma(columnNamesWithCommas));
						for (int j = 0; j < columnNames.size(); j++) {
							doc.addField(columnNames.get(j), rs.getString(columnNames.get(j)));
							docs.add(doc);
						}
					}
				}
				try {
					if (docs.size() > 1000) {
						// Commit within 5 minutes.
						UpdateResponse resp = solrServer.add(docs, 300000);
						if (resp.getStatus() != 0) {
							System.out.println("Some error has occurred, status is: " + resp.getStatus());
						}
						docs.clear();
					}
				}
				catch (SolrServerException e) {
					System.out.println("Error generated" + e);
				}
				catch (IOException e) {
					System.out.println("Error generated" + e);
				}
			}
			catch (SQLException e) {
				System.out.println("Error generated" + e);
			}
			finally {
				if (rs != null)
					try {
						rs.close();
					}
					catch (SQLException e) {
						System.out.println("Error generated" + e);
					}
			}
		}
	}
	
	public List<String> splitLineUsingComma(String line) {
		String[] result = line.split(",");
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
