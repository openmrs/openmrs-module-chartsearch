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
package org.openmrs.module.chartsearch.api.db;

import java.util.List;

import org.openmrs.module.chartsearch.categories.CategoryFilter;
import org.openmrs.module.chartsearch.categories.SubCategoryFilter;

public interface SubCategoryFilterDAO {
	
	public SubCategoryFilter getSubCategoryFilter(Integer subCategoryFilterId);
	
	public List<SubCategoryFilter> getSubCategoryFilters(CategoryFilter categoryFilter);
	
	public void createSubCategoryFilter(SubCategoryFilter subCategoryFilter);
	
	public void updateSubCategoryFilter(SubCategoryFilter subCategoryFilter);
	
	public void deleteSubCategoryFilter(SubCategoryFilter subCategoryFilter);
	
	public SubCategoryFilter getSubCategoryFilterByUuid(String subCategoryFilterUuid);
}
