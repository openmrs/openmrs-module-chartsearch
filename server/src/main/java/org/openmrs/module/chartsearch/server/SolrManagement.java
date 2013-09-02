package org.openmrs.module.chartsearch.server;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;

public class SolrManagement {
	
	private final SolrServer solrServer;
	
	public SolrManagement(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	public void shutdown() {
		if (solrServer instanceof EmbeddedSolrServer)
			solrServer.shutdown();
	}
	
}
