package org.openmrs.module.chartsearch.synonyms;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Eli on 21/04/14.
 */
public class SynonymGroup {
    private HashSet<String> synonymGroup;
    private String groupName;

    public SynonymGroup(String groupName, String synonymList) {
        synonymGroup = new HashSet<String>();
        if(SynonymGroups.getSynonymGroupByName(groupName).equals(null)){
            this.groupName = groupName;
        }
        else{
            this.groupName= "defaultName" + SynonymGroups.getCounter();
        }

        addSynonyms(synonymList);
    }

    public boolean setGroupName(String groupName) {
        if(SynonymGroups.getSynonymGroupByName(groupName).equals(null)){
            this.groupName = groupName;
            return true;
        }
        else{
            return false;
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void addSynonyms(String newSynonyms) { //for now, will be parsed. separated by ;.
        String[] synToAdd = newSynonyms.split(";");
        for (String syn : synToAdd) {
            addSynonym(syn);
        }
    }

    public HashSet getSynonyms(){
        return synonymGroup;
    }

    public String addSynonym(String newSynonym) {
        if (!newSynonym.equals("")){
            if(SynonymGroups.isSynonymContainedInGroup(newSynonym).equals(null)) {
            synonymGroup.add(newSynonym);
            return "true";
            }
            return "duplicateInOtherGroup";
        }
        return "synonymIsEmpty";
    }

    public boolean editSynonym(String oldSynonym, String newSynonym) {
        if (synonymGroup.contains(oldSynonym) && !synonymGroup.contains(newSynonym)) {
            if (SynonymGroups.isSynonymContainedInGroup(newSynonym).equals(null)) {
                synonymGroup.remove(oldSynonym);
                addSynonym(newSynonym);
                return true;
            }
        }
        return false;
    }

    public boolean removeSynonym(String synonymToDel) {
        if (synonymGroup.contains(synonymToDel)) {
            synonymGroup.remove(synonymToDel);
            return true;
        }
        return false;
    }

    public boolean contains(String synonymToCheck) {
        if (synonymGroup.contains(synonymToCheck)) {
            return true;
        }
        return false;
    }

    public boolean contains(SynonymGroup otherGroup) {
        HashSet<String> intersection = new HashSet<String>((Collection<? extends String>) otherGroup); // use the copy constructor
        intersection.retainAll(synonymGroup);
        if (intersection.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void merge(SynonymGroup otherGroup) {
        synonymGroup.addAll((Collection<? extends String>) otherGroup);
    }
    @Override
    public String toString() {
        {
            String str = getGroupName() + '\n';
            for (String syn : synonymGroup) {
                str += syn.toString() + '\n';
            }
            return str;
        }

    }
}
