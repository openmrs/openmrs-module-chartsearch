/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr.nonPatient;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.util.OpenmrsConstants;

/**
 * A web project that has database data attached to it. Such as Chart Search Module et-cetera
 */
@Entity
@Table(name = "chartsearch_project")
public class SearchProject extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "project_id")
	private int projectId;
	
	@Column(name = "name", length = 38, nullable = false)
	private String projectName;
	
	@Column(name = "description")
	private String projectDescription;
	
	@Column(name = "database", length = 15)
	private String database;
	
	@Column(name = "database_user", length = 15)
	private String databaseUser;
	
	@Column(name = "database_user_password", length = 38)
	private String databaseUSerPassword;
	
	@Column(name = "server_name", length = 15)
	private String serverName;
	
	@Column(name = "dbms", length = 8)
	private String dbms;
	
	@Column(name = "port_number", length = 5)
	private String portNumber;
	
	/**
	 * The SQL Query to be run to return the data that need to be indexed for this particular
	 * project
	 */
	@Column(name = "sql_query", length = 65535, nullable = false)
	private String sqlQuery;
	
	/**
	 * column names separated by commas, these should be unique from one another and the same ones
	 * as mentioned in the sqlQuery, take use of AS key word in MySQL to make them unique
	 */
	@Column(name = "column_names", length = 255, nullable = false)
	private String columnNames;
	
	/**
	 * List of column names that need to be added as fields obtained from the client
	 */
	private List<String> columnNamesList;
	
	/**
	 * Creates a SearchProject object when registering a project, must always use this when creating
	 * this object
	 * 
	 * @param projectName
	 * @param sqlQuery
	 * @param columnNamesList
	 */
	public SearchProject(String projectName, String sqlQuery, List<String> columnNamesList, String database) {
		setProjectName(projectName);
		setSqlQuery(sqlQuery);
		setColumnNamesList(columnNamesList);
		setColumnNames(getColumnNamesSeparatedWithCommas());
		setDatabase(database);
	}
	
	public SearchProject() {
		
	}
	
	/**
	 * @see org.openmrs.BaseOpenmrsObject#getUuid()
	 */
	@Basic
	@Access(AccessType.PROPERTY)
	@Column(name = "uuid", length = 38, unique = true)
	@Override
	public String getUuid() {
		return super.getUuid();
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getProjectDescription() {
		return projectDescription;
	}
	
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public String getSqlQuery() {
		return sqlQuery;
	}
	
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
	/**
	 * Doesn't return from the database, use {@link #getColumnNames()}
	 * 
	 * @return
	 */
	public List<String> getColumnNamesList() {
		return columnNamesList;
	}
	
	public void setColumnNamesList(List<String> columnNamesList) {
		this.columnNamesList = columnNamesList;
	}
	
	@Override
	public Integer getId() {
		return getProjectId();
	}
	
	@Override
	public void setId(Integer id) {
		setProjectId(id);
	}
	
	/**
	 * Read only - Returns all columns separated by a comma and space(, ) from the the
	 * {@link #getColumnNamesList()} Auto generated method comment
	 * 
	 * @return comma separated column names otherwise NULL if no column name exists
	 */
	public String getColumnNamesSeparatedWithCommas() {
		String commaSeperateColumnNames = "";
		List<String> columnNames = getColumnNamesList();
		for (int i = 0; i < columnNames.size(); i++) {
			if (i != columnNames.size() - 1) {
				commaSeperateColumnNames += columnNames.get(i) + ", ";
			} else
				commaSeperateColumnNames += columnNames.get(i);
		}
		if (StringUtils.isBlank(commaSeperateColumnNames)) {
			commaSeperateColumnNames = null;
		}
		return commaSeperateColumnNames;
	}
	
	/**
	 * Gets column names separated commas
	 * 
	 * @see {@link #getColumnNamesSeparatedWithCommas()}
	 */
	public String getColumnNames() {
		return columnNames;
	}
	
	/**
	 * @see {@link #getColumnNamesSeparatedWithCommas()}
	 */
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	
	public String getDatabase() {
		return database;
	}
	
	/**
	 * Sets the database name, if it is empty or whitespace or bull, it sets it to the openmrs
	 * database
	 * 
	 * @param database
	 */
	public void setDatabase(String database) {
		if (StringUtils.isBlank(database)) {
			database = System.getProperty(OpenmrsConstants.DATABASE_NAME);
		}
		this.database = database;
	}
	
	public String getDatabaseUser() {
		return databaseUser;
	}
	
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}
	
	public String getDatabaseUSerPassword() {
		return databaseUSerPassword;
	}
	
	public void setDatabaseUSerPassword(String databaseUSerPassword) {
		this.databaseUSerPassword = databaseUSerPassword;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String getDbms() {
		return dbms;
	}
	
	public void setDbms(String dbms) {
		this.dbms = dbms;
	}
	
	public String getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}
}
