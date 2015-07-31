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
		
		ChartsearchPageController.searchAndReturnResults(search_phrase, patient, categories, searchAPIInstance, false);
		return GeneratingJson.generateJson(false);
	}
	
	public JSONObject deleteSearchHistory(@RequestParam("historyUuid") String historyUuid) {
		JSONObject json = new JSONObject();
		ChartSearchCache cache = new ChartSearchCache();
		
		if (cache.deleteSearchHistory(historyUuid)) {
			json.put("searchHistory", GeneratingJson.getAllSearchHistoriesToSendToTheUI(false));
			
			return json;
		} else
			return null;
	}
}
