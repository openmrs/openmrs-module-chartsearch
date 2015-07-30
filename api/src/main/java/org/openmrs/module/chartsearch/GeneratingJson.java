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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.response.FacetField.Count;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.cache.ChartSearchBookmark;
import org.openmrs.module.chartsearch.cache.ChartSearchHistory;
import org.openmrs.module.chartsearch.cache.ChartSearchNote;
import org.openmrs.module.chartsearch.cache.ChartSearchPreference;
import org.openmrs.module.chartsearch.solr.ChartSearchSearcher;

/**
 * Responsible for generating the JSON object to be returned to the view(s)
 */
public class GeneratingJson {
	
	final String DATEFORMAT = "dd/MM/yyyy HH:mm:ss";
	
	private static ChartSearchService chartSearchService = getComponent(ChartSearchService.class);
	
	public static ChartSearchService getChartSearchService() {
		return chartSearchService;
	}
	
	public static String generateJson(boolean wholePageIsToBeLoaded) {
		
		JSONObject jsonToReturn = new JSONObject();
		List<ChartListItem> returnedResults = SearchAPI.getInstance().getResults();
		boolean foundNoResults = false;
		JSONObject noResults = new JSONObject();
		String searchPhrase = SearchAPI.getInstance().getSearchPhrase().getPhrase();
		
		jsonToReturn.put("search_phrase", searchPhrase);
		
		if (returnedResults == null || returnedResults.isEmpty()) {
			foundNoResults = true;
			noResults.put("foundNoResults", foundNoResults);
			noResults.put("foundNoResultsMessage",
			    Context.getMessageSourceService().getMessage("chartsearch.results.foundNoResults"));
		} else {
			JSONArray arr_of_groups = new JSONArray();
			
			JSONArray arr_of_locations = new JSONArray();
			JSONArray arr_of_providers = new JSONArray();
			JSONArray arr_of_datatypes = new JSONArray();
			JSONObject failedPrivilegeMessages = new JSONObject();
			
			noResults.put("foundNoResults", foundNoResults);
			addObjectsToJsonToReturnElseAddFailedPrivilegesMessages(jsonToReturn, arr_of_groups, arr_of_locations,
			    arr_of_providers, arr_of_datatypes, failedPrivilegeMessages);
			
			addFacetsToJSONObjectToReturn(jsonToReturn);
			
			//add failed privileges to json to be returned to the view
			jsonToReturn.put("failedPrivileges", failedPrivilegeMessages);
		}
		String[] searchSuggestions = getAllPossibleSuggestionsAsArray();
		Integer patientId = SearchAPI.getInstance().getPatientId();
		
		JSONArray history = getAllSearchHistoriesToSendToTheUI(wholePageIsToBeLoaded);
		JSONArray bookmarks = getAllSearchBookmarksToReturnToUI(wholePageIsToBeLoaded);
		List<String> catNms = SearchAPI.getSelectedCategoryNames();
		JSONArray allergies = generateAllergiesJSONFromResults(returnedResults);
		JSONArray appointments = generateAppointmentsJSONFromResults(returnedResults);
		
		jsonToReturn.put("noResults", noResults);
		jsonToReturn.put("retrievalTime", SearchAPI.getInstance().getRetrievalTime());
		jsonToReturn.put("searchSuggestions", searchSuggestions);
		jsonToReturn.put("searchHistory", history);
		jsonToReturn.put("searchBookmarks", bookmarks);
		jsonToReturn.put("appliedCategories", (String[]) catNms.toArray(new String[catNms.size()]));
		jsonToReturn.put("patientAllergies", allergies);
		jsonToReturn.put("patientAppointments", appointments);
		jsonToReturn.put("allLocations", getChartSearchService().getAllLocationsFromTheDB());
		jsonToReturn.put("allProviders", getChartSearchService().getAllProvidersFromTheDB());
		
		addBothPersonalAndGlobalNotesToJSON(searchPhrase, patientId, jsonToReturn, wholePageIsToBeLoaded);
		
		return jsonToReturn.toString();
	}
	
