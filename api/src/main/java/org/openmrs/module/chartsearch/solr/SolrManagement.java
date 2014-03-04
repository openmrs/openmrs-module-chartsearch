package org.openmrs.module.chartsearch.solr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;

public class SolrManagement {
	
	//private SolrServer solrServer;

	protected Log log = LogFactory.getLog(getClass());	
	public SolrManagement() {
		//this.solrServer = SolrSingleton.getInstance().getServer();
	}
	
	public void shutdown() {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		if (solrServer == null) {
			log.warn("SolrServer is null");
			return;
		}
		if (solrServer instanceof EmbeddedSolrServer)
			solrServer.shutdown();
	}
	
}
