package org.openmrs.module.chartsearch;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;

import java.util.ArrayList;
import java.util.List;

public class SearchAPI {
	//private static String searchPhrase;
	private static List<ChartListItem> resultList;
    private static SearchAPI instance;
    private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);

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

    public List<ChartListItem> search(Integer patientId, SearchPhrase searchPhrase){
        System.out.println("phrase :" + searchPhrase.getPhrase());
        if(searchPhrase.getPhrase().equals(",")){
            searchPhrase.setPhrase("");
        }

        String finalPhrase = searchPhrase.getPhrase();

        Integer length = Integer.valueOf(999999999); //amount of obs we want - all of them
        Integer start = Integer.valueOf(0);//starting from first obs.
        List<ChartListItem> items = new ArrayList<ChartListItem>();

        /*String synonyms = search_phrase.getPhrase();
        SynonymGroup synGroup = SynonymGroups.isSynonymContainedInGroup(search_phrase.getPhrase());
        if (!synGroup.equals(null)) {
            for (String syn : (HashSet<String>) synGroup.getSynonyms()) {
                synonyms += " OR " + syn;
            }
        }*/

        try {
            items = searcher.getDocumentList(patientId, finalPhrase, start, length); //searching for the phrase.
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }


    private <T> T getComponent(Class<T> clazz) {
        List<T> list = Context.getRegisteredComponents(clazz);
        if (list == null || list.size() == 0)
            throw new RuntimeException("Cannot find component of " + clazz);
        return list.get(0);
    }


}

