	public static void addBothPersonalAndGlobalNotesToJSON(String searchPhrase, Integer patientId, JSONObject json,
	                                                       boolean wholePageIsToBeLoaded) {
		JSONArray allPersonalNotes = GeneratingJson.getAllPersonalNotesOnASearch(searchPhrase, patientId,
		    wholePageIsToBeLoaded);
		JSONArray allGlobalNotes = GeneratingJson.getAllGlobalNotesOnASearch(searchPhrase, patientId, wholePageIsToBeLoaded);
		String userName = Context.getAuthenticatedUser().getUsername();
		String systemId = Context.getAuthenticatedUser().getSystemId();
		
		json.put("personalNotes", allPersonalNotes);
		json.put("globalNotes", allGlobalNotes);
		json.put("currentUser", null == userName ? systemId : userName);
	}
	
	private static String[] getAllPossibleSuggestionsAsArray() {
		List<String> allPossibleSuggestions = chartSearchService.getAllPossibleSearchSuggestions(SearchAPI.getPatientId());
		String[] searchSuggestions = new String[allPossibleSuggestions.size()];
		searchSuggestions = (String[]) allPossibleSuggestions.toArray(searchSuggestions);
		return searchSuggestions;
	}
	
	public static JSONArray getAllSearchHistoriesToSendToTheUI(boolean wholePageIsToBeLoaded) {
		JSONArray histories = new JSONArray();
		List<ChartSearchHistory> allHistory = chartSearchService.getAllSearchHistory();
		
		for (ChartSearchHistory history : allHistory) {
			JSONObject json = null;
			if (Context.getAuthenticatedUser().getUserId().equals(history.getHistoryOwner().getUserId())
			        && history.getPatient().getPatientId().equals(SearchAPI.getInstance().getPatientId())) {
				json = generateHistoryJSON(wholePageIsToBeLoaded, history);
			}
			if (null != json) {
				histories.add(json);
			}
		}
		
		return histories;
	}
	
	public static JSONArray getAllSearchHistoriesToSendToTheManageUI(boolean wholePageIsToBeLoaded) {
		JSONArray histories = new JSONArray();
		List<ChartSearchHistory> allHistory = chartSearchService.getAllSearchHistory();
		
		for (ChartSearchHistory history : allHistory) {
			JSONObject json = null;
			if (Context.getAuthenticatedUser().getUserId().equals(history.getHistoryOwner().getUserId())) {
				json = generateHistoryJSON(wholePageIsToBeLoaded, history);
			}
			if (null != json) {
				histories.add(json);
			}
		}
		
		return histories;
	}
	
	private static JSONObject generateHistoryJSON(boolean wholePageIsToBeLoaded, ChartSearchHistory history) {
		JSONObject json;
		json = new JSONObject();
		
		if (wholePageIsToBeLoaded) {
			json.put("searchPhrase", appendBackwardSlashBeforeMustBePassedCharacters(history.getSearchPhrase()));
		} else {
			json.put("searchPhrase", history.getSearchPhrase());
		}
		json.put("lastSearchedAt", history.getLastSearchedAt().getTime());//passing timestamp from java to client js is a better practice
		json.put("formattedLastSearchedAt", Context.getDateFormat().format(history.getLastSearchedAt()));
		json.put("uuid", history.getUuid());
		json.put("patientId", history.getPatient().getPatientId());
		json.put("patientFamilyName", history.getPatient().getPersonName().getFamilyName());
		return json;
	}
	
	public static JSONArray getAllPersonalNotesOnASearch(String searchPhrase, Integer patientId,
	                                                     boolean wholePageIsToBeLoaded) {
		JSONArray jsonArr = new JSONArray();
		List<ChartSearchNote> allNotes = chartSearchService.getAllSearchNotes();
		List<ChartSearchNote> allPersonalNotes = new ArrayList<ChartSearchNote>();
		
		for (ChartSearchNote note : allNotes) {
			if (note.getPatient().getPatientId().equals(patientId) && note.getSearchPhrase().equals(searchPhrase)
			        && note.getPriority().equals("LOW")
			        && Context.getAuthenticatedUser().getUserId().equals(note.getNoteOwner().getUserId())) {
				allPersonalNotes.add(note);
			}
		}
		
		if (!allPersonalNotes.isEmpty()) {
			for (ChartSearchNote note : allPersonalNotes) {
				JSONObject json = new JSONObject();
				String userName = note.getNoteOwner().getUsername();
				String systemId = note.getNoteOwner().getSystemId();
				
				json.put("uuid", note.getUuid());
				json.put("createdOrLastModifiedAt", note.getCreatedOrLastModifiedAt().getTime());
				json.put("backgroundColor", note.getDisplayColor());
				json.put("formatedCreatedOrLastModifiedAt", Context.getDateFormat()
				        .format(note.getCreatedOrLastModifiedAt()));
				addPhraseAndCommentNotesAttributes(wholePageIsToBeLoaded, note, json);
				json.put("noteOwner", null == userName ? systemId : userName);
				
				jsonArr.add(json);
			}
		}
		return jsonArr;
	}
	
