/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.web.dwr;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.server.PatientInfo;
import org.openmrs.module.chartsearch.server.StatisticsInfo;
import org.openmrs.module.chartsearch.solr.ChartSearchCustomIndexer;
import org.openmrs.module.chartsearch.solr.ChartSearchIndexer;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.openmrs.module.chartsearch.synonyms.SynonymGroups;

public class DWRCommands {
	
	protected static final Log log = LogFactory.getLog(DWRCommands.class);
	
	private static String indexingProgressInfo = "";
	
	private ChartSearchIndexer chartSearchIndexer = getComponent(ChartSearchIndexer.class);
	
	/**
	 * @return the indexingProgressInfo
	 */
	public String getIndexingProgressInfo() {
		return indexingProgressInfo;
	}
	
	/**
	 * @param indexingProgressInfo the indexing progress Information to set
	 */
	public void setIndexingProgressInfo(String progressInfo) {
		DWRCommands.indexingProgressInfo = progressInfo;
	}
	
	public PatientInfo getPatientInfo(Integer patientId) {
		PatientInfo info = chartSearchIndexer.getPatientInfo(patientId);
		return info;
	}
	
	public StatisticsInfo getStatistics() {
		StatisticsInfo stats = chartSearchIndexer.getStatistics();
		return stats;
	}
	
	public Integer clearIndex(String strategy, String ids, Integer maxPatients, Integer ago) {
		Integer pruneCount = chartSearchIndexer.clearIndex(strategy, ids, maxPatients, ago);
		return pruneCount;
	}
	
	public int changeDaemonsCount(int count) {
		int daemonsCount = chartSearchIndexer.changeDaemonsCount(count);
		return daemonsCount;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String deleteSynonymGroup(String groupName) {
		ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
		SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
		synonymGroupsInstance.clearSynonymGroups();
		List synGroups = chartSearchService.getAllSynonymGroups();
		synonymGroupsInstance.setSynonymGroupsHolder(synGroups);
		SynonymGroup grpToDel = synonymGroupsInstance.getSynonymGroupByName(groupName);
		if (synonymGroupsInstance.deleteSynonymGroupByName(groupName)) {
			chartSearchService.purgeSynonymGroup(grpToDel);
			return groupName;
		}
		return "-1";
	}
	
	public void indexAllPatientData(Integer numberOfResults) {
		try {
			ChartSearchCustomIndexer.indexAllPatientData(numberOfResults, DWRCommands.class);
			setIndexingProgressInfo("finished");
		}
		catch (SQLException e) {
			log.error("Error generated", e);
		}
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
