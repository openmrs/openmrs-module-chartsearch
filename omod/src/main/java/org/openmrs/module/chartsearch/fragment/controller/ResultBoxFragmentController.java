
package org.openmrs.module.chartsearch.fragment.controller;


import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class ResultBoxFragmentController {

	public void controller(FragmentModel fragmentModel) {		
		fragmentModel.addAttribute("prevLocations",   SearchAPI.getResults());
	}
	
	

}