	public static void addPhraseAndCommentNotesAttributes(boolean wholePageIsToBeLoaded, ChartSearchNote note,
	                                                      JSONObject json) {
		if (wholePageIsToBeLoaded) {
			json.put("comment", appendBackwardSlashBeforeMustBePassedCharacters(note.getComment()));
			json.put("searchPhrase", appendBackwardSlashBeforeMustBePassedCharacters(note.getSearchPhrase()));
		} else {
			json.put("comment", note.getComment());
			json.put("searchPhrase", note.getSearchPhrase());
		}
	}
	
	public static JSONArray getAllGlobalNotesOnASearch(String searchPhrase, Integer patientId, boolean wholePageIsToBeLoaded) {
		JSONArray jsonArr = new JSONArray();
		List<ChartSearchNote> allNotes = chartSearchService.getAllSearchNotes();
		List<ChartSearchNote> allGlobalNotes = new ArrayList<ChartSearchNote>();
		
		for (ChartSearchNote note : allNotes) {
			if (note.getPatient().getPatientId().equals(patientId) && note.getSearchPhrase().equals(searchPhrase)
			        && note.getPriority().equals("HIGH")) {
				allGlobalNotes.add(note);
			}
		}
		
		if (!allGlobalNotes.isEmpty()) {
			for (ChartSearchNote note : allGlobalNotes) {
				JSONObject json = new JSONObject();
				String userName = note.getNoteOwner().getUsername();
				String systemId = note.getNoteOwner().getSystemId();
				
				json.put("uuid", note.getUuid());
				json.put("createdOrLastModifiedAt", note.getCreatedOrLastModifiedAt().getTime());
				json.put("formatedCreatedOrLastModifiedAt", Context.getDateFormat()
				        .format(note.getCreatedOrLastModifiedAt()));
				json.put("backgroundColor", note.getDisplayColor());
				
				addPhraseAndCommentNotesAttributes(wholePageIsToBeLoaded, note, json);
				
				json.put("noteOwner", null == userName ? systemId : userName);
				
				jsonArr.add(json);
			}
		}
		return jsonArr;
	}
	
	public static JSONArray getAllSearchBookmarksToReturnToUI(boolean wholePageIsToBeLoaded) {
		JSONArray bookmarks = new JSONArray();
		List<ChartSearchBookmark> allBookmarks = chartSearchService.getAllSearchBookmarks();
		
		for (ChartSearchBookmark curBookmark : allBookmarks) {
			JSONObject json = null;
			
			if (Context.getAuthenticatedUser().getUserId().equals(curBookmark.getBookmarkOwner().getUserId())
			        && curBookmark.getPatient().getPatientId().equals(SearchAPI.getInstance().getPatientId())) {
				json = generateBookmarksJSON(wholePageIsToBeLoaded, curBookmark);
			}
			
			if (null != json) {
				bookmarks.add(json);
			}
		}
		return bookmarks;
	}
	
	public static JSONArray getAllSearchBookmarksToReturnTomanagerUI(boolean wholePageIsToBeLoaded) {
		JSONArray bookmarks = new JSONArray();
		List<ChartSearchBookmark> allBookmarks = chartSearchService.getAllSearchBookmarks();
		
		for (ChartSearchBookmark curBookmark : allBookmarks) {
			JSONObject json = null;
			
			if (Context.getAuthenticatedUser().getUserId().equals(curBookmark.getBookmarkOwner().getUserId())) {
				json = generateBookmarksJSON(wholePageIsToBeLoaded, curBookmark);
			}
			
			if (null != json) {
				bookmarks.add(json);
			}
		}
		return bookmarks;
	}
	
