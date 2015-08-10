/**
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
}
