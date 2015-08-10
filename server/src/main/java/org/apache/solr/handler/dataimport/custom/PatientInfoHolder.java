/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
	
	public PatientInfoHolder(PatientInfoCache cache) {
		this.cache = cache;
	}
	
	public Date getLastIndexTime(int patientId) {
		if (cache.contains(patientId)) {
			return cache.get(patientId).getLastIndexTime();
		} else {
			return null;
		}
	}
	
	public void setLastIndexTime(int patientId) {
		Date lastIndexTime = Calendar.getInstance().getTime();
		cache.put(patientId, new PatientInfo(patientId, lastIndexTime));
		log.info("Set last index time to: {}", lastIndexTime);
	}
	
}
