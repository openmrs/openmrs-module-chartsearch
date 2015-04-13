<div class="customIndexerSection">
    <h2><i class="icon-circle-arrow-down" id="expand-indexer1"></i> ${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex") }</h2>
    <div class="customIndexerSubSection1">
        <div id="enter-required-fields"></div>
        <div id="save-project-feedback"></div>
        <div id="save-project-warning"></div>
        <form id="index-new-project-data">
            <p>
                <input name="projectName" class="project_name" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.projectName") }" type="text"></input>
                <input name="projectDesc" class="project_desc" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.projectDesc") }" type="text"></input>
                <textarea name="mysqlQuery" class="mysql_query" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.sql") }"></textarea>
                <textarea name="columns" class="column_names" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.columns") }"></textarea>
                <input class="database_type" type="hidden" value="default"></input>
            </p>
            <p>
                <input id="installed-p-database" type="checkbox"></input>
                <label>Choose Database from your previously imported</label>
            </p>
            <p id="installed-p-databases">
            	<select id="installed-databases2">
					<option value="" name="selectedDatabase">Choose Database</option>
						<div id="imported-databases-options2">
							<% if (installedDatabases) { %>
								<% installedDatabases.each { installedDatabase -> %>
					    			<option class="installed-database" value="$installedDatabase">$installedDatabase</option>
								<% } %>
					    	<% } %>
					    </div>
				</select>
            </p>
            <br />
            <p>
                <input id="non-openmrs-db" type="checkbox"></input>
                <label>Remote Database Connection Details</label>
            </p>
            <p id="non-openmrs-db-name">
            	<b>NOT YET SUPPORTED, SO USE THE ABOVE TWO for now:</b> Either use default/openmrs or imported database<br />
                <input name="databaseName" class="db_name" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseName") }" type="text"></input>
                <input name="databaseUser" class="db_user" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseUser") }" type="text"></input>
                <input name="databaseUserPassword" class="db_password" placeholder="${ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseUserPassword") }" type="password"></input>
                <input name="databaseServer" class="db_server" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseServer") }" type="text"></input>
                <input name="databaseManager" class="db_manager" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.dbms") }" type="text"></input>
                <input name="databasePortNumber" class="db_port" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databasePortNumber") }" type="text"></input>
            </p>
            <input id="index-project-data" type="submit" value="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.indexSubmit")}"></input>
        </form>
    </div>
    <h2><i class="icon-circle-arrow-right" id="expand-indexer2"></i> ${ ui.message("chartsearch.refApp.customIndexing.updateProjectIndex") }</h2>
    <div class="customIndexerSubSection2">
    	<p>
	    	<select id="installed-search-projects">
	    		<option value="" name="selectedProject">Choose Search Project</option>
	    		<% if (projectNames) { %>
	    			<% projectNames.each { projectName -> %>
	    				<option class="installed-selected-search-projects" value="$projectName">$projectName</option>
	    			<% } %>
	    		<% } %>
	    	</select>
    	</p>
    	<p>
    		<br />
    		<b>NOTICE:</b>
    		<br />${ ui.message("chartsearch.refApp.customIndexing.updateProjectIndex.notice")}<br /><br />
    	</p>
    	<form id="update-project-data-index">
		    <div id="updating-installed-search-projects"></div>
		    <input hide id="update-index-project-data" type="submit" value="${ ui.message("chartsearch.refApp.customIndexing.updateProjectIndex.indexSubmit")}"></input>
	    </form>
    </div>
</div>



