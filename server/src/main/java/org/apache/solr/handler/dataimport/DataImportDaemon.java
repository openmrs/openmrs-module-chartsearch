/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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
				log.info("Import started in daemon #{}", id);
				chartSearchIndexUpdater.handleRequest(info.getRequest(), info.getResponse());
				log.info("Import finished in daemon #{}", id);
			}
			catch (InterruptedException e) {
				log.info("The import daemon #{} is interrupted", id);
				Thread.currentThread().interrupt();
			}
			catch (Exception e) {
				log.error("Exception");
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public ChartSearchIndexUpdater getIndexUpdater() {
		return chartSearchIndexUpdater;
	}
	
}
