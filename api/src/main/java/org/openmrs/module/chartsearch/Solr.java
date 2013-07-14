package org.openmrs.module.chartsearch;

import java.io.File;
import java.io.FileNotFoundException;

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
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;

public class Solr {

	private static boolean started;

	private static Log log = LogFactory.getLog(Solr.class);

	private static SolrServer solrServer;

	private Solr() {

	}

	public static boolean isStarted() {
		return started;
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

			/*
			 * System.setProperty( "solr.solr.home",
			 * "E:\\eclipse\\workspace\\openmrs\\chartsearch\\EmbeddedSolrTest\\src\\main\\resources"
			 * );
			 */
			CoreContainer.Initializer initializer = new CoreContainer.Initializer();
			CoreContainer coreContainer;
			try {
				coreContainer = initializer.initialize();
				solrServer = new EmbeddedSolrServer(coreContainer, "");
				// new HttpSolrServer("http://localhost:8983/solr");
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
		/*
		 * if (SolrEngineHolder.INSTANCE == null) { SolrEngineHolder.INSTANCE =
		 * new Solr(); SolrEngineHolder.INSTANCE.init(); }
		 */

		return SolrEngineHolder.INSTANCE;
	}

	public static void startServer() {
		if (isStarted())
			return;

		SolrEngineHolder.INSTANCE = new Solr();
		SolrEngineHolder.INSTANCE.init();
		started = true;

	}

	public static void shutdownServer() {
		if (!isStarted())
			return;

		SolrEngineHolder.INSTANCE.solrServer.shutdown();
		SolrEngineHolder.INSTANCE.solrServer = null;
		SolrEngineHolder.INSTANCE = null;
		started = false;
	}

	/**
	 * Gets an instance of the solr server
	 * 
	 * @return SolrServer
	 */
	public SolrServer getServer() {
		return solrServer;
	}

	public static void addToIndex(int personId) {
		if (!isStarted())
			return;
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/dataimport");
		params.set("command", "full-import");
		params.set("clean", false);
		params.set("personId", personId);

		QueryResponse response = null;
		try {
			response = getInstance().getServer().query(params);
			query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void removeFromIndex(String uuid) {
		try {
			getInstance().getServer().deleteById(uuid, 10000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void query() {
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		QueryResponse response;
		try {
			response = getInstance().getServer().query(query);
			SolrDocumentList list = response.getResults();
			for (SolrDocument solrDocument : list) {
				log.info(solrDocument);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
