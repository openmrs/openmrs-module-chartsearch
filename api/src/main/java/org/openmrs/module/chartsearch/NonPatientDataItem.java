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

import java.util.List;

public class NonPatientDataItem extends ChartListItem {
	
	private String databaseName;
	
	//Use this at indexing to add an entry for an entity in data-config.xml and schema.xml files
	private String sqlQuery;
	
	private List<String> tableNames;
	
	private List<String> columnNames;
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getSqlQuery() {
		return sqlQuery;
	}
	
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
	public List<String> getTableNames() {
		return tableNames;
	}
	
	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}
	
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	//To be Removed for test purposes {cc refers to concept_class}
	private String ccName;
	
	private String ccFilterQuery;
	
	private String ccDescription;
	
	public String getCcName() {
		return ccName;
	}
	
	public void setCcName(String ccName) {
		this.ccName = ccName;
	}
	
	public String getCcFilterQuery() {
		return ccFilterQuery;
	}
	
	public void setCcFilterQuery(String ccFilterQuery) {
		this.ccFilterQuery = ccFilterQuery;
	}
	
	public String getCcDescription() {
		return ccDescription;
	}
	
	public void setCcDescription(String ccDescription) {
		this.ccDescription = ccDescription;
	}
	
}
