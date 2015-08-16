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

import org.apache.solr.common.SolrDocument;
import org.openmrs.Form;

/**
 * Represents this module's customized {@link SolrDocument} for {@link Form}
 */
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
