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
package org.openmrs.module.chartsearch.categories;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.emrapi.diagnosis.Diagnosis;
import org.openmrs.module.emrapi.diagnosis.DiagnosisService;

/**
 * Creates category item for diagnosis which is probably a single concept with a coded answer and
 * Patient's problem list.
 */
public class DiagnosesCategoryFilter extends CategoryFilter {
	
	DiagnosisService diagnosisService;
	
	List<Diagnosis> diagnoses;
	
	public List<Diagnosis> getDiagnoses() {
		return diagnoses;
	}
	
	PatientService patientService = Context.getPatientService();
	
	public List<Diagnosis> fetchDiagnosesFromWhenAPatientWasCreated(Integer patientId) {
		Patient patient = patientService.getPatient(patientId);
		
		return diagnosisService.getDiagnoses(patient, patient.getDateCreated());
	}
}
