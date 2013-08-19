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

import static org.apache.solr.handler.dataimport.DataImporter.IMPORT_CMD;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.Custom.SolrQueryInfo;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DataImportDaemon implements Runnable {

	private static final Logger log = LoggerFactory
			.getLogger(DataImportDaemon.class);

	private final BlockingQueue<SolrQueryInfo> queue;
	private final int id;
	private final DataImporter importer;
	private final NamedList initArgs;

	private boolean debugEnabled = true;

	public DataImportDaemon(BlockingQueue<SolrQueryInfo> queue, int id,
			DataImporter dataImporter, NamedList initArgs) {
		this.queue = queue;
		this.id = id;
		this.importer = dataImporter;
		this.initArgs = initArgs;
	}

	@Override
	public void run() {
		log.info("Daemon #{} is running", id);
		while (true) {
			try {
				SolrQueryInfo info = queue.take();
				handleRequest(info.getRequest(), info.getResponse());

			} catch (InterruptedException e) {
				log.error("The thread #{} is interrupted", id);
			}
		}
	}

	private void handleRequest(SolrQueryRequest req, SolrQueryResponse rsp) {
		SolrParams params = req.getParams();
		Integer patientId = params.getInt("personId");
		log.info("Daemon #{} taked from queue patient #{}", id, patientId);

		// TODO: figure out why just the first one is OK...
		ContentStream contentStream = null;
		Iterable<ContentStream> streams = req.getContentStreams();
		if (streams != null) {
			for (ContentStream stream : streams) {
				contentStream = stream;
				break;
			}
		}

		// TODO Default params
		RequestInfo requestParams = new RequestInfo(getParamsMap(params),
				contentStream);

		NamedList defaultParams = (NamedList) initArgs.get("defaults");

		String command = requestParams.getCommand();

		String message = StringUtils.EMPTY;

		if (DataImporter.FULL_IMPORT_CMD.equals(command)
				|| DataImporter.DELTA_IMPORT_CMD.equals(command)
				|| IMPORT_CMD.equals(command)) {

			try {
				importer.maybeReloadConfiguration(requestParams, defaultParams);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("Error generated", e);
			}
			UpdateRequestProcessorChain processorChain = req.getCore()
					.getUpdateProcessingChain(
							params.get(UpdateParams.UPDATE_CHAIN));
			UpdateRequestProcessor processor = processorChain.createProcessor(
					req, rsp);
			SolrResourceLoader loader = req.getCore().getResourceLoader();
			SolrWriter sw = getSolrWriter(processor, loader, requestParams, req);

			if (requestParams.isDebug()) {
				if (debugEnabled) {
					importer.runCmd(requestParams, sw);
					rsp.add("mode", "debug");
					rsp.add("documents",
							requestParams.getDebugInfo().debugDocuments);
					if (requestParams.getDebugInfo().debugVerboseOutput != null) {
						rsp.add("verbose-output",
								requestParams.getDebugInfo().debugVerboseOutput);
					}
				} else {
					message = DataImporter.MSG.DEBUG_NOT_ENABLED;
				}
			} else {			
				importer.runCmd(requestParams, sw);
			}
			log.info("Import finished in the daemon {}", id);
		} else if (DataImporter.RELOAD_CONF_CMD.equals(command)) {
			try {
				if (importer.maybeReloadConfiguration(requestParams,
						defaultParams)) {
					message = DataImporter.MSG.CONFIG_RELOADED;
				} else {
					message = DataImporter.MSG.CONFIG_NOT_RELOADED;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("Error generated", e);
			}
		}

		
		//TODO Do something visible for user with response
		rsp.add("importResponse", message);
		rsp.add("statusMessages", importer.getStatusMessages());

	}

	private SolrWriter getSolrWriter(final UpdateRequestProcessor processor,
			final SolrResourceLoader loader, final RequestInfo requestParams,
			SolrQueryRequest req) {

		return new SolrWriter(processor, req) {

			@Override
			public boolean upload(SolrInputDocument document) {
				try {
					return super.upload(document);
				} catch (RuntimeException e) {
					log.error("Exception while adding: " + document, e);
					return false;
				}
			}
		};
	}

	private Map<String, Object> getParamsMap(SolrParams params) {
		Iterator<String> names = params.getParameterNamesIterator();
		Map<String, Object> result = new HashMap<String, Object>();
		while (names.hasNext()) {
			String s = names.next();
			String[] val = params.getParams(s);
			if (val == null || val.length < 1)
				continue;
			if (val.length == 1)
				result.put(s, val[0]);
			else
				result.put(s, Arrays.asList(val));
		}
		return result;
	}

}
