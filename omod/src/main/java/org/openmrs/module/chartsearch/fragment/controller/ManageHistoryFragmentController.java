/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.fragment.controller;

import net.sf.json.JSONArray;

import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManageHistoryFragmentController {
	
	ChartSearchCache cache = new ChartSearchCache();
	
	public void controller(FragmentModel fragmentModel) {
		fragmentModel.addAttribute("allFoundHistory", GeneratingJson.getAllSearchHistoriesToSendToTheManageUI(true)
		        .toString());
	}
	
	public JSONArray deleteSelectedHistory(@RequestParam("selectedUuids[]") String[] uuids) {
		return cache.deleteHistoryOfSelectedUuids(uuids);
	}
	
	public JSONArray setBookmarkAsDefaultSearch(@RequestParam("selectedBookmarkUuid") String uuid) {
		return cache.setBookmarkAsDefaultSearch(uuid);
	}
}
