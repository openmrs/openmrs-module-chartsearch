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

import org.apache.commons.lang3.StringUtils;
import org.openmrs.BaseOpenmrsObject;

/**
 * A web project that has database data attached to it. Such as Chart Search Module et-cetera
 */
public class SearchProject extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String projectName;
	
	private String projectDescription;
	
	private String projectUuid;
	
	private Integer projectId;
	
	/**
	 * column names separated by commas, these should be unique from one another and the same ones
	 * as mentioned in the sqlQuery, take use of AS key word in MySQL to make them unique 
	 */
	private String columnNames;
	
	/**
	 * Creates a SearchProject object when registering a project
	 * 
	 * @param projectName
	 * @param sqlQuery
	 * @param columnNamesList
	 */
	public SearchProject(String projectName, String sqlQuery, List<String> columnNamesList) {
		setProjectName(projectName);
		setSqlQuery(sqlQuery);
		setColumnNamesList(columnNamesList);
		setColumnNames(getColumnNamesSeparatedWithCommas());
	}
	
	public SearchProject() {
		
	}
	
	/**
	 * The SQL Query to be run to return the data that need to be indexed for this particular
	 * project
	 */
	private String sqlQuery;
	
	/**
	 * List of column names that need to be added as fields obtained from the client
	 */
	private List<String> columnNamesList;
	
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
	
	public String getProjectUuid() {
		return projectUuid;
	}
	
	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}
	
	public Integer getProjectId() {
		return projectId;
	}
	
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
	public String getSqlQuery() {
		return sqlQuery;
	}
	
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
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
			commaSeperateColumnNames += columnNames.get(i) + ", ";
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
}
