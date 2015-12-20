/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.chartsearch.solr.ChartSearchIndexer;
import org.openmrs.module.chartsearch.solr.SolrManagement;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class ChartSearchActivator extends BaseModuleActivator {

	protected Log log = LogFactory.getLog(getClass());

	/**
	 * @see BaseModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Chart Search Module");
	}

	/**
	 * @see BaseModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Chart Search Module refreshed");
	}

	/**
	 * @see BaseModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Chart Search Module");
	}

	/**
	 * @see BaseModuleActivator#started()
	 */
	public void started() {
		log.info("Chart Search Module started");

		ChartSearchIndexer indexer = getComponent(ChartSearchIndexer.class);
		indexer.getStatistics();
	}

	/**
	 * @see BaseModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Chart Search Module");
	}

	/**
	 * @see BaseModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Chart Search Module stopped");
		SolrManagement solrManagement = getComponent(SolrManagement.class);
		solrManagement.shutdown();
	}

	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}

}
