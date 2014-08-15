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
