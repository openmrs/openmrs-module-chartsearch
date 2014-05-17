package org.openmrs.module.chartsearch.synonyms;

import java.util.*;

/**
 * Created by Eli on 21/04/14.
 */
public class SynonymGroups {
    private int counter;
    private List<SynonymGroup> synonymGroupsHolder;
    private static SynonymGroups instance;

    private SynonymGroups() {

        counter = 0;
        synonymGroupsHolder = new ArrayList<SynonymGroup>();
    }



    public static SynonymGroups getInstance() {
        if (instance == null) {
            instance = new SynonymGroups();
        }
        return instance;
    }

    public void clearSynonymGroups(){
        this.counter = 0;
        this.synonymGroupsHolder.clear();

    }

    public int getCounter() {
        return counter;
    }

    public int getNumberOfGroups() {
        return synonymGroupsHolder.size();
    }

    public SynonymGroup isSynonymContainedInGroup(String syn) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            Set<Synonym> synSet = grp.getSynonymSet();
            for (Synonym synInGrp : synSet) {
                if (synInGrp.equals(syn)) {
                    return grp;
                }
            }
        }
        return null;
    }

    public SynonymGroup isSynFromGroupContainedInOtherGroup(SynonymGroup checkGroup) {
        for (SynonymGroup grp : synonymGroupsHolder) {
            HashSet<Synonym> intersection = new HashSet<Synonym>((Collection<? extends Synonym>) checkGroup.getSynonyms()); // use the copy constructor
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
            boolean newIsCategory = FirstGrpToMerge.getIsCategory();
            ArrayList<Synonym> newSynonymSet = new ArrayList<Synonym>();
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

    public List<SynonymGroup> getSynonymGroupsHolder() {
        return synonymGroupsHolder;
    }

    public void setSynonymGroupsHolder(List<SynonymGroup> synonymGroupsHolder) {
        for(SynonymGroup synGrp : synonymGroupsHolder){
            addSynonymGroup(synGrp);
        }
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
                Set<String> synonymsOfGrp = currnetGrp.getSynonyms();
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

 /*

    public static void main(String[] args){
        Synonym s1 = new Synonym("syn");
        List<Synonym> synList = new ArrayList<Synonym>();
        synList.add(s1);
        SynonymGroup synGrp = new SynonymGroup("grpName", false,synList );
        System.out.println(synGrp.contains("syn"));
    }*/
}
