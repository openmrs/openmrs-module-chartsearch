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

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 */
public class IndexManagement {

	private static Log log = LogFactory.getLog(IndexManagement.class);
	private SolrServer solrServer;
	private final int PATIENTS_MAX_COUNT = 40;
	
	public IndexManagement(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public void clearIndexOldData() {
		/*SolrQuery query = new SolrQuery();
		String queryString = "meta:true";
		query.addSort("last_index_time", ORDER.asc);
		query.setFields("id");
		query.setRows(5);
		QueryResponse response;
		try {
			response = solrServer.query(query);
			if (response.getResults().isEmpty()) return;
			List<Integer> personIds = Collections.emptyList();
			Iterator<SolrDocument> iterator = response.getResults().iterator();
			while (iterator.hasNext()) {
				SolrDocument doc = iterator.next();
				Integer personId = (Integer) doc.getFieldValue("id");
				personIds.add(personId);
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		}*/
		
		

	}

}
