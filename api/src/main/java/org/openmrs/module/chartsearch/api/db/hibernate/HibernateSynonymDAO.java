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
import org.openmrs.module.chartsearch.api.db.SynonymDAO;
import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;

@SuppressWarnings("rawtypes")
public class HibernateSynonymDAO extends HibernateSingleClassDAO implements SynonymDAO {
	
	/**
	 * You must call this before using any of the data access methods, since it's not actually
	 * possible to write them all with compile-time class information.
	 */
	@SuppressWarnings("unchecked")
    protected HibernateSynonymDAO() {
		super(Synonym.class);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public List<Synonym> getSynonymsByGroup(SynonymGroup synonymGroup) {
		String sQuery = "from Synonym as synonym where synonym.group.group_id = :synonymGroupId ";
		
		Query query = super.sessionFactory.getCurrentSession().createQuery(sQuery);
		query.setParameter("synonymGroupId", synonymGroup.getGroup_id());
		
		return query.list();
	}
	
	@Override
	public Integer getSynonymsCountByGroup(SynonymGroup synonymGroup) {
		String sQuery = "from Synonym as synonym where synonym.group.group_id = :synonymGroupId ";
		
		Query query = super.sessionFactory.getCurrentSession().createQuery(sQuery);
		query.setParameter("synonymGroupId", synonymGroup.getGroup_id());
		
		return query.list().size();
	}
}
