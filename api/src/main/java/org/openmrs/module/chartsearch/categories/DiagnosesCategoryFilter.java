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

import org.openmrs.ConceptClass;
import org.openmrs.api.context.Context;

/**
 * Creates category item for diagnosis
 */
public class DiagnosesCategoryFilter extends CategoryFilter {
	
	/**
	 * Creating object for DiagnosisFilterCategories Diagnoses (Diagnosis).
	 * 
	 * @param uuid
	 */
	public DiagnosesCategoryFilter(String uuid) {
		if (uuid != null && uuid != "" && uuid != " ") {
			ConceptClass cc = Context.getConceptService().getConceptClassByUuid(uuid);
			
			setCatName(cc.getName());
			setCatDescription(cc.getDescription());
			setCatUuid(cc.getUuid());
			setCatId(cc.getId());
		}
	}
	
}
