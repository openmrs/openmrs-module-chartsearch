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

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles any other different indexing policies that are not handled by ChartSearchIndexer. e.g.
 * Indexing any specified number of documents without specifying a given patient
 */
public class ChartSearchCustomIndexer {
	
	private static final Logger log = LoggerFactory.getLogger(ChartSearchCustomIndexer.class);
	
	//o.uuid as id
	private static String id;
	
	//obs_id
	private static Integer obsId;
	
	//person_id
	private static Integer personId;
	
	//obs_datetime
	private static Date obsDatetime;
	
	//obs_group_id
	private static Integer obsGroupId;
	
	//cn1.name as concept_name
	private static String conceptName;
	
	//cn2.name as coded
	private static String coded;
	
	//value_boolean
	private static Boolean valueBoolean;
	
	//value_datetime
	private static Date valueDatetime;
	
	//value_numeric
	private static float valueNumeric;
	
	//value_text
	private static String valueText;
	
	//concept_class_name
	private static String conceptClassName;
	
	private static ChartSearchService chartSearchService;
	
	public static ChartSearchService getChartSearchService() {
		try {
			chartSearchService = Context.getService(ChartSearchService.class);
		}
		catch (APIAuthenticationException e) {
			System.out.println("Not Authenticated!!!");
		}
		return chartSearchService;
	}
	
	public static String getId() {
		return id;
	}
	
	public static Integer getObsId() {
		return obsId;
	}
	
	public static Integer getPersonId() {
		return personId;
	}
	
	public static Date getObsDatetime() {
		return obsDatetime;
	}
	
	public static Integer getObsGroupId() {
		return obsGroupId;
	}
	
	public static String getConceptName() {
		return conceptName;
	}
	
	public static String getCoded() {
		return coded;
	}
	
	public static Boolean isValueBoolean() {
		return valueBoolean;
	}
	
	public static Date getValueDatetime() {
		return valueDatetime;
	}
	
	public static Float getValueNumeric() {
		return valueNumeric;
	}
	
	public static String getValueText() {
		return valueText;
	}
	
	public static String getConceptClassName() {
		return conceptClassName;
	}
	
	public static Boolean getValueBoolean() {
		return valueBoolean;
	}
	
	public static void setValueBoolean(Boolean valueBoolean) {
		ChartSearchCustomIndexer.valueBoolean = valueBoolean;
	}
	
	public static void setId(String id) {
		ChartSearchCustomIndexer.id = id;
	}
	
	public static void setObsId(Integer obsId) {
		ChartSearchCustomIndexer.obsId = obsId;
	}
	
	public static void setPersonId(Integer personId) {
		ChartSearchCustomIndexer.personId = personId;
	}
	
	public static void setObsDatetime(Date obsDatetime) {
		ChartSearchCustomIndexer.obsDatetime = obsDatetime;
	}
	
	public static void setObsGroupId(Integer obsGroupId) {
		ChartSearchCustomIndexer.obsGroupId = obsGroupId;
	}
	
	public static void setConceptName(String conceptName) {
		ChartSearchCustomIndexer.conceptName = conceptName;
	}
	
	public static void setCoded(String coded) {
		ChartSearchCustomIndexer.coded = coded;
	}
	
	public static void setValueDatetime(Date valueDatetime) {
		ChartSearchCustomIndexer.valueDatetime = valueDatetime;
	}
	
	public static void setValueNumeric(float valueNumeric) {
		ChartSearchCustomIndexer.valueNumeric = valueNumeric;
	}
	
	public static void setValueText(String valueText) {
		ChartSearchCustomIndexer.valueText = valueText;
	}
	
	public static void setConceptClassName(String conceptClassName) {
		ChartSearchCustomIndexer.conceptClassName = conceptClassName;
	}
	
	@SuppressWarnings("rawtypes")
	public static void indexAllPatientData(Integer numberOfResults, Class showProgressToClass) throws SQLException {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		if (numberOfResults == null || !(numberOfResults instanceof Integer)) {
			numberOfResults = 400;
		}
		getChartSearchService().indexAllPatientData(numberOfResults, solrServer, showProgressToClass);
		try {
			solrServer.commit();
		}
		catch (SolrServerException e) {
			log.error("Error generated", e);
		}
		catch (IOException e) {
			log.error("Error generated", e);
		}
	}
	
}
