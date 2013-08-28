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
 *PATIENT_INFO_TIMEOUT
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.apache.solr.handler.dataimport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CloseHook;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.custom.ChartSearchDataImportProperties;
import org.apache.solr.handler.dataimport.custom.IndexSizeManager;
import org.apache.solr.handler.dataimport.custom.PatientInfoCache;
import org.apache.solr.handler.dataimport.custom.PatientInfoHolder;
import org.apache.solr.handler.dataimport.custom.PatientInfoProviderCSVImpl;
import org.apache.solr.handler.dataimport.custom.SolrQueryInfo;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.UpdateHandler;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 *
 */
public class ChartSearchDataImportHandler extends RequestHandlerBase implements SolrCoreAware {
	
	private static final Logger log = LoggerFactory.getLogger(ChartSearchDataImportHandler.class);
	
	private final BlockingQueue<SolrQueryInfo> queue = new LinkedBlockingQueue<SolrQueryInfo>();
	
	private final PatientInfoCache cache = new PatientInfoCache(new PatientInfoProviderCSVImpl());
	
	private final PatientInfoHolder patientInfoHolder = new PatientInfoHolder(cache);
	
	private ExecutorService executorService;
	
	private ChartSearchDataImportProperties chartSearchProperties;
	
	private String myName = "csdataimport";

	private ScheduledExecutorService patientInfoScheduledExecutorService;

	private ScheduledExecutorService indexSizeManagerScheduledExecutorService;
	
	@Override
	public void init(NamedList args) {
		super.init(args);
	}
	
	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
		
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
		for (Map.Entry<String, SolrRequestHandler> e : core.getRequestHandlers().entrySet()) {
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
		
		chartSearchProperties = new ChartSearchDataImportProperties(myName, core.getResourceLoader().getConfigDir());
		
		runDataImportDaemons(core, chartSearchProperties.getDaemonsCount());
		
		runScheduledIndexSizeManager(core, chartSearchProperties.getIndexMaxPatients(),
		    chartSearchProperties.getIndexSizeManagerTimeout());
		
		runScheduledPatientInfoUpdates(core, chartSearchProperties.getPatientInfoTimeout());
		
		core.addCloseHook(new CloseHook() {
			
			@Override
			public void preClose(SolrCore core) {
				executorService.shutdownNow();
				indexSizeManagerScheduledExecutorService.shutdownNow();
				patientInfoScheduledExecutorService.shutdownNow();
				log.info("ExecutorServices were shutdown");				
			}
			
			@Override
			public void postClose(SolrCore core) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void runDataImportDaemons(SolrCore core, int daemonsCount) {
		ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("ChartSearchDataImport Daemon #%d").build();
		executorService = Executors.newFixedThreadPool(daemonsCount, factory);
		for (int i = 0; i < daemonsCount; i++) {
			try {
				String importerName = myName;// String.format("%s - #%d",
				                             // myName, i);
				executorService.execute(new DataImportDaemon(i, queue, new ChartSearchIndexUpdater(new DataImporter(core,
				        importerName), initArgs, patientInfoHolder)));
				log.info("Executed daemon #{} with dataimporter #{}", i, importerName);
			}
			catch (Exception e) {
				log.error("Error in DataImporter instantiating", e);
			} 
			
		}
	}
	
	private void runScheduledIndexSizeManager(SolrCore core, int indexMaxpatients, int timeout) {
		UpdateHandler updateHandler = core.getUpdateHandler();
		SolrQueryRequest request = new SolrQueryRequestBase(
		                                                    core, new MapSolrParams(new HashMap<String, String>())) {};
		final IndexSizeManager indexSizeManager = new IndexSizeManager(updateHandler, request, cache, indexMaxpatients);
		
		indexSizeManagerScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		indexSizeManagerScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				indexSizeManager.clearIndex();
				
			}
		}, 10, timeout, TimeUnit.SECONDS);
	}
	
	private void runScheduledPatientInfoUpdates(SolrCore core, int timeout) {
		patientInfoScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		patientInfoScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				cache.save();
				
			}
		}, 10, timeout, TimeUnit.SECONDS);
		
	}
}
