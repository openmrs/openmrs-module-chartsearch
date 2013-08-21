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

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CloseHook;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.Custom.SolrQueryInfo;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 *
 */
public class CSDataImportHandler extends RequestHandlerBase implements
		SolrCoreAware {

	private static final Logger log = LoggerFactory
			.getLogger(CSDataImportHandler.class);
	private final BlockingQueue<SolrQueryInfo> queue = new LinkedBlockingQueue<SolrQueryInfo>();
	private ExecutorService executorService;
	private final int THREADS_COUNT = 3;

	private String myName = "csdataimport";

	@Override
	public void init(NamedList args) {
		super.init(args);

	}

	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {

		queue.put(new SolrQueryInfo(req, rsp));
		SolrParams params = req.getParams();
		Integer personId = params.getInt("personId");

		rsp.add("personId", personId);
		rsp.setHttpCaching(false);

	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void inform(SolrCore core) {

		// hack to get the name of this handler
		for (Map.Entry<String, SolrRequestHandler> e : core
				.getRequestHandlers().entrySet()) {
			SolrRequestHandler handler = e.getValue();
			// this will not work if startup=lazy is set
			if (this == handler) {
				String name = e.getKey();
				if (name.startsWith("/")) {
					myName = name.substring(1);
				}
				// some users may have '/' in the handler name. replace with
				// '_'
				myName = myName.replaceAll("/", "_");
			}
		}

		ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(
				"CSDataImport Daemon #%d").build();
		executorService = Executors.newFixedThreadPool(THREADS_COUNT, factory);
		for (int i = 0; i < THREADS_COUNT; i++) {
			try {
				String importerName = myName;//String.format("%s - #%d", myName, i);
				executorService.execute(new DataImportDaemon(queue, i,
						new DataImporter(core, importerName), initArgs));
				log.info("Executed daemon #{} with dataimporter #{}", i, importerName);
			} catch (Exception e) {
				log.error("Error in DataImporter instantiating", e);
			}

		}

		core.addCloseHook(new CloseHook() {

			@Override
			public void preClose(SolrCore core) {
				executorService.shutdownNow();
				log.info("ExecutorService was shutdown");

			}

			@Override
			public void postClose(SolrCore core) {
				// TODO Auto-generated method stub
			}
		});
	}
}
