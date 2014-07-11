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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.SearchPhrase;
import org.openmrs.module.chartsearch.page.controller.ChartsearchPageController;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class SearchWithAjaxFragmentController {
	
	@RequestMapping(value = "/module/chartsearch/chartsearch.page", method = RequestMethod.POST)
	public @ResponseBody
	String getResultsFromTheServer(@BindParams SearchPhrase search_phrase, @RequestParam("patientId") Patient patient,
	                               HttpServletRequest request, FragmentModel model, @RequestBody String data) {
		
		System.out.println(data);
		List<ChartListItem> resultsItems = new ArrayList<ChartListItem>();
		resultsItems.addAll(ChartsearchPageController.setCategoriesPhraseAndSearchAndReturnResults(search_phrase, patient,
		    request));
		
		SearchAPI searchAPI = SearchAPI.getInstance();
		ArrayList<String> results = new ArrayList<String>();
		String jsonResults = GeneratingJson.generateJson();
		results.add(jsonResults);
		searchAPI.clearResults();
		model.addAttribute("results", results);
		model.addAttribute("resultsItems", resultsItems);
		
		return jsonResults;
	}
	
	public void controller(@BindParams SearchPhrase search_phrase, @RequestParam("patientId") Patient patient,
	                       HttpServletRequest request, FragmentModel model) {
		List<ChartListItem> resultsItems = new ArrayList<ChartListItem>();
		resultsItems.addAll(ChartsearchPageController.setCategoriesPhraseAndSearchAndReturnResults(search_phrase, patient,
		    request));
		
	}
}
