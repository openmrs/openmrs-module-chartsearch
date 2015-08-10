/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
