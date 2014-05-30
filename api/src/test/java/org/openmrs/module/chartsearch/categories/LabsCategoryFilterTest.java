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

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for {@link LabsCategoryFilter}
 */
@Ignore
@SuppressWarnings("deprecation")
public class LabsCategoryFilterTest extends ChartSearchCategoriesTest {
	
	@Test
	public void testingWhetherLabsFilterCategoryIsCreatedAsExpected() {
		
		String dbLabsUuid = "4719138c-8553-4c81-bb92-753f41f8be16";
		
		 cc = fetchAConceptClassByUuid(dbLabsUuid);
		 LabsCategoryFilter labsCategory = new LabsCategoryFilter("4719138c-8553-4c81-bb92-753f41f8be16");
		 
		 Assert.assertEquals(labsCategory.getCategoryDescription(), cc.getDescription());
	}
}
