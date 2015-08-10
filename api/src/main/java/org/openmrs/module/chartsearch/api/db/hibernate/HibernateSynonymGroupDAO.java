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
