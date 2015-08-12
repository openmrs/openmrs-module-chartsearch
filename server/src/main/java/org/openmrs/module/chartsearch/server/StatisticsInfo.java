/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.server;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class StatisticsInfo {
	
	private String strategyName;
	
	private int pruneCount;
	
	private List<HashMap<String, Object>> daemonStates;
	
	public StatisticsInfo(String indexClearStrategyName, int clearedPatientsCount, List<HashMap<String, Object>> daemonStates) {
		this.strategyName = indexClearStrategyName;
		this.pruneCount = clearedPatientsCount;
		this.daemonStates = daemonStates;
	}
	
	public String getStrategyName() {
		return strategyName;
	}
	
	public int getPruneCount() {
		return pruneCount;
	}
	
	public List<HashMap<String, Object>> getDaemonStates() {
		return daemonStates;
	}
	
	@Override
	public String toString() {
		return String.format("Strategy name: %s\nPruneCount: %s\nDaemon states: %s", strategyName.toString(), pruneCount,
		    daemonStates.toString());
	}
}
