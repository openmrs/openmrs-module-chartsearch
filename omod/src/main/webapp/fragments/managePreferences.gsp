<style type="text/css">
	.pref-summary-item {
		background-color: #F0EAEA;
		color:black;
		border: 1px solid white;
		overflow:hidden;
		height: 20px;
		cursor: pointer;
		padding: 10px 10px 10px 10px;
		font-weight:bold;
	}
	
	.pref-summary-item:hover {
		background-color: #d6d6d6;
	}
	
	#pref-summary-item-toggle-details, #pref-summary-item-notescolors-details, #pref-summary-item-categoryfilter-details {
		display:none;
	}
	
	.pref-summary-item-details {
		padding-left:10px;
	}
</style>

<script type="text/javascript">
	var jq = jQuery;
	var prefs ='${preferences}';
    var preferences = JSON.parse(prefs);
    
	jq(document).ready(function() {
		jq('#pref-summary-item-toggle').click(function(event) {
			jq('#pref-summary-item-toggle-details').toggle();
		});
		
		jq('#pref-summary-item-categoryfilter').click(function(event) {
			jq('#pref-summary-item-categoryfilter-details').toggle();
		});
		
		jq('#pref-summary-item-notescolors').click(function(event) {
			jq('#pref-summary-item-notescolors-details').toggle();
		});
	
	});

</script>



<h1>Manage Preferences</h1>
<div class="pref-summary-item" id="pref-summary-item-toggle">Enable/Disable Features</div>
<div id="pref-summary-item-toggle-details" class="pref-summary-item-details">
	Details for toggle
</div>

<div class="pref-summary-item" id="pref-summary-item-categoryfilter">Category Filters</div>
<div id="pref-summary-item-categoryfilter-details" class="pref-summary-item-details">
	Details for cat filters
</div>

<div class="pref-summary-item" id="pref-summary-item-notescolors">Notes Colors</div>
<div id="pref-summary-item-notescolors-details" class="pref-summary-item-details">
	Details for notes colors
</div>






