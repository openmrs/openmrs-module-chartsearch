package org.openmrs.module.chartsearch;

import net.sf.json.JSONArray;

public class PreferenceJSON {
	
	private boolean history;
	
	private boolean bookmarks;
	
	private boolean notes;
	
	private boolean quickSearches;
	
	private boolean defaultSearch;
	
	private boolean duplicateResults;
	
	private boolean multiFiltering;
	
	private JSONArray categories;
	
	public PreferenceJSON(boolean history, boolean bookmarks, boolean notes, boolean quickSearches, boolean defaultSearch,
	    boolean duplicateResults, boolean multiFiltering, JSONArray categories) {
		this.history = history;
		this.bookmarks = bookmarks;
		this.notes = notes;
		this.quickSearches = quickSearches;
		this.defaultSearch = defaultSearch;
		this.duplicateResults = duplicateResults;
		this.multiFiltering = multiFiltering;
		this.categories = categories;
	}
	
	public boolean isHistory() {
		return history;
	}
	
	public boolean isBookmarks() {
		return bookmarks;
	}
	
	public boolean isNotes() {
		return notes;
	}
	
	public boolean isQuickSearches() {
		return quickSearches;
	}
	
	public boolean isDefaultSearch() {
		return defaultSearch;
	}
	
	public boolean isDuplicateResults() {
		return duplicateResults;
	}
	
	public boolean isMultiFiltering() {
		return multiFiltering;
	}
	
	public JSONArray getCategories() {
		return categories;
	}
	
	public void setHistory(boolean history) {
		this.history = history;
	}
	
	public void setBookmarks(boolean bookmarks) {
		this.bookmarks = bookmarks;
	}
	
	public void setNotes(boolean notes) {
		this.notes = notes;
	}
	
	public void setQuickSearches(boolean quickSearches) {
		this.quickSearches = quickSearches;
	}
	
	public void setDefaultSearch(boolean defaultSearch) {
		this.defaultSearch = defaultSearch;
	}
	
	public void setDuplicateResults(boolean duplicateResults) {
		this.duplicateResults = duplicateResults;
	}
	
	public void setMultiFiltering(boolean multiFiltering) {
		this.multiFiltering = multiFiltering;
	}
	
	public void setCategories(JSONArray categories) {
		this.categories = categories;
	}
	
}
