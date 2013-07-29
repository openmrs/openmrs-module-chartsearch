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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class Indexer {

	private final SolrServer solrServer;
	
	private static Log log = LogFactory.getLog(Indexer.class);

	private final static int COMMIT_DELAY = 5000;

	public Indexer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public void indexPatiendData(Integer personId) {
		Date lastIndexTime;
		try {
			lastIndexTime = getLastIndexTime(personId);

			if (lastIndexTime == null) {
				fullImport(personId);
			} else {
				deltaImport(personId, lastIndexTime);
			}
		} catch (SolrServerException e) {
			log.error("Error generated", e);
		}
		try {
			setLastIndexTime(personId);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		}

	}

	//TODO rewrite & do more intuitive
	private Date getLastIndexTime(int personId) throws SolrServerException {
		SolrQuery query = new SolrQuery();
		String queryString = String.format("uuid:%d", personId);
		query.setQuery(queryString);
		QueryResponse response = solrServer.query(query);
		if (response.getResults().isEmpty())
			return null;
		return (Date) response.getResults().get(0)
				.getFieldValue("last_index_time");

	}

	private void setLastIndexTime(int personId) throws SolrServerException,
			IOException {
		SolrInputDocument document = new SolrInputDocument();
		Date lastIndexTimeUnformatted = Calendar.getInstance().getTime();
		document.addField("last_index_time", lastIndexTimeUnformatted);
		// document.addField("person_id", personId);
		document.addField("uuid", personId);
		solrServer.add(document, COMMIT_DELAY);

	}

	private void fullImport(int personId) throws SolrServerException {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/dataimport");
		params.set("command", "full-import");
		params.set("clean", false);
		params.set("personId", personId);

		solrServer.query(params);
	}

	private void deltaImport(int personId, Date lastIndexTime)
			throws SolrServerException {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/dataimport");
		params.set("command", "delta-import");
		params.set("clean", false);

		params.set("personId", personId);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.set("lastIndexTime", formatter.format(lastIndexTime));

		solrServer.query(params);
	}

	public void removeFromIndex(String uuid) throws SolrServerException, IOException {
		solrServer.deleteById(uuid, COMMIT_DELAY);

	}

}
