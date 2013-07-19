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
package org.openmrs.module.chartsearch;


import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.util.OpenmrsUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 * 
 * @startuml
Actor User

participant CSM
participant Database
participant DIH
participant SOLR

User -> CSM : open patient dashboard
activate CSM 
    CSM -> DIH: import data for selected patient
deactivate CSM
activate DIH        
   	DIH -> Database : get changed data for selected patient
    activate Database
       	Database -> DIH : return changed data                
    deactivate Database
    DIH -> SOLR : update index
deactivate DIH 
activate SOLR
deactivate SOLR
    
@enduml
 * 
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
	}
	
	
		
}
