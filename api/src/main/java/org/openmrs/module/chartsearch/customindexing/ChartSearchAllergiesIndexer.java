/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.customindexing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

public class ChartSearchAllergiesIndexer {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void indexPatientAllergies(Integer patientId, SolrServer solrServer, PatientService patientService) {
		Collection<SolrInputDocument> docs = new ArrayList();
		
		if (patientId != null) {
			Patient patient = Context.getPatientService().getPatient(patientId);
			if (patient != null) {
				Allergies allAllergies = patientService.getAllergies(patient);
				Allergies allergies = new Allergies();//current patient's allergies
				
				for (Allergy allergy : allAllergies) {
					if (allergy.getPatient().getPatientId().equals(patientId)) {
						allergies.add(allergy);
					}
				}
				for (Allergy allergy : allergies) {
					SolrInputDocument doc = addAllergiesPropertiesToSolrDoc(allergy);
					
					docs.add(doc);
				}
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
		}
	}
	
	private SolrInputDocument addAllergiesPropertiesToSolrDoc(Allergy allergy) {
		SolrInputDocument doc = new SolrInputDocument();
		
		if (allergy != null && allergy.getAllergyId() != null) {
			String nonCodedAllergen = allergy.getAllergen().getNonCodedAllergen();
			List<AllergyReaction> reactions = allergy.getReactions();
			String codedAllegen = null;
			String severity = null;
			String codedReaction = null;
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
				
				nonCodedReactions = reaction.getReactionNonCoded();
				if (reaction.getReaction() != null) {
					ConceptName codedReactioncName = reaction.getReaction().getName();
					codedReaction = codedReactioncName.getName();
				}
				
			}
			
			doc.addField("id", allergy.getUuid());
			doc.addField("allergy_id", allergy.getAllergyId());
			doc.addField("allergy_coded_name", codedAllegen);
			doc.addField("allergy_non_coded_name", nonCodedAllergen);
			doc.addField("allergy_severity", severity);
			doc.addField("allergy_type", allergy.getAllergen().getAllergenType());
			doc.addField("allergy_coded_reaction", codedReaction);
			doc.addField("allergy_non_coded_reaction", nonCodedReactions);
			doc.addField("allergy_comment", allergy.getComment());
		}
		return doc;
	}
	
	private void printFailureIfNoResponseIsreturned(UpdateResponse resp) {
		if (resp.getStatus() != 0) {
			System.out.println("Some error has occurred, status is: " + resp.getStatus());
		}
	}
}
