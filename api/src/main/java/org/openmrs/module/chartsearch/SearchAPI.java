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
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;
import org.openmrs.module.chartsearch.synonyms.SynonymsAPI;

public class SearchAPI {
	
	private static SearchPhrase searchPhrase;
	
	private static List<ChartListItem> resultList;
	
	private static SearchAPI instance;
	
	private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);
	
	private static List<String> selectedCategoryNames;
	
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
	
	public List<ChartListItem> search(Integer patientId, SearchPhrase searchPhrase, List<String> selectedCategoryNames) {
		SearchAPI.searchPhrase = searchPhrase;
		SearchAPI.setSelectedCategoryNames(selectedCategoryNames);
		System.out.println("phrase :" + searchPhrase.getPhrase());
		if (searchPhrase.getPhrase().equals(",")) {
			searchPhrase.setPhrase("");
		}
		
		String searchPhraseStr = searchPhrase.getPhrase();
		
		Integer length = Integer.valueOf(999999999); //amount of obs we want - all of them
		Integer start = Integer.valueOf(0);//starting from first obs.
		List<ChartListItem> items = new ArrayList<ChartListItem>();
		
		String finalPhrase;
		finalPhrase = SynonymsAPI.getSynonymsForSearch(searchPhraseStr);
		
		System.out.println("finalPhrase :" + finalPhrase);
		
		try {
			items = searcher.getDocumentList(patientId, finalPhrase, start, length, getSelectedCategoryNames()); //searching for the phrase.
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
}
