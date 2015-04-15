package org.openmrs.module.chartsearch.fragment.controller;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocumentList;
import org.openmrs.module.chartsearch.SearchProjectAccess;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class NonPatientSearchesFragmentController {
	
	SearchProjectAccess accessSearchProject = new SearchProjectAccess();
	
	public void controller(FragmentModel model) {
		List<String> initiallyIndexed = accessSearchProject.getInitiallyIndexedSearchProjectNames();
		model.put("initiallyIndexedProjects", initiallyIndexed);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject searchNonPatientSpecificDataForAlreadyIndexed(@RequestParam("selectedProject") String selectedProject,
	                                                                @RequestParam("searchText") String searchText) {
		JSONObject json = new JSONObject();
		if (StringUtils.isNotBlank(searchText) && StringUtils.isNotBlank(selectedProject)) {
			SolrDocumentList results = accessSearchProject.searchNonPatientSpecificDataForAlreadyIndexed(searchText,
			    selectedProject);
			
			if (results.isEmpty()) {
				json.put("noResultsReturned", "No Results were found to match: <b>" + searchText
				        + "</b> Or there was no index to search against.");
			}
			json.put("returnedSearchedResults", results);
			json.put("solrFieldNames", accessSearchProject.getAllFieldsOfASearchProject(selectedProject));
		}
		List indexedSPs = accessSearchProject.getInitiallyIndexedSearchProjectNames();
		String[] indexedSPsArr = new String[indexedSPs.size()];
		
		indexedSPsArr = (String[]) indexedSPs.toArray(indexedSPsArr);
		json.put("initiallyIndexedProjects", indexedSPsArr);
		
		return json;
	}
}
