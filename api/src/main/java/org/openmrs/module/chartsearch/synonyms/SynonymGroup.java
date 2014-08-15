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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SynonymGroup {
	
	private Set<Synonym> synonymSet;
	
	private String groupName;
	
	private boolean isCategory;
	
	private int group_id;
	
	public SynonymGroup() {
	}
	
	public SynonymGroup(int group_id) {
		this.group_id = group_id;
	}
	
	public SynonymGroup(String groupName, boolean isCategory, List<Synonym> synonymList) {
		synonymSet = new HashSet<Synonym>();
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
	
	public void addSynonyms(List<Synonym> newSynonyms) {
		for (Synonym syn : newSynonyms) {
			addSynonym(syn);
		}
	}
	
	@SuppressWarnings("rawtypes")
    public Set getSynonyms() {
		return synonymSet;
	}
	
	public boolean addSynonym(Synonym newSynonym) {
		if (!newSynonym.equals("") && !contains(newSynonym.getSynonymName())) {
			newSynonym.setGroup(this);
			synonymSet.add(newSynonym);
			return true;
		}
		return false;
	}
	
	public boolean editSynonym(Synonym oldSynonym, Synonym newSynonym) {
		if (this.contains(oldSynonym.getSynonymName())) {
			
			this.removeSynonym(oldSynonym.getSynonymName());
			addSynonym(newSynonym);
			return true;
			
		}
		return false;
	}
	
	public boolean removeSynonym(String synonymToDel) {
		if (this.contains(synonymToDel)) {
			for (Synonym syn : synonymSet) {
				if (syn.getSynonymName().equalsIgnoreCase(synonymToDel)) {
					synonymSet.remove(syn);
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean contains(String synonymToCheck) {
		for (Synonym syn : synonymSet) {
			if (syn.getSynonymName().equalsIgnoreCase(synonymToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
    public boolean contains(SynonymGroup otherGroup) {
		HashSet<Synonym> intersection = new HashSet<Synonym>((Collection<? extends Synonym>) otherGroup); // use the copy constructor
		intersection.retainAll(synonymSet);
		if (intersection.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getGroup_id() {
		return group_id;
	}
	
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	
	@Override
	public String toString() {
		{
			String str = getGroupName() + '\n';
			for (Synonym syn : synonymSet) {
				str += syn.getSynonymName().toString() + '\n';
			}
			return str;
		}
		
	}
	
	public boolean getIsCategory() {
		return isCategory;
	}
	
	public void setIsCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}
	
	public Set<Synonym> getSynonymSet() {
		return synonymSet;
	}
	
	public void setSynonymSet(Set<Synonym> synonymSet) {
		this.synonymSet = synonymSet;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SynonymGroup))
			return false;
		
		SynonymGroup that = (SynonymGroup) o;
		
		if (isCategory != that.isCategory)
			return false;
		if (!groupName.equalsIgnoreCase(that.groupName))
			return false;
		if (!synonymSet.equals(that.synonymSet))
			return false;
		
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