	private static JSONObject generateBookmarksJSON(boolean wholePageIsToBeLoaded, ChartSearchBookmark curBookmark) {
		JSONObject json;
		json = new JSONObject();
		
		if (wholePageIsToBeLoaded) {
			json.put("bookmarkName", appendBackwardSlashBeforeMustBePassedCharacters(curBookmark.getBookmarkName()));
			json.put("searchPhrase", appendBackwardSlashBeforeMustBePassedCharacters(curBookmark.getSearchPhrase()));
		} else {
			json.put("bookmarkName", curBookmark.getBookmarkName());
			json.put("searchPhrase", curBookmark.getSearchPhrase());
		}
		json.put("categories", curBookmark.getSelectedCategories());
		json.put("uuid", curBookmark.getUuid());
		json.put("patientId", curBookmark.getPatient().getPatientId());
		json.put("isDefaultSearch", curBookmark.isDefaultSearch());
		json.put("patientFamilyName", curBookmark.getPatient().getPersonName().getFamilyName());
		
		return json;
	}
	
	private static void addObjectsToJsonToReturnElseAddFailedPrivilegesMessages(JSONObject jsonToReturn,
	                                                                            JSONArray arr_of_groups,
	                                                                            JSONArray arr_of_locations,
	                                                                            JSONArray arr_of_providers,
	                                                                            JSONArray arr_of_datatypes,
	                                                                            JSONObject failedPrivilegeMessages) {
		try {
			getChartSearchService().addLocationsToJSONToReturn(jsonToReturn, arr_of_locations);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noLocations"));
		}
		
		try {
			getChartSearchService().addProvidersToJSONToReturn(jsonToReturn, arr_of_providers);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noProviders"));
		}
		
		try {
			getChartSearchService().addDatatypesToJSONToReturn(jsonToReturn, arr_of_datatypes);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noDatatypes"));
		}
		
		try {
			getChartSearchService().addObsGroupsToJSONToReturn(jsonToReturn, arr_of_groups);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noObsGroups"));
		}
		
		JSONObject jsonObs = null;
		JSONArray arr_of_obs = new JSONArray();
		try {
			getChartSearchService().addSingleObsToJSONToReturn(jsonToReturn, jsonObs, arr_of_obs);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noSingleObs"));
		}
		
		JSONObject jsonForms = null;
		JSONArray arr_of_forms = new JSONArray();
		try {
			getChartSearchService().addFormsToJSONToReturn(jsonToReturn, jsonForms, arr_of_forms);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noForms"));
		}
		
		JSONObject jsonEncounters = null;
		JSONArray arr_of_encounters = new JSONArray();
		try {
			getChartSearchService().addEncountersToJSONToReturn(jsonToReturn, jsonEncounters, arr_of_encounters);
		}
		catch (APIAuthenticationException e) {
			failedPrivilegeMessages.put("message",
			    Context.getMessageSourceService().getMessage("chartsearch.privileges.failedPrivileges.noEncounters"));
		}
	}
	
	private static void addFacetsToJSONObjectToReturn(JSONObject jsonToReturn) {
		JSONArray arr_of_facets = new JSONArray();
		JSONObject facet = new JSONObject();
		LinkedList<Count> facets = new LinkedList<Count>();
		
		facets.addAll(ChartSearchSearcher.getFacetFieldValueNamesAndCounts());
		if (!facets.isEmpty()) {
			for (int i = facets.indexOf(facets.getFirst()); i <= facets.indexOf(facets.getLast()); i++) {
				facet.put("facet", generateFacetsJson(facets.get(i)));
				arr_of_facets.add(facet);
			}
		}
		jsonToReturn.put("facets", arr_of_facets);
	}
	
