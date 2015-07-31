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
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManageNotesFragmentController {
	
	ChartSearchCache cache = new ChartSearchCache();
	
	public void controller(FragmentModel fragmentModel, UiUtils ui) {
		fragmentModel.addAttribute("allFoundNotes", ui.escapeJs(cache.fetchAllNotesForManageUI().toString()));
	}
	
	public JSONArray deleteSelectedNotes(@RequestParam("selectedUuids[]") String[] selectedUuids) {
		return cache.deleteSelectedNotes(selectedUuids);
	}
	
	public JSONArray saveEdittedNote(@RequestParam("uuid") String uuid, @RequestParam("comment") String comment,
	                                 @RequestParam("priority") String priority) {
		return cache.saveEdittedNote(uuid, comment, priority);
	}
}
