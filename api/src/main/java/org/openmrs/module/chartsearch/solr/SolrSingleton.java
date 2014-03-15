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
package org.openmrs.module.chartsearch.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
