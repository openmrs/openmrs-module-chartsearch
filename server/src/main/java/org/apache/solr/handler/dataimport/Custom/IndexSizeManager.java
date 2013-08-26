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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.UpdateHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IndexSizeManager {
	
	public static Logger log = LoggerFactory.getLogger(IndexSizeManager.class);
	
	private final UpdateHandler handler;
	
	private SolrQueryRequest req;
	
	private final PatientInfoCache cache;
	
	private final int maxPatientsInIndex;
	
	public IndexSizeManager(UpdateHandler handler, SolrQueryRequest req, PatientInfoCache cache, int maxPatientsInIndex) {
		this.handler = handler;
		this.req = req;
		this.cache = cache;
		this.maxPatientsInIndex = maxPatientsInIndex;
	}
	
	public void clearIndex() {
		synchronized (cache) {
			
			Map<Integer, PatientInfo> map = cache.getAll();
			
			int deleteCount = map.size() - maxPatientsInIndex;
			
			if (deleteCount <= 0)
				return;
			
			List<PatientInfo> deletePatients = new LinkedList<PatientInfo>(map.values());
			
			Collections.sort(deletePatients, new Comparator<PatientInfo>() {
				
				@Override
				public int compare(PatientInfo o1, PatientInfo o2) {
					// TODO Auto-generated method stub
					return o1.getLastIndexTime().compareTo(o2.getLastIndexTime());
				}
				
			});
			
			CommitUpdateCommand commitCmd = new CommitUpdateCommand(req, true);
			
			int deleteRealCount = 0;
			for (PatientInfo patientInfo : deletePatients) {
				
				DeleteUpdateCommand delCmd = new DeleteUpdateCommand(req);
				delCmd.query = String.format("person_id:%d", patientInfo.getPatientId());
				
				deletePatient(patientInfo, commitCmd, delCmd);
				
				--deleteCount;
				++deleteRealCount;
				if (deleteCount == 0) {					
					log.info("Index cleared, deleted %d patients", deleteRealCount);
					cache.save();
					return;
				}
			}
			
		}
		
	}
	
	private void deletePatient(PatientInfo patientInfo, CommitUpdateCommand commitCmd, DeleteUpdateCommand delCmd) {
		try {
			handler.deleteByQuery(delCmd);
			handler.commit(commitCmd);
			cache.remove(patientInfo.getPatientId());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		}
	}
	
}
