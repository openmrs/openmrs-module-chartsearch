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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.openmrs.module.chartsearch.Indexer;
import org.openmrs.module.chartsearch.Searcher;
import org.openmrs.module.chartsearch.task.DataImportTaskExecutor;
import org.openmrs.web.controller.PortletController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 */

public class PatientDashBoardChartSearchTabController extends PortletController {

	@Autowired
	private DataImportTaskExecutor indexer;	

	@Override
	protected void populateModel(HttpServletRequest request,
			Map<String, Object> model) {
		
		  Integer personId = (Integer) model.get("personId");
		  indexer.indexPatientData(personId);
		  
		 
	}
}
