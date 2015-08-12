/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.apache.solr.handler.dataimport.custom;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.openmrs.module.chartsearch.server.PatientInfo;

/**
 *
 */
public class IndexClearStrategyNoActionImpl implements IndexClearStrategy {
	
	@Override
	public List<Integer> getPatientsToDelete(Collection<PatientInfo> patients) {
		return Collections.emptyList();
	}
	
	@Override
	public String toString() {
		return "No Action Index Clear Strategy";
	}
}
