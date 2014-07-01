/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.chartsearch.solr;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.FacetParams;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.EncounterItem;
import org.openmrs.module.chartsearch.FormItem;
import org.openmrs.module.chartsearch.ObsItem;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.categories.CategoryFilter;

/**
 * Handles all searches from the Chart Search Module UI from its ChartsearchPageController
 */
public class ChartSearchSearcher {
	
	private static Log log = LogFactory.getLog(ChartSearchSearcher.class);
	
	private ChartSearchService chartSearchService;
	
	private List<Count> facetFieldValueNamesAndCounts;
	
	public ChartSearchService getChartSearchService() {
		if (Context.isAuthenticated()) {
			chartSearchService = Context.getService(ChartSearchService.class);
		} else
			log.debug("Not Authenticated!!!");
		return chartSearchService;
	}
	
	/**
	 * stores and returns FacetField.Count values
	 * 
	 * @return facetFieldValueNamesAndCounts
	 */
	public List<Count> getFacetFieldValueNamesAndCounts() {
		return facetFieldValueNamesAndCounts;
	}
	
	public ChartSearchSearcher() {
		//this.solrServer = SolrSingleton.getInstance().getServer();
	}
	
	public Long getDocumentListCount(Integer patientId, String searchText) throws Exception {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		searchText = StringUtils.isNotBlank(searchText) ? searchText : "*";
		if (StringUtils.isNumeric(searchText)) {
			searchText = searchText + ".*" + " || " + searchText;
		}
		
		SolrQuery query = new SolrQuery(String.format("text:(%s)", searchText));
		query.addFilterQuery(String.format("person_id:%d", patientId));
		query.setRows(0); // Intentionally setting to this value such that we
		// get the count very quickly.
		QueryResponse response = solrServer.query(query);
		return response.getResults().getNumFound();
	}
	
	public List<ChartListItem> getDocumentList(Integer patientId, String searchText, Integer start, Integer length,
	                                           List<String> selectedCategories) throws Exception {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();

		//TODO re-use ChartSearchSntax class hear
		System.out.println("searchText: " +searchText);
		ChartSearchSyntax searchSyntax = new ChartSearchSyntax(searchText);
		searchText = searchSyntax.getSearchQuery();
		
		// TODO Move to Eli's code
		searchText = StringUtils.isNotBlank(searchText) ? searchText : "*";
		if (StringUtils.isNumeric(searchText)) {
			//this allows 36 returning 36.* for numerics
			searchText = searchText + ".*" + " || " + searchText;
		}
		
		SolrQuery query = new SolrQuery(String.format("text:(%s)", searchText));
		query.addFilterQuery(String.format("person_id:%d", patientId));
		
		//TODO add selected categories to the query here or use all categories
		addSelectedFilterQueriesToQuery(query, selectedCategories);
		
		query.setStart(start);
		query.setRows(length);
		query.setHighlight(true).setHighlightSnippets(1).setHighlightSimplePre("<b>").setHighlightSimplePost("</b>");
		query.setParam("hl.fl", "text");
		
		//TODO add facet fields here and if more than one, getAndUseCountsForFacetFields(..)
		query.remove(FacetParams.FACET_FIELD);
		query.setFacet(true);
		//adding facet field for concept_class
		query.addFacetField("concept_class_name");
		
		System.out.println("Observations:");
		QueryResponse response = solrServer.query(query);
		
		this.facetFieldValueNamesAndCounts = getAndUseFacetFieldsNamesAndCounts(response);
		
		Iterator<SolrDocument> iter = response.getResults().iterator();
		
		List<ChartListItem> list = new ArrayList<ChartListItem>();
		while (iter.hasNext()) {
			SolrDocument document = iter.next();
			
			String uuid = (String) document.get("id");
			Integer obsId = (Integer) document.get("obs_id");
			Date obsDate = (Date) document.get("obs_datetime");
			Integer obsGroupId = (Integer) document.get("obs_group_id");
			List<String> values = ((List<String>) document.get("value"));
			
			String value = "";
			if (values != null) {
				value = values.get(0);
			}
			
			String conceptName = (String) document.get("concept_name");
			
			ObsItem item = new ObsItem();
			item.setUuid(uuid);
			item.setObsId(obsId);
			item.setConceptName(conceptName);
			item.setObsDate(obsDate.toString());
			item.setObsGroupId(obsGroupId);
			item.setValue(value);
			
			if (response.getHighlighting().get(uuid) != null) {
				List<String> highlights = response.getHighlighting().get(uuid).get("text");
				if (highlights != null && !highlights.isEmpty()) {
					item.setHighlights(new ArrayList<String>(highlights));
				}
			}
			list.add(item);
			System.out.println(document.get("obs_id") + ", " + document.get("concept_name") + ", "
			        + document.get("obs_datetime"));
		}
		
		// Encounters
		System.out.println("Encounters:");
		SolrQuery query3 = new SolrQuery(String.format("encounter_type:(%s)", searchText));
		query3.addFilterQuery(String.format("patient_id:%d", patientId));
		QueryResponse response3 = solrServer.query(query3);
		Iterator<SolrDocument> iter3 = response3.getResults().iterator();
		
		while (iter3.hasNext()) {
			SolrDocument document = iter3.next();
			EncounterItem item = new EncounterItem();
			item.setUuid((String) document.get("id"));
			item.setEncounterId((Integer) document.get("encounter_id"));
			item.setEncounterType((String) document.get("encounter_type"));
			list.add(item);
			
			System.out.println(document.get("encounter_id") + ", " + document.get("encounter_type") + ", "
			        + document.get("encounter_datetime"));
		}
		
		// forms
		System.out.println("Forms:");
		SolrQuery query2 = new SolrQuery(String.format("form_name:(%s)", searchText));
		QueryResponse response2 = solrServer.query(query2);
		Iterator<SolrDocument> iter2 = response2.getResults().iterator();
		
		while (iter2.hasNext()) {
			SolrDocument document = iter2.next();
			FormItem item = new FormItem();
			item.setUuid((String) document.get("id"));
			item.setEncounterType((String) document.get("encounter_type_name"));
			item.setFormId((Integer) document.get("form_id"));
			item.setFormName((String) document.get("form_name"));
			list.add(item);
			
			System.out.println(document.get("form_id") + ", " + document.get("form_name") + ", "
			        + document.get("encounter_type_name"));
		}
		
		return list;
	}
	
