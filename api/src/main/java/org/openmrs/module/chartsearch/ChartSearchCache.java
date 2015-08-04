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
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.cache.ChartSearchBookmark;
import org.openmrs.module.chartsearch.cache.ChartSearchCategoryDisplayName;
import org.openmrs.module.chartsearch.cache.ChartSearchHistory;
import org.openmrs.module.chartsearch.cache.ChartSearchNote;
import org.openmrs.module.chartsearch.cache.ChartSearchPreference;
import org.openmrs.module.chartsearch.categories.CategoryFilter;

import com.google.common.collect.Lists;

/**
 * This basically provides access to chart-search module stored records such as notes on searches,
 * bookmarked searches, search history, search suggestions among others from the database emulating
 * the normal cache/buffer
 */
public class ChartSearchCache {
	
	Random randomGenerator = new Random();
	
	private Integer uniqueNumber = randomGenerator.nextInt(100);
	
	private static final Logger logger = Logger.getLogger(ChartSearchCache.class);
	
	private ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	public void saveOrUpdateSearchHistory(String searchText, Integer patientId) {
		ChartSearchHistory history = new ChartSearchHistory();
		List<ChartSearchHistory> allHistory = chartSearchService.getAllSearchHistory();
		ChartSearchHistory exisitingHistory = checkIfSearchIsAlreadyInHistory(allHistory, searchText, patientId);
		
		if (StringUtils.isNotBlank(searchText) && null != patientId && fetchRightMatchedPreferences().isEnableHistory()) {
			history.setLastSearchedAt(new Date());
			
			if (null != exisitingHistory) {
				User user = Context.getUserService().getUser(exisitingHistory.getHistoryOwner().getUserId());
				Patient patient = Context.getPatientService().getPatient(exisitingHistory.getPatient().getPatientId());
				
				if (patient.getPatientId().equals(patientId)
				        && user.getUserId().equals(Context.getAuthenticatedUser().getUserId())) {
					history.setSearchPhrase(exisitingHistory.getSearchPhrase());
					history.setUuid(exisitingHistory.getUuid());
					history.setHistoryOwner(user);
					history.setPatient(patient);
				} else {
					setNewHistoryOwnerPatientAndPhrase(searchText, patientId, history);
				}
				chartSearchService.deleteSearchHistory(exisitingHistory);
			} else {
				setNewHistoryOwnerPatientAndPhrase(searchText, patientId, history);
			}
			chartSearchService.saveSearchHistory(history);
		}
	}
	
	private void setNewHistoryOwnerPatientAndPhrase(String searchText, Integer patientId, ChartSearchHistory history) {
		history.setHistoryOwner(Context.getAuthenticatedUser());
		history.setPatient(Context.getPatientService().getPatient(patientId));
		history.setSearchPhrase(searchText);
	}
	
	private ChartSearchHistory checkIfSearchIsAlreadyInHistory(List<ChartSearchHistory> allHistory, String searchText,
	                                                           Integer patientId) {
		if (!allHistory.isEmpty()) {
			for (int i = 0; i < allHistory.size(); i++) {
				ChartSearchHistory history = allHistory.get(i);
				if (searchText.equals(history.getSearchPhrase())
				        && Context.getAuthenticatedUser().getUserId().equals(history.getHistoryOwner().getUserId())
				        && history.getPatient().getPatientId().equals(patientId)) {
					return history;
				}
			}
		}
		return null;
	}
	
	/**
	 * Deletes a search history whose uuid is provided
	 * 
	 * @see ChartSearchService#deleteSearchHistory(ChartSearchHistory)
	 * @param historyUuid
	 * @return deleted, whether the history was or not deleted
	 */
	public boolean deleteSearchHistory(String historyUuid) {
		ChartSearchHistory history = chartSearchService.getSearchHistoryByUuid(historyUuid);
		
		if (StringUtils.isNotBlank(historyUuid)) {
			chartSearchService.deleteSearchHistory(history);
			return true;
		} else
			return false;
	}
	
