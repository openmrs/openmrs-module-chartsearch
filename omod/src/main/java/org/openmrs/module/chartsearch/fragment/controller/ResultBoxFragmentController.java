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

import org.apache.commons.lang3.StringEscapeUtils;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;

/**
 * Spring controller for the resultBox.gsp fragment
 */
public class ResultBoxFragmentController {
	
	public void controller(FragmentModel fragmentModel, UiUtils ui) {
		SearchAPI searchAPI = SearchAPI.getInstance();
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.add(StringEscapeUtils.escapeEcmaScript((GeneratingJson.generateJson())));
		searchAPI.clearResults();
		fragmentModel.addAttribute("resultList", resultList);//bind the result list for the view
	}
	
}
