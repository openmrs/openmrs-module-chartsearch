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
				SolrQueryInfo info = queue.take();
				chartSearchIndexUpdater.handleRequest(info.getRequest(), info.getResponse());				
			}
			catch (InterruptedException e) {
				log.info("The thread #{} is interrupted", id);
				Thread.currentThread().interrupt();
			}
		}
	}
	
	
	
}
