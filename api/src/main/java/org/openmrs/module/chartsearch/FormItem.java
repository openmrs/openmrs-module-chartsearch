package org.openmrs.module.chartsearch;

public class FormItem extends ChartListItem {
	
	private Integer formId;
	private String formName;
	private String encounterType;
	
	public Integer getFormId() {
		return formId;
	}
	public void setFormId(Integer formId) {
		this.formId = formId;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getEncounterType() {
		return encounterType;
	}
	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}
	
}
