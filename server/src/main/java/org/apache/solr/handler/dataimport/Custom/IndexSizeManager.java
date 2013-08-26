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
package org.apache.solr.handler.dataimport.custom;

import java.io.IOException;

import org.apache.solr.handler.UpdateRequestHandler;
import org.apache.solr.handler.dataimport.SolrWriter;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IndexSizeManager {
	
	public static Logger log = LoggerFactory.getLogger(IndexSizeManager.class);
	
	private final UpdateHandler handler;
	
	public IndexSizeManager(UpdateHandler handler) {
		this.handler = handler;
		
	}
	
	public void clearIndex() {
		DeleteUpdateCommand delCmd = new DeleteUpdateCommand(null);
		delCmd.query = "*:*";
		CommitUpdateCommand commitCmd = new CommitUpdateCommand(null, true);
		try {
			handler.deleteByQuery(delCmd);
			handler.commit(commitCmd);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error generated", e);
		}
		
	}
	
}
