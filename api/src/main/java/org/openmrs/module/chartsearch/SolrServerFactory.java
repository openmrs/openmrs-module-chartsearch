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
package org.openmrs.module.chartsearch;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.server.EmbeddedSolrServerCreator;
import org.openmrs.module.chartsearch.server.HttpSolrServerCreator;

/**
 *
 */
public class SolrServerFactory {
	
	public static SolrServer getSolrServer() {
		//if (Context.getAdministrationService() == null) throw new RuntimeException("123");
		/*Boolean.parseBoolean(Context.getAdministrationService().getGlobalProperty(
		    ChartSearchMainProperties.USE_DEDICATED_SOLR_SERVER));*/
		Boolean useDedicatedSolrServer = Boolean.parseBoolean(Context.getAdministrationService().getGlobalProperty(
		    ChartSearchMainProperties.USE_DEDICATED_SOLR_SERVER));
		if (useDedicatedSolrServer){
			String dedicatedSolrUrl = Context.getAdministrationService().getGlobalProperty(
			    ChartSearchMainProperties.DEDICATED_SOLR_SERVER_URL);
			return new HttpSolrServerCreator(dedicatedSolrUrl).createSolrServer();
		}
		else {
			return new EmbeddedSolrServerCreator(SolrUtils.getEmbeddedSolrProperties()).createSolrServer();
		}
		//return new EmbeddedSolrServerCreator(SolrUtils.getEmbeddedSolrProperties()).createSolrServer();
		
	}
}
