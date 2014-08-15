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
