package org.openmrs.module.chartsearch.api.db.hibernate;

import org.hibernate.Query;
import org.openmrs.module.chartsearch.api.db.SynonymDAO;
import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Eli on 16/05/14.
 */
public class HibernateSynonymDAO extends HibernateSingleClassDAO implements SynonymDAO {
    /**
     * You must call this before using any of the data access methods, since it's not actually
     * possible to write them all with compile-time class information.
     *
     *
     */
    protected HibernateSynonymDAO() {
        super(Synonym.class);
    }

    @Override
    public List<Synonym> getSynonymsByGroup(SynonymGroup synonymGroup) {
        String sQuery = "from Synonym as synonym where synonym.group.group_id = :synonymGroupId ";

        Query query = super.sessionFactory.getCurrentSession().createQuery(
                sQuery);
        query.setParameter("synonymGroupId", synonymGroup.getGroup_id());

        return query.list();
    }

    @Override
    public Integer getSynonymsCountByGroup(SynonymGroup synonymGroup) {
        String sQuery = "from Synonym as synonym where synonym.group.group_id = :synonymGroupId ";

        Query query = super.sessionFactory.getCurrentSession().createQuery(
                sQuery);
        query.setParameter("synonymGroupId", synonymGroup.getGroup_id());

        return query.list().size();
    }
}
