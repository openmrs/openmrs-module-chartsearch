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
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.chartsearch.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.module.chartsearch.api.db.CategoryFilterDAO;
import org.openmrs.module.chartsearch.categories.CategoryFilter;

/**
 * It is a default implementation of {@link CategoryFilterDAO}.
 */
public class HibernateCategoryFilterDAO implements CategoryFilterDAO {
	
	protected final static Log log = LogFactory.getLog(HibernateCategoryFilterDAO.class);
	
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
	public CategoryFilter getCategoryFilter(Integer categoryFilterId) {
		log.info("Get category filter with id: " + categoryFilterId);
		return (CategoryFilter) sessionFactory.getCurrentSession().get(CategoryFilter.class, categoryFilterId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryFilter> getAllCategoryFilters() {
		log.info("Getting all category filters from the database");
		return sessionFactory.getCurrentSession().createQuery("FROM CategoryFilter category ORDER BY category.categoryId")
		        .list();
	}
	
	@Override
	public void createCategoryFilter(CategoryFilter categoryFilter) {
		sessionFactory.getCurrentSession().save(categoryFilter);
	}
	
	@Override
	public void updateCategoryFilter(CategoryFilter categoryFilter) {
		log.debug("Updating an existing category filter named: " + categoryFilter.getCategoryName());
		sessionFactory.getCurrentSession().update(categoryFilter);
	}
	
	@Override
	public void deleteCategoryFilter(CategoryFilter categoryFilter) {
		sessionFactory.getCurrentSession().delete(categoryFilter);
	}
	
	@Override
	public CategoryFilter getCategoryFilterByUuid(String uuid) {
		return (CategoryFilter) sessionFactory.getCurrentSession()
		        .createQuery("FROM CategoryFilter category WHERE category.categoryUuid = :uuid").setString("uuid", uuid)
		        .uniqueResult();
	}
}
