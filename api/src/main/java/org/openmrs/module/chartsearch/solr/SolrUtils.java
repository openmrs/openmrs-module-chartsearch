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

import java.io.File;
import java.util.Properties;

import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.server.EmbeddedSolrProperties;
import org.openmrs.util.OpenmrsUtil;

/**
 *
 */
public class SolrUtils {
	
	public static EmbeddedSolrProperties getEmbeddedSolrProperties() {
		Properties properties = Context.getRuntimeProperties();
		String dbUrl = properties.getProperty("connection.url");
		String dbUser = properties.getProperty("connection.username");
		String dbPassword = properties.getProperty("connection.password");
		String solrHome = new File(OpenmrsUtil.getApplicationDataDirectory(), "chartsearch").getAbsolutePath();
		
		return new EmbeddedSolrProperties(solrHome, dbUrl, dbUser, dbPassword);
	}
	
}
