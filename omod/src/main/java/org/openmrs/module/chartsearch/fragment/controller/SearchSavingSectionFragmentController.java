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

import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class SearchSavingSectionFragmentController {
	
	ChartSearchCache cache = new ChartSearchCache();
	
	public void controller(FragmentModel model) {
		
	}
	
	public boolean checkIfPhraseExisitsInHistory(@RequestParam("searchPhrase") String searchPhrase) {
		boolean exists = false;
		
		if (cache.checkIfPhraseExisitsInHistory(searchPhrase)) {
			exists = true;
		}
		
		return exists;
	}
	
	public String saveOrUpdateBookmark(@RequestParam("selectedCategories") String selectedCategories, @RequestParam("searchPhrase") String searchPhrase, @RequestParam("bookmarkName") String bookmarkName,
	                                    @RequestParam("patientId") Integer patientId) {
		return cache.saveOrUpdateBookmark(selectedCategories, searchPhrase, bookmarkName, patientId);
	}
	
	public boolean checkIfBookmarkExists(@RequestParam("phrase") String phrase, @RequestParam("bookmarkName") String bookmarkName, @RequestParam("categories") String categories) {
		if (null != cache.checkIfBookmarkExistsForPhrase(phrase, bookmarkName, categories)) {
			return true;
		} else
			return false;
	}
}
