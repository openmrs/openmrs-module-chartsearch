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