	@SuppressWarnings("unused")
	public static JSONObject createJsonObservation(Obs obs) {
		JSONObject jsonObs = new JSONObject();
		jsonObs.put("observation_id", obs.getObsId());
		jsonObs.put("concept_name", obs.getConcept().getDisplayString());
		
		Date obsDate = obs.getObsDatetime() == null ? new Date() : obs.getObsDatetime();
		
		SimpleDateFormat formatDateJava = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String dateStr = obsDate.getTime() + "";
		
		jsonObs.put("date", dateStr);
		
		if (obs.getConcept().getDatatype().isNumeric()) { // ADD MORE DATATYPES
		
			ConceptNumeric conceptNumeric = Context.getConceptService().getConceptNumeric(obs.getConcept().getId());
			jsonObs.put("units_of_measurement", conceptNumeric.getUnits());
			jsonObs.put("absolute_high", conceptNumeric.getHiAbsolute());
			jsonObs.put("absolute_low", conceptNumeric.getLowAbsolute());
			jsonObs.put("critical_high", conceptNumeric.getHiCritical());
			jsonObs.put("critical_low", conceptNumeric.getLowCritical());
			jsonObs.put("normal_high", conceptNumeric.getHiNormal());
			jsonObs.put("normal_low", conceptNumeric.getLowNormal());
		}
		jsonObs.put("value_type", obs.getConcept().getDatatype().getName());
		
		jsonObs.put("value", obs.getValueAsString(Context.getLocale()));
		jsonObs.put("location", obs.getLocation().getDisplayString());
		jsonObs.put("creator", obs.getCreator().getDisplayString());
		Set<EncounterProvider> encounterProviders = obs.getEncounter().getEncounterProviders();
		
		if (encounterProviders != null && encounterProviders.iterator().hasNext()) {
			EncounterProvider provider = encounterProviders.iterator().next();
			if (provider.getProvider() != null) {
				jsonObs.put("provider", provider.getProvider().getName());
			}
		}
		
		SearchAPI searchAPI = SearchAPI.getInstance();
		if (!searchAPI.getSearchPhrase().getPhrase().equals("") && !searchAPI.getSearchPhrase().getPhrase().equals("*")) {
			for (ChartListItem item : searchAPI.getResults()) {
				if (item != null && item instanceof ObsItem && ((ObsItem) item).getObsId() != null) {
					if (((ObsItem) item).getObsId() == obs.getObsId()) {
						jsonObs.put("chosen", "true");
					}
				}
			}
		}
		
		return jsonObs;
	}
	
	public static JSONObject createJsonForm(Form form) {
		JSONObject jsonForm = new JSONObject();
		jsonForm.put("form_id", form.getFormId());
		
		Date formDate = form.getDateCreated() == null ? new Date() : form.getDateCreated();
		
		SimpleDateFormat formatDateJava = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String dateStr = formDate.getTime() + "";
		jsonForm.put("date", dateStr);
		jsonForm.put("encounter_type", form.getEncounterType().getName());
		jsonForm.put("creator", form.getCreator().getName());
		
		formDate = form.getDateChanged() == null ? new Date() : form.getDateChanged();
		dateStr = formatDateJava.format(formDate);
		jsonForm.put("last_changed_date", dateStr);
		
		return jsonForm;
	}
	
	public static JSONObject createJsonEncounter(Encounter encounter) {
		JSONObject jsonEncounter = new JSONObject();
		jsonEncounter.put("encounter_id", encounter.getEncounterId());
		
		return jsonEncounter;
	}
	
	public static Set<Set<Obs>> generateObsGroupFromSearchResults() {
		Set<Set<Obs>> obsGroups = new HashSet<Set<Obs>>();
		
		SearchAPI searchAPI = SearchAPI.getInstance();
		List<ChartListItem> searchResultsList = searchAPI.getResults();
		for (ChartListItem item : searchResultsList) { //for each item in results we classify it by its obsGroup, and add all of the group.
			if (item != null && item instanceof ObsItem && ((ObsItem) item).getObsId() != null) {
				int itemObsId = ((ObsItem) item).getObsId();
				Obs obsGrp = Context.getObsService().getObs(itemObsId).getObsGroup();
				if (obsGrp != null) {
					int groupId = obsGrp.getId();
					Set<Obs> obsGroup = obsGrp.getGroupMembers();
					boolean found = false; //if found == true then we don't need to add the group.
					for (Set<Obs> grp : obsGroups) {
						Obs ob = new Obs(-1);
						if (grp.iterator().hasNext()) {
							ob = grp.iterator().next();
						}
						if (ob.getObsGroup() != null && ob.getObsGroup().getId() != null) {
							if (ob.getObsGroup().getId() == groupId) {
								found = true;
							}
						}
					}
					if (!found) {
						obsGroups.add(obsGroup);
					}
				}
			}
		}
		return obsGroups;
	}
	
