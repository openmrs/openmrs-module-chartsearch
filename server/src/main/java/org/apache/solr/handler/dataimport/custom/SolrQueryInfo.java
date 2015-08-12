/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.apache.solr.handler.dataimport.custom;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 *
 */
public final class SolrQueryInfo {
	
	private final SolrQueryRequest request;
	
	private final SolrQueryResponse response;
	
	//TODO do not store response
	public SolrQueryInfo(SolrQueryRequest request, SolrQueryResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public SolrQueryRequest getRequest() {
		return request;
	}
	
	public SolrQueryResponse getResponse() {
		return response;
	}
	
}
