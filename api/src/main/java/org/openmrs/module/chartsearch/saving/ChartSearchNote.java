/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.saving;

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

/**
 * Represents a user's observation or comments on a specific set of results after a search
 */
@Entity
@Table(name = "chartsearch_note")
public class ChartSearchNote extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "note_id")
	private Integer noteId;
	
	@Column(name = "comment", nullable = false)
	private String comment;
	
	@Column(name = "search_phrase", nullable = false)
	private String searchPhrase;
	
	@Column(name = "priority", nullable = false)
	private String priority = "LOW";
	
	@Column(name = "created_or_last_modified_at", nullable = false)
	private Date createdOrLastModifiedAt;
	
	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User noteOwner;
	
	@Column(name = "display_color", length = 10)
	private String displayColor;
	
	@Override
	public Integer getId() {
		return getNoteId();
	}
	
	@Override
	public void setId(Integer id) {
		setNoteId(id);
	}
	
	/**
	 * @see org.openmrs.BaseOpenmrsObject#getUuid()
	 *//*
	@Basic
	@Access(AccessType.PROPERTY)
	@Column(name = "uuid", length = 38, unique = true)
	@Override
	public String getUuid() {
		return super.getUuid();
	}*/
	
	public Integer getNoteId() {
		return noteId;
	}
	
	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getSearchPhrase() {
		return searchPhrase;
	}
	
	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}
	
	/**
	 * LOW(default)/HIGH severity, LOW priority are personal notes whereas HIGH priority are seen by
	 * other users
	 */
	public String getPriority() {
		return priority;
	}
	
	/**
	 * LOW(default)/HIGH severity, LOW priority are personal notes whereas HIGH priority are seen by
	 * other users
	 */
	public void setPriority(String priority) {
		if ("LOW".equals(priority) || "HIGH".equals(priority)) {
			this.priority = priority;
		} else {
			this.priority = "LOW";
		}
	}
	
	public Date getCreatedOrLastModifiedAt() {
		return createdOrLastModifiedAt;
	}
	
	public void setCreatedOrLastModifiedAt(Date createdOrLastModifiedAt) {
		this.createdOrLastModifiedAt = createdOrLastModifiedAt;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public User getNoteOwner() {
		return noteOwner;
	}
	
	public void setNoteOwner(User noteOwner) {
		this.noteOwner = noteOwner;
	}
	
	public String getDisplayColor() {
		return displayColor;
	}
	
	/**
	 * Supports only ten light colors that can better give background to black text
	 * 
	 * @param displayColor
	 */
	public void setDisplayColor(String displayColor) {
		if ("orange".equals(displayColor) || "yellow".equals(displayColor)|| "violet".equals(displayColor) || "lime".equals(displayColor)
		        || "beige".equals(displayColor) || "cyan".equals(displayColor) || "lightgreen".equals(displayColor)
		        || "deeppink".equals(displayColor) || "magenta".equals(displayColor) || "red".equals(displayColor)) {//don't persist the first option
			this.displayColor = displayColor;
		} else {
			this.displayColor = "white";
		}
	}
	
}