	public static Set<Obs> generateObsSinglesFromSearchResults() {
		SearchAPI searchAPI = SearchAPI.getInstance();
		Set<Obs> obsSingles = new HashSet<Obs>();
		for (ChartListItem item : searchAPI.getResults()) {
			if (item != null && item instanceof ObsItem && ((ObsItem) item).getObsId() != null) {
				int itemObsId = ((ObsItem) item).getObsId();
				
				Obs obs = Context.getObsService().getObs(itemObsId);
				if (obs != null && obs.getObsGroup() == null && !obs.isObsGrouping()) {
					obsSingles.add(Context.getObsService().getObs(itemObsId));
				}
			}
		}
		return obsSingles;
	}
	
	public static JSONArray generateAllergiesJSONFromResults(List<ChartListItem> returnedResults) {
		JSONArray allergies = new JSONArray();
		
		for (ChartListItem item : returnedResults) {
			if (item != null && item instanceof AllergyItem) {
				AllergyItem allergy = (AllergyItem) item;
				JSONObject json = new JSONObject();
				if (allergy.getAllergyId() != null) {
					json.put("allergenId", allergy.getAllergyId());
					json.put("allergenUuid", allergy.getUuid());
					json.put("allergenCodedName", allergy.getAllergenCodedName());
					json.put("allergenNonCodedName", allergy.getAllergenNonCodedName());
					json.put("allergenSeverity", allergy.getAllergenSeverity());
					json.put("allergenType", allergy.getAllergenType());
					json.put("allergenCodedReaction", allergy.getAllergenCodedReaction());
					json.put("allergenNonCodedReaction", allergy.getAllergenNonCodedReaction());
					json.put("allergenComment", allergy.getAllergenComment());
					json.put("allergenDate", allergy.getAllergenDate().getTime());
					
					allergies.add(json);
				}
			}
		}
		
		return allergies;
	}
	
	private static JSONArray generateAppointmentsJSONFromResults(List<ChartListItem> returnedResults) {
		JSONArray appointments = new JSONArray();
		
		for (ChartListItem item : returnedResults) {
			if (item != null && item instanceof AppointmentItem) {
				AppointmentItem appointment = (AppointmentItem) item;
				JSONObject json = new JSONObject();
				if (appointment.getAppointmentId() != null) {
					json.put("id", appointment.getAppointmentId());
					json.put("uuid", appointment.getUuid());
					json.put("status", appointment.getStatus());
					json.put("reason", appointment.getReason());
					json.put("type", appointment.getType());
					json.put("start", appointment.getStart().getTime());
					json.put("end", appointment.getEnd().getTime());
					json.put("typeDesc", appointment.getTypeDesc());
					json.put("cancelReason", appointment.getCancelReason());
					json.put("provider", appointment.getProvider());
					json.put("location", appointment.getLocation());
					
					appointments.add(json);
				}
			}
		}
		
		return appointments;
	}
	
	public static JSONObject generateLocationJson(String location) {
		JSONObject jsonLocation = new JSONObject();
		jsonLocation.put("location", location);
		return jsonLocation;
	}
	
	public static JSONObject generateProviderJson(String provider) {
		JSONObject jsonProvider = new JSONObject();
		jsonProvider.put("provider", provider);
		return jsonProvider;
	}
	
	public static JSONObject generateDatatypeJson(String datatype) {
		JSONObject jsonDatatype = new JSONObject();
		jsonDatatype.put("datatype", datatype);
		return jsonDatatype;
	}
	
	public static Set<String> generateLocationsFromResults() {
		Set<String> res = new HashSet<String>();
		SearchAPI searchAPI = SearchAPI.getInstance();
		
		for (ChartListItem item : searchAPI.getResults()) {
			if (item != null && item instanceof ObsItem && ((ObsItem) item).getObsId() != null) {
				int itemObsId = ((ObsItem) item).getObsId();
				
				Obs obs = Context.getObsService().getObs(itemObsId);
				if (obs != null) {
					res.add(obs.getLocation().getDisplayString());
				}
			}
		}
		return res;
	}
	
