
package org.openmrs.module.chartsearch.fragment.controller;


import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;

public class ResultBoxFragmentController {

	public void controller(FragmentModel fragmentModel)
    {
        SearchAPI searchAPI =SearchAPI.getInstance();
       ArrayList<String> resultList = new ArrayList<String>();
        resultList.add(GeneratingJson.generateJson());
        searchAPI.clearResults();
		fragmentModel.addAttribute("resultList",   resultList);
	}
	
	

}