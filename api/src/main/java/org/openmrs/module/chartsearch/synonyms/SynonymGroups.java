package org.openmrs.module.chartsearch.synonyms;

import java.util.*;

/**
 * Created by Eli on 21/04/14.
 */
public class SynonymGroups {
    private int counter;
    private Vector<SynonymGroup> synonymGroupsHolder;
    private static SynonymGroups instance;

    private SynonymGroups() {

        counter = 0;
        synonymGroupsHolder = new Vector<SynonymGroup>();
    }

    public static SynonymGroups getInstance() {
        if (instance == null) {
            instance = new SynonymGroups();
        }
        return instance;
    }

    public int getCounter() {
        return counter;
    }

    public int getNumberOfGroups() {
        return synonymGroupsHolder.size();
    }

    public SynonymGroup isSynonymContainedInGroup(String syn) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            HashSet<String> synSet = grp.getSynonyms();
            for (String synInGrp : synSet) {
                if (synInGrp.equals(syn)) {
                    return grp;
                }
            }
        }
        return null;
    }

    public SynonymGroup isSynFromGroupContainedInOtherGroup(SynonymGroup checkGroup) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            HashSet<String> intersection = new HashSet<String>((Collection<? extends String>) checkGroup.getSynonyms()); // use the copy constructor
            intersection.retainAll((Collection<?>) grp.getSynonyms());
            if (!intersection.isEmpty()) {
                return grp;
            }
        }
        return null;
    }

    public boolean addSynonymGroup(SynonymGroup newGroup) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            if (grp.getGroupName().equals(newGroup.getGroupName())) {
                return false;
            }
        }
        synonymGroupsHolder.add(newGroup);
        counter++;
        return true;
    }

    public SynonymGroup mergeSynonymGroups(SynonymGroup FirstGrpToMerge, SynonymGroup SecondGroupToMerge) {
        SynonymGroup mergedGroup = null;
        if (FirstGrpToMerge != null && SecondGroupToMerge != null) {
            String newName = FirstGrpToMerge.getGroupName();
            boolean newIsCategory = FirstGrpToMerge.isCategory();
            ArrayList<String> newSynonymSet = new ArrayList<String>();
            newSynonymSet.addAll(FirstGrpToMerge.getSynonyms());
            newSynonymSet.addAll(SecondGroupToMerge.getSynonyms());
            mergedGroup = new SynonymGroup(newName, newIsCategory, newSynonymSet);
            return mergedGroup;
        }
        return mergedGroup;
    }

    public SynonymGroup getSynonymGroupByName(String name) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            if (grp.getGroupName().equals(name)) {
                return grp;
            }
        }
        return null;
    }

    public SynonymGroup getSynonymGroupBySynonym(String syn) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            if (grp.getSynonyms().contains(syn)) {
                return grp;
            }
        }
        return null;
    }

    public boolean deleteSynonymGroupByName(String name) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            if (grp.getGroupName().equals(name)) {
                synonymGroupsHolder.remove(grp);

                return true;
            }
        }
        return false;
    }

    public Vector<SynonymGroup> getSynonymGroupsHolder() {
        return synonymGroupsHolder;
    }


    public Vector<String> getAllMatchingSynonymsOfPhrase(String phrase) {
        return getAllMatchingSynonymsOfPhraseRec(phrase, new Vector<String>());
    }

    public String getStrOfAllSynMatchingPhrase(String phrase) {
        String returnStr = new String();
        Vector<String> matchingSyns = getAllMatchingSynonymsOfPhrase(phrase);
        Iterator<String> iter = matchingSyns.iterator();
        if (iter.hasNext())
            returnStr += iter.next();
        while (iter.hasNext()) {
            returnStr += "," + iter.next();
        }
        return returnStr;
    }

    private Vector<String> getAllMatchingSynonymsOfPhraseRec(String phrase, Vector<String> synonymSet) {
        if (synonymSet.contains(phrase))
            return synonymSet;
        else {
            synonymSet.add(phrase);
            SynonymGroup currnetGrp = getSynonymGroupByName(phrase);
            if (currnetGrp != null) {
                HashSet<String> synonymsOfGrp = currnetGrp.getSynonyms();
                for (String syn : synonymsOfGrp) {
                    getAllMatchingSynonymsOfPhraseRec(syn, synonymSet);
                }
            }
        }
        return synonymSet;
    }


    @Override
    public String toString() {
        {
            String str = "";
            for (SynonymGroup grp : synonymGroupsHolder) {
                str += grp.toString() + '\n';
            }
            return str;
        }

    }

}
