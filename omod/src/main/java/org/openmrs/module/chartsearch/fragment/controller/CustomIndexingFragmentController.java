package org.openmrs.module.chartsearch.fragment.controller;

import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.module.chartsearch.solr.SolrSingleton;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataIndexer;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class CustomIndexingFragmentController {
	
	public void controller(FragmentModel model) {
	}
	
	public JSONObject getSolrResultsFromTheServer() {
		NonPatientDataIndexer nonP = new NonPatientDataIndexer();
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		
		nonP.generateDocumentsAndAddFieldsAndCommitToSolr(solrServer, 1);
		
		return null;
	}
}
