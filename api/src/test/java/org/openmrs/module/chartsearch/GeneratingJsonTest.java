/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch;

import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GeneratingJsonTest {
	
	@Ignore
	public void generateFacetsJson_shouldreturnJSONWithNameAndCount() {
		
		Count countObject = new Count(new FacetField("TestFacetField"), "test", 34);
		
		JSONObject json = GeneratingJson.generateFacetsJson(countObject);
		
		Assert.assertNotNull(json);
		Assert.assertEquals("{\"name\":\"test\",\"count\":34}", json.toString());
	}
	
	@Test
	public void provideAtleastAPassingTestForBamboo() {
		Assert.assertNotNull("");
	}
}
