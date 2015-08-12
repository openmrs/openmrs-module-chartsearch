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
	public List<Integer> getPatientsToDelete(Collection<PatientInfo> patients) {
		long nonUsageMaxStartTime = new Date().getTime() - (nonUsageMaxTime * 1000);
		Date nonUsageMaxStartDate = new Date(nonUsageMaxStartTime);
		
		List<Integer> deletedPatients = new LinkedList<Integer>();
		for (PatientInfo patientInfo : patients) {
			if (patientInfo.getLastIndexTime().before(nonUsageMaxStartDate)) {
				deletedPatients.add(patientInfo.getPatientId());
			}
		}
		return deletedPatients;
	}
	
	@Override
	public String toString() {
		return "Non Usage Time Index Clear Strategy";
	}
	
}
