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

import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;

public class ResultBoxFragmentController {
	
	public void controller(FragmentModel fragmentModel,
                           UiUtils ui) {
		SearchAPI searchAPI = SearchAPI.getInstance();
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.add(ui.escapeJs(GeneratingJson.generateJson(true)));
		searchAPI.clearResults();
		fragmentModel.addAttribute("resultList", resultList);//bind the result list for the view
	}

}
