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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.OpenmrsObject;
import org.openmrs.Person;
import org.openmrs.activelist.Problem;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;

/**
 * Creates category item for diagnosis which is probably a single concept with a coded answer and
 * Patient's problem list.
 */
public class DiagnosesCategoryFilter extends SubCategoryFilter {
	
	ConceptService conceptService = Context.getConceptService();
	
	PatientService patientService = Context.getPatientService();
	
	EncounterService encounterService = Context.getEncounterService();
	
	ObsService obsService = Context.getObsService();
	
	/**
	 * @param patientId
	 * @return concept with coded answer or concept class "diagnosis" fetched by patient and concept
	 */
	public List<Concept> fetchConceptWithCodedAnswerForAPatient(Integer patientId) {
		List<Concept> diagnoses = new ArrayList<Concept>();
		String diagnosisClassUuid = "8d4918b0-c2cc-11de-8d13-0010c6dffd0f";
		Person person = patientService.getPatient(patientId);
		
		Concept concept = (Concept) conceptService.getConceptsByClass(conceptService
		        .getConceptClassByUuid(diagnosisClassUuid));
		
		List<Obs> obs = obsService.getObservationsByPersonAndConcept(person, concept);
		
		for (Iterator<Obs> iterator = obs.iterator(); iterator.hasNext();) {
			diagnoses.add(concept);
		}
		return diagnoses;
	}
	
	/**
	 * @param patientId
	 * @return problem list for a patient
	 */
	public List<Problem> fetchItemsFromPatientProblemList(Integer patientId) {
		return patientService.getProblems(patientService.getPatient(patientId));
	}
	
	/**
	 * @param patientId
	 * @return diagnoses (both concept with coded answer and problem list for a patient)
	 */
	public List<OpenmrsObject> fetchCodedAnswerAndProblemListAsDiagnoses(Integer patientId) {
		List<OpenmrsObject> diadnoses = new ArrayList<OpenmrsObject>();
		
		diadnoses.addAll(fetchConceptWithCodedAnswerForAPatient(patientId));
		diadnoses.addAll(fetchItemsFromPatientProblemList(patientId));
		
		return diadnoses;
	}
}
