<div class="customIndexerSection">
   <div class="customIndexerSubSection1">
      <h2>Add A New Project/module to Index</h2>
      <form id="index-new-project-data">
         <p>
            <input id="projectName" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.projectName") }" type="text"></input>
         </p>
         <p>
            <input id="projectDesc" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.projectDesc") }" type="text"></input>
         </p>
         <p>
            <textarea id="mysqlQuery" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.sql") }"></textarea>
         </p>
         <p>
            <textarea id="columns" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.columns") }"></textarea>
         </p>
         <p>
            <input id="non-openmrs-db" type="checkbox"></input>
            <label>Non OpenMRS database</label>
         </p>
         <p id="non-openmrs-db-name">
            <input id="databaseName" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseName") }" type="text"></input>
            <input id="databaseUser" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseUser") }" type="text"></input>
            <input id="databaseUserPassword" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseUserPassword") }" type="text"></input>
            <input id="databaseServer" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databaseServer") }" type="text"></input>
            <input id="databaseManager" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.dbms") }" type="text"></input>
            <input id="databasePortNumber" placeholder="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.databasePortNumber") }" type="text"></input>
         </p>
         <input id="index-project-data" type="submit" value="${ ui.message("chartsearch.refApp.customIndexing.addNewProjectToIndex.indexSubmit")}"></input>
      </form>
   </div>
   <div class="customIndexerSubSection2">
      <h2>${ ui.message("chartsearch.refApp.customIndexing.updateProjectIndex") }</h2>
   </div>
</div>

<script type="text/javascript">
   var jq = jQuery;
   
   jq( document ).ready(function() {
	   jq("#non-openmrs-db-name").hide( "slow" );
	   
	   //hide or unhide database field
	   jq('#non-openmrs-db').click(function() {
	      	if(jq(this).prop("checked") == true) {
	      		jq("#non-openmrs-db-name").show( "slow" );
	          } else if(jq(this).prop("checked") == false) {
	              jq("#non-openmrs-db-name").hide( "fast" );
	          }
	      });
	   
	   jq('#index-project-data').click(function(event) {
	   	submitNewProjectIndexingFormWithAjax();
	   	return false;
	   });
	   
	   function submitNewProjectIndexingFormWithAjax() {
	   	var projectName = jq("#projectName").val();
	   	var projectDesc = jq("#projectDesc").val();
	   	var mysqlQuery = jq("#mysqlQuery").val();
	   	var columns = jq("#columns").val();
	   	var databaseName = jq("#databaseName").val();
	   	var databaseUser = jq("#databaseUser").val();
	   	var databaseUserPassword = jq("#databaseUserPassword").val();
	   	var databaseServer = jq("#databaseServer").val();
	   	var databaseManager = jq("#databaseManager").val();
	   	var databasePortNumber = jq("#databasePortNumber").val();
	   	
	   	jq.ajax({
	   		type: "POST",
	   		url: "${ ui.actionLink('indexDataForANewProject') }",
	   		data: {"projectName":projectName, "projectDesc":projectDesc, "mysqlQuery":mysqlQuery, "columns":columns, "databaseName":databaseName, "databaseUser":databaseUser, "databaseUserPassword":databaseUserPassword, "databaseServer":databaseServer, "databaseManager":databaseManager ,"databasePortNumber":databasePortNumber},
	   		dataType: "json",
	   		success: function(results) {
	   			alert("Number of documents found after a search were: " + results.noOfDocs);
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
</style>