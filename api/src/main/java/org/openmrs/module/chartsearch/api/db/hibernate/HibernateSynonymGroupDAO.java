package org.openmrs.module.chartsearch.api.db.hibernate;

import org.hibernate.Query;
import org.openmrs.module.chartsearch.api.db.SynonymGroupDAO;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;

import java.util.List;

/**
 * Created by Eli on 16/05/14.
 */
public class HibernateSynonymGroupDAO extends HibernateSingleClassDAO implements SynonymGroupDAO {
    /**
     * You must call this before using any of the data access methods, since it's not actually
     * possible to write them all with compile-time class information.
     */
    protected HibernateSynonymGroupDAO() {
        super(SynonymGroup.class);
    }

    @Override
    public SynonymGroup getSynonymGroupByName(String groupName) {
        String sQuery = "from SynonymGroup as sgrp where sgrp.groupName = :synonymGroupName ";

        Query query = super.sessionFactory.getCurrentSession().createQuery(
                sQuery);
        query.setParameter("synonymGroupName", groupName);

        return (SynonymGroup) query;
    }

    @Override
    public List<SynonymGroup> getSynonymGroupsIsCategory(boolean isCategory) {
        String sQuery = "from SynonymGroup as sgrp where sgrp.isCategory = :isCategory ";

        Query query = super.sessionFactory.getCurrentSession().createQuery(
                sQuery);
        query.setParameter("isCategory", isCategory);

        return query.list();
    }

    @Override
    public Integer getCountOfSynonymGroups(boolean byIsCategory) {
        Query query;
        if (byIsCategory) {
            String sQuery = "from SynonymGroup as sgrp where sgrp.isCategory = :isCategory ";

            query = super.sessionFactory.getCurrentSession().createQuery(
                    sQuery);
            query.setParameter("byIsCategory", byIsCategory);
        }
        else{
            String sQuery = "from SynonymGroup as sgrp ";

            query = super.sessionFactory.getCurrentSession().createQuery(
                    sQuery);
        }

        return query.list().size();
    }
}
