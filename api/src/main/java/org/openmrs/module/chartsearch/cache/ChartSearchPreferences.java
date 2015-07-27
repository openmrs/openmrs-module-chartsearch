/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.cache;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsObject;

@Entity
@Table(name = "chartsearch_preferences")
public class ChartSearchPreferences extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "preference_id")
	private Integer preferenceId;
	
	/**
	 * Enabling and disabling history similar to Google chrome's Incognito mode
	 */
	private boolean enableHistory;
	
	private boolean enableBookmarks;
	
	private boolean enableNotes;
	
	private boolean enableDuplicateResults;
	
	private boolean enableMultipleFiltering;
	
	private String[] personalNotesColors;
	
	private boolean enableQuickSearches;
	
	private boolean enableDefaultSearch;
	
	@Override
	public Integer getId() {
		return getPreferenceId();
	}
	
	@Override
	public void setId(Integer id) {
		setPreferenceId(id);
	}
	
	public Integer getPreferenceId() {
		return preferenceId;
	}
	
	public void setPreferenceId(Integer preferenceId) {
		this.preferenceId = preferenceId;
	}
	
	public boolean isEnableHistory() {
		return enableHistory;
	}
	
	public void setEnableHistory(boolean enableHistory) {
		this.enableHistory = enableHistory;
	}
	
	public boolean isEnableBookmarks() {
		return enableBookmarks;
	}
	
	public void setEnableBookmarks(boolean enableBookmarks) {
		this.enableBookmarks = enableBookmarks;
	}
	
	public boolean isEnableNotes() {
		return enableNotes;
	}
	
	public void setEnableNotes(boolean enableNotes) {
		this.enableNotes = enableNotes;
	}
	
	public boolean isEnableDuplicateResults() {
		return enableDuplicateResults;
	}
	
	public void setEnableDuplicateResults(boolean enableDuplicateResults) {
		this.enableDuplicateResults = enableDuplicateResults;
	}
	
	public boolean isEnableMultipleFiltering() {
		return enableMultipleFiltering;
	}
	
	public void setEnableMultipleFiltering(boolean enableMultipleFiltering) {
		this.enableMultipleFiltering = enableMultipleFiltering;
	}
	
	public String[] getPersonalNotesColors() {
		return personalNotesColors;
	}
	
	public void setPersonalNotesColors(String[] personalNotesColors) {
		this.personalNotesColors = personalNotesColors;
	}
	
	public boolean isEnableQuickSearches() {
		return enableQuickSearches;
	}
	
	public void setEnableQuickSearches(boolean enableQuickSearches) {
		this.enableQuickSearches = enableQuickSearches;
	}
	
	public boolean isEnableDefaultSearch() {
		return enableDefaultSearch;
	}
	
	public void setEnableDefaultSearch(boolean enableDefaultSearch) {
		this.enableDefaultSearch = enableDefaultSearch;
	}
	
}
