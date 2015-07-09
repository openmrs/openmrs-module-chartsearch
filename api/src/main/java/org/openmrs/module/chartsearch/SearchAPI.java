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
package org.openmrs.module.chartsearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;
import org.openmrs.module.chartsearch.synonyms.SynonymsAPI;

public class SearchAPI {
	
	private static SearchPhrase searchPhrase;
	
	private static List<ChartListItem> resultList;
	
	private static SearchAPI instance;
	
	private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);
	
	private static List<String> selectedCategoryNames;
	
	private static double retrievalTime;
	
	private static Integer patientId;
	
	public static SearchAPI getInstance() {
		if (instance == null) {
			instance = new SearchAPI();
		}
		return instance;
	}
	
	private SearchAPI() {
		resultList = new ArrayList<ChartListItem>();
	}
	
	public List<ChartListItem> showResults(List<ChartListItem> results) {
		setResults(results);
		return results;
	}
	
	public List<ChartListItem> getResults() {
		return resultList;
	}
	
	public void setResults(List<ChartListItem> results) {
		SearchAPI.resultList = results;
	}
	
	public void clearResults() {
		SearchAPI.resultList.clear();
	}
	
	public List<ChartListItem> search(Integer patientId, SearchPhrase searchPhrase, List<String> selectedCategoryNames,
	                                  boolean reloadWholePage) {
		SearchAPI.patientId = patientId;
		List<String> categories = null;
		ChartSearchCache cache = new ChartSearchCache();
		
		if (reloadWholePage && (searchPhrase.getPhrase().equals(",") || searchPhrase.getPhrase().equals(""))) {
			JSONObject defaultSearchProps = cache.returnDefaultSearchPhrase(searchPhrase.getPhrase(),
			    SearchAPI.getPatientId());
			String phrase = (String) defaultSearchProps.get("searchPhrase");
			List<String> cats = (List<String>) defaultSearchProps.get("selectedCategories");
			SearchAPI.searchPhrase = new SearchPhrase(phrase);
			
			searchPhrase.setPhrase(phrase);
			if (cats != null && !cats.isEmpty()) {
				categories = cats;
			} else {
				categories = new ArrayList<String>();
			}
		} else {
			SearchAPI.searchPhrase = searchPhrase;
			categories = selectedCategoryNames;
		}
		SearchAPI.selectedCategoryNames = categories;
		
		String searchPhraseStr = searchPhrase.getPhrase();
		
		Integer length = Integer.valueOf(999999999); //amount of obs we want - all of them
		Integer start = Integer.valueOf(0);//starting from first obs.
		List<ChartListItem> items = new ArrayList<ChartListItem>();
		
		String finalPhrase;
		finalPhrase = SynonymsAPI.getSynonymsForSearch(searchPhraseStr);
		
		System.out.println("finalPhrase :" + finalPhrase);
		
		double startSearchingTime = new Date().getTime();
		try {
			items = searcher.getDocumentList(patientId, finalPhrase, start, length, getSelectedCategoryNames()); //searching for the phrase.
			
			//saving search record where necessary every after a search.
			cache.saveOrUpdateSearchHistory(finalPhrase, patientId);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		double endSearchingTime = new Date().getTime();
		
		SearchAPI.retrievalTime = (endSearchingTime - startSearchingTime) / 1000.0;
		
		return items;
	}
	
	public SearchPhrase getSearchPhrase() {
		return searchPhrase;
	}
	
	public void setSearchPhrase(SearchPhrase searchPhrase) {
		SearchAPI.searchPhrase = searchPhrase;
	}
	
	public static List<String> getSelectedCategoryNames() {
		return selectedCategoryNames;
	}
	
	public static void setSelectedCategoryNames(List<String> selectedCategoryNames) {
		SearchAPI.selectedCategoryNames = selectedCategoryNames;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
	
	/**
	 * Time taken to retrieve results in seconds
	 */
	public static double getRetrievalTime() {
		return retrievalTime;
	}
	
	public static Integer getPatientId() {
		return patientId;
	}
	
}
