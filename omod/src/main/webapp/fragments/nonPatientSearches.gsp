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




<script type="text/javascript">
    var jq = jQuery;
    var indexedSearchProjects;
    jq("#search_phrase").focus();
    
    jq(document).ready(function() {
    	jq("#submit_search_non-p-specific").click(function() {
    		jq(".failed-to-submit-form").html("");
    		
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
    		jq.ajax({
    			type: "POST",
		            url: "${ ui.actionLink('searchNonPatientSpecificDataForAlreadyIndexed') }",
		            data: { "selectedProject": selectedProj, "searchText":searchText },
		            dataType: "json",
		            success: function(results) {
		            	var indexedSProjs = '<option value="" name="selectedSP">Choose Search Project</option>';;
		                
		                jq("#init-installed-search-projects").empty().append(indexedSProjs);
		                
		                if(results.noResultsReturned) {
			                jq(".failed-to-submit-form").html(results.noResultsReturned);
			                jq("#init-installed-search-projects").val(selectedProj);
		                } else {
		                	//TODO Display results in a most relevant way
			                alert(results.returnedSearchedResults);
			                jq('#search_non_patient_data_form').trigger("reset");
			                
		                }
		                
		                indexedSearchProjects = results.initiallyIndexedProjects;
		        		
		        		for(i = 0; i < indexedSearchProjects.length; i++) {
		        			indexedSProjs += '<option class="indexed-search-projects" value="' + indexedSearchProjects[i] + '">' + indexedSearchProjects[i] + '</option>';
		        		}
		            },
		            error: function(e) {
		                alert("Error occurred!!! " + e);
		            }
    		});
    	}
    });
    
</script>

<style type="text/css">

.failed-to-submit-form {
	color:red;
}
</style>                      