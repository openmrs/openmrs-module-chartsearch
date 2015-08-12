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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.handler.dataimport.custom.IndexClearStrategies;
import org.openmrs.module.chartsearch.solr.ChartSearchIndexer;
import org.openmrs.web.controller.PortletController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO to be removed
 */
public class PatientDashBoardChartSearchTabController extends PortletController {
	
	@Autowired
	private ChartSearchIndexer chartSearchIndexer;
	
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		Integer personId = (Integer) model.get("personId");
		
		chartSearchIndexer.clearIndex(IndexClearStrategies.IDS.toString(), personId + "", 0, 0);
		chartSearchIndexer.indexPatientData(personId);
	}
}
