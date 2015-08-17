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
import org.openmrs.module.appointmentscheduling.Appointment;

/**
 * Represents this module's customized {@link SolrDocument} for a Patient placed {@link Appointment} for
 * medical attention
 */
public class AppointmentItem extends ChartListItem {
	
	/**
	 * Purpose for the appointment request
	 */
	private String reason;
	
	/**
	 * Unique numeric Identify for an appointment
	 */
	private Integer appointmentId;
	
	/**
	 * Current state of an appointment
	 */
	private String status;
	
	/**
	 * Starting time for an appointment
	 */
	private Date start;
	
	/**
	 * Ending time for an appointment
	 */
	private Date end;
	
	/**
	 * Appointment Category
	 */
	private String type;
	
	/**
	 * Description of an Appointment Category
	 */
	private String typeDesc;
	
	/**
	 * A reason for an appointment whose status is Closed/Canceled
	 */
	private String cancelReason;
	
	/**
	 * Provider with whom the patient is to encounter or meet during the appointment
	 */
	private String provider;
	
	/**
	 * Place or Location of preference where to meet for an appointment
	 */
	private String location;
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Integer getAppointmentId() {
		return appointmentId;
	}
	
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeDesc() {
		return typeDesc;
	}
	
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	public String getCancelReason() {
		return cancelReason;
	}
	
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
}
