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
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.chartsearch.web.controller;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for manageCategories.jsp.
 */
@Controller
public class ManageCategoriesFormController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/chartsearch/manageCategories.form", method = RequestMethod.GET)
	public ModelMap manageCategories() {
		ModelMap model = new ModelMap();
		return model;
	}
	
	//just for testing purposes
	@RequestMapping(value = "/module/chartsearch/manageCategories.form", method = RequestMethod.POST)
	public @ResponseBody String
	returnResultsWithAjax(@RequestBody String[] data) {
		SearchAPI searchAPI = SearchAPI.getInstance();
		searchAPI.clearResults();
		
		return GeneratingJson.generateJson();	
	}
}
