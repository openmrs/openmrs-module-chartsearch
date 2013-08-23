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

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class PatientInfoHolder {
	
	private static final Logger log = LoggerFactory.getLogger(PatientInfoHolder.class);
	
	// TODO rewrite & do more intuitive
	public Date getLastIndexTime(int patientId) {
		if (PatientInfoCache.data.containsKey(patientId)) {
			return PatientInfoCache.data.get(patientId).getTime();
		} else {
			return null;
		}
		
	}
	
	public void setLastIndexTime(int patientId){
		Date lastIndexTime = Calendar.getInstance().getTime();
		PatientInfoCache.data.put(patientId, new PatientInfo(lastIndexTime));
		log.info("Set last index time to: {}", lastIndexTime);
		
	}
	
	private static class PatientInfoCache {
		
		static Map<Integer, PatientInfo> data = new ConcurrentHashMap<Integer, PatientInfo>();
		
	}
	
}
