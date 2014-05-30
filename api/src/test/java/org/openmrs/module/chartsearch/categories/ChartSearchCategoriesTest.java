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

import org.junit.Ignore;
import org.openmrs.ConceptClass;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.ConceptDAO;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Standard Test for InBuilt categories tests like {@link DiagnosesCategoryFilter} where
 * {@link DiagnosesCategoryFilterTest} inherits from
 */
@Ignore
public class ChartSearchCategoriesTest extends BaseModuleContextSensitiveTest {
	
	ConceptService cs;
	
	ConceptClass cc;
	
	private ConceptDAO dao = null;
	
	/**
	 * Run this before each unit test in this class.
	 * 
	 * @throws Exception
	 */
	//@Before
	public void runBeforeEachTest() throws Exception {
		cs = Context.getConceptService();
		cc = new ConceptClass();
		if (dao == null)
			// fetching the conceptDao from the spring application context
			dao = (ConceptDAO) applicationContext.getBean("conceptDAO");
	}
	
	protected void saveANewConceptClass(String name, String description, String uuid) {
		cc.setName(name);
		cc.setDescription(description);
		cc.setUuid(uuid);
		cs.saveConceptClass(cc);
	}
	
	protected ConceptClass fetchAConceptClassByUuid(String uuid) {
		
		/*there are 18 entries in standardTestDataset.xml including; Test, Procedure, Drug, Diagnosis,
		 * Finding, Anatomy, Question, LabSet, MedSet, ConvSet, Misc, Symptom, Symptom/Finding, Specimen,
		 * Misc Order, Program, Workflow, State*/
		return cs.getConceptClassByUuid(uuid);
	}
}
