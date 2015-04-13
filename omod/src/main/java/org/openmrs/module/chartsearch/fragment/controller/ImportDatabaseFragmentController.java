package org.openmrs.module.chartsearch.fragment.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.chartsearch.SearchProjectAccess;
import org.openmrs.module.chartsearch.solr.SolrUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ImportDatabaseFragmentController {
	
	SearchProjectAccess access = new SearchProjectAccess();
	
	public void controller(FragmentModel model) {
		model.addAttribute("existingDbs", access.getAllExistingDatabases());
		model.addAttribute("existingPersonalDBs", access.getPersonalDatabases());
	}
	
	/**
	 * Upload single file using Spring Controller
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject uploadSQLSourceFile(@RequestParam("databaseName") String name,
	                                      @RequestParam("sqlDatabaseFilePath") String file) {
		JSONObject json = new JSONObject();
		String message = null;
		boolean wasSuccessfull = false;
		if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(file)) {
			File sqlPath = new File(file);
			if (!sqlPath.exists()) {
				message = "SQL Source file path specified was wrong or mis-typed: Ensure that it's right and Retry";
			} else if (!file.endsWith(".sql")) {
				message = "The file provide was not an SQL source file, provide another Path whose file ends with .sql";
			} else {
				access.createAndDumpToNonExistingDatabase(name, file);
				String backUpRoot = SolrUtils.getEmbeddedSolrProperties().getSolrHome() + File.separator + "backup";
				File backUpLocation = new File(backUpRoot);
				if (!backUpLocation.exists()) {
					backUpLocation.mkdir();
				}
				String backUpDBSources = backUpRoot + File.separator + "dbSources";
				File backUpSQL = new File(backUpDBSources);
				if (!backUpSQL.exists()) {
					backUpSQL.mkdir();
				}
				File dbSourcesLocationsFile = new File(backUpDBSources + File.separator + "databasesSources.txt");
				try {
					if (!dbSourcesLocationsFile.exists()) {
						FileUtils.writeStringToFile(dbSourcesLocationsFile,
						    "This File Contains paths to all SQL Source Files ran on newly added databases\n\n");
					}
					FileWriter writer = new FileWriter(dbSourcesLocationsFile, true);
					
					writer.write(file + " Ran on: " + name + " Database At: " + new Date() + "\n");
					writer.close();
					wasSuccessfull = true;
					message = "You have successfully imported and installed a new database: <b>" + name + "</br>";
				}
				catch (IOException e) {
					System.out.println("Error generated" + e);
				}
			}
		}
		List allDbs = access.getAllExistingDatabases();
		String[] existingDbs = new String[allDbs.size()];
		List allPDbs = access.getPersonalDatabases();
		String[] existingPDbs = new String[allPDbs.size()];
		
		List allSPs = access.getExistingSearchProjectNames();
		String[] existingSPs = new String[allSPs.size()];
		
		existingSPs = (String[]) allSPs.toArray(existingSPs);
		
		existingDbs = (String[]) allDbs.toArray(existingDbs);
		existingPDbs = (String[]) allPDbs.toArray(existingPDbs);
		
		json.put("wasSuccessfull", wasSuccessfull);
		json.put("message", message);
		json.put("databases", existingDbs);
		json.put("personalDatabases", existingPDbs);
		json.put("projectNames", existingSPs);
		
		return json;
	}
	
	public JSONObject fetchTablesAndColumnsOfADatabase(@RequestParam("selectedDatabase") String selectedDB) {
		JSONObject jsonObj = null;
		
		if (StringUtils.isNotBlank(selectedDB)) {
			jsonObj = access.getTablesAndColumnsOfDatabase(selectedDB);
		}
		
		return jsonObj;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject deletePreviouslyImportedDatabase(@RequestParam("selectedDatabase") String dbName) {
		JSONObject json = new JSONObject();
		if (StringUtils.isNotBlank(dbName)) {
			access.deleteImportedDatabase(dbName);
			json.put("message", "Successfully Deleted Database Named: <b>" + dbName + "</b>");
		}
		List allDbs = access.getAllExistingDatabases();
		String[] existingDbs = new String[allDbs.size()];
		List allPDbs = access.getPersonalDatabases();
		String[] existingPDbs = new String[allPDbs.size()];
		
		existingDbs = (String[]) allDbs.toArray(existingDbs);
		existingPDbs = (String[]) allPDbs.toArray(existingPDbs);
		
		json.put("databases", existingDbs);
		json.put("personalDatabases", existingPDbs);
		
		return json;
	}
}
