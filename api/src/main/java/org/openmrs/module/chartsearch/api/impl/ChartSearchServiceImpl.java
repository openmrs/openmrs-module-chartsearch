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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.chartsearch.GeneratingJson;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.api.db.CategoryFilterDAO;
import org.openmrs.module.chartsearch.api.db.ChartSearchDAO;
import org.openmrs.module.chartsearch.api.db.SynonymDAO;
import org.openmrs.module.chartsearch.api.db.SynonymGroupDAO;
import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;
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
		for (Obs obsSingle : GeneratingJson.generateObsSinglesFromSearchResults()) {
			if (obsSingle != null) {
				jsonObs = GeneratingJson.createJsonObservation(obsSingle);
			}
			arr_of_obs.add(jsonObs);
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
	public void deleteSearchProject(SearchProject project) {
		dao.deleteSearchProject(project);
	}
	
	@Override
	public void saveSearchProject(SearchProject project) {
		dao.saveSearchProject(project);
	}
	
	@Override
	public SearchProject getSearchProject(Integer projectId) {
		return dao.getSearchProject(projectId);
	}
	
	@Override
	public List<SearchProject> getAllSearchProjects() {
		return dao.getAllSearchProjects();
	}
	
	@Override
	public String getAllColumnNamesFromAllProjectsSeperatedByCommaAndSpace() {
		List<SearchProject> allProjects = getAllSearchProjects();
		String columnsFromAllProject = "";
		
		for (int i = 0; i < allProjects.size(); i++) {
			if (!columnsFromAllProject.contains(allProjects.get(i).getColumnNames())) {
				if (i == allProjects.size() - 1) {//if on the last project
					columnsFromAllProject += allProjects.get(i).getColumnNames();
				} else {
					columnsFromAllProject += allProjects.get(i).getColumnNames() + ", ";
				}
			}
		}
		return columnsFromAllProject;
	}
	
	public String getAllFieldsSetInSchemaByDefault() {
		return "id, meta, obs_id, person_id, concept_name, obs_datetime, value_boolean, value_datetime, "
		        + "text, obs_group_id, value_numeric, coded, value_text, concept_class_name, form_id, form_name, "
		        + "encounter_type_name, date_created, encounter_id, patient_id, e_form_id, encounter_type, "
		        + "encounter_datetime, visit_id, _version_, value, project_id, project_name, project_description, "
		        + "project_uuid, project_db_name, cc_name, cc_filter_query, cc_description";
	}
	
	/**
	 * Gets all column names from all projects of all projects and checks whether the passed in
	 * column name is one of the existing columns, should be checked before adding any new column
	 * 
	 * @see org.openmrs.module.chartsearch.api.ChartSearchService#checkIfColumnExists(java.lang.String)
	 */
	@Override
	public boolean checkIfColumnExists(String columnName) {
		String allColumnsFromAllProjects = getAllColumnNamesFromAllProjectsSeperatedByCommaAndSpace();
		if (allColumnsFromAllProjects.contains(columnName)) {
			return true;
		} else
			return false;
	}
	
	@Override
	public SearchProject getSearchProjectByUuid(String uuid) {
		return dao.getSearchProjectByUuid(uuid);
	}
}
