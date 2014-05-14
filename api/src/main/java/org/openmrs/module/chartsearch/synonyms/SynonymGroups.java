package org.openmrs.module.chartsearch.synonyms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

/**
 * Created by Eli on 21/04/14.
 */
public class SynonymGroups {
    private static int counter;
    private static Vector<SynonymGroup> synonymGroups;

    public SynonymGroups() {
        counter = 0;
        synonymGroups = new Vector<SynonymGroup>();
    }

    public static int getCounter() {
        return counter;
    }

    public static int getNumberOfGroups() {
        return synonymGroups.size();
    }

    public static SynonymGroup isSynonymContainedInGroup(String syn) {
        for (SynonymGroup grp : synonymGroups) {
            if (grp.contains(syn)) {
                return grp;
            }
        }
        return null;
    }

    public static SynonymGroup isSynFromGroupContainedInOtherGroup(SynonymGroup checkGroup) {
        for (SynonymGroup grp : synonymGroups) {
            HashSet<String> intersection = new HashSet<String>((Collection<? extends String>) checkGroup); // use the copy constructor
            intersection.retainAll((Collection<?>) grp);
            if (!intersection.isEmpty()) {
                return grp;
            }
        }
        return null;
    }

    public static void addSynonymGroup(SynonymGroup newGroup) {
        if (isSynFromGroupContainedInOtherGroup(newGroup) == null) {
            synonymGroups.add(newGroup);
            counter++;
        }
    }

    public static void mergeSynonymGroups(SynonymGroup grpToMerge) {
        SynonymGroup grp = isSynFromGroupContainedInOtherGroup(grpToMerge);
        if (!grp.equals(null)) {
            grp.merge(grpToMerge);

        }
    }

    public static SynonymGroup getSynonymGroupByName(String name) {
        for (SynonymGroup grp : synonymGroups) {
            if (grp.getGroupName().equals(name)) {
                return grp;
            }
        }
        return null;
    }

    public static SynonymGroup getSynonymGroupBySynonym(String syn) {
        for (SynonymGroup grp : synonymGroups) {
            if (grp.contains(syn)) {
                return grp;
            }
        }
        return null;
    }

    public static boolean deleteSynonymGroupByName(String name) {
        for (SynonymGroup grp : synonymGroups) {
            if (grp.getGroupName().equals(name)) {
                synonymGroups.remove(grp);

                return true;
            }
        }
        return false;
    }

    public static Vector<SynonymGroup> getSynonymGroups() {
        return synonymGroups;
    }

    @Override
    public String toString() {
        {
            String str = "";
            for (SynonymGroup grp : synonymGroups) {
                str += grp.toString() + '\n';
            }
            return str;
        }

    }
}
