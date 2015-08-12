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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openmrs.module.chartsearch.server.PatientInfo;

public class PatientInfoCache {
	
	private Map<Integer, PatientInfo> data = new ConcurrentHashMap<Integer, PatientInfo>();
	
	private final PatientInfoProvider source;
	
	private boolean isDirty;
	
	public PatientInfoCache(PatientInfoProvider source) {
		this.source = source;
		update();
		isDirty = false;
	}
	
	public synchronized void update() {
		Map<Integer, PatientInfo> tempData = source.getData();
		if (tempData != null) {
			data = new ConcurrentHashMap<Integer, PatientInfo>(tempData);
		}
	}
	
	public synchronized void save() {
		if (isDirty) {
			source.updateData(data.values());
			isDirty = false;
		}
	}
	
	public synchronized Map<Integer, PatientInfo> getAll() {
		return new HashMap<Integer, PatientInfo>(data);
	}
	
	public synchronized PatientInfo get(Integer patientId) {
		return data.get(patientId);
	}
	
	public synchronized void put(Integer patientId, PatientInfo patientInfo) {
		data.put(patientId, patientInfo);
		isDirty = true;
	}
	
	public synchronized Boolean contains(Integer patientId) {
		return data.containsKey(patientId);
	}
	
	public synchronized void remove(Integer patientId) {
		data.remove(patientId);
		isDirty = true;
	}
	
	public int size() {
		return data.size();
	}
}
