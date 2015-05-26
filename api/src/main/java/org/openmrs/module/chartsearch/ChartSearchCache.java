/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch;

import java.util.List;

import org.apache.log4j.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;

/**
 * This basically provides access to chart-search module stored records such as notes on searches,
 * bookmarked searches, search history, search suggestions among others from the database emulating
 * the normal cache/buffer
 */
public class ChartSearchCache {
	
	private static final Logger logger = Logger.getLogger(ChartSearchCache.class);
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	/**
	 * TODO remove if not used, added to JSONToReturn already from {@link GeneratingJson}
	 * 
	 * @param patientId
	 * @return
	 */
	public List<String> getAllSearchSuggestions(Integer patientId) {
		List<String> allSuggestions = chartSearchService.getAllPossibleSearchSuggestions(patientId);
		
		logger.info("Available possible search suggestions ARE: " + allSuggestions);
		
		return allSuggestions;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
