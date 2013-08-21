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
package org.openmrs.module.chartsearch.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 */
public class EmbeddedSolrServerCreator extends SolrServerCreator {

	private static final Logger log = LoggerFactory
			.getLogger(EmbeddedSolrServerCreator.class);

	private SolrServer solrServer;

	private final EmbeddedSolrProperties properties;
	

	public EmbeddedSolrServerCreator(EmbeddedSolrProperties properties) {
		this.properties = properties;
	}

	@Override
	public SolrServer createSolrServer() {

		// Get the solr home folder
		// Tell solr that this is our home folder
		System.setProperty("solr.solr.home", properties.getSolrHome());

		log.info(String.format("solr.solr.home: %s", properties.getSolrHome()));

		// If user has not setup solr config folder, set a default one
		String configFolder = properties.getSolrHome() + File.separatorChar + "collection1"
				+ File.separatorChar + "conf";
		// if (!new File(configFolder).exists()) {
		URL url = getClass().getClassLoader().getResource("collection1/conf");
		File file = new File(url.getFile());
		try {
			FileUtils.copyDirectoryToDirectory(file, new File(properties.getSolrHome()
					+ File.separatorChar + "collection1"));
			setDataImportConnectionInfo(configFolder);
		} catch (IOException e) {
			log.error("Failed to copy Solr config folder", e);
		} catch (Exception e) {
			log.error("Failed to set dataImport connection info", e);
		}
		CoreContainer.Initializer initializer = new CoreContainer.Initializer();
		CoreContainer coreContainer;
		try {
			coreContainer = initializer.initialize();
			solrServer = new EmbeddedSolrServer(coreContainer, "");
			return solrServer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		}

	}

	private void setDataImportConnectionInfo(String configFolder)
			throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(getClass().getClassLoader()
				.getResourceAsStream("collection1/conf/data-config.xml"));
		Element node = (Element) doc.getElementsByTagName("dataSource").item(0);
		
		node.setAttribute("url", properties.getDbUrl());
		node.setAttribute("user", properties.getDbUser());
		node.setAttribute("password", properties.getDbPassword());

		String xml = doc2String(doc);
		File file = new File(configFolder + File.separatorChar
				+ "data-config.xml");
		FileUtils.writeStringToFile(file, xml, "UTF-8");
	}

	public static String doc2String(Node doc) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		StringWriter outStream = new StringWriter();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(outStream);
		transformer.transform(source, result);
		return outStream.getBuffer().toString();
	}

}
