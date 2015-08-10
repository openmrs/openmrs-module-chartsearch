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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.openmrs.module.chartsearch.server.PatientInfo;

/**
 *
 */
public class IndexClearStrategyBasicImpl implements IndexClearStrategy {
	
	private final int maxPatientsInIndex;
	
	public IndexClearStrategyBasicImpl(int maxPatientsInIndex) {
		this.maxPatientsInIndex = maxPatientsInIndex;
		
	}
	
	@Override
	public List<Integer> getPatientsToDelete(Collection<PatientInfo> patients) {
		int deleteCount = patients.size() - maxPatientsInIndex;
		
		if (deleteCount <= 0)
			return Collections.emptyList();
		
		List<PatientInfo> sortedPatients = new LinkedList<PatientInfo>(patients);
		
		Collections.sort(sortedPatients, new Comparator<PatientInfo>() {
			
			@Override
			public int compare(PatientInfo o1, PatientInfo o2) {
				return o1.getLastIndexTime().compareTo(o2.getLastIndexTime());
			}
			
		});
		
		List<Integer> deletedPatients = new LinkedList<Integer>();
		
		for (PatientInfo patientInfo : sortedPatients) {
			deletedPatients.add(patientInfo.getPatientId());
			--deleteCount;
			if (deleteCount == 0)
				break;
		}
		
		return deletedPatients;
	}
	
	@Override
	public String toString() {
		return "Basic Index Clear Strategy";
	}
	
}
