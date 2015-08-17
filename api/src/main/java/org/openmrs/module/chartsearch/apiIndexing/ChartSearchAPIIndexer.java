/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.apiIndexing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.openmrs.ConceptName;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.allergyapi.Allergies;
import org.openmrs.module.allergyapi.Allergy;
import org.openmrs.module.allergyapi.AllergyReaction;
import org.openmrs.module.allergyapi.api.PatientService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.appointmentscheduling.api.AppointmentService;

/**
 * Handles all Indexing that is done onto documents whose data is obtained from the Database using
 * various respective APIs
 */
public class ChartSearchAPIIndexer {
	
	/**
	 * Solr Indexing process of patient's allergies using the OpenMRS Allergy API
	 * (org.openmrs.module.allergyapi)
	 * 
	 * @param patientId, Numeric unique identifier for the patient whose allergies are being indexed
	 * @param solrServer, Solr server instance to handle the indexing
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void indexPatientAllergies(Integer patientId, SolrServer solrServer) {
		Collection<SolrInputDocument> docs = new ArrayList();
		PatientService patientService = Context.getService(PatientService.class);
		
		if (patientId != null) {
			Patient patient = Context.getPatientService().getPatient(patientId);
			if (patient != null) {
				Allergies allergies = patientService.getAllergies(patient);
				
				for (Allergy allergy : allergies) {
					SolrInputDocument doc = addAllergiesPropertiesToSolrDoc(allergy);
					
					docs.add(doc);
				}
				indexDocumentsUsingSolr(solrServer, docs);
			}
		}
	}
	
	/**
	 * Receives a collection of obtained documents from the various APIs and hands it over to Solr
	 * for indexing
	 * 
	 * @param solrServer, Solr server instance to handle the indexing
	 * @param docs, a collection of patient data generated in form of solr documents
	 */
	private void indexDocumentsUsingSolr(SolrServer solrServer, Collection<SolrInputDocument> docs) {
		if (!docs.isEmpty()) {
			try {
				if (docs.size() > 1000) {// Commit within 5 minutes.
					UpdateResponse resp = solrServer.add(docs, 300000);
					printFailureIfNoResponseIsreturned(resp);
				} else {
					UpdateResponse resp = solrServer.add(docs);
					printFailureIfNoResponseIsreturned(resp);
				}
				solrServer.commit();
			}
			catch (SolrServerException e) {
				System.out.println("Error generated" + e);
			}
			catch (IOException e) {
				System.out.println("Error generated" + e);
			}
		}
	}
	
	private SolrInputDocument addAllergiesPropertiesToSolrDoc(Allergy allergy) {
		SolrInputDocument doc = new SolrInputDocument();
		
		if (allergy != null && allergy.getAllergyId() != null) {
			Integer patientId = allergy.getPatient().getPatientId();
			String nonCodedAllergen = allergy.getAllergen().getNonCodedAllergen();
			List<AllergyReaction> reactions = allergy.getReactions();
			String codedAllegen = null;
			String severity = null;
			String codedReaction = "";
			String nonCodedReactions = null;
			
			if (allergy.getAllergen().getCodedAllergen() != null) {
				ConceptName codedCName = allergy.getAllergen().getCodedAllergen().getName();
				codedAllegen = codedCName.getName();
			}
			if (allergy.getSeverity() != null) {
				ConceptName severityCName = allergy.getSeverity().getName();
				severity = severityCName.getName();
			}
			for (int i = 0; i < reactions.size(); i++) {
				AllergyReaction reaction = reactions.get(i);
				
				if (StringUtils.isNotBlank(reaction.getReactionNonCoded())) {
					nonCodedReactions = reaction.getReactionNonCoded();
					break;
				} else {
					if (reaction.getReaction() != null) {
						String codedReactioncName = reaction.getReaction().getName().getName();
						if (StringUtils.isNotBlank(codedReactioncName)) {
							if (i == reactions.size() - 1) {
								codedReaction += codedReactioncName;
							} else {
								codedReaction += codedReactioncName + ", ";
							}
						}
					}
				}
				
			}
			
			doc.addField("id", allergy.getUuid());
			doc.addField("allergy_id", allergy.getAllergyId());
			doc.addField("allergy_coded_name", codedAllegen);
			doc.addField("patient_id", patientId);
			doc.addField("allergy_non_coded_name", nonCodedAllergen);
			doc.addField("allergy_severity", severity);
			doc.addField("allergy_type", allergy.getAllergen().getAllergenType());
			doc.addField("allergy_coded_reaction", codedReaction);
			doc.addField("allergy_non_coded_reaction", nonCodedReactions);
			doc.addField("allergy_comment", allergy.getComment());
			doc.addField("allergy_date",
			    allergy.getDateChanged() != null ? allergy.getDateChanged() : allergy.getDateCreated());
		}
		return doc;
	}
	
