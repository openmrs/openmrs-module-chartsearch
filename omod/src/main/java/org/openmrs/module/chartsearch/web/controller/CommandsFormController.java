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
package org.openmrs.module.chartsearch.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.handler.dataimport.custom.IndexClearStrategies;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The main controller.
 */
@Controller
@RequestMapping("/module/chartsearch/commands")
public class  CommandsFormController {
	
	protected final Log log = LogFactory.getLog(getClass());		
	
	@RequestMapping(method = RequestMethod.GET)
	public void showForm(ModelMap map) {
		Map<IndexClearStrategies,String> clearStrategy = new LinkedHashMap<IndexClearStrategies,String>();
		clearStrategy.put(IndexClearStrategies.IDS, "By patient ids");
		clearStrategy.put(IndexClearStrategies.BASIC, "By max patients in index");
		clearStrategy.put(IndexClearStrategies.NON_USAGE_TIME, "By max non usage time");
		map.put("clearStrategies", clearStrategy);
	}
	
	
	/*@RequestMapping(method = RequestMethod.POST)
	public String handleSubmission(Integer patientId) {
		
		PatientInfo patientInfo = indexer.getPatientState(patientId);
		

		return "redirect:commands.form";
	}*/
}
