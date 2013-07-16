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

public class Solr {

	private static boolean started;

	private static Log log = LogFactory.getLog(Solr.class);

	private static SolrServer solrServer;
	
	private final static int COMMIT_DELAY = 5000;

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

			solrServer = getSelectedServer();
		} catch (Exception e) {
			log.error("Solr Home Exception");
			e.printStackTrace();
		}
	}
	
	private static SolrServer getSelectedServer(){
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
			return new EmbeddedSolrServer(coreContainer, "");			
			 //new HttpSolrServer("http://localhost:8983/solr");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Solr getInstance() throws Exception {
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

	public static void updateIndex(int personId) {
		if (!isStarted())
			return;
		
		Date lastIndexTime = getLastIndexTime(personId);
		if (lastIndexTime == null) {
			fullImport(personId);
			setLastIndexTime(personId);
		} else {
			deltaImport(personId, lastIndexTime);
			setLastIndexTime(personId);
		}
	}

	public static void removeFromIndex(String uuid) {
		try {
			getInstance().getServer().deleteById(uuid, COMMIT_DELAY);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void fullImport(int personId) {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/dataimport");
		params.set("command", "full-import");
		params.set("clean", false);
		params.set("personId", personId);

		QueryResponse response = null;
		try {
			response = getInstance().getServer().query(params);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static void deltaImport(int personId, Date lastIndexTime) {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/dataimport");
		params.set("command", "delta-import");
		params.set("clean", false);

		params.set("personId", personId);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.set("lastIndexTime", formatter.format(lastIndexTime));

		QueryResponse response = null;
		try {
			response = getInstance().getServer().query(params);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static Date getLastIndexTime(int personId) {
		SolrQuery query = new SolrQuery();
		String queryString = String.format("uuid:%d", personId);
		query.setQuery(queryString);
		try {
			QueryResponse response = getInstance().getServer().query(query);
			if (response.getResults().isEmpty())
				return null;
			return (Date) response.getResults().get(0)
					.getFieldValue("last_index_time");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void setLastIndexTime(int personId) {
		SolrInputDocument document = new SolrInputDocument();
		Date lastIndexTimeUnformatted = Calendar.getInstance().getTime();
		document.addField("last_index_time", lastIndexTimeUnformatted);
		//document.addField("person_id", personId);
		document.addField("uuid", personId);
		try {
			getInstance().getServer().add(document, COMMIT_DELAY);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
			e.printStackTrace();
		}

	}	
	

}
