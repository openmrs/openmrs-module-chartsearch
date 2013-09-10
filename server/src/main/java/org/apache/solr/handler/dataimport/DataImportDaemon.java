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
package org.apache.solr.handler.dataimport;


import java.util.concurrent.BlockingQueue;

import org.apache.solr.handler.dataimport.custom.SolrQueryInfo;
import org.openmrs.module.chartsearch.server.ConfigCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DataImportDaemon implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(DataImportDaemon.class);
	
	private final BlockingQueue<SolrQueryInfo> queue;
	
	private final int id;

	private final ChartSearchIndexUpdater chartSearchIndexUpdater;
	
	//private SolrQueryInfo solrQueryInfo;
	
	private int successCount = 0;
	
	private int failCount = 0;
	
	private String status; 
	
	public DataImportDaemon(int id, BlockingQueue<SolrQueryInfo> queue, ChartSearchIndexUpdater chartSearchIndexUpdater) {
		this.queue = queue;
		this.id = id;
		this.chartSearchIndexUpdater = chartSearchIndexUpdater;
	}
	
	@Override
	public void run() {
		log.info("Daemon #{} is running", id);
		while (!(Thread.currentThread().isInterrupted())) {
			try {
				status = ConfigCommands.Labels.IDLE;
				SolrQueryInfo info = queue.take();
				status = ConfigCommands.Labels.BUSY;
				chartSearchIndexUpdater.handleRequest(info.getRequest(), info.getResponse());
				//solrQueryInfo = info;
				
				//TODO check success or fail
				successCount++;
			}
			catch (InterruptedException e) {
				log.info("The thread #{} is interrupted", id);
				Thread.currentThread().interrupt();
			}
			catch (Exception e){
				log.error("Exception");
			}
		}
	}

	public String getStatus() {
		return status;
    }

	public int getSuccessCount() {
		//TODO add check success or fail
	    return successCount;
    }

	public int getFailCount() {
		//TODO add check success or fail
	    return failCount;
    }

	public int getId() {
	    return id;
    }
	
	
	
}
