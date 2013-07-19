package org.openmrs.module.chartsearch;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;

public class SolrUtil {

	private static Log log = LogFactory.getLog(SolrUtil.class);

	private SolrServer solrServer;

	/**
	 * Gets an instance of the solr server
	 * 
	 * @return SolrServer
	 */
	public SolrServer getSolrServer() {
		return solrServer;
	}	
	
	private void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	private void init() {
		try {
			setSolrServer(getSelectedServer());
		} catch (Exception e) {
			log.error("Solr Home Exception");
			e.printStackTrace();
		}
	}
	
	private SolrServer getSelectedServer(){
		
	}/*
	public static void shutdownServer() { 	
		SolrEngineHolder.INSTANCE.solrServer.shutdown();
		SolrEngineHolder.INSTANCE.solrServer = null;
		SolrEngineHolder.INSTANCE = null;
		started = false;
	}
*/
	

	
	

}