	public static Set<String> generateProvidersFromResults() {
		Set<String> res = new HashSet<String>();
		SearchAPI searchAPI = SearchAPI.getInstance();
		
		for (ChartListItem item : searchAPI.getResults()) {
			if (item != null && item instanceof ObsItem && ((ObsItem) item).getObsId() != null) {
				int itemObsId = ((ObsItem) item).getObsId();
				
				Obs obs = Context.getObsService().getObs(itemObsId);
				if (obs != null) {
					Set<EncounterProvider> encounterProviders = obs.getEncounter().getEncounterProviders();
					
					if (encounterProviders != null && encounterProviders.iterator().hasNext()) {
						EncounterProvider provider = encounterProviders.iterator().next();
						if (provider.getProvider() != null) {
							res.add(provider.getProvider().getName());
						}
					}
				}
			}
		}
		return res;
	}
	
	public static Set<String> generateDatatypesFromResults() {
		Set<String> res = new HashSet<String>();
		SearchAPI searchAPI = SearchAPI.getInstance();
		
		for (ChartListItem item : searchAPI.getResults()) {
			if (item != null && item instanceof ObsItem && ((ObsItem) item).getObsId() != null) {
				int itemObsId = ((ObsItem) item).getObsId();
				
				Obs obs = Context.getObsService().getObs(itemObsId);
				if (obs != null) {
					res.add(obs.getConcept().getDatatype().getName());
				}
			}
		}
		return res;
	}
	
	public static Set<Form> generateFormsFromSearchResults() {
		SearchAPI searchAPI = SearchAPI.getInstance();
		Set<Form> forms = new HashSet<Form>();
		List<ChartListItem> searchResultsList = searchAPI.getResults();
		
		for (ChartListItem item : searchResultsList) {
			
			if (item != null && item instanceof FormItem) {
				
				if (((FormItem) item).getFormId() != null) {
					int itemFormId = ((FormItem) item).getFormId();
					Form form = Context.getFormService().getForm(itemFormId);
					if (form != null) {
						forms.add(Context.getFormService().getForm(itemFormId));
					}
				}
			}
		}
		return forms;
	}
	
	public static Set<Encounter> generateEncountersFromSearchResults() {
		SearchAPI searchAPI = SearchAPI.getInstance();
		Set<Encounter> encounters = new HashSet<Encounter>();
		List<ChartListItem> searchResultsList = searchAPI.getResults();
		for (ChartListItem item : searchResultsList) {
			if (item != null && item instanceof EncounterItem && ((EncounterItem) item).getEncounterId() != null) {
				int itemEncounterId = ((EncounterItem) item).getEncounterId();
				
				Encounter encounter = Context.getEncounterService().getEncounter(itemEncounterId);
				if (encounter != null) {
					encounters.add(Context.getEncounterService().getEncounter(itemEncounterId));
				}
			}
		}
		return encounters;
	}
	
	public static JSONObject generateFacetsJson(Count facet) {
		JSONObject counts = new JSONObject();
		counts.put("name", facet.getName());
		counts.put("count", facet.getCount());
		return counts;
	}
	
	private static String appendBackwardSlashBeforeMustBePassedCharacters(String str) {
		if (str.contains("'")) {
			//str = str.replace("'", "\\'");
		}
		if (str.contains("\"")) {
			str = str.replace("\"", "\\\"");
		}
		return str;
	}
	
	private static JSONObject generatePreferencesJSON(ChartSearchPreference pref) {
		JSONObject json = new JSONObject();
		//ChartSearchPreference pref = chartSearchService.getRightMatchedPreferences();
		
		if (pref != null) {
			json.put("owner", pref.getPreferenceOwner().getUsername());
			json.put("notesColors", pref.gePersonalNotesColorsArray());
			json.put("uuid", pref.getUuid());
			json.put("enableBookmarks", pref.isEnableBookmarks());
			json.put("enableDefaultSearch", pref.isEnableDefaultSearch());
			json.put("enableDuplicateResults", pref.isEnableDuplicateResults());
			json.put("enableHistory", pref.isEnableHistory());
			json.put("enableMultipleFiltering", pref.isEnableMultipleFiltering());
			json.put("enableNotes", pref.isEnableNotes());
			json.put("enableQuickSearches", pref.isEnableQuickSearches());
		}
		return json;
	}
	
	public static JSONObject generateDaemonPreferencesJSON() {
		return generatePreferencesJSON(chartSearchService.getChartSearchPreferenceOfAUser(2));
	}
	
	public static JSONObject generateRightMatchedPreferencesJSON() {
		return generatePreferencesJSON(chartSearchService.getRightMatchedPreferences());
	}
	
	private static <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
