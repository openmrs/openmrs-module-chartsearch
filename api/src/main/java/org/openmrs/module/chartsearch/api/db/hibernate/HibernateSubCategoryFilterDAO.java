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
import org.openmrs.module.chartsearch.api.db.SubCategoryFilterDAO;
import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.categories.SubCategoryFilter;

public class HibernateSubCategoryFilterDAO implements SubCategoryFilterDAO {
	
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
	public SubCategoryFilter getSubCategoryFilter(Integer subCategoryFilterId) {
		log.info("Geting sub category whose ID is: " + subCategoryFilterId);
		return (SubCategoryFilter) sessionFactory.getCurrentSession().get(SubCategoryFilter.class, subCategoryFilterId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SubCategoryFilter> getSubCategoryFilters(CategoryFilter categoryFilter) {
		log.info("Getting all sub category from the database whose whose Category is: " + categoryFilter.getCategoryName());
		return sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            "FROM SubCategoryFilter scf WHERE scf.categoryFilter = " + categoryFilter
		                    + " ORDER BY scf.subCategoryId").list();
	}
	
	@Override
	public void createSubCategoryFilter(SubCategoryFilter subCategoryFilter) {
		session.getTransaction().begin();
		log.debug("Creating a sub category filter for: " + subCategoryFilter.getCategoryFilter().getCategoryName());
		sessionFactory.getCurrentSession().save(subCategoryFilter);
		session.getTransaction().commit();
	}
	
	@Override
	public void updateSubCategoryFilter(SubCategoryFilter subCategoryFilter) {
		session.getTransaction().begin();
		log.debug("Updating an existing sub category filter: " + subCategoryFilter.getSubCategoryName()
		        + "which belongs to: " + subCategoryFilter.getCategoryFilter().getCategoryName());
		sessionFactory.getCurrentSession().update(subCategoryFilter);
		session.getTransaction().commit();
	}
	
	@Override
	public void deleteSubCategoryFilter(SubCategoryFilter subCategoryFilter) {
		session.getTransaction().begin();
		log.debug("About to delete sub category filter named: " + subCategoryFilter.getSubCategoryName());
		sessionFactory.getCurrentSession().delete(subCategoryFilter);
		session.getTransaction().commit();
	}
	
	@Override
	public SubCategoryFilter getSubCategoryFilterByUuid(String subCategoryFilterUuid) {
		return (SubCategoryFilter) sessionFactory.getCurrentSession()
		        .createQuery("FROM SubCategoryFilter scf where scf.subCategoryUuid = :uuid")
		        .setString("uuid", subCategoryFilterUuid).uniqueResult();
	}
	
}
