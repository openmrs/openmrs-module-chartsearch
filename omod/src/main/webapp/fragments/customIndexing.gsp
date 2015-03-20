<!-- Section informative content to be totally removed -->
<p>Section for managing custom indexing</p>

<br />
<input type="button" value="Index ChartSearch module Data" id="chartsearch-indexing" />


<script type="text/javascript">
 var jq = jQuery;
 
jq( document ).ready(function() {
	jq('#chartsearch-indexing').click(function(event) {
		submitChartSearchIndexingFormWithAjax();
		return false;
	});
	
	function submitChartSearchIndexingFormWithAjax() {
			jq.ajax({
					type: "POST",
					 url: "${ ui.actionLink('getSolrResultsFromTheServer') }",
					data: jq('#chartsearch-indexing').serialize(),
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