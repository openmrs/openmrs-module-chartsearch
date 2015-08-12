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

import java.util.ArrayList;
import java.util.List;

public class ObsItem extends ChartListItem {
	
	private Integer obsId;
	
	private Integer obsGroupId;
	
	private String obsDate = "";
	
	private String conceptName = "";
	
	private String value = "";
	
	private ArrayList<String> highlights = new ArrayList<String>();
	
	private String location = "";
	
	public Integer getObsId() {
		return obsId;
	}
	
	public void setObsId(Integer obsId) {
		this.obsId = obsId;
	}
	
	public String getConceptName() {
		return conceptName;
	}
	
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return Returns the obsDate.
	 */
	public String getObsDate() {
		return obsDate;
	}
	
	/**
	 * @param obsDate The obsDate to set.
	 */
	public void setObsDate(String obsDate) {
		this.obsDate = obsDate;
	}
	
	public List<String> getHighlights() {
		return highlights;
	}
	
	public void setHighlights(ArrayList<String> highlights) {
		this.highlights = highlights;
	}
	
	public Integer getObsGroupId() {
		return obsGroupId;
	}
	
	public void setObsGroupId(Integer obsGroupId) {
		this.obsGroupId = obsGroupId;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
}