	public boolean checkIfPhraseExisitsInHistory(String searchPhrase, Integer patientId) {
		List<ChartSearchHistory> allHistory = chartSearchService.getAllSearchHistory();
		boolean exists = false;
		
		for (ChartSearchHistory history : allHistory) {
			if (history.getSearchPhrase().equals(searchPhrase) && history.getPatient().getPatientId().equals(patientId)) {
				exists = true;
			}
		}
		
		return exists;
	}
	
	public JSONObject saveOrUpdateBookmark(String selectedCategories, String searchPhrase, String bookmarkName,
	                                       Integer patientId) {
		JSONObject json = new JSONObject();
		
		if (StringUtils.isNotBlank(searchPhrase) && StringUtils.isNotBlank(bookmarkName) && null != patientId
		        && fetchRightMatchedPreferences().isEnableBookmarks()) {
			ChartSearchBookmark bookmark = new ChartSearchBookmark();
			ChartSearchBookmark existingBookmark = checkIfBookmarkExistsForPhrase(searchPhrase, selectedCategories,
			    patientId);
			
			if (null != existingBookmark) {
				existingBookmark.setBookmarkName(bookmarkName);
				chartSearchService.saveSearchBookmark(existingBookmark);
				
				json.put("currentUuid", chartSearchService.getSearchBookmarkByUuid(existingBookmark.getUuid()));
			} else {
				if (bookmarkNameExists(bookmarkName)) {
					bookmarkName += "(" + uniqueNumber + ")";
				}
				bookmark.setBookmarkName(bookmarkName);
				bookmark.setSearchPhrase(searchPhrase);
				bookmark.setPatient(Context.getPatientService().getPatient(patientId));
				bookmark.setSelectedCategories(selectedCategories);
				bookmark.setBookmarkOwner(Context.getAuthenticatedUser());
				chartSearchService.saveSearchBookmark(bookmark);
				
				json.put("currentUuid", chartSearchService.getSearchBookmarkByUuid(bookmark.getUuid()).getUuid());
			}
			json.put("allBookmarks", GeneratingJson.getAllSearchBookmarksToReturnToUI());
			
			return json;
		} else {
			return null;
		}
	}
	
