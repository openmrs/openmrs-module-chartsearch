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
package org.apache.solr.handler.dataimport.custom;

import java.util.Collection;
import java.util.List;

import org.openmrs.module.chartsearch.server.PatientInfo;


/**
 * This clear strategy uses only on force prunes via commands
 */
public class IndexClearStrategyWithIdImpl implements IndexClearStrategy{
	
    private List<Integer> ids;

	public IndexClearStrategyWithIdImpl(List<Integer> ids) {
		this.ids = ids; 
    }

	@Override
    public List<Integer> getPatientsToDelete(Collection<PatientInfo> patients) {
	    return ids;
    }
	
	@Override
	public String toString() {
	    return "Id Based Index Clear Strategy";
	}
}
