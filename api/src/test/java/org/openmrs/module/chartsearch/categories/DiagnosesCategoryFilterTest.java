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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.ConceptClass;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.ConceptDAO;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Test class for {@link org.openmrs.module.chartsearch.categories.DiagnosesCategoryFilter}
 */
@SuppressWarnings("deprecation")
public class DiagnosesCategoryFilterTest extends BaseModuleContextSensitiveTest {
	
	ConceptService cs;
	
	ConceptClass cc;
	
	private ConceptDAO dao = null;
	
	/**
	 * Run this before each unit test in this class.
	 * 
	 * @throws Exception
	 */
	@Before
	public void runBeforeEachTest() throws Exception {
		cs = Context.getConceptService();
		cc = new ConceptClass();
		if (dao == null)
			// fetching the conceptDao from the spring application context
			dao = (ConceptDAO) applicationContext.getBean("conceptDAO");
	}
	
	private void saveANewConceptClass() {
		cc.setName("Diagnosis");
		cc.setDescription("Conclusion drawn through findings");
		cc.setUuid("uuid");
		cs.saveConceptClass(cc);
	}
	
	@Test
	public void testingWhetherDiagnosisFilterCategoryIsCreatedAsExpected() {
		saveANewConceptClass();
		
		DiagnosesCategoryFilter diagnosisCategory = new DiagnosesCategoryFilter("uuid");
		
		Assert.assertEquals(diagnosisCategory.getCatName(), cc.getName());
	}
}
