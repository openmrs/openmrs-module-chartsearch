<h2>Importing your own Database</h2>
Define your database Name and upload a .sql source file that contains your database dump file;
<br />
NOTE:<br />
1. Ensure that your sql file has no CREATE and DROP database queries<br />
2. Provide a valida SQL database Name

<br /><br />

<p id="failed-to-upload-sql">
</p>
<p id="upload-sql-feedback">
</p>

<form id="upload-sql-db-file" enctype="multipart/form-data">
	<p>		
		<input type="text" placeholder="Path to your SQL Source (*.sql) file on this (server/computer running openmrs) system" name="sqlDatabaseFilePath" id="sql-db-to-upload">
		<input name="databaseName" id="database_name" placeholder="Database Name" type="text"></input>
		<input type="submit" id="submit-sql-upload" value="Import Database"></input>
    </p>
</form>

<h2>Manage Already Imported Databases</h2>
An abstract view of your previously added database structures and Ability to un-install/remove any
<br />
<br />
<p>
	<select id="installed-databases">
		<option value="" name="selectedDatabase">Choose Database</option>
			<div id="imported-databases-options">
				<% if (existingPersonalDBs) { %>
					<% existingPersonalDBs.each { existingPersonalDB -> %>
		    			<option class="installed-database" value="$existingPersonalDB">$existingPersonalDB</option>
					<% } %>
		    	<% } %>
		    </div>
	</select>
</p>
<br />
<p>
<form id="delete-database-form">
	<div id="upload-sql-feedback"></div>
	<input type="submit" id="delete-database" value="Delete This Database"></input>
</form>
</p>

