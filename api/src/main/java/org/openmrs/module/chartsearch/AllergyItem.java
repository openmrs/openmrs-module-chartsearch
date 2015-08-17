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

import java.util.Date;

import org.apache.solr.common.SolrDocument;
import org.openmrs.module.allergyapi.Allergy;

/**
 * Represents this module's customized {@link SolrDocument} for a {@link Allergy}<br >
 * An Allergen is any substance that can cause an Allergy (hyper-sensitivity reaction to a
 * particular allergen)
 */
public class AllergyItem extends ChartListItem {
	
	/**
	 * Patient Allergen ID
	 */
	private Integer allergyId;
	
	/**
	 * Patient Allergen Name obtained/selected from the Concept Dictionary
	 */
	private String allergenCodedName;
	
	/**
	 * Patient Allergen Name manually typed in from the User Interface
	 */
	private String allergenNonCodedName;
	
	/**
	 * Patient Allergen Intensity; either Mild or Moderate or Severe
	 */
	private String allergenSeverity;
	
	/**
	 * Patient allergen Category; either Drug, Food or Environment
	 */
	private String allergenType;
	
	/**
	 * Body change/Response from an Allergen obtained/selected from the Concept Dictionary
	 */
	private String allergenCodedReaction;
	
	/**
	 * Body change/Response from an Allergen typed in or entered from the User Interface
	 */
	private String allergenNonCodedReaction;
	
	/**
	 * A providers Notice or comment on a patient allergen
	 */
	private String allergenComment;
	
	/**
	 * An entry date for an allergen
	 */
	private Date allergenDate;
	
	public Integer getAllergyId() {
		return allergyId;
	}
	
	public void setAllergyId(Integer allergyId) {
		this.allergyId = allergyId;
	}
	
	public String getAllergenCodedName() {
		return allergenCodedName;
	}
	
	public void setAllergenCodedName(String allergenCodedName) {
		this.allergenCodedName = allergenCodedName;
	}
	
	public String getAllergenNonCodedName() {
		return allergenNonCodedName;
	}
	
	public void setAllergenNonCodedName(String allergenNonCodedName) {
		this.allergenNonCodedName = allergenNonCodedName;
	}
	
	public String getAllergenSeverity() {
		return allergenSeverity;
	}
	
	public void setAllergenSeverity(String allergenSeverity) {
		this.allergenSeverity = allergenSeverity;
	}
	
	public String getAllergenType() {
		return allergenType;
	}
	
	public void setAllergenType(String allergenType) {
		this.allergenType = allergenType;
	}
	
	public String getAllergenCodedReaction() {
		return allergenCodedReaction;
	}
	
	public void setAllergenCodedReaction(String allergenCodedReaction) {
		this.allergenCodedReaction = allergenCodedReaction;
	}
	
	public String getAllergenNonCodedReaction() {
		return allergenNonCodedReaction;
	}
	
	public void setAllergenNonCodedReaction(String allergenNonCodedReaction) {
		this.allergenNonCodedReaction = allergenNonCodedReaction;
	}
	
	public String getAllergenComment() {
		return allergenComment;
	}
	
	public void setAllergenComment(String allergenComment) {
		this.allergenComment = allergenComment;
	}
	
	public Date getAllergenDate() {
		return allergenDate;
	}
	
	public void setAllergenDate(Date allergenDate) {
		this.allergenDate = allergenDate;
	}
	
}
