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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openmrs.User;

/**
 * One row column (for each user) table which stores and updates preferences defined by a user
 */
@Entity
@Table(name = "chartsearch_preferences")
public class ChartSearchPreferences implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "preference_id", nullable = false)
	private Integer preferenceId;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38)
	private String uuid = UUID.randomUUID().toString();
	
	/**
	 * Enabling and disabling history similar to Google chrome's Incognito mode
	 */
	@Column(name = "enable_history")
	private boolean enableHistory;
	
	@Column(name = "enable_bookmark")
	private boolean enableBookmarks;
	
	@Column(name = "enable_notes")
	private boolean enableNotes;
	
	@Column(name = "enable_duplicateresults")
	private boolean enableDuplicateResults;
	
	@Column(name = "enable_multiplefiltering")
	private boolean enableMultipleFiltering;
	
	/**
	 * Comma separated personal notes colors
	 */
	@Column(name = "personalnotes_colors")
	private String personalNotesColors;
	
	@Column(name = "enable_history")
	private boolean enableQuickSearches;
	
	@Column(name = "enable_defaultsearch")
	private boolean enableDefaultSearch;
	
	@Column(name = "user_id", nullable = false)
	private User loggedInUSer;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	
	public String getPersonalNotesColors() {
		return personalNotesColors;
	}
	
	public void setPersonalNotesColors(String personalNotesColors) {
		this.personalNotesColors = personalNotesColors;
	}
	
	public String[] personalNotesColorsArray() {
		return personalNotesColors.split(", ");
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
	
	public User getLoggedInUSer() {
		return loggedInUSer;
	}
	
	public void setLoggedInUSer(User loggedInUSer) {
		this.loggedInUSer = loggedInUSer;
	}
	
}
