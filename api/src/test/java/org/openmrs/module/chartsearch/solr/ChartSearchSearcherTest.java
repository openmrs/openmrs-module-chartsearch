/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;

public class ChartSearchSearcherTest {
	
	/**
	 * Testing functionality for the start. TODO to be re-edited or removed
	 */
	@Ignore
	public void getNonPatientDocumentsTest() {
		ChartSearchSearcher searcher = getComponent(ChartSearchSearcher.class);
		searcher.getNonPatientDocumentList("*");
		Assert.assertNotNull("");
	}
	

	/**
	 * TODO testing tika solrj Indexing option
	 * Auto generated method comment
	 *
	 */
	@Test
	public void test() {
		try {
			SolrjIndexer idxer = new SolrjIndexer();
			
			idxer.doSqlDocuments();
			
			idxer.endIndexing();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
