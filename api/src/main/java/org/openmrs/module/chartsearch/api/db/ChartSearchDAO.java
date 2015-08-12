/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.api.db;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.cache.ChartSearchBookmark;
import org.openmrs.module.chartsearch.cache.ChartSearchCategoryDisplayName;
import org.openmrs.module.chartsearch.cache.ChartSearchHistory;
import org.openmrs.module.chartsearch.cache.ChartSearchNote;
import org.openmrs.module.chartsearch.cache.ChartSearchPreference;

/**
 * Database methods for {@link ChartSearchService}.
 */
public interface ChartSearchDAO {
	
	public void indexAllPatientData(Integer numberOfResults, SolrServer solrServer,
	                                @SuppressWarnings("rawtypes") Class showProgressToClass);
	
	public ChartSearchHistory getSearchHistory(Integer searchId);
	
	public void saveSearchHistory(ChartSearchHistory searchHistory);
	
	public void deleteSearchHistory(ChartSearchHistory searchHistory);
	
	public ChartSearchHistory getSearchHistoryByUuid(String uuid);
	
	public List<ChartSearchHistory> getAllSearchHistory();
	
	public ChartSearchBookmark getSearchBookmark(Integer bookmarkId);
	
	public void saveSearchBookmark(ChartSearchBookmark bookmark);
	
	public void deleteSearchBookmark(ChartSearchBookmark bookmark);
	
	public List<ChartSearchBookmark> getAllSearchBookmarks();
	
	public ChartSearchBookmark getSearchBookmarkByUuid(String uuid);
	
	public void saveSearchNote(ChartSearchNote note);
	
	public void deleteSearchNote(ChartSearchNote note);
	
	public ChartSearchNote getSearchNote(Integer noteId);
	
	public ChartSearchNote getSearchNoteByUuid(String uuid);
	
	public List<ChartSearchNote> getAllSearchNotes();
	
	public boolean saveANewChartSearchPreference(ChartSearchPreference preference);
	
	public void deleteChartSearchPreference(ChartSearchPreference preference);
	
	public ChartSearchPreference getChartSearchPreference(Integer preferenceId);
	
	public List<ChartSearchPreference> getAllChartSearchPreferences();
	
	public ChartSearchPreference getChartSearchPreferenceByUuid(String uuid);
	
	public ChartSearchPreference getChartSearchPreferenceOfAUser(Integer userId);
	
	void updateChartSearchPreference(ChartSearchPreference pref);
	
	public ChartSearchPreference getRightMatchedPreferences();
	
	public ChartSearchCategoryDisplayName getCategoryDisplayNameByUuid(String uuid);
	
	public List<ChartSearchCategoryDisplayName> getAllCategoryDisplayNames();
	
	public void saveChartSearchCategoryDisplayName(ChartSearchCategoryDisplayName displayName);
	
	public void deleteChartSearchCategoryDisplayName(ChartSearchCategoryDisplayName displayName);
}
