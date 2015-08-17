/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.cache;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openmrs.module.chartsearch.categories.CategoryFilter;

/**
 * Category filter item display name that shows up on the user interface, each entry is attached
 * onto {@link ChartSearchPreference}
 */
@Entity
@Table(name = "chartsearch_category_displayname")
public class ChartSearchCategoryDisplayName implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "displayname_id", nullable = false)
	private Integer displayNameId;
	
	@Column(name = "diplay_name", nullable = false)
	private String displayName;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38)
	private String uuid = UUID.randomUUID().toString();
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private CategoryFilter categoryFilter;
	
	@ManyToOne
	@JoinColumn(name = "preference_id", nullable = false)
	private ChartSearchPreference preference;
	
	public Integer getDisplayNameId() {
		return displayNameId;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public CategoryFilter getCategoryFilter() {
		return categoryFilter;
	}
	
	public void setDisplayNameId(Integer displayNameId) {
		this.displayNameId = displayNameId;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void setCategoryFilter(CategoryFilter categoryFilter) {
		this.categoryFilter = categoryFilter;
	}
	
	public ChartSearchPreference getPreference() {
		return preference;
	}
	
	public void setPreference(ChartSearchPreference preference) {
		this.preference = preference;
	}
	
}