	/**
	 * Adds filter Queries to the query for selected categories returned from the UI
	 * 
	 * @param query
	 * @param selectedCats
	 */
	public void addSelectedFilterQueriesToQuery(SolrQuery query, List<String> selectedCats) {
		String filterQuery = "";
		LinkedList<String> selectedCategories = new LinkedList<String>();
		selectedCategories.addAll(selectedCats);
		if (selectedCategories == null || selectedCategories.isEmpty()) {
			System.out.println("Either selectedCategories is null or empty.");
		} else {
			LinkedList<CategoryFilter> existingCategories = new LinkedList<CategoryFilter>();
			existingCategories.addAll(getChartSearchService().getAllCategoryFilters());
			int indexOfFirstSelected = selectedCategories.indexOf(selectedCategories.getFirst());
			int indexOfLastSelected = selectedCategories.indexOf(selectedCategories.getLast());
			int indexOfFirstExisting = existingCategories.indexOf(existingCategories.getFirst());
			int indexOfLastExisting = existingCategories.indexOf(existingCategories.getLast());
			
			for (int i = indexOfFirstSelected; i <= indexOfLastSelected; i++) {
				String currentSelected = selectedCategories.get(i);
				for (int j = indexOfFirstExisting; j <= indexOfLastExisting; j++) {
					CategoryFilter currentExisting = existingCategories.get(j);
					String currentExistingName = currentExisting.getCategoryName();
					if (currentSelected.equals(currentExistingName.toLowerCase())) {
						if (i != indexOfLastSelected) {
							filterQuery += currentExisting.getFilterQuery() + " OR ";
						} else
							filterQuery += currentExisting.getFilterQuery();
					}
				}
			}
			query.addFilterQuery(filterQuery);
		}
	}
	
	/**
	 * Looks for any added filter fields onto the query and and returns its value names and counts
	 * 
	 * @param response
	 */
	public List<Count> getAndUseFacetFieldsNamesAndCounts(QueryResponse response) {
		List<FacetField> facets = response.getFacetFields();
		LinkedList<FacetField> conceptNameFacets = new LinkedList<FacetField>();
		conceptNameFacets.addAll(facets);
		
		//TODO use iteration when adding other facet fields rather than concept class
		FacetField conceptNameFacet = conceptNameFacets.get(0);
		return conceptNameFacet.getValues();
	}
}
