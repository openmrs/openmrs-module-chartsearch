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

/**
 * Creates Categories Filter Item for results returned by CSM like; diagnosis, labs, meds, orders,
 * reports, vitals or defined categories by the user
 */
public class CategoryFilter {
	
	private String catName;
	
	private String catUuid;
	
	private Integer catId;
	
	private String catDescription;
	
	private String hitCounts;
	
	public String getCatName() {
		return catName;
	}
	
	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	public String getCatUuid() {
		return catUuid;
	}
	
	public void setCatUuid(String catUuid) {
		this.catUuid = catUuid;
	}
	
	public Integer getCatId() {
		return catId;
	}
	
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	
	public String getHitCounts() {
		return hitCounts;
	}
	
	public void setHitCounts(String hitCounts) {
		this.hitCounts = hitCounts;
	}
	
	public String getCatDescription() {
		return catDescription;
	}
	
	public void setCatDescription(String catDescription) {
		this.catDescription = catDescription;
	}
	
}
