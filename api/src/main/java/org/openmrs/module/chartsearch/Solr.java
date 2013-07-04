package org.openmrs.module.chartsearch;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;

public class Solr {

	private static Solr solr;
	private SolrServer solrServer;

	private Solr() {

	}

	private void init() {
		//Get the solr home folder
		String solrHome = Context.getAdministrationService().getGlobalProperty("chartsearch.home",
		    new File(OpenmrsUtil.getApplicationDataDirectory(), "chartsearch").getAbsolutePath());
		
		//Tell solr that this is our home folder
		System.setProperty("solr.solr.home", solrHome);
		
		CoreContainer.Initializer initializer = new CoreContainer.Initializer();
		CoreContainer coreContainer;
		try {
			coreContainer = initializer.initialize();
			solrServer = new EmbeddedSolrServer(coreContainer, "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Solr getInstance() {
		if (solr == null) {
			solr = new Solr();
			solr.init();
		}

		return solr;
	}

	public SolrServer getServer() {
		return solrServer;
	}

	// public static

}
