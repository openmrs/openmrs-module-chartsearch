/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.server.PatientInfo;
import org.openmrs.module.chartsearch.server.StatisticsInfo;

/**
 * Calls into the real indexing and searching
 */
public class SolrSearch {
	
	private ChartSearchIndexer indexer = getComponent(ChartSearchIndexer.class);
	
	private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);
	
	public SolrSearch() {
		
	}
	
	public void initiateServer() {
		SolrSingleton.getInstance().getServer();
	}
	
	public void indexPatientData(Integer personId) {
		indexer.indexPatientData(personId);
	}
	
	public PatientInfo getPatientInfo(Integer patientId) {
		return indexer.getPatientInfo(patientId);
	}
	
	public StatisticsInfo getStatistics() {
		return indexer.getStatistics();
	}
	
	public Integer clearIndex(String strategy, String ids, Integer maxPatients, Integer ago) {
		return indexer.clearIndex(strategy, ids, maxPatients, ago);
	}
	
	public int changeDaemonsCount(int count) {
		return indexer.changeDaemonsCount(count);
	}
	
	public Long getDocumentListCount(Integer patientId, String searchText) {
		try {
			return searcher.getDocumentListCount(patientId, searchText);
		}
		catch (Exception e) {
			return (long) 0;
		}
	}
	
	public List<ChartListItem> search(Integer patientId, String searchText, Integer start, Integer length,
	                                  List<String> selectedCategoryNames) {
		try {
			return searcher.getDocumentList(patientId, searchText, start, length, selectedCategoryNames);
		}
		catch (Exception e) {
			return new ArrayList<ChartListItem>();
		}
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
	
}
