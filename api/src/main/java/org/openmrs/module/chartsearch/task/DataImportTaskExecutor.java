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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.openmrs.module.chartsearch.IndexManagement;
import org.openmrs.module.chartsearch.Indexer;
import org.openmrs.module.chartsearch.Indexer.DIHStatus;
import org.springframework.core.task.TaskExecutor;

/**
 *
 */
public class DataImportTaskExecutor {
	private static Log log = LogFactory.getLog(DataImportTaskExecutor.class);
	
	private class DataImportTask implements Runnable {
		private int patientId;
		
		public DataImportTask(int patientId){
			this.patientId = patientId;
		}

		public void run() {
			try {
				while (indexer.checkStatus() != Indexer.DIHStatus.IDLE) {
					Thread.sleep(1000);					
				}
				indexer.indexPatientData(patientId);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				log.error("Error generated", e);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error("Error generated", e);
			}			
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
