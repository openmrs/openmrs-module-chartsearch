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
 * Represents in sub-category within a category like LabSet in Tests, Drug in Orders et-cetera
 */
public class SubCategoryFilter extends CategoryFilter {
	
	private CategoryFilter subcategoryFor;
	
	public CategoryFilter getSubcategoryFor() {
		return subcategoryFor;
	}
	
	public void setSubcategoryFor(CategoryFilter subcategoryFor) {
		this.subcategoryFor = subcategoryFor;
	}
}
