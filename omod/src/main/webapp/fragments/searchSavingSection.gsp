<style type="text/css">
	#search-saving-section {
		padding-top:8px;
	}
	
	#delete-search-record, #favorite-search-record, #comment-on-search-record {
		cursor: pointer;
	}
	
	.search-record-dialog-content {
		display:none;
	}
	
	.ui-dialog > .ui-widget-header {
		background: #00463f;
		color:white;
	}
	
	.ui-widget-header .ui-icon {
		background-image: url(../scripts/jquery-ui/css/green/images/ui-icons_ffffff_256x240.png);
	}

</style>

<script type="text/javascript">
	var jq = jQuery;
    
    jq(document).ready(function() {
    
    	jq("#delete-search-record").click(function(event) {
    		invokeDialog("#delete-search-record-dialog", "Delete this Search Record");
    	});
    
    	jq("#favorite-search-record").click(function(event) {
			invokeDialog("#favorite-search-record-dialog", "Bookmark this Search Record");
    	});
    	
    	jq("#comment-on-search-record").click(function(event) {
			invokeDialog("#comment-on-search-record-dialog", "Add Notes to This Search");
    	});
    	
    	jq("#quick-searches").click(function(event){
    		invokeDialog("#quick-searches-dialog-message", "Quick Searches");
    		return false;
    	});
    	
    	/*Overrides jq.("").dialog()*/
    	function invokeDialog(dialogMessageElement, dialogTitle) {
	    	jq(dialogMessageElement).dialog({
				title: dialogTitle
			});
    	}
    	
    });
       
</script>



<i id="delete-search-record" class="icon-remove medium"></i>
${ ui.includeFragment("chartsearch", "deleteSearchRecord") }

<i id="favorite-search-record" class="icon-star-empty medium"></i>
${ ui.includeFragment("chartsearch", "favoriteSearchRecord") }

<i id="comment-on-search-record" class="icon-comment medium"></i>
${ ui.includeFragment("chartsearch", "commentOnSearchRecord") }


<a id="quick-searches" href="">QuickSearch</a>
${ ui.includeFragment("chartsearch", "quickSearches") }

