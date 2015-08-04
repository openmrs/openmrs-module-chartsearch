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

import java.util.ArrayList;
import java.util.Arrays;

import net.sf.json.JSONObject;

import org.openmrs.module.chartsearch.AllColors;
import org.openmrs.module.chartsearch.ChartSearchCache;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManagePreferencesFragmentController {
	
	ChartSearchCache cache = new ChartSearchCache();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void controller(FragmentModel model, UiUtils ui) {
		AllColors allColors = new AllColors();
		String[] allColorsArray = new String[allColors.ALLCOLORSLENGTH];
		ArrayList list = new ArrayList(Arrays.asList(allColors));
		
		list.addAll(Arrays.asList(allColors.REDBASEDCOLORS));
		list.addAll(Arrays.asList(allColors.GREENBASEDCOLORS));
		list.addAll(Arrays.asList(allColors.BLUEBASEDCOLORS));
		for (int i = 0; i < allColorsArray.length; i++) {
			if (list.get(i) instanceof String) {
				allColorsArray[i] = (String) list.get(i);
			}
		}
		allColors.printAllColors(true);
		
		model.put("preferences", GeneratingJson.generateRightMatchedPreferencesJSON().toString());
		model.put("daemonPreferences", GeneratingJson.generateDaemonPreferencesJSON().toString());
		model.put("categoryFilters", ui.escapeJs(GeneratingJson.generateAllCategoriesJSON().toString()));
		model.put("allColors", allColorsArray);
		model.put("redBasedColors", allColors.REDBASEDCOLORS);
		model.put("greenBasedColors", allColors.GREENBASEDCOLORS);
		model.put("blueBasedColors", allColors.BLUEBASEDCOLORS);
	}
	
	public JSONObject saveOrUpdatePrefereces(@RequestParam("history") Boolean enableHistory,
	                                         @RequestParam("bookmarks") Boolean enableBookmarks,
	                                         @RequestParam("notes") Boolean enableNotes,
	                                         @RequestParam("quickSearches") Boolean enableQuickSearches,
	                                         @RequestParam("defaultSearch") Boolean enableDefaultSearch,
	                                         @RequestParam("duplicateResults") Boolean enableDuplicateResults,
	                                         @RequestParam("multiFiltering") Boolean enableMultiFiltering,
	                                         @RequestParam("categories[]") String[] cats,
	                                         @RequestParam("selectedColors") String selectedColors) {
		return cache.saveOrUpdatePreferences(enableHistory, enableBookmarks, enableNotes, enableQuickSearches,
		    enableDefaultSearch, enableDuplicateResults, enableMultiFiltering, cats, selectedColors);
	}
	
	public JSONObject restoreDefaultPrefereces() {
		return cache.restorePreferences();
	}
}
