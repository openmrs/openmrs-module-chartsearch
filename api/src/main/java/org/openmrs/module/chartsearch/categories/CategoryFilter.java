/**
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

package org.openmrs.module.chartsearch.categories;

import org.openmrs.ConceptClass;
import org.openmrs.api.context.Context;

/**
 * Creates Categories Filter Item for results returned by CSM like; diagnosis, labs, meds, orders,
 * reports, vitals or other defined categories by the user. A Category Filter can have sub-categories like
 * Tests(LabSet, Test, MedSet, ConvSet), Orders (Drug), Diagnoses (Diagnosis) et-cetera
 */
public class CategoryFilter {
	
	private String categoryName;
	
	private String categoryUuid;
	
	private Integer categoryId;
	
	private String categoryDescription;
	
	private String hitCounts;
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String catName) {
		this.categoryName = catName;
	}
	
	public String getCategoryUuid() {
		return categoryUuid;
	}
	
	public void setCategoryUuid(String catUuid) {
		this.categoryUuid = catUuid;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer catId) {
		this.categoryId = catId;
	}
	
	public String getHitCounts() {
		return hitCounts;
	}
	
	public void setHitCounts(String hitCounts) {
		this.hitCounts = hitCounts;
	}
	
	public String getCategoryDescription() {
		return categoryDescription;
	}
	
	public void setCategoryDescription(String catDescription) {
		this.categoryDescription = catDescription;
	}
	
	/**
	 * Sets properties of a category filter to properties of an existing concept class that is
	 * stored as a concept class in the openmrs database
	 * 
	 * @param uuid
	 */
	protected void setCategoryFilterPropertiesStoredAsConceptClass(String uuid) {
		try {
			if (uuid != "" && uuid != " ") {
				ConceptClass cc = Context.getConceptService().getConceptClassByUuid(uuid);
				
				setCategoryName(cc.getName());
				setCategoryDescription(cc.getDescription());
				setCategoryUuid(cc.getUuid());
				setCategoryId(cc.getId());
			} else
				System.out.println("Provided no uuid (uuid passed in is \"\" or \" \"!!!)");
		}
		catch (NullPointerException e) {
			System.out.println("The uuid provided for the concept class doesn't match any of those exising in the DB!!!");
		}
	}
	
}
