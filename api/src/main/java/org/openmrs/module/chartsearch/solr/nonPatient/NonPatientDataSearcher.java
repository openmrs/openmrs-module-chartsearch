package org.openmrs.module.chartsearch.solr.nonPatient;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.ChartSearchSyntax;
import org.openmrs.module.chartsearch.solr.SolrSingleton;

public class NonPatientDataSearcher {//TODO may need to add and access this as a bean

	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	public SolrDocumentList getNonPatientDocumentList(String searchText, int projectId) {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		SolrQuery query = new SolrQuery();
		QueryResponse response = null;
		
		if (StringUtils.isBlank(searchText)) {
			searchText = "*";
		}
		ChartSearchSyntax searchSyntax = new ChartSearchSyntax(searchText);
		searchText = searchSyntax.getSearchQuery();
		
		query.setQuery("text:" + searchText);
		query.addFilterQuery("project_id:" + projectId);
		query.setStart(0);
		query.setRows(999999999);
		query.setHighlight(true).setHighlightSnippets(1).setHighlightSimplePre("<b>").setHighlightSimplePost("</b>");
		query.setParam("hl.fl", "text");
		
		try {
			response = solrServer.query(query);
		}
		catch (SolrServerException e) {
			System.out.println("Error generated" + e);
		}
		SolrDocumentList results = response.getResults();
		/*NonPatientDataItem item = new NonPatientDataItem();
		List<ChartListItem> items = new ArrayList<ChartListItem>();
		Iterator<SolrDocument> iterator = results.iterator();
		while (iterator.hasNext()) {
			SolrDocument document = iterator.next();
			SearchProject project = chartSearchService.getSearchProject(projectId);
			List<String> columns = NonPatientDataIndexer.removeSpacesAndSplitLineUsingComma("");
			
		}*/
		
		return results;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
