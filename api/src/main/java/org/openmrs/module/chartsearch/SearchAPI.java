package org.openmrs.module.chartsearch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchAPI {
	private static String searchPhrase;
	private static List<String> resultList;

	public SearchAPI(){
		resultList = new ArrayList<String>();
	}

	public SearchAPI(String searchPhrase){
		SearchAPI.setSearchPhrase(searchPhrase);
	}

	public static void search(){
		//todo
		List<String> temp = new ArrayList<String>();
		temp.add(getSearchPhrase());
		showResults(temp);
	}

	public static List<String> showResults(List<String> results){
		SearchAPI.setResults(results);
		return results;
	}


	public static String getSearchPhrase() {
		return searchPhrase;
	}

	public static void setSearchPhrase(String searchPhrase) {
		if(searchPhrase == null)
            SearchAPI.searchPhrase ="default search phrase";
		else
			SearchAPI.searchPhrase = searchPhrase;
	}

	public static List<String> getResults() {
		return resultList;
	}

	public static void setResults(List<String> results) {
		SearchAPI.resultList = results;
	}






}

















