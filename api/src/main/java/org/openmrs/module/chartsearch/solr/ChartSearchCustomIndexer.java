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

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptName;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chartsearch.api.ChartSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public static ConceptService getConceptService() {
		return Context.getConceptService();
	}
	
	private static ObsService getObsService() {
		return Context.getObsService();
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param obsConceptIdOrValueCodedId
	 * @return
	 */
	public static List<ConceptName> getEnglishFullySpecifiedConceptNames(Integer obsConceptIdOrValueCodedId) {
		List<ConceptName> conceptNames = new ArrayList<ConceptName>();
		LinkedList<Concept> concepts = new LinkedList<Concept>();
		concepts.addAll(getConceptService().getAllConcepts());
		
		for (int i = concepts.indexOf(concepts.getFirst()); i <= concepts.indexOf(concepts.getLast()); i++) {
			for (int j = 0; j <= conceptNames.size(); j++) {
				for (Concept concept : concepts) {
					concept = concepts.get(i);
					for (ConceptName name : concept.getNames()) {
						if (name.getLocale().equals("en") && name.getConceptNameType().equals("FULLY_SPECIFIED")
						        && concept.getId() == obsConceptIdOrValueCodedId) {
							conceptNames.add(name);
						}
					}
				}
			}
		}
		return conceptNames;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param person
	 * @return
	 */
	public static List<String> getConceptClassNamesForAPerson(Person person) {
		List<String> conceptClassNames = new ArrayList<String>();
		
		for (Obs obs : getObsService().getObservationsByPerson(person)) {
			for (ConceptClass conceptClass : getConceptService().getConceptClasses()) {
				for (Concept concept : getConceptService().getAllConcepts()) {
					if (obs.getConcept().getId() == concept.getId()
					        && concept.getConceptClass().getId() == conceptClass.getId()) {
						conceptClassNames.add(conceptClass.getName());
					}
				}
			}
		}
		return conceptClassNames;
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
	
	@SuppressWarnings("unused")
	private void indexPatients(Integer numberOfPatient) {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		
		/*
		 * SELECT  o.uuid as id,  obs_id,	 person_id,  obs_datetime, obs_group_id, cn1.name as concept_name,   
			  cn2.name as coded, value_boolean,  value_datetime, value_numeric, value_text, cc.concept_class_name
			  FROM openmrs.obs o 
			  inner join (SELECT * FROM openmrs.concept_name c WHERE c.locale = 'en' AND concept_name_type = 'FULLY_SPECIFIED') as cn1  on cn1.concept_id = o.concept_id 
			  LEFT join (SELECT * FROM openmrs.concept_name c WHERE c.locale = 'en' AND concept_name_type = 'FULLY_SPECIFIED') as cn2 on cn2.concept_id = o.value_coded
			  LEFT join (SELECT DISTINCT o.concept_id, class.name AS concept_class_name FROM concept_class class JOIN concept c ON c.class_id = class.concept_class_id
							JOIN obs o ON o.concept_id = c.concept_id) AS cc ON cc.concept_id = o.concept_id  
			  WHERE o.uuid='${dih.delta.id}' AND o.voided=0 AND cn1.voided=0
		 */
		LinkedList<Obs> observations = new LinkedList<Obs>();
		
		for (int i = observations.indexOf(observations.getFirst()); i <= observations.indexOf(observations.getLast()); i++) {
			SolrInputDocument document = new SolrInputDocument();
			Obs obs = observations.get(i);
			List<ConceptName> c1 = getEnglishFullySpecifiedConceptNames(obs.getConcept().getId());
			List<ConceptName> c2 = getEnglishFullySpecifiedConceptNames(obs.getValueCoded().getId());
			
			document.addField("id" + i, obs.getUuid());
			document.addField("obs_id" + i, obs.getId());
			
			try {
				UpdateResponse response = solrServer.add(document);
				solrServer.commit();
			}
			catch (SolrServerException e) {
				
			}
			catch (IOException e) {
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void indexAllPatientData(Integer numberOfResults, Class showProgressToClass)
	    throws SQLException {
		SolrServer solrServer = SolrSingleton.getInstance().getServer();
		Collection docs = new ArrayList();
		
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
