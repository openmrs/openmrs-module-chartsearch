<div class="customIndexerSection">
	<h2><i class="icon-circle-arrow-down" id="expand-indexer1"></i> ${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex") }</h2>
	<div class="customIndexerSubSection1">
      <div id="enter-required-fields"></div>
      <form id="index-new-project-data">
         <p>
            <input name="projectName" class="project_name" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.projectName") }" type="text"></input>
            <input name="projectDesc" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.projectDesc") }" type="text"></input>
            <textarea name="mysqlQuery" class="mysql_query" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.sql") }"></textarea>
            <textarea name="columns" class="column_names" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.columns") }"></textarea>
         </p>
         <p>
         	<input id="non-openmrs-db" type="checkbox"></input>
            <label>Non OpenMRS database</label>
         </p>
         <p id="non-openmrs-db-name">
            <input name="databaseName" class="db_name" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseName") }" type="text"></input>
            <input name="databaseUser" class="db_user" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseUser") }" type="text"></input>
            <input name="databaseUserPassword" class="db_password" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseUserPassword") }" type="password"></input>
            <input name="databaseServer" class="db_server" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseServer") }" type="text"></input>
            <input name="databaseManager" class="db_manager" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.dbms") }" type="text"></input>
            <input name="databasePortNumber" class="db_port" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databasePortNumber") }" type="text"></input>
         </p>
         <input id="index-project-data" type="submit" value="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.indexSubmit")}"></input>
      </form>
	</div>
	<h2><i class="icon-circle-arrow-right" id="expand-indexer2"></i> ${ ui.message("chartsearch.refApp.customIndexing.updateProjectIndex") }</h2>
	<div class="customIndexerSubSection2">
		This will contain existing projects  and allow updates to them
	</div>
</div>

<script type="text/javascript">
   var jq = jQuery;
   
   jq( document ).ready(function() {
	   jq("#non-openmrs-db-name").hide("fast");
	   jq(".customIndexerSubSection2").hide("fast");
	   
	   //hide or unhide database field
	   jq('#non-openmrs-db').click(function() {
	      	if(jq(this).prop("checked") == true) {
	      		jq("#non-openmrs-db-name").show("slow");
	        } else if(jq(this).prop("checked") == false) {
	            jq("#non-openmrs-db-name").hide("fast");
	        }
	   });
	   
	   jq('#index-project-data').click(function(event) {
		    var requiredFieldsMsg = "";
		   	var projectName=jq(".project_name").val();
		   	var mysqlQuery=jq(".mysql_query").val();
		   	var columns=jq(".column_names").val();
		   	
		   	if (!projectName) {
		   		requiredFieldsMsg += "Project Name is Required<br />";
		   	}
		   	if (!mysqlQuery) {
		   		requiredFieldsMsg += "Database Query is Required<br />";
		   	}
			if (!columns) {
		   		requiredFieldsMsg += "Column Names are Required<br />";
		   	}
			if (!projectName || !mysqlQuery || !columns) {
				if(jq("#non-openmrs-db").prop("checked") == true) {
		      		if(!jq(".db_name").val() || !jq(".db_user").val() || !jq(".db_password").val() || !jq(".db_server").val() || !jq(".db_manager").val() || !jq(".db_port").val()) {
		      			requiredFieldsMsg += "All Fields for Non OpenMRS Database are Required<br />";
		      		}
		        }
				jq('#enter-required-fields').html(requiredFieldsMsg + "<br />");
				requiredFieldsMsg = "";
			} else {
		   		submitNewProjectIndexingFormWithAjax();
		   	}
		   	return false;
	   });
	   
	   //expand or hide new project indexer section
	   jq('#expand-indexer1').click(function() {
	   		if (jq("#expand-indexer1").hasClass("icon-circle-arrow-down")) {
		   		jq("#expand-indexer1").addClass("icon-circle-arrow-right");
		      	jq("#expand-indexer1").removeClass("icon-circle-arrow-down");
		      	jq(".customIndexerSubSection1").hide("fast");
	      	} else if (jq("#expand-indexer1").hasClass("icon-circle-arrow-right")) {
	      		jq("#expand-indexer1").addClass("icon-circle-arrow-down");
		      	jq("#expand-indexer1").removeClass("icon-circle-arrow-right");
	      		jq(".customIndexerSubSection1").show("fast");
	      	}
	   });
	   
	   //expand or hide existing project indexer section
	   jq('#expand-indexer2').click(function() {
	   		if (jq("#expand-indexer2").hasClass("icon-circle-arrow-down")) {
		   		jq("#expand-indexer2").addClass("icon-circle-arrow-right");
		      	jq("#expand-indexer2").removeClass("icon-circle-arrow-down");
		      	jq(".customIndexerSubSection2").hide("fast");
	      	} else if (jq("#expand-indexer2").hasClass("icon-circle-arrow-right")) {
	      		jq("#expand-indexer2").addClass("icon-circle-arrow-down");
		      	jq("#expand-indexer2").removeClass("icon-circle-arrow-right");
		      	jq(".customIndexerSubSection2").show("fast");
	      	}
	   });
	   
	   function hideIndexingSection2() {
		   	jq("#expand-indexer2").addClass("icon-arrow-right");
		    jq("#expand-indexer2").removeClass("icon-arrow-down");
		    jq(".customIndexerSubSection2").hide("fast");
	   }
	   
	   function submitNewProjectIndexingFormWithAjax() {
	   	jq.ajax({
	   		type: "POST",
	   		url: "${ ui.actionLink('indexDataForANewProject') }",
	   		data: jq('#index-new-project-data').serialize(),
	   		dataType: "json",
	   		success: function(results) {
	   			alert("Number of documents indexed were: " + results.numberOfIndexedDocs);
	   			//TODO get  the json results from and represent it in html/client side language
	   		},
	   		error: function(e) {
	   			//alert("Error occurred!!! " + e);
	   		}
	   	});
	   }
   });
   
</script>

<style type="text/css">
	#enter-required-fields {
		color:red;
	}
</style>