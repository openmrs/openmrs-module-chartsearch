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

	public IndexClearStrategyBasicImpl(int maxPatientsInIndex){
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
				// TODO Auto-generated method stub
				return o1.getLastIndexTime().compareTo(o2.getLastIndexTime());
			}
			
		});
		
		List<Integer> deletedPatients = new LinkedList<Integer>();
		
		for (PatientInfo patientInfo : sortedPatients) {
			deletedPatients.add(patientInfo.getPatientId());
	        --deleteCount;
	        if (deleteCount == 0) break;
        }
		
		return deletedPatients;
    }
	
	@Override
	public String toString() {
	    return "Basic Index Clear Strategy";
	}
	
}
