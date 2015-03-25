package org.openmrs.module.chartsearch.fragment.controller;

import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.module.chartsearch.NonPatientToBeIndexedProject;
import org.openmrs.module.chartsearch.solr.SolrSingleton;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataIndexer;
import org.openmrs.module.chartsearch.solr.nonPatient.NonPatientDataSearcher;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestBody;

public class CustomIndexingFragmentController {
	
	public void controller(FragmentModel model) {
	}
	
	public JSONObject indexDataForANewProject(@RequestBody NonPatientToBeIndexedProject project) {
		String projectName = project.getProjectName();
		NonPatientDataIndexer nonI = new NonPatientDataIndexer();
		NonPatientDataSearcher nonS = new NonPatientDataSearcher();
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		
		//Indexing project's data. projectId should come from the UI, 
		//testing chartsearch in the first place which is id = 1
		nonI.generateDocumentsAndAddFieldsAndCommitToSolr(solrServer, 1, false);
		
		JSONObject json = new JSONObject();
		//search against solr to obtain the documents returned but return their number instead
		json.put("noOfDocs", nonS.getNonPatientDocumentList("", 1));
		
		return json;
	}
}
