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
package org.openmrs.module.chartsearch.server;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 *
 */
public class HttpSolrServerCreator extends SolrServerCreator {
	
	private final String solrHttpAddress;
	
	public HttpSolrServerCreator(String solrHttpAddress) {
		this.solrHttpAddress = solrHttpAddress;
	}
	
	public String getSolrHttpAddress() {
		return solrHttpAddress;
	}
	
	/**
	 * @see org.openmrs.module.chartsearch.server.SolrServerCreator#createSolrServer()
	 */
	@Override
	public SolrServer createSolrServer() {
		return new HttpSolrServer(getSolrHttpAddress());
	}
	
}
