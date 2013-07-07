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

	private static Log log = LogFactory.getLog(Solr.class);

	private SolrServer solrServer;

	private Solr() {

	}

	private static class SolrEngineHolder {

		private static Solr INSTANCE = null;
	}

	private void init() {
		try {
			String solrHome = Context.getAdministrationService()
					.getGlobalProperty(
							"chartsearch.home",
							new File(OpenmrsUtil.getApplicationDataDirectory(),
									"chartsearch").getAbsolutePath());

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
		} catch (Exception e) {
			log.error("Solr Home Exception");
			e.printStackTrace();
		}
	}

	/**
	 * Get the static/singular instance of the module class loader
	 * 
	 * @return OpenmrsClassLoader
	 */
	public static Solr getInstance() throws Exception {
		if (SolrEngineHolder.INSTANCE == null) {
			SolrEngineHolder.INSTANCE = new Solr();
			SolrEngineHolder.INSTANCE.init();
		}

		return SolrEngineHolder.INSTANCE;
	}

	/**
	 * Gets an instance of the solr server
	 * 
	 * @return SolrServer
	 */
	public SolrServer getServer() {
		return solrServer;
	}

}
