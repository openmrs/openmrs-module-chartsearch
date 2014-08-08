package org.openmrs.module.chartsearch.fragment.controller;

import java.util.ArrayList;

import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class ResultBoxFragmentController {
	
	public void controller(FragmentModel fragmentModel) {
		SearchAPI searchAPI = SearchAPI.getInstance();
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.add(GeneratingJson.generateJson()); //generate json and add it to the result from the search
		searchAPI.clearResults(); //clear the previous results
		fragmentModel.addAttribute("resultList", resultList); //bind the result list for the view
	}
	
}
