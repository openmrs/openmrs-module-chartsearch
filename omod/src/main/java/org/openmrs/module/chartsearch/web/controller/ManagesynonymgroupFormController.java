/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.web.controller;

import java.util.ArrayList;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.openmrs.module.chartsearch.synonyms.SynonymsAPI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("chartsearch.ManagesynonymgroupFormController")
@RequestMapping("/module/chartsearch/managesynonymgroup")
public class ManagesynonymgroupFormController {
	
	@RequestMapping(method = RequestMethod.GET)
	public void showForm(ModelMap map, @RequestParam(value = "synonymGroupId", required = false) Integer groupId) {
		if (groupId != null) {
			
			ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
			SynonymGroup synonymGroup = chartSearchService.getSynonymGroupById(groupId);
			map.put("synonymGroup", synonymGroup);
			if (synonymGroup.getIsCategory()) {
				map.put("isCategory", "checked");
			}
			
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String handleSubmission(@RequestParam Map<String, String> requestParams) {
		String groupName = requestParams.get("groupName");
		
		if (requestParams.get("button").equals("Save Synonym Group")) { //save synonym group
		
			int synLen = "synonymName".length();
			ArrayList<Synonym> synonymList = new ArrayList<Synonym>();
			for (String param : requestParams.keySet()) {
				try {
					if (param.substring(0, synLen).equals("synonymName")) {
						if (requestParams.get(param) != null || !requestParams.get(param).equals("")) {
							Synonym newSyn = new Synonym(requestParams.get(param));
							synonymList.add(newSyn);
						}
					}
				}
				catch (Exception e) {}
				
			}
			
			boolean category;
			if (requestParams.get("category") == null) {
				category = false;
			} else {
				category = true;
			}
			boolean saveOrUpdate = false; //true for save, false for update
			String oldGroupNameForUpdate = "";
			if (requestParams.get("save").equals("save")) {
				saveOrUpdate = true;
			} else {
				oldGroupNameForUpdate = requestParams.get("save");
			}
			
			SynonymGroup synGrp = new SynonymGroup(groupName, category, synonymList);
			
			if (saveOrUpdate) { // save new group
			
				SynonymsAPI.saveNewSynonymGroup(synGrp);
			} else { // update existing group
				SynonymsAPI.updateSynonymGroup(oldGroupNameForUpdate, synGrp);
			}
		} else if (requestParams.get("button").equals("Delete Synonym Group")) { //delete synonym group
			SynonymsAPI.deleteSynonymGroup(groupName);
		}
		
		return "redirect:managesynonymgroups.form";
	}
	
}
