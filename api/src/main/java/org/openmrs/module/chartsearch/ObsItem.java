package org.openmrs.module.chartsearch;

import java.util.ArrayList;
import java.util.List;

public class ObsItem extends ChartListItem{
	
	private Integer obsId;
	private Integer obsGroupId;
	private String obsDate = "";
	private String conceptName = "";
	private String value = "";
	private ArrayList<String> highlights = new ArrayList<String>();
	
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

}
