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
package org.openmrs.module.chartsearch.api.db.hibernate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.db.ChartSearchDAO;
import org.openmrs.module.chartsearch.solr.ChartSearchCustomIndexer;
import org.openmrs.module.chartsearch.solr.nonPatient.SearchProject;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is a default implementation of {@link ChartSearchDAO}.
 */
@Transactional
public class HibernateChartSearchDAO implements ChartSearchDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * SQL processing to get patient data to be indexed
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void indexAllPatientData(Integer numberOfResults, SolrServer solrServer, Class showProgressToClass) {
		PreparedStatement preparedStatement = null;
		SolrInputDocument doc = new SolrInputDocument();
		String sql = " SELECT  o.uuid as id,  obs_id,	 person_id,  obs_datetime, obs_group_id, cn1.name as concept_name, cn2.name as coded, value_boolean,  value_datetime, value_numeric, value_text, cc.concept_class_name FROM openmrs.obs o "
		        + "inner join (SELECT * FROM openmrs.concept_name c WHERE c.locale = 'en' AND concept_name_type = 'FULLY_SPECIFIED') as cn1 on cn1.concept_id = o.concept_id "
		        + "LEFT join (SELECT * FROM openmrs.concept_name c WHERE c.locale = 'en' AND concept_name_type = 'FULLY_SPECIFIED') as cn2 on cn2.concept_id = o.value_coded  "
		        + "LEFT join (SELECT DISTINCT o.concept_id, class.name AS concept_class_name FROM concept_class class JOIN concept c ON c.class_id = class.concept_class_id JOIN obs o ON o.concept_id = c.concept_id) AS cc ON cc.concept_id = o.concept_id "
		        + "WHERE o.voided=0 AND cn1.voided=0 LIMIT " + numberOfResults;
		
		try {
			String info = (String) showProgressToClass.getMethod("getIndexingProgressInfo").invoke(
			    showProgressToClass.newInstance());
			
			info = Context.getMessageSourceService().getMessage("chartsearch.indexing.patientData.fetchingData");
			setIndexingProgressInfo(showProgressToClass, info);
			
			log.info("SQL Query for indexing all data is: " + sql);
			
			preparedStatement = sessionFactory.getCurrentSession().connection().prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			info = Context.getMessageSourceService().getMessage("chartsearch.indexing.patientData.finishedFetchingData");
			setIndexingProgressInfo(showProgressToClass, info);
			
			while (rs.next()) {
				setResultsFieldValues(rs);
				addResultsFieldValuesToADocument(doc);
				setIndexingProgressInfo(showProgressToClass, info);
				
				UpdateResponse resp = solrServer.add(doc);
				
				resp = solrServer.commit(true, true);
				resp = solrServer.optimize(true, true);
				
				setIndexingProgressInfo(showProgressToClass, info);
				doc.clear();
			}
			info = Context.getMessageSourceService().getMessage("chartsearch.indexing.patientData.finishedIndexingData");
		}
		catch (Exception e) {
			System.out.println("Error getting mrn log" + e);
		}
		finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				}
				catch (SQLException e) {
					log.error("Error generated while closing statement", e);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setIndexingProgressInfo(Class showProgressToClass, String info) throws IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
		try {
			showProgressToClass.getMethod("setIndexingProgressInfo", new Class[] { String.class }).invoke(
			    showProgressToClass.newInstance(), info);
		}
		catch (IllegalArgumentException e) {
			log.error("Error generated", e);
		}
		catch (SecurityException e) {
			log.error("Error generated", e);
		}
		catch (InstantiationException e) {
			log.error("Error generated", e);
		}
	}
	
	private static void setResultsFieldValues(ResultSet rs) throws SQLException {
		ChartSearchCustomIndexer.setId(rs.getString("id"));
		ChartSearchCustomIndexer.setObsId(rs.getInt("obs_id"));
		ChartSearchCustomIndexer.setPersonId(rs.getInt("person_id"));
		ChartSearchCustomIndexer.setObsDatetime(rs.getDate("obs_datetime"));
		ChartSearchCustomIndexer.setObsGroupId(rs.getInt("obs_group_id"));
		ChartSearchCustomIndexer.setConceptName(rs.getString("concept_name"));
		ChartSearchCustomIndexer.setCoded(rs.getString("coded"));
		ChartSearchCustomIndexer.setValueBoolean(rs.getBoolean("value_boolean"));
		ChartSearchCustomIndexer.setValueDatetime(rs.getDate("value_datetime"));
		ChartSearchCustomIndexer.setValueNumeric(rs.getFloat("value_numeric"));
		ChartSearchCustomIndexer.setValueText(rs.getString("value_text"));
		ChartSearchCustomIndexer.setConceptClassName(rs.getString("concept_class_name"));
	}
	
	private static void addResultsFieldValuesToADocument(SolrInputDocument doc) {
		doc.addField("id", ChartSearchCustomIndexer.getId());
		doc.addField("obs_id", ChartSearchCustomIndexer.getObsId());
		doc.addField("person_id", ChartSearchCustomIndexer.getPersonId());
		doc.addField("obs_datetime", ChartSearchCustomIndexer.getObsDatetime());
		doc.addField("obs_group_id", ChartSearchCustomIndexer.getObsGroupId());
		doc.addField("concept_name", ChartSearchCustomIndexer.getConceptName());
		doc.addField("coded", ChartSearchCustomIndexer.getCoded());
		doc.addField("value_boolean", ChartSearchCustomIndexer.isValueBoolean());
		doc.addField("value_datetime", ChartSearchCustomIndexer.getValueDatetime());
		doc.addField("value_numeric", ChartSearchCustomIndexer.getValueNumeric());
		doc.addField("value_text", ChartSearchCustomIndexer.getValueText());
		doc.addField("concept_class_name", ChartSearchCustomIndexer.getConceptClassName());
	}
	
	@Override
	public SearchProject getSearchProject(Integer projectId) {
		log.info("Getting a non patient project with id: " + projectId);
		return (SearchProject) sessionFactory.getCurrentSession().get(SearchProject.class, projectId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SearchProject> getAllSearchProjects() {
		log.info("Getting all Search projects from the database");
		List<SearchProject> projects = sessionFactory.getCurrentSession().createCriteria(SearchProject.class).list();
		//TODO Fix things here, this code is not working
		return projects;
	}
	
	/**
	 * Saves either a new or an edited search projects, a Search project is a description of like a
	 * module etc
	 * 
	 * @param project
	 */
	@Override
	public void saveSearchProject(SearchProject project) {
		sessionFactory.getCurrentSession().saveOrUpdate(project);
	}
	
	@Override
	public void deleteSearchProject(SearchProject project) {
		sessionFactory.getCurrentSession().delete(project);
	}
	
	public SearchProject getSearchProjectByUuid(String uuid) {
		SearchProject p = null;
		
		p = (SearchProject) sessionFactory.getCurrentSession().createQuery("from SearchProject p where p.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
		
		return p;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List getAllExistingDatabases() {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SHOW DATABASES");
		List result = query.list();
		return result;
	}
	
	public boolean createAndDumpToNonExistingDatabase(String dbName, String sqlSourcePath) {
		boolean state = false;
		Query query1 = sessionFactory.getCurrentSession().createSQLQuery("CREATE DATABASE IF NOT EXISTS " + dbName);
		
		if (!getAllExistingDatabases().contains(dbName)) {
			query1.executeUpdate();
			
			Query query2 = sessionFactory.getCurrentSession().createSQLQuery("USE " + dbName);
			
			query2.executeUpdate();
			
			Connection conn = sessionFactory.getCurrentSession().connection();
			System.out.println("Current connection is: " + conn.toString());
			ScriptRunner sr = new ScriptRunner(conn);
			Reader reader;
			try {
				reader = new BufferedReader(new FileReader(sqlSourcePath));
				sr.runScript(reader);
				System.out.println("All databases now after creating: " + dbName + " Are: "
				        + getAllExistingDatabases().toString());
				state = true;
				
				Query query3 = sessionFactory.getCurrentSession().createSQLQuery("USE " + OpenmrsConstants.DATABASE_NAME);
				query3.executeUpdate();
			}
			catch (FileNotFoundException e) {
				log.error("Error generated", e);
			}
		}
		return state;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public JSONObject getAllTablesAndColumnNamesOfADatabase(String databaseName) {
		JSONObject tablesAndColumns = new JSONObject();
		
		Query query1 = sessionFactory.getCurrentSession().createSQLQuery("USE " + databaseName);
		query1.executeUpdate();
		
		Query query2 = sessionFactory.getCurrentSession().createSQLQuery(
		    "SELECT DISTINCT TABLE_NAME from information_schema.columns WHERE table_schema = " + databaseName);
		List allTableNames = query2.list();
		
		for (int i = 0; i < allTableNames.size(); i++) {
			Query query3 = sessionFactory.getCurrentSession().createSQLQuery(
			    "SELECT DISTINCT COLUMN_NAME FROM information_schema.columns WHERE table_schema = " + databaseName
			            + " AND table_name = " + allTableNames.get(i));
			List columns = query3.list();
			tablesAndColumns.put(allTableNames.get(i), columns);
		}
		
		Query query4 = sessionFactory.getCurrentSession().createSQLQuery("USE " + OpenmrsConstants.DATABASE_NAME);
		query4.executeUpdate();
		
		return tablesAndColumns;
	}
	
	@Override
	public void deleteImportedDatabase(String dbName) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("DROP DATABASE IF EXISTS " + dbName);
		query.executeUpdate();
		
		Query query2 = sessionFactory.getCurrentSession().createSQLQuery("USE " + OpenmrsConstants.DATABASE_NAME);
		query2.executeUpdate();
	}
	
	/**
	 * Usage example at: {@link #getResultsFromSQLRunOnANonDefaultOpenMRSDatabase(String, String)}
	 */
	private void useNonDefaultDatabase(String databaseName) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("USE " + databaseName);
		query.executeUpdate();
	}
	
	/**
	 * Must always be invoked every after calling {@link #useDefaultOpenMRSDatabase()} Usage example
	 * at: {@link #getResultsFromSQLRunOnANonDefaultOpenMRSDatabase(String, String)}
	 */
	private void useDefaultOpenMRSDatabase() {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("USE " + OpenmrsConstants.DATABASE_NAME);
		query.executeUpdate();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List getResultsFromSQLRunOnANonDefaultOpenMRSDatabase(String databaseName, String sqlQuery) {
		useNonDefaultDatabase(databaseName);
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);
		List list = query.list();
		
		useDefaultOpenMRSDatabase();
		return list;
	}
}
