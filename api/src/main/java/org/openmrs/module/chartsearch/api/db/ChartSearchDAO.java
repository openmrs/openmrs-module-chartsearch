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
package org.openmrs.module.chartsearch.api.db;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.saving.ChartSearchBookmark;
import org.openmrs.module.chartsearch.saving.ChartSearchHistory;
import org.openmrs.module.chartsearch.saving.ChartSearchNote;


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
}
