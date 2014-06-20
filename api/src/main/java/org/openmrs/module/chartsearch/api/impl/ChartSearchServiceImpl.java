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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.api.db.CategoryFilterDAO;
import org.openmrs.module.chartsearch.api.db.FacetForACategoryFilterDAO;
import org.openmrs.module.chartsearch.api.db.SynonymDAO;
import org.openmrs.module.chartsearch.api.db.SynonymGroupDAO;
import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.categories.FacetForACategoryFilter;
import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is a default implementation of {@link ChartSearchService}.
 */
public class ChartSearchServiceImpl extends BaseOpenmrsService implements ChartSearchService {
	
	protected final Log log = LogFactory.getLog(this.getClass());

    private SynonymDAO synonymDAO;
	
    private SynonymGroupDAO synonymGroupDAO;
    
    private CategoryFilterDAO categoryFilterDAO;
    
    private FacetForACategoryFilterDAO facetForACategoryFilterDAO;

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

	public FacetForACategoryFilterDAO getFacetForACategoryFilterDAO() {
	    return facetForACategoryFilterDAO;
    }

	public void setFacetForACategoryFilterDAO(FacetForACategoryFilterDAO facetForACategoryFilterDAO) {
	    this.facetForACategoryFilterDAO = facetForACategoryFilterDAO;
    }

	@Override
    @Transactional(readOnly = true)
    public SynonymGroup getSynonymGroupById(Integer id) {
        return (SynonymGroup)getSynonymGroupDAO().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SynonymGroup> getAllSynonymGroups() {
        List<SynonymGroup> list = new ArrayList<SynonymGroup>();
        list.addAll(getSynonymGroupDAO().getAll());
        return list;
    }

    @Override
    @Transactional
    public void purgeSynonymGroup(SynonymGroup synGroup) {
        getSynonymGroupDAO().delete(synGroup);
    }

    @Override
    @Transactional
    public SynonymGroup saveSynonymGroup(SynonymGroup synGroup) throws APIException {
        return (SynonymGroup)getSynonymGroupDAO().saveOrUpdate(synGroup);
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
        return (Synonym)getSynonymDAO().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Synonym> getAllSynonyms() {
        return getSynonymDAO().getAll();
    }

    @Override
    @Transactional
    public void purgeSynonym(Synonym synonym) {
        getSynonymDAO().delete(synonym);
    }

    @Override
    @Transactional
    public Synonym saveSynonym(Synonym synonym) throws APIException {
        return (Synonym)getSynonymDAO().saveOrUpdate(synonym);
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
	@Transactional(readOnly = true)
    public FacetForACategoryFilter getFacetForACategoryFilterByItsId(Integer facetForACategoryFilter) {
	    return getFacetForACategoryFilterDAO().getFacetForACategoryFilter(facetForACategoryFilter);
    }

	@Override
	@Transactional(readOnly = true)
    public List<FacetForACategoryFilter> getAllFacetsForACategoryFilter(CategoryFilter categoryFilter) {
	    return getFacetForACategoryFilterDAO().getAllFacetsForACategoryFilter(categoryFilter);
    }

	@Override
	@Transactional
    public void createFacetForACategoryFilter(FacetForACategoryFilter facetForACategoryFilter) {
		getFacetForACategoryFilterDAO().createFacetForACategoryFilter(facetForACategoryFilter);
    }

	@Override
	@Transactional
    public void updateFacetForACategoryFilter(FacetForACategoryFilter facetForACategoryFilter) {
		getFacetForACategoryFilterDAO().updateFacetForACategoryFilter(facetForACategoryFilter);
    }

	@Override
	@Transactional
    public void deleteFacetForACategoryFilter(FacetForACategoryFilter facetForACategoryFilter) {
		getFacetForACategoryFilterDAO().deleteFacetForACategoryFilter(facetForACategoryFilter);
    }

	@Override
	@Transactional(readOnly = true)
    public CategoryFilter getCategoryFilterWhereAFacetBelongs(FacetForACategoryFilter facetForACategoryFilter) {
	    return getFacetForACategoryFilterDAO().getCategoryFilterWhereAFacetBelongs(facetForACategoryFilter);
    }

	@Override
	@Transactional(readOnly = true)
    public FacetForACategoryFilter getFacetForACategoryFilterByItsUuid(String uuid) {
	    return getFacetForACategoryFilterDAO().getFacetForACategoryFilterByUuid(uuid);
    }

	@Override
    public List<FacetForACategoryFilter> getAllFacets() {
	    return getFacetForACategoryFilterDAO().getAllFacets();
    }
}