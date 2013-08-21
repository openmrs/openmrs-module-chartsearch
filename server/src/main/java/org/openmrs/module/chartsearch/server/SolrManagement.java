package org.openmrs.module.chartsearch.server;

import org.apache.solr.client.solrj.SolrServer;

public class SolrManagement {
	
	private final SolrServer solrServer;
	
	public SolrManagement(SolrServer solrServer){
		this.solrServer = solrServer;
	}
	
	public void shutdown(){
		solrServer.shutdown();
	}

}
