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
package org.chartsearch.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.core.CloseHook;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.DataImportHandler;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.SolrWriter;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.RawResponseWriter;
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
	private final BlockingQueue<PatientInfo> queue = new LinkedBlockingQueue<PatientInfo>();
	private ExecutorService executorService;
	private final int THREADS_COUNT = 3;

	private String myName = "dataimport";

	@Override
	public void init(NamedList args) {
		super.init(args);

	}

	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {

		SolrParams params = req.getParams();
		Integer patientId = params.getInt("patientId");
		PatientInfo patientInfo = new PatientInfo(patientId);
		queue.put(patientInfo);

		rsp.add("patientId", patientId);
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
				//First attempt. Using reflection to instantiating DataImporter
				Constructor<DataImporter> constructor = DataImporter.class.getDeclaredConstructor(
						SolrCore.class, String.class);
				constructor.setAccessible(true);
				DataImporter dataImporter = constructor.newInstance(core,
						myName);
				executorService.execute(new DataImportDaemon(queue, i,
						dataImporter));
				log.info("Executed daemon #{}", i);
			} catch (Exception e){
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
