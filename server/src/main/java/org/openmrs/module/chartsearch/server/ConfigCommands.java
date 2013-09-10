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


/**
 *
 */
public class ConfigCommands {
	public static final String PATIENT_STATE = "patient-state"; 
	
	public static final String STATS = "stats";
	
	public static final String PRUNE = "prune";
	public static final String PRUNE_IDS = "ids"; 
	public static final String PRUNE_CLEAR_STRATEGY = "clear-strategy";  
	public static final String PRUNE_MAX_PATIENTS = "max-patients";
	public static final String PRUNE_AGO = "ago";    
	
	
	public class Labels {
		public static final String PATIENT_LAST_INDEX_TIME = "last index time";	
		public static final String CLEAR_STRATEGY = "clear strategy";
		public static final String CLEARED_PATIENTS_COUNT = "cleared patients count";
		public static final String DAEMON_ID = "daemon id";
		public static final String DAEMON_STATUS = "daemon status";
		public static final String DAEMON_SUCCESS_COUNT = "daemon success count";
		public static final String DAEMON_FAIL_COUNT = "daemon fail count";
		public static final String DAEMONS_STATE = "daemons state";
		public static final String IDLE = "idle";
		public static final String BUSY = "busy";
	}
}
