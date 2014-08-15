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

public class Synonym {
	
	private String synonymName;
	
	private int synonymId;
	
	private SynonymGroup group;
	
	public Synonym() {
	}
	
	public Synonym(String synonymName) {
		this.synonymName = synonymName;
	}
	
	public String getSynonymName() {
		return synonymName;
	}
	
	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}
	
	public int getSynonymId() {
		return synonymId;
	}
	
	public void setSynonymId(int synonymId) {
		this.synonymId = synonymId;
	}
	
	public SynonymGroup getGroup() {
		return group;
	}
	
	public void setGroup(SynonymGroup group) {
		this.group = group;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof String))
			return false;
		
		String synonym = (String) o;
		
		if (!synonymName.equalsIgnoreCase(synonym))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return synonymName.hashCode();
	}
	
	@Override
	public String toString() {
		return synonymName;
	}
}
