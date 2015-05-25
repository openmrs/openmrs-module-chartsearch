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
<div class="search-record-dialog-content" id="delete-search-record-dialog">Do you want to delete?</div>

<i id="favorite-search-record" class="icon-star-empty medium"></i>
<div class="search-record-dialog-content" id="favorite-search-record-dialog">Favoriting or bookmarking Search Results</div>

<i id="comment-on-search-record" class="icon-comment medium"></i>
<div class="search-record-dialog-content" id="comment-on-search-record-dialog">Add Notes to a search</div>


<a id="quick-searches" href="">QuickSearch</a>
<div class="search-record-dialog-content" id="quick-searches-dialog-message"></div>
