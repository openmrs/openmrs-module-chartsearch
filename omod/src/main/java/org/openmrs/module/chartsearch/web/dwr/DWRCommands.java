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
package org.openmrs.module.chartsearch.web.dwr;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.server.PatientInfo;
import org.openmrs.module.chartsearch.server.StatisticsInfo;
import org.openmrs.module.chartsearch.solr.Indexer;

public class DWRCommands {
	
	protected static final Log log = LogFactory.getLog(DWRCommands.class);
	
	private Indexer indexer = getComponent(Indexer.class);
	
	public PatientInfo getPatientInfo(Integer patientId) {
		PatientInfo info = indexer.getPatientInfo(patientId);
		return info;
	}
	
	public StatisticsInfo getStatistics() {
		StatisticsInfo stats = indexer.getStatistics();
		return stats;
	}
	
	public Integer clearIndex(String ids, Integer strategyCode, Integer maxPatients, Integer ago){
		Integer pruneCount = indexer.clearIndex(ids, strategyCode, maxPatients, ago);
		return pruneCount;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