	private boolean bookmarkNameExists(String bookmarkName) {
		List<ChartSearchBookmark> existingBookmarks = chartSearchService.getAllSearchBookmarks();
		
		if (!existingBookmarks.isEmpty()) {
			for (ChartSearchBookmark bookmark : existingBookmarks) {
				if (bookmark.getBookmarkName().equals(bookmarkName)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ChartSearchBookmark checkIfBookmarkExistsForPhrase(String phrase, String categories, Integer patientId) {
		List<ChartSearchBookmark> existingBookmarks = chartSearchService.getAllSearchBookmarks();
		
		if (!existingBookmarks.isEmpty()) {
			for (ChartSearchBookmark bookmark : existingBookmarks) {
				if (bookmark.getSearchPhrase().equals(phrase) && bookmark.getSelectedCategories().equals(categories)
				        && bookmark.getPatient().getPatientId().equals(patientId)) {
					return bookmark;
				}
			}
		}
		
		return null;
	}
	
	public JSONArray deleteSearchBookmark(String uuid) {
		ChartSearchBookmark bookmark = chartSearchService.getSearchBookmarkByUuid(uuid);
		
		if (StringUtils.isNotBlank(uuid) && null != bookmark) {
			chartSearchService.deleteSearchBookmark(bookmark);
			return GeneratingJson.getAllSearchBookmarksToReturnToUI();
		} else
			return null;
	}
	
	public JSONObject getSearchBookmarkSearchDetailsByUuid(String uuid) {
		if (StringUtils.isNotBlank(uuid)) {
			ChartSearchBookmark bookmark = chartSearchService.getSearchBookmarkByUuid(uuid);
			JSONObject json = new JSONObject();
			String[] categories = bookmark.getSelectedCategories().split(", ");
			
			json.put("searchPhrase", bookmark.getSearchPhrase());
			json.put("bookmarkName", bookmark.getBookmarkName());
			json.put("commaCategories", bookmark.getSelectedCategories());
			json.put("categories", categories);
			json.put("isDefaultSearch", bookmark.isDefaultSearch());
			
			return json;
		} else
			return null;
	}
	
	public JSONArray saveBookmarkProperties(String uuid, String bookmarkName, String searchPhrase, String selectedCategories) {
		ChartSearchBookmark bookmark = chartSearchService.getSearchBookmarkByUuid(uuid);
		if (bookmark != null && fetchRightMatchedPreferences().isEnableBookmarks()) {
			bookmark.setBookmarkName(bookmarkName);
			//bookmark.setSearchPhrase(searchPhrase); search phrase needs not to be modified
			bookmark.setSelectedCategories(selectedCategories);
			
			chartSearchService.saveSearchBookmark(bookmark);
		}
		return GeneratingJson.getAllSearchBookmarksToReturnTomanagerUI();
	}
	
	public JSONArray deleteBookmarkInTheDialog(String uuid) {
		ChartSearchBookmark bookmark = chartSearchService.getSearchBookmarkByUuid(uuid);
		if (bookmark != null) {
			chartSearchService.deleteSearchBookmark(bookmark);
		}
		return GeneratingJson.getAllSearchBookmarksToReturnTomanagerUI();
	}
	
	public String fetchLastHistorySearchPhrase(Integer patientId) {
		List<ChartSearchHistory> allHistory = chartSearchService.getAllSearchHistory();
		List<ChartSearchHistory> rightHistory = new ArrayList<ChartSearchHistory>();
		String rightHistoryLastSearchPhrase = null;
		
		if (null != allHistory && !allHistory.isEmpty() && null != patientId) {
			for (ChartSearchHistory history : allHistory) {
				if (Context.getAuthenticatedUser().getUserId().equals(history.getHistoryOwner().getUserId())
				        && history.getPatient().getPatientId().equals(patientId)) {
					rightHistory.add(history);
				}
			}
			if (null != rightHistory && !rightHistory.isEmpty()) {
				ChartSearchHistory lastRightHistory = rightHistory.get(rightHistory.size() - 1);
				rightHistoryLastSearchPhrase = lastRightHistory.getSearchPhrase();
			}
		}
		return rightHistoryLastSearchPhrase;
	}
	
	public JSONObject saveANewNoteOrCommentOnToASearch(String searchPhrase, Integer patientId, String comment,
	                                                   String priority, String backgroundColor) {
		if (null != patientId && StringUtils.isNotBlank(searchPhrase) && StringUtils.isNotBlank(comment)
		        && fetchRightMatchedPreferences().isEnableNotes()) {
			JSONObject json = new JSONObject();
			ChartSearchNote note = new ChartSearchNote();
			
			note.setSearchPhrase(searchPhrase);
			note.setComment(comment);
			note.setDisplayColor(backgroundColor);
			note.setCreatedOrLastModifiedAt(new Date());
			note.setNoteOwner(Context.getAuthenticatedUser());
			note.setPatient(Context.getPatientService().getPatient(patientId));
			note.setPriority(priority);
			
			chartSearchService.saveSearchNote(note);
			
			if (null != chartSearchService.getSearchNote(note.getNoteId())) {
				GeneratingJson.addBothPersonalAndGlobalNotesToJSON(searchPhrase, patientId, json);
			} else {
				json = null;
			}
			
			return json;
		} else
			return null;
	}
	
	public JSONObject deleteSearchNote(String uuid, String searchPhrase, Integer patientId) {
		JSONObject json = new JSONObject();
		
		if (StringUtils.isNotBlank(uuid)) {
			ChartSearchNote note = chartSearchService.getSearchNoteByUuid(uuid);
			if (null != note && Context.getAuthenticatedUser().getUserId().equals(note.getNoteOwner().getUserId())) {
				chartSearchService.deleteSearchNote(note);
			}
		}
		
		GeneratingJson.addBothPersonalAndGlobalNotesToJSON(searchPhrase, patientId, json);
		
		return json;
	}
	
	public JSONArray deleteHistoryOfSelectedUuids(String[] uuids) {
		if (null != uuids && uuids.length != 0) {
			for (int i = 0; i < uuids.length; i++) {
				String uuid = uuids[i];
				ChartSearchHistory history = chartSearchService.getSearchHistoryByUuid(uuid);
				chartSearchService.deleteSearchHistory(history);
			}
		}
		return GeneratingJson.getAllSearchHistoriesToSendToTheManageUI();
	}
	
	public JSONArray deleteBookmarkOfSelectedUuids(String[] uuids) {
		if (null != uuids && uuids.length != 0) {
			for (int i = 0; i < uuids.length; i++) {
				String uuid = uuids[i];
				ChartSearchBookmark bookmark = chartSearchService.getSearchBookmarkByUuid(uuid);
				chartSearchService.deleteSearchBookmark(bookmark);
			}
		}
		return GeneratingJson.getAllSearchBookmarksToReturnTomanagerUI();
	}
	
	/**
	 * Supports a search for everything as initial default search, which is overwritten by most
	 * recent search history and also bound to be overwritten if the user has set up a default
	 * search from amoung his/her saved bookmarks
	 * 
	 * @return searchPhrase and selectedCategories if bookmark is default
	 */
	public JSONObject returnDefaultSearchPhrase(String currentSPhrase, Integer patientId) {
		JSONObject json = new JSONObject();
		if (StringUtils.isBlank(currentSPhrase)) {
			ChartSearchCache cache = new ChartSearchCache();
			String lastSearchPhraseFromHistory = cache.fetchLastHistorySearchPhrase(patientId);
			List<ChartSearchBookmark> allBookmarks = chartSearchService.getAllSearchBookmarks();
			ChartSearchBookmark defaultBookmark = null;
			
			for (ChartSearchBookmark bookmark : allBookmarks) {
				if (bookmark.isDefaultSearch()
				        && bookmark.getBookmarkOwner().getUserId().equals(Context.getAuthenticatedUser().getUserId())) {
					defaultBookmark = bookmark;
				} else
					getDefaultSearchFromHistoryIfItExists(currentSPhrase, json, lastSearchPhraseFromHistory);
			}
			
			if (defaultBookmark != null) {
				List<String> categories = defaultBookmark.getSelectedCategoriesAsList();
				
				json.put("searchPhrase", defaultBookmark.getSearchPhrase());
				if (categories != null && !categories.isEmpty()) {
					json.put("selectedCategories", categories);
				}
			} else {
				getDefaultSearchFromHistoryIfItExists(currentSPhrase, json, lastSearchPhraseFromHistory);
			}
		}
		
		return json;
	}
	
	private void getDefaultSearchFromHistoryIfItExists(String currentSPhrase, JSONObject json,
	                                                   String lastSearchPhraseFromHistory) {
		if (StringUtils.isNotBlank(lastSearchPhraseFromHistory)) {
			json.put("searchPhrase", lastSearchPhraseFromHistory);
		} else
			json.put("searchPhrase", currentSPhrase);
	}
	
	/**
	 * Sets only one, selected as bookmark and sets all others to false since default search from
	 * bookmarks can only be one
	 * 
	 * @param uuid
	 * @return
	 */
	public JSONArray setBookmarkAsDefaultSearch(String uuid) {
		if (StringUtils.isNotBlank(uuid)) {
			List<ChartSearchBookmark> allBookmarks = chartSearchService.getAllSearchBookmarks();
			
			for (ChartSearchBookmark bookmark : allBookmarks) {
				if (bookmark.getBookmarkOwner().getUserId().equals(Context.getAuthenticatedUser().getUserId())) {
					if (bookmark.getUuid().equals(uuid)) {
						bookmark.setIsDefaultSearch(true);
					} else {
						bookmark.setIsDefaultSearch(false);
					}
					chartSearchService.saveSearchBookmark(bookmark);
				}
			}
		}
		return GeneratingJson.getAllSearchBookmarksToReturnTomanagerUI();
	}
	
	public JSONArray fetchAllNotesForManageUI() {
		JSONArray jsonArr = new JSONArray();
		List<ChartSearchNote> allNotes = Lists.reverse(chartSearchService.getAllSearchNotes());//re-arrange to get the most recent/added first
		
		for (ChartSearchNote note : allNotes) {
			JSONObject json = new JSONObject();
			if (note.getNoteOwner().getUserId().equals(Context.getAuthenticatedUser().getUserId()) && note != null) {//manage owned notes only
				json.put("uuid", note.getUuid());
				json.put("createdOrLastModifiedAt", note.getCreatedOrLastModifiedAt().getTime());
				json.put("backgroundColor", note.getDisplayColor());
				json.put("formatedCreatedOrLastModifiedAt", Context.getDateFormat()
				        .format(note.getCreatedOrLastModifiedAt()));
				
				GeneratingJson.addPhraseAndCommentNotesAttributes(note, json);
				
				json.put("patientId", note.getPatient().getPatientId());
				json.put("patientFName", note.getPatient().getFamilyName());
				json.put("priority", note.getPriority());
			}
			jsonArr.add(json);
		}
		
		return jsonArr;
	}
	
	public JSONArray deleteSelectedNotes(String[] uuids) {
		if (uuids != null && uuids.length != 0) {
			for (int i = 0; i < uuids.length; i++) {
				ChartSearchNote note = chartSearchService.getSearchNoteByUuid(uuids[i]);
				if (note != null) {
					chartSearchService.deleteSearchNote(note);
				}
			}
		}
		return fetchAllNotesForManageUI();
	}
	
	public JSONArray saveEdittedNote(String uuid, String comment, String priority) {
		ChartSearchNote note = chartSearchService.getSearchNoteByUuid(uuid);
		if (note != null) {
			if (note.getComment().equals(comment) && note.getPriority().equals(priority)) {
				return null;
			} else {
				note.setComment(comment);
				note.setPriority(priority);
				
				chartSearchService.saveSearchNote(note);
				
				return fetchAllNotesForManageUI();
			}
		} else
			return null;
	}
	
	public JSONObject restorePreferences() {
		List<ChartSearchPreference> allPrefs = chartSearchService.getAllChartSearchPreferences();
		List<ChartSearchCategoryDisplayName> allDNames = chartSearchService.getAllCategoryDisplayNames();
		
		for (ChartSearchCategoryDisplayName name : allDNames) {
			if (name.getPreference().getPreferenceOwner().getUserId().equals(Context.getAuthenticatedUser().getUserId())) {
				chartSearchService.deleteChartSearchCategoryDisplayName(name);
			}
		}
		
		for (ChartSearchPreference pref : allPrefs) {
			if (pref.getPreferenceOwner().getUserId().equals(Context.getAuthenticatedUser().getUserId())) {
				chartSearchService.deleteChartSearchPreference(pref);
				break;
			}
		}
		
		return GeneratingJson.generateDaemonPreferencesJSON();
	}
	
	public JSONObject saveOrUpdatePreferences(Boolean enableHistory, Boolean enableBookmarks, Boolean enableNotes,
	                                          Boolean enableQuickSearches, Boolean enableDefaultSearch,
	                                          Boolean enableDuplicateResults, Boolean enableMultiFiltering, String[] cats,
	                                          String selectedColors) {
		List<ChartSearchPreference> allPrefs = chartSearchService.getAllChartSearchPreferences();
		Boolean exists = false;
		ChartSearchPreference preference = new ChartSearchPreference();
		
		for (ChartSearchPreference pref : allPrefs) {
			if (pref.getPreferenceOwner().getUserId().equals(Context.getAuthenticatedUser().getUserId())) {
				exists = true;
				preference = pref;
				break;
			}
		}
		
		preference.setEnableBookmarks(enableBookmarks);
		preference.setEnableDefaultSearch(enableDefaultSearch);
		preference.setEnableDuplicateResults(enableDuplicateResults);
		preference.setEnableHistory(enableHistory);
		preference.setEnableMultipleFiltering(enableMultiFiltering);
		preference.setEnableNotes(enableNotes);
		preference.setEnableQuickSearches(enableQuickSearches);
		preference.setPersonalNotesColors(selectedColors);
		
		if (exists) {
			chartSearchService.updateChartSearchPreference(preference);
		} else {
			preference.setPreferenceOwner(Context.getAuthenticatedUser());
			chartSearchService.saveANewChartSearchPreference(preference);
		}
		
		if (cats != null && cats.length != 0) {
			for (int i = 0; i < cats.length; i++) {
				String[] catUuidAndName = cats[i].split("<=>");
				String uuid = catUuidAndName[0];
				String displayName = catUuidAndName[1];
				String name = catUuidAndName[2];
				ChartSearchCategoryDisplayName catName = chartSearchService.getCategoryDisplayNameByUuid(uuid);
				List<CategoryFilter> cfs = chartSearchService.getAllCategoryFilters();
				CategoryFilter categoryFilter = null;
				
				for (CategoryFilter cf : cfs) {
					if (name.equals(cf.getCategoryName())) {
						categoryFilter = cf;
					}
				}
				
				if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(displayName)) {
					if (catName != null && categoryDiplayNameDoesExist(displayName, name)) {
						catName.setDisplayName(displayName);
						chartSearchService.saveChartSearchCategoryDisplayName(catName);
					} else {
						ChartSearchCategoryDisplayName newName = new ChartSearchCategoryDisplayName();
						
						newName.setDisplayName(displayName);
						newName.setPreference(preference);
						newName.setCategoryFilter(categoryFilter);
						
						chartSearchService.saveChartSearchCategoryDisplayName(newName);
					}
				}
			}
		}
		
		return GeneratingJson.generateRightMatchedPreferencesJSON();
	}
	
	private boolean categoryDiplayNameDoesExist(String displayName, String name) {
		boolean exist = false;
		List<ChartSearchCategoryDisplayName> allCatDNames = chartSearchService.getAllCategoryDisplayNames();
		
		for (ChartSearchCategoryDisplayName name2 : allCatDNames) {
			if (name2.getDisplayName().toLowerCase().equals(displayName.toLowerCase())
			        && Context.getAuthenticatedUser().getUserId()
			                .equals(name2.getPreference().getPreferenceOwner().getUserId())
			        && name.toLowerCase().equals(name2.getCategoryFilter().getCategoryName())
			        && !categoryDisplayNameExistAmongDisplayNames(displayName)) {
				exist = true;
			}
		}
		
		return exist;
	}
	
	public boolean categoryDisplayNameExistAmongDisplayNames(String dName) {
		List<ChartSearchCategoryDisplayName> allCatDNames = chartSearchService.getAllCategoryDisplayNames();
		boolean exist = false;
		List<String> dNames = new ArrayList<String>();
		
		for (ChartSearchCategoryDisplayName name : allCatDNames) {
			if (Context.getAuthenticatedUser().getUserId().equals(name.getPreference().getPreferenceOwner().getUserId())) {
				dNames.add(name.getDisplayName());
			}
		}
		if (dNames.contains(dName)) {
			exist = true;
		}
		
		return exist;
	}
	
	public ChartSearchPreference fetchRightMatchedPreferences() {
		return chartSearchService.getRightMatchedPreferences();
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
	
	public String[] fetchPersonalNotesColors() {
		ChartSearchPreference preference = Context.getService(ChartSearchService.class).getRightMatchedPreferences();
		String[] pColors = null;
		
		if (preference != null && preference.gePersonalNotesColorsArray() != null
		        && preference.gePersonalNotesColorsArray().length > 0) {
			pColors = preference.gePersonalNotesColorsArray();
		}
		
		return pColors;
	}
}
