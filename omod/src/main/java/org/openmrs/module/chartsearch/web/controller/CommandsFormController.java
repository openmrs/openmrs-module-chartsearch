/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
 * The main controller for commands page.
 */
@Controller
@RequestMapping("/module/chartsearch/commands")
public class CommandsFormController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ModelMap MAP;
	
	@RequestMapping(method = RequestMethod.GET)
	public void showForm(ModelMap map) {
		Map<IndexClearStrategies, String> clearStrategy = new LinkedHashMap<IndexClearStrategies, String>();
		clearStrategy.put(IndexClearStrategies.IDS, "By patient ids");
		clearStrategy.put(IndexClearStrategies.BASIC, "By max patients in index");
		clearStrategy.put(IndexClearStrategies.NON_USAGE_TIME, "By max non usage time");
		map.put("clearStrategies", clearStrategy);
	}
}
