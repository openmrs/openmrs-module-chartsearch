/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
