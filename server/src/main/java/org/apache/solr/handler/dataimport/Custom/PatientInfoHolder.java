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

import org.openmrs.module.chartsearch.server.PatientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class PatientInfoHolder {
	
	private static final Logger log = LoggerFactory.getLogger(PatientInfoHolder.class);
	private final PatientInfoCache cache;
		
	public PatientInfoHolder(PatientInfoCache cache){
		this.cache = cache;
	}
	
	public Date getLastIndexTime(int patientId) {
		if (cache.contains(patientId)) {
			return cache.get(patientId).getLastIndexTime();
		} else {
			return null;
		}		
	}
	
	public void setLastIndexTime(int patientId){
		Date lastIndexTime = Calendar.getInstance().getTime();
		cache.put(patientId, new PatientInfo(patientId, lastIndexTime));
		log.info("Set last index time to: {}", lastIndexTime);	
	}
	
}
