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
package org.openmrs.module.chartsearch.server;


import java.util.Date;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Indexer {
	
	private final SolrServer solrServer;
	
	private static final Logger log = LoggerFactory.getLogger(Indexer.class);
	
	
	public Indexer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	public void indexPatientData(Integer personId) {
		doImport(personId);
		/*Date lastIndexTime = patientInfoHolder.getLastIndexTime(personId);
		try {			
			if (lastIndexTime == null) {
				fullImport(personId);
			} else {
				deltaImport(personId, lastIndexTime);
			}
		}
		catch (SolrServerException e) {
			log.error("Error generated", e);
		}
		patientInfoHolder.setLastIndexTime(personId);
		log.info("Last index time was setted to person #{}", personId);*/
		
	}
	
	private void doImport(int personId){
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
		ModifiableSolrParams params = new ModifiableSolrParams();
		//TODO take path from config
		params.set("qt", "/csdataimport");
		params.set("command", ConfigCommands.PATIENT_STATE);
		params.set("personId", patientId);
		
		try {
			QueryResponse response = solrServer.query(params);
			Date lastIndexTime = (Date) response.getResponse().get(ConfigCommands.Labels.PATIENT_LAST_INDEX_TIME);
			if (lastIndexTime == null) return null;
			return new PatientInfo(patientId, lastIndexTime);
		}
		catch (SolrServerException ex){
			log.error(String.format("Failed to get patient state for #%d", patientId), ex);
			return null;
		}
    }
	
	/*private void fullImport(int personId) throws SolrServerException {
		ModifiableSolrParams params = new ModifiableSolrParams();
		//TODO take path from config
		params.set("qt", "/csdataimport");
		params.set("command", "full-import");
		params.set("clean", false);
		params.set("personId", personId);
		
		solrServer.query(params);
	}
	
	private void deltaImport(int personId, Date lastIndexTime) throws SolrServerException {
		ModifiableSolrParams params = new ModifiableSolrParams();
		//TODO take path from config
		params.set("qt", "/csdataimport");
		params.set("command", "delta-import");
		params.set("clean", false);
		
		params.set("personId", personId);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.set("lastIndexTime", formatter.format(lastIndexTime));
		
		solrServer.query(params);
	}*/
	
}
