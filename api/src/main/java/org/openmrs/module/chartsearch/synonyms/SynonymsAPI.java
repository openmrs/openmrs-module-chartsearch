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
