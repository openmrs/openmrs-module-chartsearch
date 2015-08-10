/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.synonyms;

import java.util.List;
import java.util.Vector;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;

public class SynonymsAPI {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void saveNewSynonymGroup(SynonymGroup newGrp) {

        if (Context.isAuthenticated()) {
            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            List synGroups = chartSearchService.getAllSynonymGroups();

            if (synGroups != null) {
                synonymGroupsInstance.setSynonymGroupsHolder(synGroups);
                if (synonymGroupsInstance.addSynonymGroup(newGrp)) {
                    chartSearchService.saveSynonymGroup(newGrp);
                }
            }
            synonymGroupsInstance.clearSynonymGroups();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void updateSynonymGroup(String oldGrpName, SynonymGroup newGrp) {
        if (Context.isAuthenticated()) {
            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            List synGroups = chartSearchService.getAllSynonymGroups();

            if (synGroups != null) {
                synonymGroupsInstance.setSynonymGroupsHolder(synGroups);

                SynonymGroup synGrpToUpdate = synonymGroupsInstance.getSynonymGroupByName(oldGrpName);
                if (synonymGroupsInstance.editSynonymGroupByName(oldGrpName, newGrp)) {
                    chartSearchService.purgeSynonymGroup(synGrpToUpdate);
                    chartSearchService.saveSynonymGroup(newGrp);
                }

                synonymGroupsInstance.clearSynonymGroups();
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void deleteSynonymGroup(String groupName) {
        if (Context.isAuthenticated()) {
            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            List synGroups = chartSearchService.getAllSynonymGroups();

            if (synGroups != null) {
                synonymGroupsInstance.setSynonymGroupsHolder(synGroups);


                SynonymGroup grpToDel = synonymGroupsInstance.getSynonymGroupByName(groupName);
                if (synonymGroupsInstance.deleteSynonymGroupByName(groupName)) {
                    chartSearchService.purgeSynonymGroup(grpToDel);
                }

                synonymGroupsInstance.clearSynonymGroups();
            }
        }
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String getSynonymsForSearch(String phrase){
        String ans = phrase;
        if (Context.isAuthenticated()) {
            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            List synGroups = chartSearchService.getAllSynonymGroups();

            if (synGroups != null) {
                synonymGroupsInstance.setSynonymGroupsHolder(synGroups);

                ans = synonymGroupsInstance.getStrOfAllSynMatchingPhrase(phrase);

                synonymGroupsInstance.clearSynonymGroups();
            }
        }
        return ans;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Vector<String> getGroupNamesBySynonym(String phrase){
        Vector<String> ans = new Vector<String>();
        if (Context.isAuthenticated()) {
            ChartSearchService chartSearchService = Context.getService(ChartSearchService.class);
            SynonymGroups synonymGroupsInstance = SynonymGroups.getInstance();
            synonymGroupsInstance.clearSynonymGroups();
            List synGroups = chartSearchService.getAllSynonymGroups();

            if (synGroups != null) {
                synonymGroupsInstance.setSynonymGroupsHolder(synGroups);

                ans = synonymGroupsInstance.getAllMatchingSynonymGroupNamesOfPhrase(phrase);

                synonymGroupsInstance.clearSynonymGroups();
            }
        }
        return ans;
    }

}
