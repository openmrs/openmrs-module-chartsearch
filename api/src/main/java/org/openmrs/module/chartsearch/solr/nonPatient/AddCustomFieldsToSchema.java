/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chartsearch.solr.nonPatient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains all functionalities needed to add an entry of a new field into schema.xml file
 */
public class AddCustomFieldsToSchema {
	
	/**
	 * Generates a field entry to be added to the schema.xml file that looks like: <field
	 * name="cc_filter_query" type="text_general" indexed="true" stored="true" required="false" />
	 * 
	 * @param fieldName, the field name to be added
	 * @param indexed, is the field to be indexed
	 * @param type, the datatype for the field
	 * @param stored, is the field to be stored
	 * @param required, is the field to be required
	 * @return generated field entry
	 */
	public static String generateAWellWrittenFieldEntry(String fieldName, String type, boolean indexed, boolean stored,
	                                                    boolean required) {
		String indexedString = "false";
		String storedString = "false";
		String requiredString = "false";
		if (indexed) {
			indexedString = "true";
		}
		if (stored) {
			storedString = "true";
		}
		if (required) {
			requiredString = "true";
		}
		
		//the two tabs at the start are for proper formatting of the text field after added to the file
		String fieldEntry = "\t\t<field name=\"" + fieldName + "\" type=\"" + type + "\" indexed=\"" + indexedString
		        + "\" stored=\"" + storedString + "\" required=\"" + requiredString + "\" />";
		return fieldEntry;
	}
	
	public static String generateAWellWrittenFieldEntry(List<String> fieldNames, String typeForAllFields,
	                                                    boolean indexedForAllFields, boolean storedForAllFields,
	                                                    boolean requiredForAllFields) {
		String indexedString = "false";
		String storedString = "false";
		String requiredString = "false";
		if (indexedForAllFields) {
			indexedString = "true";
		}
		if (storedForAllFields) {
			storedString = "true";
		}
		if (requiredForAllFields) {
			requiredString = "true";
		}
		String fieldEntry = "";
		for (int i = 0; i < fieldNames.size(); i++) {
			//the two tabs at the start are for proper formatting of the text field after added to the file
			fieldEntry += "\t\t<field name=\"" + fieldNames.get(i) + "\" type=\"" + typeForAllFields + "\" indexed=\""
			        + indexedString + "\" stored=\"" + storedString + "\" required=\"" + requiredString + "\" />\n";
		}
		return fieldEntry;
	}
	
	/**
	 * Reads the schema file line by line and edits it to add a new field entry
	 * 
	 * @param schemaFileLocation
	 * @param fieldEntry, the field entry line, use
	 *            {@link #generateAWellWrittenFieldEntry(String, String, boolean, boolean, boolean)}
	 * @return new lines of the file in a List
	 */
	public static void readSchemaFileLineByLineAndWritNewFieldEntry(String schemaFileLocation, String newSchemaFilePath,
	                                                                String fieldEntry) {
		//reading file line by line in Java using BufferedReader       
		FileInputStream fis = null;
		BufferedReader reader = null;
		
		try {
			fis = new FileInputStream(schemaFileLocation);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			System.out.println("Reading " + schemaFileLocation + " file line by line using BufferedReader");
			File newSchemaFile = new File(newSchemaFilePath);
			if (newSchemaFile.exists())
				newSchemaFile.delete();
			String line = reader.readLine();
			while (line != null) {
				FileWriter fileWritter = new FileWriter(newSchemaFile, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				System.out.println(line);
				//write to the file from here.
				if (line.equals("\t\t<!-- Fields from modules and other projects starts here -->")) {
					bufferWritter.write("\t\t<!-- Fields from modules and other projects starts here -->\n" + fieldEntry
					        + "\n");
					bufferWritter.close();
				} else {
					bufferWritter.write(line + "\n");
					bufferWritter.close();
				}
				
				line = reader.readLine();
			}
			
		}
		catch (FileNotFoundException ex) {
			Logger.getLogger(AddCustomFieldsToSchema.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch (IOException ex) {
			Logger.getLogger(AddCustomFieldsToSchema.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		finally {
			try {
				reader.close();
				fis.close();
			}
			catch (IOException ex) {
				Logger.getLogger(AddCustomFieldsToSchema.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		copyNewSchemaFileToPreviouslyUsed(schemaFileLocation, newSchemaFilePath);
	}
	
	/*public static void main(String[] args) {
		/*try {
			String data = "<field name=\"test_field\" type=\"text_general\" indexed=\"true\" stored=\"true\" required=\"false\" />";
			
			File file = new File("/home/k-joseph/Desktop/schema.xml");
			
			//if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			//true = append file
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();
			
			System.out.println("Done");
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		*/
	/*List<String> fieldNames = new ArrayList<String>();
	fieldNames.add("cc_test1");
	fieldNames.add("cc_test2");
	fieldNames.add("cc_test3");
	fieldNames.add("cc_test4");
	fieldNames.add("cc_test5");
	fieldNames.add("cc_test6");
	fieldNames.add("cc_test7_cc_test7_cc_test7");
	
	readSchemaFileLineByLineAndWritNewFieldEntry("/home/k-joseph/Desktop/schema.xml",
	    "/home/k-joseph/Desktop/new-schema.xml",
	    generateAWellWrittenFieldEntry(fieldNames, "general_text", true, true, false));
	copyNewSchemaFileToPreviouslyUsed("/home/k-joseph/Desktop/schema.xml", "/home/k-joseph/Desktop/new-schema.xml");
	}*/
	
	/**
	 * Overwrites the previous schema file with the newly generated
	 * 
	 * @param previousSchema currently used schema file
	 * @param newSchema to be used instead of the previous
	 */
	private static void copyNewSchemaFileToPreviouslyUsed(String previousSchema, String newSchema) {
		File previousSchemaFile = new File(previousSchema);
		File newSchemaFile = new File(newSchema);
		
		if (previousSchemaFile.exists() && newSchemaFile.exists()) {
			previousSchemaFile.delete();
			newSchemaFile.renameTo(previousSchemaFile);
			
			/**
			 * TODO there might be need to automatically edit the
			 * appdata/chartsearch/collection1/conf/schema.xml as well so as to avoid the need to
			 * restart the module, the other way to do this stuff is to automatically restart the
			 * chartsearch module
			 */
		}
	}
}
