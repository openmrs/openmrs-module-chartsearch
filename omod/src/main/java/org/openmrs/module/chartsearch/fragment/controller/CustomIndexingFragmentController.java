package org.openmrs.module.chartsearch.fragment.controller;

import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.module.chartsearch.solr.SolrSingleton;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataIndexer;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataSearcher;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class CustomIndexingFragmentController {
	
	public void controller(FragmentModel model) {
	}
	
	public JSONObject getSolrResultsFromTheServer() {
		NonPatientDataIndexer nonI = new NonPatientDataIndexer();
		NonPatientDataSearcher nonS = new NonPatientDataSearcher();
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		
		//Indexing project's data. projectId should come from the UI
		nonI.generateDocumentsAndAddFieldsAndCommitToSolr(solrServer, 1);
		
		JSONObject json = new JSONObject();
		//search against solr to obtain the documents returned but return their number instead
		json.put("noOfDocs", nonS.getNonPatientDocumentList());
		
		return json;
	}
}
