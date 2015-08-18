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
import net.sf.json.JSONObject;

import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring controller for the manageBookmarks.gsp fragment
 */
public class ManageBookmarksFragmentController {
	
	ChartSearchCache cache = new ChartSearchCache();
	
	public void controller(FragmentModel fragmentModel, UiUtils ui) {
		fragmentModel.addAttribute("allFoundBookmarks",
		    ui.escapeJs(GeneratingJson.getAllSearchBookmarksToReturnTomanagerUI().toString()));
	}
	
	public JSONObject fetchBookmarkDetails(@RequestParam("uuid") String uuid) {
		return cache.getSearchBookmarkSearchDetailsByUuid(uuid);
	}
	
	public JSONArray deleteSelectedBookmarks(@RequestParam("selectedUuids[]") String[] uuids) {
		return cache.deleteBookmarkOfSelectedUuids(uuids);
	}
	
	public JSONArray saveBookmarkProperties(@RequestParam("bookmarkUuid") String uuid,
	                                        @RequestParam("bookmarkName") String bookmarkName,
	                                        @RequestParam("searchPhrase") String searchPhrase,
	                                        @RequestParam(value = "selectedCategories", required = false) String selectedCategories) {
		return cache.saveBookmarkProperties(uuid, bookmarkName, searchPhrase, selectedCategories);
	}
	
	public JSONArray deleteBookmarkInTheDialog(@RequestParam("bookmarkUuid") String uuid) {
		return cache.deleteBookmarkInTheDialog(uuid);
	}
	
	public JSONArray setBookmarkAsDefaultSearch(@RequestParam("selectedUuid") String uuid) {
		return cache.setBookmarkAsDefaultSearch(uuid);
	}
}
