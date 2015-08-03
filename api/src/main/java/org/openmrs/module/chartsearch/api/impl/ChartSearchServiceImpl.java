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
package org.openmrs.module.chartsearch.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Provider;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.allergyapi.Allergies;
import org.openmrs.module.allergyapi.Allergy;
import org.openmrs.module.allergyapi.api.PatientService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.appointmentscheduling.api.AppointmentService;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.api.db.CategoryFilterDAO;
import org.openmrs.module.chartsearch.api.db.ChartSearchDAO;
import org.openmrs.module.chartsearch.api.db.SynonymDAO;
import org.openmrs.module.chartsearch.api.db.SynonymGroupDAO;
import org.openmrs.module.chartsearch.cache.ChartSearchBookmark;
import org.openmrs.module.chartsearch.cache.ChartSearchCategoryDisplayName;
import org.openmrs.module.chartsearch.cache.ChartSearchHistory;
import org.openmrs.module.chartsearch.cache.ChartSearchNote;
import org.openmrs.module.chartsearch.cache.ChartSearchPreference;
import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is a default implementation of {@link ChartSearchService}.
 */
public class ChartSearchServiceImpl extends BaseOpenmrsService implements ChartSearchService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SynonymDAO synonymDAO;
	
	private SynonymGroupDAO synonymGroupDAO;
	
	private CategoryFilterDAO categoryFilterDAO;
	
	private ChartSearchDAO dao;
	
	/**
	 * Getters and Setters
	 */
	public SynonymGroupDAO getSynonymGroupDAO() {
		return synonymGroupDAO;
	}
	
	public void setSynonymGroupDAO(SynonymGroupDAO synonymGroupDAO) {
		this.synonymGroupDAO = synonymGroupDAO;
	}
	
	public SynonymDAO getSynonymDAO() {
		return synonymDAO;
	}
	
	public void setSynonymDAO(SynonymDAO synonymDAO) {
		this.synonymDAO = synonymDAO;
	}
	
	public CategoryFilterDAO getCategoryFilterDAO() {
		return categoryFilterDAO;
	}
	
	public void setCategoryFilterDAO(CategoryFilterDAO categoryFilterDAO) {
		this.categoryFilterDAO = categoryFilterDAO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public SynonymGroup getSynonymGroupById(Integer id) {
		return (SynonymGroup) getSynonymGroupDAO().getById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<SynonymGroup> getAllSynonymGroups() {
		List<SynonymGroup> list = new ArrayList<SynonymGroup>();
		list.addAll(getSynonymGroupDAO().getAll());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void purgeSynonymGroup(SynonymGroup synGroup) {
		getSynonymGroupDAO().delete(synGroup);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public SynonymGroup saveSynonymGroup(SynonymGroup synGroup) throws APIException {
		return (SynonymGroup) getSynonymGroupDAO().saveOrUpdate(synGroup);
	}
	
	@Override
	@Transactional(readOnly = true)
	public SynonymGroup getSynonymGroupByName(String groupName) {
		return getSynonymGroupDAO().getSynonymGroupByName(groupName);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SynonymGroup> getSynonymGroupsIsCategory(boolean isCategory) {
		return getSynonymGroupDAO().getSynonymGroupsIsCategory(isCategory);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer getCountOfSynonymGroups(boolean byIsCategory) {
		return getSynonymGroupDAO().getCountOfSynonymGroups(byIsCategory);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Synonym getSynonymById(Integer id) {
		return (Synonym) getSynonymDAO().getById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Synonym> getAllSynonyms() {
		return getSynonymDAO().getAll();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void purgeSynonym(Synonym synonym) {
		getSynonymDAO().delete(synonym);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Synonym saveSynonym(Synonym synonym) throws APIException {
		return (Synonym) getSynonymDAO().saveOrUpdate(synonym);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Synonym> getSynonymsByGroup(SynonymGroup synonymGroup) {
		return getSynonymDAO().getSynonymsByGroup(synonymGroup);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer getSynonymsCountByGroup(SynonymGroup synonymGroup) {
		return getSynonymDAO().getSynonymsCountByGroup(synonymGroup);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CategoryFilter getACategoryFilterByItsId(Integer categoryFilterId) {
		return getCategoryFilterDAO().getCategoryFilter(categoryFilterId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CategoryFilter> getAllCategoryFilters() {
		return getCategoryFilterDAO().getAllCategoryFilters();
	}
	
	public void setDao(ChartSearchDAO dao) {
		this.dao = dao;
	}
	
	@Override
	@Transactional
	public void createACategoryFilter(CategoryFilter categoryFilter) {
		getCategoryFilterDAO().createCategoryFilter(categoryFilter);
	}
	
	@Override
	@Transactional
	public void updateACategoryFilter(CategoryFilter categoryFilter) {
		getCategoryFilterDAO().updateCategoryFilter(categoryFilter);
	}
	
	@Override
	@Transactional
	public void deleteACategoryFilter(CategoryFilter categoryFilter) {
		getCategoryFilterDAO().deleteCategoryFilter(categoryFilter);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CategoryFilter getACategoryFilterByItsUuid(String uuid) {
		return getCategoryFilterDAO().getCategoryFilterByUuid(uuid);
	}
	
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_ENCOUNTERS })
	public void addEncountersToJSONToReturn(JSONObject jsonToReturn, JSONObject jsonEncounters, JSONArray arr_of_encounters) {
		for (Encounter encounter : GeneratingJson.generateEncountersFromSearchResults()) {
			if (encounter != null) {
				jsonEncounters = GeneratingJson.createJsonEncounter(encounter);
			}
			arr_of_encounters.add(jsonEncounters);
		}
		jsonToReturn.put("encounters", arr_of_encounters);
	}
	
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_FORMS })
	public void addFormsToJSONToReturn(JSONObject jsonToReturn, JSONObject jsonForms, JSONArray arr_of_forms) {
		for (Form form : GeneratingJson.generateFormsFromSearchResults()) {
			if (form != null) {
				jsonForms = GeneratingJson.createJsonForm(form);
			}
			arr_of_forms.add(jsonForms);
		}
		jsonToReturn.put("forms", arr_of_forms);
	}
	
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_OBS })
	public void addSingleObsToJSONToReturn(JSONObject jsonToReturn, JSONObject jsonObs, JSONArray arr_of_obs) {
		JSONArray duplicateJsonObs = new JSONArray();
		boolean enableDupResults = getRightMatchedPreferences().isEnableDuplicateResults();
		
		for (Obs obsSingle : GeneratingJson.generateObsSinglesFromSearchResults()) {
			if (obsSingle != null) {
				jsonObs = GeneratingJson.createJsonObservation(obsSingle);
				
				if (arr_of_obs.size() == 0) {
					arr_of_obs.add(jsonObs);
				} else {
					if (enableDupResults) {
						JSONObject dupObsJson = null;
						
						for (int i = 0; i < arr_of_obs.size(); i++) {
							JSONObject obs = arr_of_obs.getJSONObject(i);
							
							if (obs.getString("concept_name").equals(jsonObs.getString("concept_name"))) {
								dupObsJson = jsonObs;
								break;
							}
							
						}
						
						if (dupObsJson != null && dupObsJson.equals(jsonObs)
						        && getRightMatchedPreferences().isEnableDuplicateResults()) {
							duplicateJsonObs.add(dupObsJson);
						} else {
							arr_of_obs.add(jsonObs);
						}
					}
				}
			}
		}
		
		if (enableDupResults) {
			jsonToReturn.put("duplicate_obs_singles", duplicateJsonObs);
		}
		jsonToReturn.put("obs_singles", arr_of_obs);
	}
	
	@SuppressWarnings("unused")
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_OBS })
	public void addObsGroupsToJSONToReturn(JSONObject jsonToReturn, JSONArray arr_of_groups) {
		Set<Set<Obs>> setOfObsGroups = GeneratingJson.generateObsGroupFromSearchResults();
		for (Set<Obs> obsGrpSet : setOfObsGroups) { //for each obs group we go through it's obs
			JSONArray arr_of_obs = new JSONArray(); //array of all the obs in a given obs group
			JSONObject jsonObs = null;
			JSONObject jsonGrp = new JSONObject();
			for (Obs obs : obsGrpSet) { //for each obs in a group we create the single obs and add it to the obs array
				if (obs != null) {
					jsonObs = GeneratingJson.createJsonObservation(obs);
				}
				arr_of_obs.add(jsonObs);
			}
			int obsGrpId = -1; //init the obs grp id to -1, if there is a grp in
			if (obsGrpSet.iterator().hasNext()) { //check inside of group an individual obs, if available, what its grp id
				Obs obsFromObsGrp = obsGrpSet.iterator().next();
				Obs obsGrp = obsFromObsGrp.getObsGroup();
				obsGrpId = obsGrp.getObsId();
				
				jsonGrp.put("group_Id", obsGrpId);
				jsonGrp.put("group_name", obsGrp.getConcept().getDisplayString());
				
				Date obsDate = obsGrp.getObsDatetime() == null ? new Date() : obsGrp.getObsDatetime();
				SimpleDateFormat formatDateJava = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				String obsDateStr = obsDate.getTime() + "";
				
				jsonGrp.put("last_taken_date", obsDateStr);
				jsonGrp.put("observations", arr_of_obs);
				arr_of_groups.add(jsonGrp);
			}
		}
		
		jsonToReturn.put("obs_groups", arr_of_groups); //add the obs groups array to the json
	}
	
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_CONCEPT_DATATYPES })
	public void addDatatypesToJSONToReturn(JSONObject jsonToReturn, JSONArray arr_of_datatypes) {
		for (String datatype : GeneratingJson.generateDatatypesFromResults()) {
			JSONObject jsonDatatype = GeneratingJson.generateDatatypeJson(datatype);
			arr_of_datatypes.add(jsonDatatype);
		}
		jsonToReturn.put("datatypes", arr_of_datatypes);
	}
	
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_PROVIDERS })
	public void addProvidersToJSONToReturn(JSONObject jsonToReturn, JSONArray arr_of_providers) {
		for (String provider : GeneratingJson.generateProvidersFromResults()) {
			JSONObject jsonProvider = GeneratingJson.generateProviderJson(provider);
			arr_of_providers.add(jsonProvider);
		}
		jsonToReturn.put("providers", arr_of_providers);
	}
	
	@Override
	@Authorized(value = { PrivilegeConstants.VIEW_LOCATIONS })
	public void addLocationsToJSONToReturn(JSONObject jsonToReturn, JSONArray arr_of_locations) {
		for (String location : GeneratingJson.generateLocationsFromResults()) {
			JSONObject jsonLoc = GeneratingJson.generateLocationJson(location);
			arr_of_locations.add(jsonLoc);
		}
		jsonToReturn.put("locations", arr_of_locations);
	}
	
	@Override
	public void indexAllPatientData(Integer numberOfResults, SolrServer solrServer, Class showProgressToClass) {
		dao.indexAllPatientData(numberOfResults, solrServer, showProgressToClass);
	}
	
	@Override
	public List<String> getAllPossibleSearchSuggestions(Integer patientId) {
		ObsService obsService = Context.getObsService();
		List<String> suggestions = new ArrayList<String>();
		
		List<Obs> allPatientObs = obsService.getObservationsByPerson(Context.getPatientService().getPatient(patientId));
		Allergies allAllergies = Context.getService(PatientService.class).getAllergies(
		    Context.getPatientService().getPatient(patientId));
		List<Appointment> appointments = Context.getService(AppointmentService.class).getAllAppointments();
		
		for (Obs currentObs : allPatientObs) {
			if (currentObs != null && currentObs.getConcept() != null && currentObs.getConcept().getName() != null) {
				String currentConceptName = currentObs.getConcept().getName().getName();
				if (StringUtils.isNotBlank(currentConceptName)) {
					suggestions.add(currentConceptName);
				}
			}
		}
		for (Allergy allergy : allAllergies) {
			if (allergy != null) {
				String nonCoded = allergy.getAllergen().getNonCodedAllergen();
				String coded = allergy.getAllergen().getCodedAllergen().getName().getName();
				if (StringUtils.isNotBlank(nonCoded)) {
					suggestions.add(nonCoded);
				} else {
					suggestions.add(coded);
				}
			}
		}
		for (Appointment app : appointments) {
			if (app != null) {
				String appType = app.getAppointmentType().getDisplayString();
				if (StringUtils.isNotBlank(appType)) {
					suggestions.add(appType);
				}
			}
		}
		
		return suggestions;
	}
	
	@Override
	public ChartSearchHistory getSearchHistory(Integer searchId) {
		return dao.getSearchHistory(searchId);
	}
	
	@Override
	public void saveSearchHistory(ChartSearchHistory searchHistory) {
		dao.saveSearchHistory(searchHistory);
	}
	
	@Override
	public void deleteSearchHistory(ChartSearchHistory searchHistory) {
		dao.deleteSearchHistory(searchHistory);
	}
	
	@Override
	public ChartSearchHistory getSearchHistoryByUuid(String uuid) {
		return dao.getSearchHistoryByUuid(uuid);
	}
	
	@Override
	public List<ChartSearchHistory> getAllSearchHistory() {
		return dao.getAllSearchHistory();
	}
	
	@Override
	public ChartSearchBookmark getSearchBookmark(Integer bookmarkId) {
		return dao.getSearchBookmark(bookmarkId);
	}
	
	@Override
	public void saveSearchBookmark(ChartSearchBookmark bookmark) {
		dao.saveSearchBookmark(bookmark);
	}
	
	@Override
	public void deleteSearchBookmark(ChartSearchBookmark bookmark) {
		dao.deleteSearchBookmark(bookmark);
	}
	
	@Override
	public List<ChartSearchBookmark> getAllSearchBookmarks() {
		return dao.getAllSearchBookmarks();
	}
	
	@Override
	public ChartSearchBookmark getSearchBookmarkByUuid(String uuid) {
		return dao.getSearchBookmarkByUuid(uuid);
	}
	
	@Override
	public void saveSearchNote(ChartSearchNote note) {
		dao.saveSearchNote(note);
	}
	
	@Override
	public void deleteSearchNote(ChartSearchNote note) {
		dao.deleteSearchNote(note);
	}
	
	@Override
	public ChartSearchNote getSearchNote(Integer noteId) {
		return dao.getSearchNote(noteId);
	}
	
	@Override
	public ChartSearchNote getSearchNoteByUuid(String uuid) {
		return dao.getSearchNoteByUuid(uuid);
	}
	
	@Override
	public List<ChartSearchNote> getAllSearchNotes() {
		return dao.getAllSearchNotes();
	}
	
	@Override
	public String[] getAllProvidersFromTheDB() {
		List<Provider> providerList = Context.getProviderService().getAllProviders();
		String[] providers = new String[providerList.size()];
		
		for (int i = 0; i < providerList.size(); i++) {
			Provider p = providerList.get(i);
			
			if (p != null && StringUtils.isNotBlank(p.getName())) {
				providers[i] = p.getName();
			}
		}
		
		return providers;
	}
	
	@Override
	public String[] getAllLocationsFromTheDB() {
		List<Location> locationList = Context.getLocationService().getAllLocations();
		String[] locations = new String[locationList.size()];
		
		for (int i = 0; i < locationList.size(); i++) {
			Location l = locationList.get(i);
			
			if (l != null && StringUtils.isNotBlank(l.getName())) {
				locations[i] = l.getName();
			}
		}
		
		return locations;
	}
	
	@Override
	public boolean saveANewChartSearchPreference(ChartSearchPreference preference) {
		return dao.saveANewChartSearchPreference(preference);
		
	}
	
	@Override
	public void deleteChartSearchPreference(ChartSearchPreference preference) {
		dao.deleteChartSearchPreference(preference);
	}
	
	@Override
	public List<ChartSearchPreference> getAllChartSearchPreferences() {
		return dao.getAllChartSearchPreferences();
	}
	
	@Override
	public ChartSearchPreference getChartSearchPreferenceByUuid(String uuid) {
		return dao.getChartSearchPreferenceByUuid(uuid);
	}
	
	@Override
	public ChartSearchPreference getChartSearchPreferenceOfAUser(Integer userId) {
		return dao.getChartSearchPreferenceOfAUser(userId);
	}
	
	@Override
	public void updateChartSearchPreference(ChartSearchPreference pref) {
		dao.updateChartSearchPreference(pref);
	}
	
	@Override
	public ChartSearchPreference getChartSearchPreference(Integer preferenceId) {
		return dao.getChartSearchPreference(preferenceId);
	}
	
	@Override
	public ChartSearchPreference getRightMatchedPreferences() {
		return dao.getRightMatchedPreferences();
	}
	
	@Override
	public ChartSearchCategoryDisplayName getCategoryDisplayNameByUuid(String uuid) {
		return dao.getCategoryDisplayNameByUuid(uuid);
	}
	
	@Override
	public List<ChartSearchCategoryDisplayName> getAllCategoryDisplayNames() {
		return dao.getAllCategoryDisplayNames();
	}
	
	@Override
	public void saveChartSearchCategoryDisplayName(ChartSearchCategoryDisplayName displayName) {
		dao.saveChartSearchCategoryDisplayName(displayName);
		
	}
	
	@Override
	public void deleteChartSearchCategoryDisplayName(ChartSearchCategoryDisplayName displayName) {
		dao.deleteChartSearchCategoryDisplayName(displayName);
	}
}
