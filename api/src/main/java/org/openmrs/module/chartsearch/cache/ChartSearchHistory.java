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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Patient;
import org.openmrs.User;

@Entity
@Table(name = "chartsearch_history")
@SuppressWarnings("serial")
public class ChartSearchHistory extends BaseOpenmrsObject implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "search_id")
	private Integer searchId;
	
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
	
	/**
	 * @see org.openmrs.BaseOpenmrsObject#getUuid()
	 */
	@Basic
	@Access(AccessType.PROPERTY)
	@Column(name = "history_uuid", length = 38, unique = true)
	@Override
	public String getUuid() {
		return super.getUuid();
	}
	
	@Override
	public Integer getId() {
		return getSearchId();
	}
	
	@Override
	public void setId(Integer id) {
		setSearchId(id);
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
