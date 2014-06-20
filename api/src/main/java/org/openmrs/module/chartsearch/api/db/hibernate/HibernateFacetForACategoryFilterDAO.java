/**
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.chartsearch.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.module.chartsearch.api.db.FacetForACategoryFilterDAO;
import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.categories.FacetForACategoryFilter;

/**
 * Default implementation for {@link FacetForACategoryFilterDAO}
 */
public class HibernateFacetForACategoryFilterDAO implements FacetForACategoryFilterDAO {
	
	protected final static Log log = LogFactory.getLog(HibernateFacetForACategoryFilterDAO.class);
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public FacetForACategoryFilter getFacetForACategoryFilter(Integer facetForACategoryFilter) {
		return (FacetForACategoryFilter) sessionFactory.getCurrentSession().get(FacetForACategoryFilter.class,
		    facetForACategoryFilter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FacetForACategoryFilter> getAllFacetsForACategoryFilter(CategoryFilter categoryFilter) {
		log.info("getting all facets for category named: " + categoryFilter.getCategoryName());
		return sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            "FROM FacetForACategoryFilter facet WHERE facet.categoryFilter = " + categoryFilter
		                    + " ORDER BY scf.facetId").list();
	}
	
	@Override
	public void createFacetForACategoryFilter(FacetForACategoryFilter facetForACategoryFilter) {
		sessionFactory.getCurrentSession().save(facetForACategoryFilter);
	}
	
	@Override
	public void updateFacetForACategoryFilter(FacetForACategoryFilter facetForACategoryFilter) {
		sessionFactory.getCurrentSession().update(facetForACategoryFilter);
	}
	
	@Override
	public void deleteFacetForACategoryFilter(FacetForACategoryFilter facetForACategoryFilter) {
		sessionFactory.getCurrentSession().delete(facetForACategoryFilter);
	}
	
	@Override
	public FacetForACategoryFilter getFacetForACategoryFilterByUuid(String uuid) {
		return (FacetForACategoryFilter) sessionFactory.getCurrentSession()
		        .createQuery("FROM FacetForACategoryFilter facet WHERE facet.facetUuid = :uuid").setString("uuid", uuid)
		        .uniqueResult();
	}
	
	@Override
	public CategoryFilter getCategoryFilterWhereAFacetBelongs(FacetForACategoryFilter facetForACategoryFilter) {
		return (CategoryFilter) sessionFactory.getCurrentSession()
		        .createQuery("FROM FacetForACategoryFilter facet WHERE facet.categoryFilter = :category_id")
		        .setInteger("category_id", facetForACategoryFilter.getCategoryFilter().getId()).uniqueResult();
	}

	@Override
    public List<FacetForACategoryFilter> getAllFacets() {
		return sessionFactory.getCurrentSession().createQuery("FROM FacetForACategoryFilter facet ORDER BY facet.facetId").list();
    }
	
}
