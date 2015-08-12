/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.api.db;

import java.util.List;

import org.openmrs.module.chartsearch.categories.CategoryFilter;

/**
 * Database methods for {@link CategoryFilter}
 */
public interface CategoryFilterDAO {
	
	public CategoryFilter getCategoryFilter(Integer categoryFilterId);
	
	public List<CategoryFilter> getAllCategoryFilters();
	
	public void createCategoryFilter(CategoryFilter categoryFilter);
	
	public void updateCategoryFilter(CategoryFilter categoryFilter);
	
	public void deleteCategoryFilter(CategoryFilter categoryFilter);
	
	public CategoryFilter getCategoryFilterByUuid(String uuid);
}
