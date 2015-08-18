/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.page.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.chartsearch.AllergyItem;
import org.openmrs.module.chartsearch.AppointmentItem;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.EncounterItem;
import org.openmrs.module.chartsearch.FormItem;
import org.openmrs.module.chartsearch.ObsItem;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.SearchPhrase;
import org.openmrs.module.chartsearch.solr.ChartSearchIndexer;
import org.openmrs.module.chartsearch.web.dwr.DWRChartSearchService;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.InjectBeans;
import org.openmrs.ui.framework.page.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring main controller for the chartsearch.gsp page
 */
public class ChartsearchPageController {
	
	private static final Logger log = LoggerFactory.getLogger(ChartsearchPageController.class);
	
	private ChartSearchIndexer chartSearchIndexer = getComponent(ChartSearchIndexer.class);
	
	public void controller(@RequestParam("patientId") Patient patient, PageModel model,
	                       @BindParams SearchPhrase search_phrase, UiSessionContext sessionContext,
	                       @InjectBeans PatientDomainWrapper patientDomainWrapper,
	                       @RequestParam(value = "categories[]", required = false) String[] categories) {
		if (patient != null) {
			patientDomainWrapper.setPatient(patient);
			model.addAttribute("patient", patientDomainWrapper);
			SearchAPI searchAPIInstance = SearchAPI.getInstance();
			indexPatientData(patient);
			searchAndReturnResults(search_phrase, patient, categories, searchAPIInstance);
		}
	}
	
	private void indexPatientData(Patient patient) {
		log.info("getting patient ID :" + patient);
		log.info("trying to index a patient");
		if (chartSearchIndexer != null && patient != null) {
			chartSearchIndexer.indexPatientData(patient.getPatientId());
		}
		log.info("indexed patient");
	}
	
	public static void searchAndReturnResults(SearchPhrase search_phrase, Patient patient, String[] categories,
	                                          SearchAPI searchAPIInstance) {
		if (search_phrase == null) {
			search_phrase = new SearchPhrase();
		}
		if (categories == null) {
			categories = new String[0];
		}
		List<String> selectedCategories = Arrays.asList(categories);
		List<ChartListItem> items = searchAPIInstance.search(patient.getPatientId(), search_phrase, selectedCategories);
		List<ChartListItem> updatedItems = new ArrayList<ChartListItem>();
		
		for (ChartListItem chartListItem : items) {
			if (chartListItem instanceof ObsItem) {
				int itemObsId = ((ObsItem) chartListItem).getObsId();
				ChartListItem updatedObservation = DWRChartSearchService.getObservationDetails(itemObsId);
				updatedItems.add(updatedObservation);
			}
			if (chartListItem instanceof FormItem) {
				updatedItems.add(chartListItem);
			}
			
			if (chartListItem instanceof EncounterItem) {
				updatedItems.add(chartListItem);
			}
			
			if (chartListItem instanceof AllergyItem) {
				updatedItems.add(chartListItem);
			}
			
			if (chartListItem instanceof AppointmentItem) {
				updatedItems.add(chartListItem);
			}
		}
		//setting results to show.
		searchAPIInstance.setResults(updatedItems);
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
	
}
