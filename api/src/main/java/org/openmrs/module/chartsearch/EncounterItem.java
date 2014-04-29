package org.openmrs.module.chartsearch;

public class EncounterItem extends ChartListItem {
	
	private Integer encounterId;
	private String encounterType;
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public String getEncounterType() {
		return encounterType;
	}
	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}
}
