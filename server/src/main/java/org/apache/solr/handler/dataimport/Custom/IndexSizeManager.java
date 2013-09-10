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
package org.apache.solr.handler.dataimport.custom;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.UpdateHandler;
import org.openmrs.module.chartsearch.server.PatientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IndexSizeManager {
	
	public static Logger log = LoggerFactory.getLogger(IndexSizeManager.class);
	
	private final UpdateHandler handler;
	
	private final SolrQueryRequest req;
	
	private final PatientInfoCache cache;

	private IndexClearStrategy strategy;
	
	private int clearedPatientsCount = 0;
	
	
	public IndexSizeManager(SolrCore core, PatientInfoCache cache, IndexClearStrategy strategy) {
		this.strategy = strategy;
		this.handler = core.getUpdateHandler();;
		this.req = new SolrQueryRequestBase(
            core, new MapSolrParams(new HashMap<String, String>())) {};;
		this.cache = cache;
	}
	
	public void clearIndex() {
		synchronized (cache) {
			
			Map<Integer, PatientInfo> map = cache.getAll();
			List<PatientInfo> patients = new LinkedList<PatientInfo>(map.values());
			
			List<PatientInfo> deletePatients = strategy.getPatientsToDelete(patients);	
			
			if (deletePatients.size() == 0){
				log.info("Nothing to clear");
				return;
			}
						
			CommitUpdateCommand commitCmd = new CommitUpdateCommand(req, true);
			
			for (PatientInfo patientInfo : deletePatients) {				
				DeleteUpdateCommand delCmd = new DeleteUpdateCommand(req);
				delCmd.query = String.format("person_id:%d", patientInfo.getPatientId());
				
				deletePatient(patientInfo, delCmd);
				clearedPatientsCount++;
			}
			commitChanges(commitCmd);
			
			cache.save();
			
			log.info("Index cleared, deleted {} patients, {} patients left in the index", deletePatients.size(), cache.size());
			
		}
		
	}
	
	private void deletePatient(PatientInfo patientInfo, DeleteUpdateCommand delCmd) {
		try {
			handler.deleteByQuery(delCmd);
			cache.remove(patientInfo.getPatientId());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		}
	}
	
	private void commitChanges(CommitUpdateCommand commitCmd){
		try {
	        handler.commit(commitCmd);
        }
        catch (IOException e) {
	        // TODO Auto-generated catch block
	        log.error("Error generated", e);
        }
	}

	public int getClearedPatientsCount() {
	    return clearedPatientsCount;
    }
	
}
