package org.openmrs.module.chartsearch;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class SolrManagement {
	
	@Autowired
	private SolrServer solrServer;
	
	public void setSolrServer(SolrServer solrServer){
		this.solrServer = solrServer;
	}
	
	public void shutdown(){
		solrServer.shutdown();
	}

}
