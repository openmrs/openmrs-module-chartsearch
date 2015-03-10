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
package org.openmrs.module.chartsearch.solr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class SolrjIndexer {
	
	private SolrServer solrServer;
	
	private Collection _docs = new ArrayList();
	
	/**
	 * ***************************SQL processing here
	 */
	public void doSqlDocuments() throws SQLException {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("Driver Loaded......");
			
			con = DriverManager.getConnection("jdbc:mysql://192.168.1.103:3306/test?" + "user=testuser&password=test123");
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select id,title,text from test");
			
			while (rs.next()) {
				// DO NOT move this outside the while loop
				// or be sure to call doc.clear()
				SolrInputDocument doc = new SolrInputDocument();
				String id = rs.getString("id");
				String title = rs.getString("title");
				String text = rs.getString("text");
				
				doc.addField("id", id);
				doc.addField("title", title);
				doc.addField("text", text);
				
				_docs.add(doc);
				
				// Completely arbitrary, just batch up more than one
				// document for throughput!
				if (_docs.size() > 1000) {
					// Commit within 5 minutes.
					UpdateResponse resp = solrServer.add(_docs, 300000);
					if (resp.getStatus() != 0) {
						System.out.println("Some horrible error has occurred, status is: " + resp.getStatus());
					}
					_docs.clear();
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (con != null) {
				con.close();
			}
		}
	}
	
	public void indexDocemumentsFromSQL(String sqlQueury) {
		
	}
}
