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
package org.openmrs.module.chartsearch.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openmrs.Concept;
import org.openmrs.module.chartsearch.api.db.CategoryFilterDAO;
import org.openmrs.module.chartsearch.categories.CategoryFilter;

@SuppressWarnings("rawtypes")
public class HibernateCategoryFilterDAO extends HibernateSingleClassDAO implements CategoryFilterDAO {
	
	@SuppressWarnings("unchecked")
    protected HibernateCategoryFilterDAO() {
		super(CategoryFilter.class);
	}
	
	protected final static Log log = LogFactory.getLog(HibernateCategoryFilterDAO.class);
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	Transaction transaction;
	
	Session session;
	
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
		log.info("Get category filter: " + categoryFilterId);
		return (CategoryFilter) sessionFactory.getCurrentSession().get(CategoryFilter.class, categoryFilterId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryFilter> getCategoryFilters() {
		log.info("Getting all category filters from the database");
		return sessionFactory.getCurrentSession().createQuery("FROM CategoryFilter cf ORDER BY cf.categoryId").list();
	}
	
	@Override
	public void createCategoryFilter(CategoryFilter categoryFilter) {
		session.getTransaction().begin();
		sessionFactory.getCurrentSession().save(categoryFilter);
		session.getTransaction().commit();
	}
	
	@Override
	public void updateCategoryFilter(CategoryFilter categoryFilter) {
		session.getTransaction().begin();
		log.debug("Updating an existing category filter: " + categoryFilter.getCategoryName());
		sessionFactory.getCurrentSession().update(categoryFilter);
		session.getTransaction().commit();
	}
	
	@Override
	public void deleteCategoryFilter(CategoryFilter categoryFilter) {
		session.getTransaction().begin();
		sessionFactory.getCurrentSession().delete(categoryFilter);
		session.getTransaction().commit();
	}
	
	@Override
	public CategoryFilter getCategoryFilterByUuid(String uuid) {
		return (CategoryFilter) sessionFactory.getCurrentSession()
		        .createQuery("from CategoryFilter cf where cf.categoryUuid = :uuid").setString("uuid", uuid).uniqueResult();
	}
}
