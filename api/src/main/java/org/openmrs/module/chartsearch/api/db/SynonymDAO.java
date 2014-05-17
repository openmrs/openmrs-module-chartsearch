package org.openmrs.module.chartsearch.api.db;

import org.openmrs.module.chartsearch.synonyms.Synonym;
import org.openmrs.module.chartsearch.synonyms.SynonymGroup;

import java.util.List;

/**
 * Created by Eli on 16/05/14.
 */
public interface SynonymDAO extends SingleClassDAO{

    /**
     * Retrieve synonyms by synonym group
     *
     * @param synonymGroup - the synonym group.
     * @return list of synonyms for the given group.
     * @should not return voided synonyms
     */
    List<Synonym> getSynonymsByGroup(SynonymGroup synonymGroup);

    /**
     * Retrieve count of synonyms by synonym group
     *
     * @param synonymGroup - the synonym group.
     * @return count of synonyms for the given group.
     * @should not return voided synonyms
     */
    Integer getSynonymsCountByGroup(SynonymGroup synonymGroup);

}
