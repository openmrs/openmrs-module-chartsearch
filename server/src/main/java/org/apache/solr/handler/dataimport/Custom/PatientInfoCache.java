package org.apache.solr.handler.dataimport.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PatientInfoCache {
	
	private Map<Integer, PatientInfo> data = new ConcurrentHashMap<Integer, PatientInfo>();
	
	private PatientInfoProvider source;
	
	public PatientInfoCache(PatientInfoProvider source) {
		this.source = source;
		update();
	}
	
	public void update() {
		Map<Integer, PatientInfo> tempData = source.getData();
		if (tempData != null) {
			data = new ConcurrentHashMap<Integer, PatientInfo>(source.getData());
		}
	}
	
	public void save() {
		source.updateData(data.values());
	}
	
	public Map<Integer, PatientInfo> getAll() {
		return new HashMap<Integer, PatientInfo>(data);
	}
	
	public PatientInfo get(Integer patientId) {
		return data.get(patientId);
	}
	
	public void put(Integer patientId, PatientInfo patientInfo) {
		data.put(patientId, patientInfo);
	}
	
	public Boolean contains(Integer patientId) {
		return data.containsKey(patientId);
	}
}
