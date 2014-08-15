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

import java.util.*;

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
	
	public void clearSynonymGroups() {
		this.counter = 0;
		this.synonymGroupsHolder.clear();
		
	}
	
	public int getCounter() {
		return counter;
	}
	
	public int getNumberOfGroups() {
		return synonymGroupsHolder.size();
	}
	
	@SuppressWarnings("unchecked")
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
			if (grp.getGroupName().equalsIgnoreCase(newGroup.getGroupName())) {
				return false;
			}
		}
		synonymGroupsHolder.add(newGroup);
		counter++;
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean editSynonymGroupByName(String groupName, SynonymGroup newGrp) {
		SynonymGroup grp = getSynonymGroupByName(groupName);
		deleteSynonymGroupByName(groupName);
		addSynonymGroup(newGrp);
		return true;
	}
	
	@SuppressWarnings("unchecked")
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
			if (grp.getGroupName().equalsIgnoreCase(name)) {
				return grp;
			}
		}
		return null;
	}
	
	public Vector<String> getSynonymGroupsNamesBySynonym(String syn) {
		Vector<String> groups = new Vector<String>();
		for (SynonymGroup grp : synonymGroupsHolder) {
			for (Synonym synInGrp : grp.getSynonymSet()) {
				if (synInGrp.getSynonymName().equalsIgnoreCase(syn)) {
					groups.add(grp.getGroupName());
					break;
				}
			}
		}
		return groups;
	}
	
	public boolean deleteSynonymGroupByName(String name) {
		for (SynonymGroup grp : synonymGroupsHolder) {
			if (grp.getGroupName().equalsIgnoreCase(name)) {
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
		for (SynonymGroup synGrp : synonymGroupsHolder) {
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
			returnStr += " || " + iter.next();
		}
		return returnStr;
	}
	
	@SuppressWarnings("unchecked")
	private Vector<String> getAllMatchingSynonymsOfPhraseRec(String phrase, Vector<String> synonymSet) {
		if (synonymSet.contains(phrase))
			return synonymSet;
		else {
			synonymSet.add(phrase);
			SynonymGroup currnetGrp = getSynonymGroupByName(phrase);
			if (currnetGrp != null) {
				Set<Synonym> synonymsOfGrp = currnetGrp.getSynonyms();
				for (Synonym syn : synonymsOfGrp) {
					String synName = syn.getSynonymName();
					getAllMatchingSynonymsOfPhraseRec(synName, synonymSet);
				}
			}
		}
		return synonymSet;
	}
	
	public Vector<String> getAllMatchingSynonymGroupNamesOfPhrase(String phrase) {
		Vector<String> groupNames = new Vector<String>();
		return getAllMatchingSynonymGroupNamesOfPhraseRec(phrase, groupNames);
	}
	
	private Vector<String> getAllMatchingSynonymGroupNamesOfPhraseRec(String phrase, Vector<String> groupNames) {
		Vector<String> thisSynGroupNames = getSynonymGroupsNamesBySynonym(phrase);
		for (String grpName : thisSynGroupNames) {
			if (groupNames.contains(grpName)) {
				thisSynGroupNames.remove(grpName);
			}
		}
		if (thisSynGroupNames.isEmpty()) {
			return groupNames;
		}
		
		groupNames.addAll(thisSynGroupNames);
		for (String groupName : groupNames) {
			return getAllMatchingSynonymGroupNamesOfPhraseRec(groupName, groupNames);
		}
		return groupNames;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (SynonymGroup grp : synonymGroupsHolder) {
			str += grp.toString() + '\n';
		}
		return str;
	}
	
}
