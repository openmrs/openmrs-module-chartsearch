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

import org.hibernate.Query;
import org.openmrs.module.chartsearch.api.db.SynonymGroupDAO;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;

@SuppressWarnings("rawtypes")
public class HibernateSynonymGroupDAO extends HibernateSingleClassDAO implements SynonymGroupDAO {
	
	/**
	 * You must call this before using any of the data access methods, since it's not actually
	 * possible to write them all with compile-time class information.
	 */
	@SuppressWarnings("unchecked")
	protected HibernateSynonymGroupDAO() {
		super(SynonymGroup.class);
	}
	
	@Override
	public SynonymGroup getSynonymGroupByName(String groupName) {
		String sQuery = "from SynonymGroup as sgrp where sgrp.groupName = :synonymGroupName ";
		
		Query query = super.sessionFactory.getCurrentSession().createQuery(sQuery);
		query.setParameter("synonymGroupName", groupName);
		
		return (SynonymGroup) query;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SynonymGroup> getSynonymGroupsIsCategory(boolean isCategory) {
		String sQuery = "from SynonymGroup as sgrp where sgrp.isCategory = :isCategory ";
		
		Query query = super.sessionFactory.getCurrentSession().createQuery(sQuery);
		query.setParameter("isCategory", isCategory);
		
		return query.list();
	}
	
	@Override
	public Integer getCountOfSynonymGroups(boolean byIsCategory) {
		Query query;
		if (byIsCategory) {
			String sQuery = "from SynonymGroup as sgrp where sgrp.isCategory = :isCategory ";
			
			query = super.sessionFactory.getCurrentSession().createQuery(sQuery);
			query.setParameter("byIsCategory", byIsCategory);
		} else {
			String sQuery = "from SynonymGroup as sgrp ";
			
			query = super.sessionFactory.getCurrentSession().createQuery(sQuery);
		}
		
		return query.list().size();
	}
}
