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

/**
 * This class represents A category to be used to filter returned results; items such as; diagnosis,
 * labs, meds, orders, Others, reports, vitals
 */
public class CategoryFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of the Category
	 */
	private String categoryName;
	
	/**
	 * The Uuid of a Category
	 */
	private String categoryUuid;
	
	private Integer categoryId;
	
	/**
	 * The Description of a Category
	 */
	private String categoryDescription;
	
	/**
	 * The Filter Query of a Category
	 */
	private String filterQuery;
	
	/**
	 * Name to be set by the user from the UI
	 */
	private String displayName;
	
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
	
	public String getFilterQuery() {
		return filterQuery;
	}
	
	public void setFilterQuery(String filterQuery) {
		this.filterQuery = filterQuery;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
