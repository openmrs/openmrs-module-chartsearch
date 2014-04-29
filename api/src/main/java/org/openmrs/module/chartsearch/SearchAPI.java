package org.openmrs.module.chartsearch;
import java.util.ArrayList;
import java.util.List;

public class SearchAPI {
	//private static String searchPhrase;
	private static List<ChartListItem> resultList;
    private static SearchAPI instance;

    public static SearchAPI getInstance(){
        if(instance == null){
            instance = new SearchAPI();
        }
        return instance;
    }

	private SearchAPI(){
		resultList = new ArrayList<ChartListItem>();
	}



	public List<ChartListItem> showResults(List<ChartListItem> results){
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






}

















