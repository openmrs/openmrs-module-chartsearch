package org.openmrs.module.chartsearch.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;

public class SolrManagement {
	
	//private SolrServer solrServer;
	
	public SolrManagement() {
		//this.solrServer = SolrSingleton.getInstance().getServer();
	}
	
	public void shutdown() {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		if (solrServer instanceof EmbeddedSolrServer)
			solrServer.shutdown();
	}
	
}
