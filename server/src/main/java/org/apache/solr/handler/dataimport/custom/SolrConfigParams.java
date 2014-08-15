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

import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrConfigParams {
	
	private static final Logger log = LoggerFactory.getLogger(SolrConfigParams.class);
	
	public static final String DAEMONS_COUNT = "daemonsCount";
	
	public static final String INDEX_SIZE_MANAGER_TIMEOUT = "indexSizeManagerTimeout";
	
	public static final String PATIENT_INFO_TIMEOUT = "patientInfoTimeout";
	
	public static final String INDEX_MAX_PATIENTS = "indexMaxPatients";
	
	public static final String INDEX_CLEAR_STRATEGY = "indexClearStrategy";
	
	public static final String PATIENT_MAX_NON_USAGE_TIME = "patientMaxNonUsageTime";
	
	public static final int DEFAULT_DAEMONS_COUNT = 3;
	
	public static final int DEFAULT_INDEX_SIZE_MANAGER_TIMEOUT = 30;
	
	public static final int DEFAULT_PATIENT_INFO_TIMEOUT = 20;
	
	public static final int DEFAULT_INDEX_MAX_PATIENTS = 100;
	
	public static final int DEFAULT_PATIENT_MAX_NON_USAGE_TIME = 30; // seconds	
	
	private SolrParams params;
	
	public SolrConfigParams(SolrParams params) {
		this.params = params;
	}
	
	private String getProperty(String key) {
		return params.get(key);
	}
	
	public int getDaemonsCount() {
		return tryGetInteger(DAEMONS_COUNT, DEFAULT_DAEMONS_COUNT);
	}
	
	public int getIndexSizeManagerTimeout() {
		return tryGetInteger(INDEX_SIZE_MANAGER_TIMEOUT, DEFAULT_INDEX_SIZE_MANAGER_TIMEOUT);
	}
	
	public int getPatientInfoTimeout() {
		return tryGetInteger(PATIENT_INFO_TIMEOUT, DEFAULT_PATIENT_INFO_TIMEOUT);
	}
	
	private int getIndexMaxPatients() {
		return tryGetInteger(INDEX_MAX_PATIENTS, DEFAULT_INDEX_MAX_PATIENTS);
	}
	
	public IndexClearStrategy getIndexClearStrategy(int strategyCode) {
		//TODO Remove duplications
		try {
			if (strategyCode == IndexClearStrategies.BASIC.ordinal())
				return new IndexClearStrategyBasicImpl(getIndexMaxPatients());
			else if (strategyCode == IndexClearStrategies.NO_ACTION.ordinal())
				return new IndexClearStrategyNoActionImpl();
			else if (strategyCode == IndexClearStrategies.NON_USAGE_TIME.ordinal())
				return new IndexClearStrategyNonUsageTimeImpl(getPatientMaxNonUsageTime());
			else
				return new IndexClearStrategyBasicImpl(getIndexMaxPatients());
			
		}
		catch (NumberFormatException ex) {
			log.error("Failed to read index clear strategy code from properties file", ex);
			return new IndexClearStrategyBasicImpl(getIndexMaxPatients());
		}
	}
	
	public IndexClearStrategy getIndexClearStrategy() {
		int strategyCode = Integer.parseInt(getProperty(INDEX_CLEAR_STRATEGY));
		return getIndexClearStrategy(strategyCode);
	}
	
	private int tryGetInteger(String propertyName, int defaultValue) {
		try {
			return Integer.parseInt(getProperty(propertyName));
		}
		catch (NumberFormatException ex) {
			log.error("Failed to read Integer value from properties file", ex);
			return defaultValue;
		}
	}
	
	private int getPatientMaxNonUsageTime() {
		return tryGetInteger(PATIENT_MAX_NON_USAGE_TIME, DEFAULT_PATIENT_MAX_NON_USAGE_TIME);
	}
}