<script type="text/javascript">
    var jq = jQuery;
    var existingDatabases;

	jq(document).ready(function() {
		jq("#delete-database").hide();
	
	    jq("#submit-sql-upload").click(function(event) {
	    	validateImportSQLForm();
	    	return false;
	    });
	    
	    jq("#delete-database").click(function(event) {
	    	var yesOrNo = confirm("Are you sure you want to delete this Database!");
	    	if(yesOrNo) {
	    		deleteSelectedDatabase();
	    	} else {
	    		//alert("No action taken"); Do nothing
	    	}
	    	return false;
	    });
	    
	    jq('#installed-databases').change(function() {
	        //getTablesAndColumnsOfDatabase();
	        jq("#upload-sql-feedback").html("");
	        
	        var selectedDatabase = jq("#installed-databases").val();
		    if(selectedDatabase) {
	        	jq("#delete-database").show()
	        } else {
	        	jq("#delete-database").hide();
	        }
	    });
	
		function validateImportSQLForm() {
			var dbName = jq("#database_name").val();
			var sqlFileToUpload = jq("#sql-db-to-upload").val();
			
			if (sqlFileToUpload != "" && dbName != "") {
				jq("#upload-sql-feedback").html("");
				jq("#failed-to-upload-sql").html("");
				
				var eDBs = "$existingDbs";
				var var1 = eDBs.split("[");
				var var2 = var1[1].split("]");
				
				if(!existingDatabases) {
					existingDatabases = var2[0].split(", ");
				}
				var dbExistsAlready = false;
				var wrongDatabaseName = false;
				
				for(i = 0; i < existingDatabases.length; i++) {
					if (existingDatabases[i] == dbName) {
						dbExistsAlready = true;
					}
				}
				
				var noSpaceIndex = dbName.indexOf(" ");
				var no
				
				if(noSpaceIndex > -1 || dbName.match(/[A-Z]/)) {
					wrongDatabaseName = true;
				}
				
				if(dbExistsAlready) {
					jq("#failed-to-upload-sql").html("The Entered Database Name: <b> " + dbName + "</b> Already Exists, Try Another one");
				} else if(wrongDatabaseName) {
					jq("#failed-to-upload-sql").html("The Database Name provided is of wrong convention: use only lower case and underscore between words and no spaces");
				}else {
					jq("#failed-to-upload-sql").html("");
					if(sqlFileToUpload.match(/.sql\$/)) {
						uploadSQLSourceDBFile(dbName, sqlFileToUpload);
					} else {
						jq("#failed-to-upload-sql").html("Your provided path needs to be of a .sql file");
					}
				}
			} else {
				jq("#failed-to-upload-sql").html("Please Add a .sql file path to Upload and provide a Database Name");
			}
		}
		
		function uploadSQLSourceDBFile(dbName, sqlPath) {
			jq("#submit-sql-upload").prop("disabled", true);
			jq("#upload-sql-feedback").html('<img class="search-spinner" src="/openmrs/ms/uiframework/resource/uicommons/images/spinner.gif">');
			
			jq.ajax({
				type: "POST",
		        url: "${ ui.actionLink('uploadSQLSourceFile') }",
		        data: {"databaseName":dbName , "sqlDatabaseFilePath":sqlPath},
		        dataType: "json",
		        success: function(feedback) {
		        	var updatedDatabase = '<option value="" name="selectedDatabase">Choose Database</option>';
		        	var existingPersonalDatabases = feedback.personalDatabases;
		        	
		        	if(feedback.wasSuccessfull) {
		        		jq("#failed-to-upload-sql").html("");
		        		jq("#upload-sql-feedback").html(feedback.message);
		        		jq("#database_name").val("");
		        		jq("#sql-db-to-upload").val("");
		        		existingDatabases = feedback.databases;
		        		
		        		for(i = 0; i < existingPersonalDatabases.length; i++) {
		        			updatedDatabase += '<option class="installed-database" value="' + existingPersonalDatabases[i] + '">' + existingPersonalDatabases[i] + '</option>';
		        		}
		        		
		        		jq("#installed-databases").empty().append(updatedDatabase);
		        		jq("#installed-databases2").empty().append(updatedDatabase);
		        		jq("#submit-sql-upload").prop("disabled", false);
		        	} else {
		        		jq("#upload-sql-feedback").html("");
		        		jq("#failed-to-upload-sql").html(feedback.message);
		        	}
		        },
		        error: function(e) {
		        	jq("#upload-sql-feedback").html("");
		        	jq("#failed-to-upload-sql").html("Something went wrong, please check the server logs for the stack trace");
		        }
			});
	    }
	    
	    function getTablesAndColumnsOfDatabase() {
	    	jq("#installed-databases").prop("disabled", true);
	    	var selectedDatabase = jq("#installed-databases").val();
		    if(selectedDatabase) {
		    	jq.ajax({
					type: "POST",
			        url: "${ ui.actionLink('fetchTablesAndColumnsOfADatabase') }",
			        data: {"selectedDatabase":selectedDatabase},
			        dataType: "json",
			        success: function(tabsAndCols) {
			        	jq("#installed-databases").prop("disabled", false);
			        	alert(tabsAndCols);
			        },
			        error: function(e) {
			        	alert(e);
			        }
				});
			} else {
				jq("#database-details").html("");
	        	jq("#delete-database").hide();
			}
	    }
	    
	    function deleteSelectedDatabase() {
	    	var selectedDatabase = jq("#installed-databases").val();
		    if(selectedDatabase) {
		    	jq.ajax({
					type: "POST",
			        url: "${ ui.actionLink('deletePreviouslyImportedDatabase') }",
			        data: {"selectedDatabase":selectedDatabase},
			        dataType: "json",
			        success: function(feedback) {
			        	var updatedDatabase = '<option value="" name="selectedDatabase">Choose Database</option>';
		        		var existingPersonalDatabases = feedback.personalDatabases;
		        		
		        		existingDatabases = feedback.databases;
		        		
		        		for(i = 0; i < existingPersonalDatabases.length; i++) {
		        			updatedDatabase += '<option class="installed-database" value="' + existingPersonalDatabases[i] + '">' + existingPersonalDatabases[i] + '</option><br />';
		        		}
		        	
		        		jq("#upload-sql-feedback").html(feedback.message);
		        		jq("#installed-databases").empty().append(updatedDatabase);
		        		jq("#installed-databases2").empty().append(updatedDatabase);
		        		jq("html, body").animate({scrollTop: 0 }, "slow");
		        		jq("#delete-database").hide();
		        		//alert"You have successfully deleted Database Named: " + selectedDatabase);
			        },
			        error: function(e) {
			        	
			        }
				});
			} else {
				jq("#database-details").html("");
	        	jq("#delete-database").hide();
			}
	    }
	});
</script>

<style>
	#failed-to-upload-sql {
		color:red;
	}
	
	#upload-sql-feedback {
		color:green;
	}
</style>