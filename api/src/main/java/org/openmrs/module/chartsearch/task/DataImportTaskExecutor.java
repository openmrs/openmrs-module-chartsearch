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
package org.openmrs.module.chartsearch.task;

import org.openmrs.module.chartsearch.Indexer;
import org.springframework.core.task.TaskExecutor;

/**
 *
 */
public class DataImportTaskExecutor {
	private class DataImportTask implements Runnable {
		private int patientId;
		
		public DataImportTask(int patientId){
			this.patientId = patientId;
		}

		public void run() {
			indexer.indexPatientData(patientId);
		}

	}
	
	private TaskExecutor taskExecutor;	
	private Indexer indexer;
	
	public DataImportTaskExecutor(TaskExecutor taskExecutor, Indexer indexer){
		this.taskExecutor = taskExecutor;
		this.indexer = indexer;
	}
	
	public void indexPatientData(int patientId){
		taskExecutor.execute(new DataImportTask(patientId));
	}


}
