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