<script type="text/javascript">
    var jq = jQuery;
    var installedFields = "$currentInstalledFields";
    var existingSearchProjects;
    var indexedSearchProjects;

	jq(document).ready(function() {
	    jq("#non-openmrs-db-name").hide("fast");
	    jq("#installed-p-databases").hide("fast");
	    jq(".customIndexerSubSection2").hide("fast");
	    jq("#update-index-project-data").hide();
	
	    //hide or unhide database field
	    jq('#non-openmrs-db').click(function() {
	        if (jq(this).prop("checked") == true) {
	            jq("#non-openmrs-db-name").show("slow");
	            jq("#installed-p-database").prop("disabled", true);
	            jq("#installed-p-databases").hide("fast");
	            jq("#installed-databases2").val("");
	        } else if (jq(this).prop("checked") == false) {
	            jq("#non-openmrs-db-name").hide("fast");
	            jq("#installed-p-database").prop("disabled", false);
	        }
	    });
	
	    jq('#index-project-data').click(function(event) {
	        var requiredFieldsMsg = "";
	        var projectName = jq(".project_name").val();
	        var mysqlQuery = jq(".mysql_query").val();
	        var columns = jq(".column_names").val();
	        
	        jq('#enter-required-fields').html("");
	
	        if (!projectName) {
	            requiredFieldsMsg += "Project Name is Required<br />";
	        }
	        if (!mysqlQuery) {
	            requiredFieldsMsg += "Database Query is Required<br />";
	        }
	        if (!columns) {
	            requiredFieldsMsg += "Column Names are Required<br />";
	        }
	        //TODO get the existing columns on get page and do a check for their existence to avoid calling into the server before validating their existance
	        if (!projectName || !mysqlQuery || !columns) {
	            if (jq("#non-openmrs-db").prop("checked") == true) {
	                if (!jq(".db_name").val() || !jq(".db_user").val() || !jq(".db_password").val() || !jq(".db_server").val() || !jq(".db_manager").val() || !jq(".db_port").val()) {
	                    requiredFieldsMsg += "All Fields for Non OpenMRS Database are Required<br />";
	                }
	            }
	            jq('#enter-required-fields').html(requiredFieldsMsg + "<br />");
	            requiredFieldsMsg = "";
	        } else {
	        	//installedFields
	        	var fieldsFromUIArray = columns.split(",")
	        	var fieldExists = false;
	        	var existingColumns = "";
	        	
	        	for(i = 0; i < fieldsFromUIArray.length; i++) {
	        		var field = fieldsFromUIArray[i];
	        		fieldExists = installedFields.indexOf(field) > -1
	        		if(fieldExists) {
	        			existingColumns += field + ", ";
	        			continue;
	        		}
	        	}
	        	
	        	if(existingColumns) {
	        		jq('#enter-required-fields').html("The Following solr field(s) exist; <b>" + existingColumns + "</b> try using other names");
	        	} else {
	            	submitNewProjectIndexingFormWithAjax();
	            }
	        }
	        return false;
	    });
	
	    //expand or hide new project indexer section
	    jq('#expand-indexer1').click(function() {
	        if (jq("#expand-indexer1").hasClass("icon-circle-arrow-down")) {
	            hideIndexingSection1();
	        } else if (jq("#expand-indexer1").hasClass("icon-circle-arrow-right")) {
	            expandIndexingSection1();
	        }
	    });
	
	    //expand or hide existing project indexer section
	    jq('#expand-indexer2').click(function() {
	        if (jq("#expand-indexer2").hasClass("icon-circle-arrow-down")) {
	            hideIndexingSection2();
	        } else if (jq("#expand-indexer2").hasClass("icon-circle-arrow-right")) {
	            expandIndexingSection2();
	        }
	    });
	
	    jq('#installed-search-projects').change(function() {
	        fetchDetailsOfSelectSearchProject();
	    });
	    
	    jq("#installed-databases2").change(function() {
	    	var nonSelected = jq('#installed-databases2').val();
	    	if(nonSelected != "") {
		    	var dbName = jq("#installed-databases2").val();
		        jq(".db_name").val(dbName);
		        jq(".database_type").val("imported");
	        } else {
	        	jq(".db_name").val("");
	        }
	    });
	
	    jq('#update-index-project-data').click(function(event) {
	        submitExistingSearchProjectUpdate();
	        return false;
	    });
	    
	    jq("#installed-p-database").click(function(event) {
	    	if (jq(this).prop("checked") == true) {
	            jq("#installed-p-databases").show("slow");
	            jq("#non-openmrs-db").prop("disabled", true);
	            jq("#non-openmrs-db-name").hide("fast");
	        } else if (jq(this).prop("checked") == false) {
	            jq("#installed-p-databases").hide("fast");
	            jq("#non-openmrs-db").prop("disabled", false);
	            jq("#installed-databases2").val("");
	        }
	    });
	
	    function hideIndexingSection2() {
	        jq("#expand-indexer2").addClass("icon-circle-arrow-right");
	        jq("#expand-indexer2").removeClass("icon-circle-arrow-down");
	        jq(".customIndexerSubSection2").hide("fast");
	    }
	
	    function expandIndexingSection2() {
	        jq("#expand-indexer2").removeClass("icon-circle-arrow-right");
	        jq("#expand-indexer2").addClass("icon-circle-arrow-down");
	        jq(".customIndexerSubSection2").show("fast");
	        hideIndexingSection1();
	    }
	
	    function hideIndexingSection1() {
	        jq("#expand-indexer1").addClass("icon-circle-arrow-right");
	        jq("#expand-indexer1").removeClass("icon-circle-arrow-down");
	        jq(".customIndexerSubSection1").hide("fast");
	    }
	
	    function expandIndexingSection1() {
	        jq("#expand-indexer1").addClass("icon-circle-arrow-down");
	        jq("#expand-indexer1").removeClass("icon-circle-arrow-right");
	        jq(".customIndexerSubSection1").show("fast");
	        hideIndexingSection2();
	    }
	
	    function submitNewProjectIndexingFormWithAjax() {
	    	var projectName = jq(".project_name").val();
	    	var projectDesc = jq(".project_desc").val();
	    	var mysqlQuery = jq(".mysql_query").val();
	    	var columns = jq(".column_names").val();
	    	var databaseName = jq(".db_name").val();
	    	var databaseUser = jq(".db_user").val();
	    	var databaseUserPassword = jq(".db_password").val();
	    	var databaseServer = jq(".db_server").val();
	    	var databaseManager = jq(".db_manager").val();
	    	var databasePortNumber = jq(".db_port").val();
	    	var databaseType = jq(".database_type").val();
	    	
	    	jq("#save-project-feedback").html("");
	    	jq("#enter-required-fields").html("");
	        jq("#index-new-project-data :input").prop("disabled", true);
	        jq.ajax({
	            type: "POST",
	            url: "${ ui.actionLink('indexDataForANewProject') }",
	            data: {
	            	"projectName":projectName, "projectDesc":projectDesc, "mysqlQuery":mysqlQuery, "columns":columns, "databaseName":databaseName, "databaseType":databaseType,
	            	"databaseUser":databaseUser, "databaseUserPassword":databaseUserPassword, "databaseServer":databaseServer, "databaseManager":databaseManager, "databasePortNumber":databasePortNumber
	            },
	            dataType: "json",
	            success: function(results) {
	                jq("#index-new-project-data :input").prop("disabled", false);
	                var updatedSProjs = '<option value="" name="selectedDatabase">Choose Search Project</option>';
	                var indexedSProjs = '<option value="" name="selectedDatabase">Choose Search Project</option>';;
		                
	                if (!results.failureMessage) {
	                    jq("#save-project-feedback").html("You have successfully saved and indexed " + projectName + "<br />Saving the project took: " + results.savingTime + " seconds<br />Indexing project data took: " + results.indexingTime + " seconds<br /><b>Indexed: " + results.numberOfIndexedDocs + " Document(s)</b><br />Project assigned UUID is: " + results.projectUuid);
	                    jq('#index-new-project-data').trigger("reset");
	                    //TODO rewrite select #installed-search-projects under non patient searches
	                    
	                    existingSearchProjects = feedback.projectNames;
		        		
		        		for(i = 0; i < existingSearchProjects.length; i++) {
		        			updatedSProjs += '<option class="installed-database" value="' + existingSearchProjects[i] + '">' + existingSearchProjects[i] + '</option>';
		        		}
		        		//installed-search-projects
		        		jq("#installed-search-projects").empty().append(updatedSProjs);
		        		
		        		
		        		indexedSearchProjects = feedback.initiallyIndexedProjects;
		        		
		        		for(i = 0; i < indexedSearchProjects.length; i++) {
		        			indexedSProjs += '<option class="indexed-search-projects" value="' + indexedSearchProjects[i] + '">' + indexedSearchProjects[i] + '</option>';
		        		}
		                
		                jq("#init-installed-search-projects").empty().append(indexedSProjs);
		                
	                    installedFields = results.currentInstalledFields;
	                } else {
	                    jq("#enter-required-fields").html(results.failureMessage);
	                }
	            },
	            error: function(e) {
	                //alert("Error occurred!!! " + e);
	            }
	        });
	    }
	
	    function displaySearchProjectDetailsFromServer(searchProject) {
	        var projectHtml = '';
	        if (searchProject) {
	            projectHtml += '<p>';
	            projectHtml += '<b>Name:</b> <input name="projectName" class="project_name" type="text" value="' + searchProject.projectName + '"></input><br />';
	            projectHtml += '<b>Description:</b> <input name="projectDesc" type="text" value="' + searchProject.projectDescription + '"></input><br />';
	            projectHtml += '<b>Database Query:</b> <textarea name="mysqlQuery" class="mysql_query">' + searchProject.projectDatabaseQuery + '</textarea><br />';
	            projectHtml += '<b>Solr Field/Column Names:</b> ' + searchProject.projectSolrFields + '<br /><br />';
	            projectHtml += '<b>Database Name:</b> <input name="databaseName" class="db_name" type="text" value="' + searchProject.projectDB + '"></input><br />';
	            projectHtml += '<input name="projectDBType" class="database_type" type="hidden" value="' + searchProject.projectDBType + '"></input>';
	            projectHtml += '</p>';
	
	            if (searchProject.projectDBType != "default" && searchProject.projectDBType != "imported") {
	                projectHtml += '<p>';
	                projectHtml += '<b>Database User:</b> <input name="databaseUser" class="db_user" type="text" value="' + searchProject.projectDBUser + ' "></input><br />';
	                projectHtml += '<b>Database Password:</b> <input name="databaseUserPassword" class="db_password" type="password" value="' + searchProject.projectDBUserPassword + '"></input><br />';
	                projectHtml += '<b>Database Server Name:</b> <input name="databaseServer" class="db_server" type="text" value="' + searchProject.projectServerName + '"></input><br />';
	                projectHtml += '<b>Database Manager:</b> <input name="databaseManager" class="db_manager" type="text" value="' + searchProject.projectDBManager + '"></input><br />';
	                projectHtml += '<b>Database Port Number:</b> <input name="databasePortNumber" class="db_port" type="text" value="' + searchProject.projectDBPortNumber + '"></input><br />';
	                projectHtml += '</p>';
	            }
	            jq("#update-index-project-data").show();
	        }
	
	        jq("#updating-installed-search-projects").html(projectHtml);
	    }
	
	    function fetchDetailsOfSelectSearchProject() {
	        var selectProject = jq("#installed-search-projects").val();
	        if (selectProject) {
		        jq.ajax({
		            type: "POST",
		            url: "${ ui.actionLink('fetchDetailsOfASelectedSearchProject') }",
		            data: {
		                "selectedSearchProject": selectProject
		            },
		            dataType: "json",
		            success: function(project) {
		                if (project) {
		                    displaySearchProjectDetailsFromServer(project);
		                }
		            },
		            error: function(e) {
		                //alert("Error occurred!!! " + e);
		            }
		        });
	        } else {
	        	jq("#updating-installed-search-projects").html("");
	        	jq("#update-index-project-data").hide();
	        }
	    }
	
	    function submitExistingSearchProjectUpdate() {
	        alert("Saving update will soon be supported! WORK IN PROGRESS :-)");
	    }
	
	});
</script>



<style type="text/css">
    #enter-required-fields {
        color: red;
    }
    #save-project-feedback {
    	color:green;
    }
    
</style>