/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.chartsearch.web.dwr;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.ChartListItem;
import org.openmrs.module.chartsearch.ObsItem;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.dwr.ObsListItem;

public class DWRChartSearchService {
	
	protected static final Log log = LogFactory.getLog(DWRChartSearchService.class);
	
	private ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);
	
	public Map<String, Object> findObsAndCount(Integer patientId, String phrase, boolean includeRetired,
	                                           List<String> includeClassNames, List<String> excludeClassNames,
	                                           List<String> includeDatatypeNames, List<String> excludeDatatypeNames,
	                                           Integer start, Integer length, boolean getMatchCount,
	                                           List<String> selectedCategories) throws APIException {
		// Map to return
		Map<String, Object> resultsMap = new HashMap<String, Object>();
		Vector<Object> objectList = new Vector<Object>();
		
		try {
			long matchCount = 0;
			if (getMatchCount) {
				// get the count of matches
				matchCount += searcher.getDocumentListCount(patientId, phrase);
			}
			
			// if we have any matches or this isn't the first ajax call when
			// the caller
			// requests for the count
			if (matchCount > 0 || !getMatchCount) {
				objectList.addAll(findBatchOfObs(patientId, phrase, includeRetired, includeClassNames, excludeClassNames,
				    includeDatatypeNames, excludeDatatypeNames, start, length, selectedCategories));
			}
			
			resultsMap.put("count", matchCount);
			resultsMap.put("objectList", objectList);
			
		}
		catch (Exception e) {
			log.error("Error while searching for observations", e);
			objectList.clear();
			objectList.add(Context.getMessageSourceService().getMessage("Obs.search.error") + " - " + e.getMessage());
			resultsMap.put("count", 0);
			resultsMap.put("objectList", objectList);
		}
		
		return resultsMap;
	}
	
	public List<Object> findBatchOfObs(Integer patientId, String phrase, boolean includeRetired,
	                                   List<String> includeClassNames, List<String> excludeClassNames,
	                                   List<String> includeDatatypeNames, List<String> excludeDatatypeNames, Integer start,
	                                   Integer length, List<String> selectedCategories) {
		// List to return
		// Object type gives ability to return error strings
		Vector<Object> objectList = new Vector<Object>();
		
		Locale defaultLocale = Context.getLocale();
		
		// get the list of locales to search on
		List<Locale> searchLocales = Context.getAdministrationService().getAllowedLocales(); // getSearchLocales();
		
		try {
			List<ChartListItem> items = searcher.getDocumentList(patientId, phrase, start, length, selectedCategories);
			objectList.addAll(items);
			
			if (objectList.size() < 1) {
				objectList.add(Context.getMessageSourceService().getMessage("general.noMatchesFoundInLocale",
				    new Object[] { "<b>" + phrase + "</b>", OpenmrsUtil.join(searchLocales, ", ") }, Context.getLocale()));
			} else {}
		}
		catch (Exception e) {
			log.error("Error while finding observations + " + e.getMessage(), e);
			objectList.add(Context.getMessageSourceService().getMessage("Obs.search.error") + " - " + e.getMessage());
		}
		
		if (objectList.size() == 0)
			objectList.add(Context.getMessageSourceService().getMessage("general.noMatchesFoundInLocale",
			    new Object[] { "<b>" + phrase + "</b>", defaultLocale }, Context.getLocale()));
		
		return objectList;
	}
	
	// TODO replace with custom DetailsItem
	public String getDetails(Integer id) {
		ObsListItem obs = new ObsListItem(Context.getObsService().getObs(id), Context.getLocale());
		
		// TODO create renderer
		String result = "<div class='cs_details_title'>Observation date:</div>" + obs.getObsDate() + "<br/>"
		        + "<div class='cs_details_title'>Concept Name:</div>" + obs.getConceptName()
		        + "<div class='cs_details_title'>Value:</div>" + obs.getValue()
		        + "<div class='cs_details_title'>Location:</div>" + obs.getLocation();
		return result;
	}
	
	public static ChartListItem getObservationDetails(Integer id) {
		ObsListItem obs = new ObsListItem(Context.getObsService().getObs(id), Context.getLocale());
		
		// TODO create renderer
		ObsItem item = new ObsItem();
		item.setConceptName(obs.getConceptName());
		item.setLocation(obs.getLocation());
		item.setValue(obs.getValue());
		item.setObsDate(obs.getObsDate());
		item.setObsId(obs.getObsId());
		
		return item;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
