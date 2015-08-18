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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solr solr instance maintained through the running instance as a single object
 */
public class SolrSingleton {
	
	private static final Logger log = LoggerFactory.getLogger(SolrSingleton.class);
	
	private SolrServer solrServer;
	
	private SolrSingleton() {
		
	}
	
	private static class SolrEngineHolder {
		
		private static SolrSingleton INSTANCE = null;
	}
	
	private void init() {
		log.info("Solr server first init !");
		solrServer = SolrServerFactory.getSolrServer();
	}
	
	public static SolrSingleton getInstance() {
		if (SolrEngineHolder.INSTANCE == null) {
			SolrEngineHolder.INSTANCE = new SolrSingleton();
			SolrEngineHolder.INSTANCE.init();
		}
		return SolrEngineHolder.INSTANCE;
	}
	
	/**
	 * Gets an instance of the solr server
	 * 
	 * @return SolrServer
	 */
	public SolrServer getServer() {
		return solrServer;
	}
}
