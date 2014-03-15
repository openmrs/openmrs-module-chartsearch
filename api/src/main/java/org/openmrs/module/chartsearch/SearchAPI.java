package org.openmrs.module.chartsearch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchAPI {
	//private static String searchPhrase;
	private static List<ChartListItem> resultList;
    private static SearchAPI instanse;

    public static SearchAPI getInstance(){
        if(instanse == null){
            instanse = new SearchAPI();
        }
        return instanse;
    }

	private SearchAPI(){
		resultList = new ArrayList<ChartListItem>();
        ChartListItem noData = new ChartListItem();
	}

	/*private SearchAPI(String searchPhrase){
		SearchAPI.setSearchPhrase(searchPhrase);
	}*/

/*	public static void search(){
		//todo
		List<ChartListItem> temp = new ArrayList<ChartListItem>();
		temp.add(getSearchPhrase());
		showResults(temp);
	}*/

	public List<ChartListItem> showResults(List<ChartListItem> results){
		setResults(results);
		return results;
	}


	/*public String getSearchPhrase() {
		return searchPhrase;
	}*/

/*	public static void setSearchPhrase(String searchPhrase) {
		if(searchPhrase == null)
            SearchAPI.searchPhrase ="default search phrase";
		else
			SearchAPI.searchPhrase = searchPhrase;
	}*/

	public List<ChartListItem> getResults() {
		return resultList;
	}

	public void setResults(List<ChartListItem> results) {
		SearchAPI.resultList = results;
	}






}

















