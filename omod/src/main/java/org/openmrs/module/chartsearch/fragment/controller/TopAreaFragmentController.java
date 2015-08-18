/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.fragment.controller;

import net.sf.json.JSONObject;

import org.openmrs.Patient;
import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.SearchPhrase;
import org.openmrs.module.chartsearch.page.controller.ChartsearchPageController;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring controller for the topArea.gsp fragment
 */
public class TopAreaFragmentController {
	
	public void controller(FragmentModel model, @RequestParam("patientId") Patient patient) {
		model.addAttribute("patientId", patient);
		model.put("preferences", GeneratingJson.generateRightMatchedPreferencesJSON().toString());
	}
	
	public String getResultsFromTheServer(FragmentModel model,
	                                      @RequestParam(value = "phrase", required = false) SearchPhrase search_phrase,
	                                      @RequestParam("patientId") Patient patient,
	                                      @RequestParam(value = "categories[]", required = false) String[] categories) {
		SearchAPI searchAPIInstance = SearchAPI.getInstance();
		searchAPIInstance.clearResults();
		
		ChartsearchPageController.searchAndReturnResults(search_phrase, patient, categories, searchAPIInstance);
		return GeneratingJson.generateJson();
	}
	
	public JSONObject deleteSearchHistory(@RequestParam("historyUuid") String historyUuid) {
		JSONObject json = new JSONObject();
		ChartSearchCache cache = new ChartSearchCache();
		
		if (cache.deleteSearchHistory(historyUuid)) {
			json.put("searchHistory", GeneratingJson.getAllSearchHistoriesToSendToTheUI());
			
			return json;
		} else
			return null;
	}
}
