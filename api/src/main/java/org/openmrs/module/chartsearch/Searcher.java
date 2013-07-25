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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 */
public class Searcher {

	private static Log log = LogFactory.getLog(Searcher.class);

	private final SolrServer solrServer;
	
	public Searcher(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	public String query() {
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		QueryResponse response;
			try {
				response = solrServer.query(query);
				return response.toString();
			} catch (SolrServerException e) {
				log.error("Error generated", e);
			}
			/*SolrDocumentList list = response.getResults();
			for (SolrDocument solrDocument : list) {
				log.info(solrDocument);*/
			return "";			

	}

}
