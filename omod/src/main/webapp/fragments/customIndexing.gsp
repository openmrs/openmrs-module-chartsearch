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
	jq('#index-project-data').click(function(event) {
		submitChartSearchIndexingFormWithAjax();
		return false;
	});
	
	function submitChartSearchIndexingFormWithAjax() {
			jq.ajax({
					type: "POST",
					 url: "${ ui.actionLink('indexDataForANewProject') }",
					data: jq('#index-new-project-data').serialize(),
					dataType: "json",
					success: function(results) {
						alert(results);
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