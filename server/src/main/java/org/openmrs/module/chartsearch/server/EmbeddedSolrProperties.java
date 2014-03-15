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
package org.openmrs.module.chartsearch.server;

/**
 *
 */
public class EmbeddedSolrProperties {
	
	private final String solrHome;
	
	private final String dbUrl;
	
	private final String dbUser;
	
	private final String dbPassword;
	
	public EmbeddedSolrProperties(String solrHome, String dbUrl, String dbUser, String dbPassword) {
		this.solrHome = solrHome;
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	public String getSolrHome() {
	    return solrHome;
    }

	public String getDbUrl() {
	    return dbUrl;
    }

	public String getDbUser() {
	    return dbUser;
    }

	public String getDbPassword() {
	    return dbPassword;
    }
	
}
