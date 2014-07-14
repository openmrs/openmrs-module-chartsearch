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

import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchWithAjaxFragmentController {
	
	private static final Logger log = LoggerFactory.getLogger(SearchWithAjaxFragmentController.class);
	
	public void controller(FragmentModel fragmentModel) {
		
	}
	
	public String getResultsFromTheServer(FragmentModel model) {
		
		//chartsearchPageController.controller(model, search_phrase, sessionContext, patient, patientDomainWrapper, request);
		
		SearchAPI searchAPI = SearchAPI.getInstance();
		ArrayList<String> results = new ArrayList<String>();
		String jsonResults = GeneratingJson.generateJson();
		results.add(jsonResults);
		searchAPI.clearResults();
		model.addAttribute("results", results);
		
		return jsonResults;
	}
	
}