	private void printFailureIfNoResponseIsreturned(UpdateResponse resp) {
		if (resp.getStatus() != 0) {
			System.out.println("Some error has occurred, status is: " + resp.getStatus());
		}
	}
	
	/**
	 * Solr Indexing process for patient's appointments using the OpenMRS Appointments API
	 * (org.openmrs.module.appointmentscheduling)
	 * 
	 * @param patientId, Numeric unique identifier for the patient whose appointments are being
	 *            indexed
	 * @param solrServer, Solr server instance to handle the indexing
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void indexPatientAppoinments(Integer patientId, SolrServer solrServer) {
		AppointmentService appointmentService = Context.getService(AppointmentService.class);
		Collection<SolrInputDocument> docs = new ArrayList();
		
		if (patientId != null) {
			Patient patient = Context.getPatientService().getPatient(patientId);
			if (patient != null) {
				List<Appointment> appointments = appointmentService.getAppointmentsOfPatient(patient);
				
				for (Appointment appointment : appointments) {
					SolrInputDocument doc = addAppointmentPropertiesToSolrDoc(appointment);
					
					docs.add(doc);
				}
				indexDocumentsUsingSolr(solrServer, docs);
			}
		}
	}
	
	private SolrInputDocument addAppointmentPropertiesToSolrDoc(Appointment appointment) {
		SolrInputDocument doc = new SolrInputDocument();
		
		if (appointment != null && appointment.getAppointmentId() != null) {
			Integer patientId = appointment.getPatient().getPatientId();
			String reason = appointment.getReason();
			String uuid = appointment.getUuid();
			Integer id = appointment.getId();
			String status = appointment.getStatus() != null ? appointment.getStatus().getName() : "";
			Date start = appointment.getTimeSlot() != null ? appointment.getTimeSlot().getStartDate() : null;
			Date end = appointment.getTimeSlot() != null ? appointment.getTimeSlot().getEndDate() : null;
			String type = appointment.getAppointmentType() != null ? appointment.getAppointmentType().getDisplayString()
			        : "";
			String typeDesc = appointment.getAppointmentType() != null ? appointment.getAppointmentType().getDescription()
			        : "";
			String cancelReason = appointment.getCancelReason() != null ? appointment.getCancelReason() : "";
			String provider = null;
			String location = null;
			
			if (appointment.getTimeSlot() != null && appointment.getTimeSlot().getAppointmentBlock() != null
			        && appointment.getTimeSlot().getAppointmentBlock().getProvider() != null) {
				provider = appointment.getTimeSlot().getAppointmentBlock().getProvider().getName();
				location = appointment.getTimeSlot().getAppointmentBlock().getLocation().getDisplayString();
			}
			
			doc.addField("id", uuid);
			doc.addField("patient_id", patientId);
			doc.addField("appointment_reason", reason);
			doc.addField("appointment_provider", provider);
			doc.addField("appointment_id", id);
			doc.addField("appointment_status", status);
			doc.addField("appointment_start", start);
			doc.addField("appointment_end", end);
			doc.addField("appointment_type", type);
			doc.addField("appointment_typeDesc", typeDesc);
			doc.addField("appointment_cancelReason", cancelReason);
			doc.addField("appointment_location", location);
		}
		
		return doc;
	}
	
	public void indexBothPatientAllergiesAndAppointments(Integer patientId, SolrServer solrServer) {
		indexPatientAllergies(patientId, solrServer);
		indexPatientAppoinments(patientId, solrServer);
	}
}
