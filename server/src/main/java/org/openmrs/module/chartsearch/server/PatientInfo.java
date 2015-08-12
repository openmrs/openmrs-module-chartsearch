/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.server;

import java.util.Date;

/**
 *
 */
public class PatientInfo {
	
	private final Integer patientId;
	
	private final Date lastIndexTime;
	
	public PatientInfo(Integer patientId, Date lastIndexTime) {
		this.patientId = patientId;
		this.lastIndexTime = new Date(lastIndexTime.getTime());
	}
	
	public PatientInfo(Integer patientId, Long lastIndexTime) {
		this.patientId = patientId;
		this.lastIndexTime = new Date(lastIndexTime);
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public Date getLastIndexTime() {
		return new Date(lastIndexTime.getTime());
	}
	
	@Override
	public String toString() {
		return "PatientInfo [patientId=" + getPatientId() + ", lastIndexTime=" + getLastIndexTime() + "]";
	}
	
}
