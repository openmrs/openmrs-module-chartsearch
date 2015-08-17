/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openmrs.Patient;
import org.openmrs.User;

/**
 * Handles a (to be) stored search phrase that can later on be used for re-searching for it etc
 */
@Entity
@Table(name = "chartsearch_history")
public class ChartSearchHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "search_id")
	private Integer searchId;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38)
	private String uuid = UUID.randomUUID().toString();
	
	/**
	 * Phrase or text searched for
	 */
	@Column(name = "search_phrase", nullable = false)
	private String searchPhrase;
	
	@Column(name = "last_searched_at", nullable = false)
	private Date lastSearchedAt;
	
	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User historyOwner;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getSearchId() {
		return searchId;
	}
	
	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}
	
	public String getSearchPhrase() {
		return searchPhrase;
	}
	
	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}
	
	public Date getLastSearchedAt() {
		return lastSearchedAt;
	}
	
	public void setLastSearchedAt(Date lastSearchedAt) {
		this.lastSearchedAt = lastSearchedAt;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public User getHistoryOwner() {
		return historyOwner;
	}
	
	public void setHistoryOwner(User historyOwner) {
		this.historyOwner = historyOwner;
	}
	
}
