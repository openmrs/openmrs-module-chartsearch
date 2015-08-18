/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartSearchMainProperties;
import org.openmrs.module.chartsearch.server.EmbeddedSolrServerCreator;
import org.openmrs.module.chartsearch.server.HttpSolrServerCreator;

/**
 * An initializing class for the Solr server instance
 */
public class SolrServerFactory {
	
	public static SolrServer getSolrServer() {
		Boolean useDedicatedSolrServer = Boolean.parseBoolean(Context.getAdministrationService().getGlobalProperty(
		    ChartSearchMainProperties.USE_DEDICATED_SOLR_SERVER));
		if (useDedicatedSolrServer) {
			String dedicatedSolrUrl = Context.getAdministrationService().getGlobalProperty(
			    ChartSearchMainProperties.DEDICATED_SOLR_SERVER_URL);
			return new HttpSolrServerCreator(dedicatedSolrUrl).createSolrServer();
		} else {
			return new EmbeddedSolrServerCreator(SolrUtils.getEmbeddedSolrProperties()).createSolrServer();
		}
	}
}
