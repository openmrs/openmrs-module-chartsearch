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
		font-size:1.2em;
	}
</style>

<script type="text/javascript">
	var jq = jQuery;
	var prefs ='${preferences}';
	var daemonPrefs = '${daemonPreferences}';
    var preferences = JSON.parse(prefs);
    var daemonPreferences = JSON.parse(daemonPrefs);//TODO use for restoring default preferences
    
	jq(document).ready(function() {
	
		updatePreferencesDisplay(preferences);
	
		jq('#pref-summary-item-toggle').click(function(event) {
			jq('#pref-summary-item-toggle-details').toggle();
		});
		
		jq('#pref-summary-item-categoryfilter').click(function(event) {
			jq('#pref-summary-item-categoryfilter-details').toggle();
		});
		
		jq('#pref-summary-item-notescolors').click(function(event) {
			jq('#pref-summary-item-notescolors-details').toggle();
		});
		
		jq("#restore-default-prefs").click(function(event) {
			updatePreferencesDisplay(daemonPreferences);
			submitPreferences(false);
		});
		
		jq("#apply-and-save-prefs").click(function(event) {
			submitPreferences(true);
		});
		
		jq("#cancel-prefs-changing").click(function(event) {
			updatePreferencesDisplay(preferences);
		});
	
	});
	
	function updatePreferencesDisplay(prefs) {
		if(prefs) {
			jq("#enable_history").prop('checked', prefs.enableHistory);
			jq("#enable_bookmarks").prop('checked', prefs.enableBookmarks);
			jq("#enable_notes").prop('checked', prefs.enableNotes);
			jq("#enable_quicksearches").prop('checked', prefs.enableQuickSearches);
			jq("#enable_defaultsearch").prop('checked', prefs.enableDefaultSearch);
			jq("#enable_multiplefiltering").prop('checked', prefs.enableMultipleFiltering);
			jq("#enable_duplicateresults").prop('checked', prefs.enableDuplicateResults);
		}
		//TODO add support for filters and note colors here
	}

	function submitPreferences(isNotDefault) {
		//TODO add support for filters and note colors here
		var duplicateResults = jq("#enable_duplicateresults").prop('checked');
		var history = jq("#enable_history").prop('checked');
		var bookmarks = jq("#enable_bookmarks").prop('checked');
		var notes = jq("#enable_notes").prop('checked');
		var quickSearches = jq("#enable_quicksearches").prop('checked');
		var defaultSearch = jq("#enable_defaultsearch").prop('checked');
		var multiFiltering = jq("#enable_multiplefiltering").prop('checked');
		var url = "${ ui.actionLink('restoreDefaultPrefereces') }";
		var data = {};
		
		if(isNotDefault === true) {
			url = "${ ui.actionLink('saveOrUpdatePrefereces') }";
			data = {"history" : history, "bookmarks" : bookmarks, "notes" : notes, "quickSearches" : quickSearches, "defaultSearch" : defaultSearch, "duplicateResults" : duplicateResults, "multiFiltering" : multiFiltering};
		}
		
		if(isNotDefault === false && (confirm("Are you sure you want to Restore Preferences?")) || isNotDefault === true) {
			jq.ajax({
			    type: "POST",
			    url: url,
			    data: data,
			    dataType: "json",
			    success: function(prefs) {
			        updatePreferencesDisplay(prefs);
			        alert("Successfully Updated Preferences");
			    },
			    error: function(e) {}
			});
		}
	}

</script>



<h1>Manage Preferences</h1>
<div class="pref-summary-item" id="pref-summary-item-toggle">Enable/Disable Features</div>
<div id="pref-summary-item-toggle-details" class="pref-summary-item-details">
	<br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_history" />Enable History Storage/Disable Safe Search</label><br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_bookmarks" />Enable Bookmarking Searches</label><br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_notes" />Enable Commenting/Noting on Searches</label><br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_quicksearches" />Enable Quick Searches</label><br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_defaultsearch" />Enable Default Searching</label><br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_multiplefiltering" />Enable Multiple Results Filtering</label><br />
	<label><input type="checkbox" class="enable-disable-check" id="enable_duplicateresults" />Include Duplicate Results</label><br />
	<br />
</div>

<div class="pref-summary-item" id="pref-summary-item-categoryfilter">Category Filters</div>
<div id="pref-summary-item-categoryfilter-details" class="pref-summary-item-details">
	<br />
	Details for cat filters
	<br /><br />
</div>

<div class="pref-summary-item" id="pref-summary-item-notescolors">Notes Colors</div>
<div id="pref-summary-item-notescolors-details" class="pref-summary-item-details">
	Details for notes colors
</div>

<div id="submit-peferences-section">
	<br />
	<input type="button" value="Restore Default" id="restore-default-prefs" /> <input type="button" value="Apply and Save" id="apply-and-save-prefs" />
	<input type="button" value="Cancel" id="cancel-prefs-changing" />
</div>






