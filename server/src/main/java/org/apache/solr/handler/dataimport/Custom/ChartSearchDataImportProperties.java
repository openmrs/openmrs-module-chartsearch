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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartSearchDataImportProperties {
	
	private Properties properties;
	
	public static final String DAEMONS_COUNT = "daemonsCount";
	
	public static final String INDEX_SIZE_MANAGER_TIMEOUT = "indexSizeManagerTimeout";
	
	public static final String PATIENT_INFO_TIMEOUT = "patientInfoTimeout";
	
	public static final String INDEX_MAX_PATIENTS = "indexMaxPatients";
	
	private static final Logger logger = LoggerFactory.getLogger(ChartSearchDataImportProperties.class);
	
	private static final int DEFAULT_DAEMONS_COUNT = 3;
	
	private static final int DEFAULT_INDEX_SIZE_MANAGER_TIMEOUT = 30;
	
	private static final int DEFAULT_PATIENT_INFO_TIMEOUT = 20;

	private static final int DEFAULT_INDEX_MAX_PATIENTS = 100;
	
	private final String handlerName;
	private final String configDir;
	
	public ChartSearchDataImportProperties(String handlerName, String configDir) {
		this.handlerName = handlerName;
		this.configDir = configDir;
		loadProperties(true);
	}
	
	public void loadProperties(boolean force) {
		try {
			/*SolrResourceLoader loader = new SolrResourceLoader(null);
			logger.info("Instance dir = " + loader.getInstanceDir());
			
			String configDir = loader.getConfigDir();
			configDir = SolrResourceLoader.normalizeDir(configDir);*/
			if (force || properties == null) {
				properties = new Properties();
				
				File dataImportProperties = new File(configDir, String.format("%s.properties", handlerName));
				
				FileInputStream fis = new FileInputStream(dataImportProperties);
				properties.load(fis);
			}
		}
		catch (FileNotFoundException fnfe) {
			logger.error(String.format("Error locating %s.properties file", handlerName), fnfe);
		}
		catch (IOException ioe) {
			logger.error(String.format("Error reading %s.properties file", handlerName), ioe);
		}
		catch (Exception e) {
			logger.error(String.format("Error loading %s.properties file", handlerName), e);
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public int getDaemonsCount() {
		int daemonsCount;
		try {
			daemonsCount = Integer.parseInt(getProperty(DAEMONS_COUNT));
		}
		catch (NumberFormatException ex) {
			daemonsCount = DEFAULT_DAEMONS_COUNT;
		}
		return daemonsCount;
	}
	
	public int getIndexSizeManagerTimeout() {
		int timeout;
		try {
			timeout = Integer.parseInt(getProperty(INDEX_SIZE_MANAGER_TIMEOUT));
		}
		catch (NumberFormatException ex) {
			timeout = DEFAULT_INDEX_SIZE_MANAGER_TIMEOUT;
		}
		return timeout;
	}
	
	public int getPatientInfoTimeout() {
		int timeout;
		try {
			timeout = Integer.parseInt(getProperty(PATIENT_INFO_TIMEOUT));
		}
		catch (NumberFormatException ex) {
			timeout = DEFAULT_PATIENT_INFO_TIMEOUT;
		}
		return timeout;
	}
	
	public int getIndexMaxPatients(){
	  int maxPatients;
    try {
      maxPatients = Integer.parseInt(getProperty(INDEX_MAX_PATIENTS));
    }
    catch (NumberFormatException ex) {
      maxPatients = DEFAULT_INDEX_MAX_PATIENTS;
    }
    return maxPatients;
	}
}
