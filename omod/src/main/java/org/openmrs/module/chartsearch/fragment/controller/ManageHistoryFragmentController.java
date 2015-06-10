package org.openmrs.module.chartsearch.fragment.controller;

import net.sf.json.JSONArray;

import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManageHistoryFragmentController {
	ChartSearchCache cache = new ChartSearchCache();
	
	public void controller(FragmentModel fragmentModel) {
		fragmentModel.addAttribute("allFoundHistory", GeneratingJson.getAllSearchHistoriesToSendToTheManageUI(true).toString());//bind the result list for the view
	}
	
	public JSONArray deleteSelectedHistory(@RequestParam("selectedUuids[]") String[] uuids) {
		return cache.deleteHistoryOfSelectedUuids(uuids);
	}
}
