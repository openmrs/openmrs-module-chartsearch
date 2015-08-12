/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
