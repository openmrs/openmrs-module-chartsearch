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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.zookeeper.data.Stat;
import org.openmrs.module.chartsearch.server.ConfigCommands;
import org.openmrs.module.chartsearch.server.PatientInfo;
import org.openmrs.module.chartsearch.server.StatisticsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Indexer {
	
	//private final SolrServer solrServer;
	
	private static final Logger log = LoggerFactory.getLogger(Indexer.class);
	
	
	public Indexer() {
		//this.solrServer = SolrSingleton.getInstance().getServer();
	}
	
	public void indexPatientData(Integer personId) {
		doImport(personId);		
	}
	
	private void doImport(int personId){
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		ModifiableSolrParams params = new ModifiableSolrParams();
		//TODO take path from config
		params.set("qt", "/csdataimport");
		params.set("command", "import");
		params.set("clean", false);
		params.set("personId", personId);
		
		try {
			solrServer.query(params);
		}
		catch (SolrServerException ex){
			log.error(String.format("Tried to import patient #%d but failed", personId), ex);
		}
	}

	public PatientInfo getPatientInfo(Integer patientId) {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		ModifiableSolrParams params = new ModifiableSolrParams();
		//TODO take path from config
		params.set("qt", "/csdataimport");
		params.set("command", ConfigCommands.PATIENT_STATE);
		params.set("personId", patientId);
		
		try {
			QueryResponse response = solrServer.query(params);
			Date lastIndexTime = (Date) response.getResponse().get(ConfigCommands.Labels.PATIENT_LAST_INDEX_TIME);
			if (lastIndexTime == null) return null;
			PatientInfo info = new PatientInfo(patientId, lastIndexTime);
			return info;
		}
		catch (SolrServerException ex){
			log.error(String.format("Failed to get patient state for #%d", patientId), ex);
			return null;
		}
    }

	public StatisticsInfo getStatistics() {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		ModifiableSolrParams params = new ModifiableSolrParams();
		//TODO take path from config
		params.set("qt", "/csdataimport");
		params.set("command", ConfigCommands.STATS);
		
		try {
			QueryResponse response = solrServer.query(params);
			NamedList<Object> responseList = response.getResponse();
			String clearStrategy = (String) responseList.get(ConfigCommands.Labels.CLEAR_STRATEGY);
			int pruneCount = (Integer) responseList.get(ConfigCommands.Labels.CLEARED_PATIENTS_COUNT);
			List<HashMap<String, Object>> daemonStates = (ArrayList<HashMap<String, Object>>) responseList.get(ConfigCommands.Labels.DAEMON_STATES);
			StatisticsInfo stats = new StatisticsInfo(clearStrategy, pruneCount, daemonStates);
			return stats;
		}
		catch (SolrServerException ex){
			log.error("Failed to get stats", ex);
			return null;
		}	    
    }	
	
}
