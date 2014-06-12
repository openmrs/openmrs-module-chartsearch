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

import org.openmrs.OpenmrsObject;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.ChartSearchIndexer;

/**
 * A category can have a sub category
 */
public class SubCategoryFilter implements OpenmrsObject {
	
	private Integer subCategoryId;
	
	private String subCategoryName;
	
	private String subCategoryDescription;
	
	private String SubCategoryUuid;
	
	/**
	 * The Category where the sub-category belongs
	 */
	private CategoryFilter categoryFilter;
	
	private ChartSearchService chartSearchService;
	
	public SubCategoryFilter(CategoryFilter categoryFilter) {
		if (categoryFilter != null) {
			setCategoryFilter(categoryFilter);
		} else {
			//Set category filter where a sub category belongs to "Others" is the user supplied nothing
			setCategoryFilter(chartSearchService.getCategoryFilterByUuid("0717136a-5c5f-4d68-b099-cbf1ad820363"));
		}
	}
	
	public Integer getSubCategoryId() {
		return subCategoryId;
	}
	
	public void setSubCategoryId(Integer subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	
	public String getSubCategoryName() {
		return subCategoryName;
	}
	
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	
	public String getSubCategoryDescription() {
		return subCategoryDescription;
	}
	
	public void setSubCategoryDescription(String subCategoryDescription) {
		this.subCategoryDescription = subCategoryDescription;
	}
	
	public String getSubCategoryUuid() {
		return SubCategoryUuid;
	}
	
	public void setSubCategoryUuid(String subCategoryUuid) {
		SubCategoryUuid = subCategoryUuid;
	}
	
	public CategoryFilter getCategoryFilter() {
		return categoryFilter;
	}
	
	public void setCategoryFilter(CategoryFilter categoryFilter) {
		this.categoryFilter = categoryFilter;
	}
	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getUuid() {
		return getSubCategoryUuid();
	}
	
	@Override
	public void setId(Integer id) {
		setSubCategoryId(id);
	}
	
	@Override
	public void setUuid(String uuid) {
		setSubCategoryUuid(uuid);
	}
	
}
