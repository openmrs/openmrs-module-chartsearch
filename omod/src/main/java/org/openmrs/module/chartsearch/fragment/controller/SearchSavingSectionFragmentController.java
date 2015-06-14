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

import org.openmrs.Patient;
import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.SearchAPI;
import org.openmrs.module.chartsearch.SearchPhrase;
import org.openmrs.module.chartsearch.page.controller.ChartsearchPageController;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import com.openmrs.module.chartsearch.saving.ChartSearchBookmark;

public class SearchSavingSectionFragmentController {
	
	ChartSearchCache cache = new ChartSearchCache();
	
	public void controller(FragmentModel model) {
		
	}
	
	public boolean checkIfPhraseExisitsInHistory(@RequestParam("searchPhrase") String searchPhrase,
	                                             @RequestParam("patientId") Integer patientId) {
		boolean exists = false;
		
		if (cache.checkIfPhraseExisitsInHistory(searchPhrase, patientId)) {
			exists = true;
		}
		
		return exists;
	}
	
	public JSONObject saveOrUpdateBookmark(@RequestParam("selectedCategories") String selectedCategories,
	                                       @RequestParam("searchPhrase") String searchPhrase,
	                                       @RequestParam("bookmarkName") String bookmarkName,
	                                       @RequestParam("patientId") Integer patientId) {
		if ("none".equals(selectedCategories)) {
			selectedCategories = "";
		}
		return cache.saveOrUpdateBookmark(selectedCategories, searchPhrase, bookmarkName, patientId);
	}
	
	public String checkIfBookmarkExists(@RequestParam("phrase") String phrase,
	                                    @RequestParam("bookmarkName") String bookmarkName,
	                                    @RequestParam("categories") String categories,
	                                    @RequestParam("patientId") Integer patientId) {
		if ("none".equals(categories)) {
			categories = "";
		}
		ChartSearchBookmark bookmark = cache.checkIfBookmarkExistsForPhrase(phrase, categories, patientId);
		if (null != bookmark) {
			return bookmark.getUuid();
		} else
			return "";
	}
	
	public JSONObject getSearchBookmarkSearchDetailsByUuid(@RequestParam("uuid") String uuid) {
		return cache.getSearchBookmarkSearchDetailsByUuid(uuid);
	}
	
	public JSONArray deleteSearchBookmark(@RequestParam("bookmarkUuid") String uuid) {
		return cache.deleteSearchBookmark(uuid);
	}
	
	public String getResultsFromTheServer(FragmentModel model, @RequestParam("phrase") SearchPhrase search_phrase,
	                                      @RequestParam("patientId") Patient patient,
	                                      @RequestParam(value = "categories[]", required = false) String[] categories) {
		SearchAPI searchAPIInstance = SearchAPI.getInstance();
		searchAPIInstance.clearResults();
		
		ChartsearchPageController.searchAndReturnResults(search_phrase, patient, categories, searchAPIInstance, false);
		return GeneratingJson.generateJson(false);
	}
	
	public JSONObject saveANewNoteOnToASearch(@RequestParam("searchPhrase") String searchPhrase,
	                                          @RequestParam("patientId") Integer patientId,
	                                          @RequestParam("comment") String comment,
	                                          @RequestParam("priority") String priority,
	                                          @RequestParam("backgroundColor") String backgroundColor) {
		return cache.saveANewNoteOrCommentOnToASearch(searchPhrase, patientId, comment, priority, backgroundColor);
	}
	
	public JSONObject deleteSearchNote(@RequestParam("uuid") String uuid, @RequestParam("searchPhrase") String searchPhrase,
	                                   @RequestParam("patientId") Integer patientId) {
		return cache.deleteSearchNote(uuid, searchPhrase, patientId);
	}
	
	public JSONObject refreshSearchNotes(@RequestParam("searchPhrase") String searchPhrase,
	                                     @RequestParam("patientId") Integer patientId) {
		JSONObject json = new JSONObject();
		
		GeneratingJson.addBothPersonalAndGlobalNotesToJSON(searchPhrase, patientId, json);
		
		return json;
	}
}
