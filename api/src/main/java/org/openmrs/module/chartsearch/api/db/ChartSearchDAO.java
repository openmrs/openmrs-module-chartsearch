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
package org.openmrs.module.chartsearch.api.db;

import java.sql.ResultSet;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;

/**
 * Database methods for {@link ChartSearchService}.
 */
public interface ChartSearchDAO {
	
	public void indexAllPatientData(Integer numberOfResults, SolrServer solrServer,
	                                @SuppressWarnings("rawtypes") Class showProgressToClass);
	
	public ResultSet executeSQL(String sql);
	
	void deleteSearchProject(SearchProject project);
	
	void saveSearchProject(SearchProject project);
	
	SearchProject getSearchProject(Integer projectId);
	
	List<SearchProject> getAllSearchProjects();
	
	public SearchProject getSearchProjectByUuid(String uuid);
}
