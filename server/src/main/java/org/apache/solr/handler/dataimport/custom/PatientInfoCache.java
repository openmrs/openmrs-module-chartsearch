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
