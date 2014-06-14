package org.openmrs.module.chartsearch.synonyms;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;

import java.util.List;
import java.util.Vector;

/**
 * Created by Eli on 31/05/14.
 */
public class SynonymsAPI {

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
