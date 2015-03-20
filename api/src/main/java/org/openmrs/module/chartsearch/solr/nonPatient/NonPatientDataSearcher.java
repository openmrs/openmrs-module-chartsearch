package org.openmrs.module.chartsearch.solr.nonPatient;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.SolrSingleton;

public class NonPatientDataSearcher {
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	public double getNonPatientDocumentList() {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		SolrQuery query = new SolrQuery();
		query.setQuery("diagn* tes");
		//query.addFilterQuery("cat:electronics", "store:amazon.com");
		
		String allColumns = chartSearchService.getAllColumnNamesFromAllProjectsSeperatedByCommaAndSpace();
		List<String> allColumnsList = NonPatientDataIndexer.removeSpacesAndSplitLineUsingComma(allColumns);
		
		//for (int i = 0; i < allColumnsList.size(); i++) {
		query.setFields("cc_name", "cc_filter_query", "cc_description");
		//}
		//query.addFilterQuery(String.format("project_id:%d", 1));
		
		query.setStart(0);
		query.set("defType", "edismax");
		
		QueryResponse response = null;
		try {
			response = solrServer.query(query);
		}
		catch (SolrServerException e) {
			System.out.println("Error generated" + e);
		}
		SolrDocumentList results = response.getResults();
		for (int i = 0; i < results.size(); ++i) {
			System.out.println(results.get(i));
		}
		return response.getResults().getNumFound();
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
