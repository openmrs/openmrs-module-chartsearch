package org.openmrs.module.chartsearch.synonyms;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Eli on 21/04/14.
 */
public class SynonymGroup {
    private Set<String> synonymSet;
    private String groupName;
    private boolean isCategory;

    public SynonymGroup() {
    }



    public SynonymGroup(String groupName, boolean isCategory, List<String> synonymList) {
        synonymSet = new HashSet<String>();
        if (!groupName.equals("")) {
            this.groupName = groupName;
        } else {
            this.groupName = "defaultName" + SynonymGroups.getInstance().getCounter();
        }
        this.isCategory = isCategory;

        addSynonyms(synonymList);
    }

    public boolean setGroupName(String groupName) {
        if (SynonymGroups.getInstance().getSynonymGroupByName(groupName) == null) {
            this.groupName = groupName;
            return true;
        } else {
            return false;
        }
    }

    public String getGroupName() {
        return groupName;
    }



    public void addSynonyms(List<String> newSynonyms) {
        for (String syn : newSynonyms) {
            addSynonym(syn);
        }
    }

    public Set getSynonyms() {
        return synonymSet;
    }

    public boolean addSynonym(String newSynonym) {
        if (!newSynonym.equals("")) {

            synonymSet.add(newSynonym);
            return true;
        }
        return false;
    }

    public boolean editSynonym(String oldSynonym, String newSynonym) {
        if (synonymSet.contains(oldSynonym)) {

            synonymSet.remove(oldSynonym);
            addSynonym(newSynonym);
            return true;

        }
        return false;
    }

    public boolean removeSynonym(String synonymToDel) {
        if (synonymSet.contains(synonymToDel)) {
            synonymSet.remove(synonymToDel);
            return true;
        }
        return false;
    }

    public boolean contains(String synonymToCheck) {
        if (synonymSet.contains(synonymToCheck)) {
            return true;
        }
        return false;
    }

    public boolean contains(SynonymGroup otherGroup) {
        HashSet<String> intersection = new HashSet<String>((Collection<? extends String>) otherGroup); // use the copy constructor
        intersection.retainAll(synonymSet);
        if (intersection.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void merge(SynonymGroup otherGroup) {
        synonymSet.addAll((Collection<? extends String>) otherGroup);
    }

    @Override
    public String toString() {
        {
            String str = getGroupName() + '\n';
            for (String syn : synonymSet) {
                str += syn.toString() + '\n';
            }
            return str;
        }

    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setCategory(boolean isCategory) {
        this.isCategory = isCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynonymGroup)) return false;

        SynonymGroup that = (SynonymGroup) o;

        if (isCategory != that.isCategory) return false;
        if (!groupName.equals(that.groupName)) return false;
        if (!synonymSet.equals(that.synonymSet)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = synonymSet.hashCode();
        result = 31 * result + groupName.hashCode();
        result = 31 * result + (isCategory ? 1 : 0);
        return result;
    }
}
