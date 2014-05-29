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

package org.openmrs.module.chartsearch.categories;

/**
 * A category filter item can still be comprising of some other members that describe what the user
 * in mostly interested in, like; Labs = { all observations of concept x, all observations groups of
 * concept set y, all forms of ID z, text search for the term "ABC" }
 */
public class CategoryFilterMember {
	
	private CategoryFilter filterCategory;
	
	private Integer id;
	
	/*can be like; obs, concept, forms*/
	private String memberName;
	
	/*value for memberName*/
	private Object value;
	
	private String description;
	
	private String uuid;
	
	public CategoryFilter getFilterCategory() {
		return filterCategory;
	}
	
	public void setFilterCategory(CategoryFilter filterCategory) {
		this.filterCategory = filterCategory;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
}
