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

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.openmrs.module.chartsearch.server.PatientInfo;

public class IndexClearStrategyNonUsageTimeImpl implements IndexClearStrategy {
	
	private final int nonUsageMaxTime;
	
	public IndexClearStrategyNonUsageTimeImpl(int maxNonUsageTime) {
		this.nonUsageMaxTime = maxNonUsageTime;
		
	}
	
	@Override
	public List<PatientInfo> getPatientsToDelete(Collection<PatientInfo> patients) {
		long nonUsageMaxStartTime = new Date().getTime() - (nonUsageMaxTime * 1000);
		Date nonUsageMaxStartDate = new Date(nonUsageMaxStartTime);
		
		List<PatientInfo> deletedPatients = new LinkedList<PatientInfo>();
		for (PatientInfo patientInfo : patients) {
			if (patientInfo.getLastIndexTime().before(nonUsageMaxStartDate)) {
				deletedPatients.add(patientInfo);
			}
		}
		return deletedPatients;
	}
	
}
