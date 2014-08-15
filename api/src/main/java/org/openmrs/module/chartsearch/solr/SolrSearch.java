/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.chartsearch.solr;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.server.PatientInfo;
import org.openmrs.module.chartsearch.server.StatisticsInfo;

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
