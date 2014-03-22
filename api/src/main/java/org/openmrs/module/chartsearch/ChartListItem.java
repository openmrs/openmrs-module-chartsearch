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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class ChartListItem {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private String uuid;
	private Integer obsId;
	private Integer obsGroupId;
	private String obsDate = "";
	private String conceptName = "";
	private String value = "";
	private ArrayList<String> highlights = new ArrayList<String>();


	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public ChartListItem() {
	}	
	
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
	
	public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){return location;}
	
}
