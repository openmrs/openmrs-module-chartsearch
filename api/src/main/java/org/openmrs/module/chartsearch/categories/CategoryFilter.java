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
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.chartsearch.categories;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;

/**
 * This class Filters returned results by category items such as; diagnosis, labs, meds, orders,
 * Others, reports, vitals plus other custom defined categories by the user from the
 * manageCategories page
 */
public class CategoryFilter extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String categoryName;
	
	private String categoryUuid;
	
	private Integer categoryId;
	
	private String categoryDescription;
	
	private String filterQuery;
	
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
	
	public String getCategoryDescription() {
		return categoryDescription;
	}
	
	public void setCategoryDescription(String catDescription) {
		this.categoryDescription = catDescription;
	}
	
	@Override
	public Integer getId() {
		return getCategoryId();
	}
	
	@Override
	public void setId(Integer id) {
		setCategoryId(id);
	}
	
	public String getFilterQuery() {
		return filterQuery;
	}
	
	public void setFilterQuery(String filterQuery) {
		this.filterQuery = filterQuery;
	}
	
}
