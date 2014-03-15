
package org.openmrs.module.chartsearch.fragment.controller;


import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;

public class ResultBoxFragmentController {

	public void controller(FragmentModel fragmentModel) {
        SearchAPI searchAPI =SearchAPI.getInstance();
       ArrayList<String> resultList = new ArrayList<String>();
        for(ChartListItem item : searchAPI.getResults()){
            resultList.add("Date: " + item.getObsDate()+ " concept Name : " + item.getConceptName()+" Value: "+ item.getValue()+
                    " Uuid: "+ item.getUuid()+ " Highlights "+ item.getHighlights() + " Obs ID: " + item.getObsId());
        }
		fragmentModel.addAttribute("resultList",   resultList);
	}
	
	

}