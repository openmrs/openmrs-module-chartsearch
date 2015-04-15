<br />
<div class="failed-to-submit-form"></div>

<form id="search_non_patient_data_form">
	<select id="init-installed-search-projects">
		<option value="" name="selectedProject">Choose Search Project</option>
		<% if (initiallyIndexedProjects) { %>
			<% initiallyIndexedProjects.each { projectName -> %>
				<option class="indexed-search-projects" value="$projectName">$projectName</option>
			<% } %>
		<% } %>
	</select>
	<br />
	<input type="text" id="search_phrase" name="searchPhrase" class="inline ui-autocomplete-input" placeholder="Something to search for" />
	<input type="submit" id="submit_search_non-p-specific" name="searchPhrase" value="Search" />
</form>

<div id="formatted-results-section">
	<div id="project-details"></div>
	<div id="results-grid"></div>
</div>
    
<br />
<p id="displaying_results_section"></p>



<script type="text/javascript">
    var jq = jQuery;
    var indexedSearchProjects;
    jq("#search_phrase").focus();
    
    jq(document).ready(function() {
    	jq("#submit_search_non-p-specific").click(function() {
    		jq(".failed-to-submit-form").html("");
    		jq("#project-details").html("");
			jq("#results-grid").html("");
    		
    		var selectedProj = jq("#init-installed-search-projects").val();
    		var searchPhrase = jq("#search_phrase").val();
    		var enterFields = "";
    		
    		if(!selectedProj) {
    			enterFields += "Please select a Search Project to Search Against and Try again!<br />";
    		}
    		if(!searchPhrase) {
    			enterFields += "You must enter what you want to search for!";
    		}
    		
    		if(enterFields) {
    			jq(".failed-to-submit-form").html(enterFields);
    		}
    		
    		if(selectedProj && searchPhrase) {
    			searchNonPatientSpecificData(selectedProj, searchPhrase);
    		}
    		
    		return false;
    	});
    	
    	function searchNonPatientSpecificData(selectedProj, searchText) {
    		jq("#formatted-results-section").hide();
    		jq.ajax({
    			type: "POST",
		            url: "${ ui.actionLink('searchNonPatientSpecificDataForAlreadyIndexed') }",
		            data: { "selectedProject": selectedProj, "searchText":searchText },
		            dataType: "json",
		            success: function(results) {
		            	
		            	var indexedSProjs = '<option value="" name="selectedSP">Choose Search Project</option>';;
		                var fieldNames = results.solrFieldNames;
		                
		                if(results.noResultsReturned) {
			                jq(".failed-to-submit-form").html(results.noResultsReturned);
			                jq("#init-installed-search-projects").val(selectedProj);
		                } else {
		                	//TODO Display results in a most relevant way
			                var strJsonResult = JSON.stringify(results.returnedSearchedResults);
			                defaultDisplayOfResults(JSON.parse(strJsonResult), fieldNames, searchText);
		                }
		                
		                indexedSearchProjects = results.initiallyIndexedProjects;
		        		
		        		for(i = 0; i < indexedSearchProjects.length; i++) {
		        			indexedSProjs += '<option class="indexed-search-projects" value="' + indexedSearchProjects[i] + '">' + indexedSearchProjects[i] + '</option>';
		        		}
		        		
		                jq("#init-installed-search-projects").empty().append(indexedSProjs);
		                jq("#init-installed-search-projects").val(selectedProj);
		            },
		            error: function(e) {
		                alert("Error occurred!!! " + e);
		            }
    		});
    	}
    	
    	function defaultDisplayOfResults(strJsonResult, fieldNames, searchPhrase) {
    		var resultsDisplay = "<h3>Result(s) Details:</h3><table id='returned-match-details-table'><tr><th>Field Name</th><th>Value</th>";
    		var projectDetails = "<h3>Search Project Details:</h3>";
    		var actionsCenter = "<h3>Actions: </h3><input type='checkbox' class='expand-results-to-docs'></input>";
    		var totalNoOfResults = strJsonResult.length;
    		var doneFieldNames = "";
			
    		jq.each(strJsonResult, function(index, result) {
    			var pName = result.project_name;
    			var pDBName = result.project_db_name;
    			var pDesc = result.project_description;
    			var pUuid = result.project_uuid;
    			var version = result._version_;
    			var docNum = index + 1;
    			
	    		resultsDisplay += "<tr><td><b>Document: " + docNum + "</b></td></tr>";
	    		
    			if (index === totalNoOfResults - 1) {
    				projectDetails += "Name: <b>" + pName + "</b><br />Description: <b>" + pDesc + "</b><br />UUID: <b>" + pUuid + "</b><br />Database: <b>" + pDBName + "</b><br /><br />"
    			}
				
	    		for(i = 0; i < fieldNames.length; i++) {
	    			var curField = fieldNames[i];
	    			var curIndexedField = result[curField];
	    				
	    			if(!doneFieldNames.indexOf(curField) > -1) {
	    				if(curIndexedField === searchPhrase || curIndexedField.indexOf(searchPhrase) > -1 || searchPhrase.indexOf(curIndexedField) > -1) {
	    					resultsDisplay += "<tr style='background-color:#009384'><td>" + curField + "</td><td><em><b>" + curIndexedField + "</b></em></td></tr>";
	    				} else {
	    					resultsDisplay += "<tr><td>" + curField + "</td><td>" + curIndexedField + "</td></tr>";
	    				}
	    			}
	    			doneFieldNames += curField + ", ";
	    		}
			});
			
			resultsDisplay += "</table>";
			
			jq("#project-details").html(projectDetails);
			jq("#results-grid").html(resultsDisplay);
			jq("#formatted-results-section").show();
    	}
    });
    
</script>

<style type="text/css">

.failed-to-submit-form {
	color:red;
}

#returned-matches-table {
  display: block;
  height: 300px;
  overflow-y: scroll;
}

</style>                      