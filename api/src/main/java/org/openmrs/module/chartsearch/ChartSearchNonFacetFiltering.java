/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;

/**
 * Handles all the logic needed to support Allergies and Appointments etc category filtering which
 * is not supported using solr faceting
 */
public class ChartSearchNonFacetFiltering {
	
	/**
	 * Checks if categories collection contains a specified categgory such as allergies and
	 * appointments
	 * 
	 * @param categories
	 * @param categoryName
	 * @return
	 */
	public boolean checkIfCategoriesContainNonFacetCategory(List<String> categories, String categoryName) {
		boolean contains = false;
		if (categories != null && !categories.isEmpty() && categories.size() > 1 && StringUtils.isNotBlank(categoryName)) {
			for (int i = 0; i < categories.size(); i++) {
				if (categories.get(i).equals(categoryName)) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}
	
	public boolean checkifCategoriesContainsOnlyOneNonFacetCategory(List<String> categories, String categoryName) {
		boolean contains = false;
		if (categories != null && !categories.isEmpty() && StringUtils.isNotBlank(categoryName)) {
			if (categories.size() == 1 && categories.get(0).equals(categoryName)) {
				contains = true;
			}
		}
		
		return contains;
	}
	
	public void applyNonFacetingLogicWhileSearching(Integer patientId, String searchText, List<String> selectedCategories,
	                                               SolrServer solrServer, SolrQuery query, List<ChartListItem> list)
	    throws SolrServerException {
		ChartSearchSearcher searcher = new ChartSearchSearcher();
		
		if (checkifCategoriesContainsOnlyOneNonFacetCategory(selectedCategories, "allergies")) {
			searcher.searchAllergiesAndGenerateSolrDoc(patientId, searchText, solrServer, list);
		} else if (checkifCategoriesContainsOnlyOneNonFacetCategory(selectedCategories, "appointments")) {
			searcher.searchAppointmentsAndGenerateSolrDoc(patientId, searchText, solrServer, list);
		} else if (checkIfCategoriesContainNonFacetCategory(selectedCategories, "allergies")
		        && !checkIfCategoriesContainNonFacetCategory(selectedCategories, "appointments")) {
			searcher.searchObservationsAndGenerateSolrDoc(solrServer, query, list);
			searcher.searchAllergiesAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchEncounterTypesAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchEFormsAndGenerateSolrDoc(searchText, solrServer, list);
		} else if (!checkIfCategoriesContainNonFacetCategory(selectedCategories, "allergies")
		        && checkIfCategoriesContainNonFacetCategory(selectedCategories, "appointments")) {
			searcher.searchObservationsAndGenerateSolrDoc(solrServer, query, list);
			searcher.searchAppointmentsAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchEncounterTypesAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchEFormsAndGenerateSolrDoc(searchText, solrServer, list);
		} else {
			searcher.searchObservationsAndGenerateSolrDoc(solrServer, query, list);
			searcher.searchAllergiesAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchAppointmentsAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchEncounterTypesAndGenerateSolrDoc(patientId, searchText, solrServer, list);
			searcher.searchEFormsAndGenerateSolrDoc(searchText, solrServer, list);
		}
	}
	
}
