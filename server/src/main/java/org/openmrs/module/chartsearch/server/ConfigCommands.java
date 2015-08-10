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
	
	public static final String SHANGE_DAEMONS_COUNT = "change-daemons-count";
	
	public static final String DAEMONS_COUNT = "daemons-count";
	
	public class Labels {
		
		public static final String PATIENT_LAST_INDEX_TIME = "last index time";
		
		public static final String CLEAR_STRATEGY = "clear strategy";
		
		public static final String CLEARED_PATIENTS_COUNT = "cleared patients count";
		
		public static final String DAEMON_ID = "daemon id";
		
		public static final String DAEMON_STATUS = "daemon status";
		
		public static final String DAEMON_SUCCESS_COUNT = "daemon success count";
		
		public static final String DAEMON_FAIL_COUNT = "daemon fail count";
		
		public static final String DAEMON_STATES = "daemon states";
		
		public static final String IDLE = "idle";
		
		public static final String BUSY = "busy";
		
		public static final String ERROR = "error";
	}
}
