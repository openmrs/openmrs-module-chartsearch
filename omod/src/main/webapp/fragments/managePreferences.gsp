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
	
	#selectedColors {
		width:940px;
	}
</style>

<script type="text/javascript">
	var jq = jQuery;
	var prefs ='${preferences}';
	var daemonPrefs = '${daemonPreferences}';
    var preferences = JSON.parse(prefs);
    var daemonPreferences = JSON.parse(daemonPrefs);//used for restoring default preferences
    var catFilters = '${categoryFilters}';
    var cats = JSON.parse(catFilters);
    var defaultColors = jq.fn.colorPicker.defaults.colors;
	var allColors = '${allColors}'.replace("[", "").replace("]", "").split(", ");
	var redBasedColors = '${redBasedColors}'.replace("[", "").replace("]", "").split(", ");
	var greenBasedColors = '${greenBasedColors}'.replace("[", "").replace("]", "").split(", ");
    var blueBasedColors = '${blueBasedColors}'.replace("[", "").replace("]", "").split(", ");
	var personalNotesColors = '${personalColorsNotes}'.replace("[", "").replace("]", "").split(", ");
        	
	jq(document).ready(function() {
	
		displayCategoriesForPreferences(cats);
		updatePreferencesDisplay(preferences);
		
		jq('#color1').colorPicker();
	
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
			if(checkIfCatDisplayNamesHaveRightNumberOfCharacters()) {
				submitPreferences(true);
			} else {
				alert("Display Names must not exceed 12 characters!");
			}
		});
		
		jq("#cancel-prefs-changing").click(function(event) {
			updatePreferencesDisplay(preferences);
		});
		
		jq("#color1").change(function(event) {
			var color = jq("#color1").val();
			var previousDelectedColors = jq("#selectedColors").val() === "" ? "" : jq("#selectedColors").val() + ", ";
				
			if(color !== undefined && color !== "" && previousDelectedColors.indexOf(color) < 0) {
				jq("#selectedColors").val(previousDelectedColors + color);
			}
		});
		
		jq("#choose-color-collection").change(function(event) {
			var selectedColorColn = jq("#choose-color-collection :selected").text();
			
			chooseColorsCollectionToChooseFrom(selectedColorColn);
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
			jq("#selectedColors").val(prefs.notesColors);
			displayCategoriesForPreferences(prefs.categoryFilters);
		}
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
		var categories = [];
		var colors = jq("#selectedColors").val();
		
		jq(".pref-cat-names").each(function(event) {
			var id = jq(this).attr("id");
			var name = jq(this).attr("title");
			var dName = jq(this).val();
			var obj = id + "<=>" + dName + "<=>" + name;
		
			if(name !== "") {
				categories.push(obj);
			}
		});
		
		if(isNotDefault === true) {
			url = "${ ui.actionLink('saveOrUpdatePrefereces') }";
			data = {"history" : history, "bookmarks" : bookmarks, "notes" : notes, "quickSearches" : quickSearches, "defaultSearch" : defaultSearch, "duplicateResults" : duplicateResults, "multiFiltering" : multiFiltering, "categories" : categories, "selectedColors":colors};
		}
		
		console.log(data);
		if(isNotDefault === false && (confirm("Are you sure you want to Restore Preferences?")) || isNotDefault === true) {
			jq.ajax({
			    type: "POST",
			    url: url,
			    data: data,
			    dataType: "json",
			    success: function(prefs) {
			        updatePreferencesDisplay(prefs);
			        preferences = prefs;
			        alert("Successfully Updated Preferences");
			    },
			    error: function(e) {
			    	alert(e);
			    }
			});
		}
	}

	function chooseColorsCollectionToChooseFrom(selectedCategory) {
		if(selectedCategory === "All Colors") {
			jq.fn.colorPicker.defaults.colors = allColors;
		} else if(selectedCategory === "Red-based Colors") {
			jq.fn.colorPicker.defaults.colors = redBasedColors;
		} else if(selectedCategory === "Green-based Colors") {
			jq.fn.colorPicker.defaults.colors = greenBasedColors;
		} else if(selectedCategory === "Blue-based Colors") {
			jq.fn.colorPicker.defaults.colors = blueBasedColors;
		} else {
			jq.fn.colorPicker.defaults.colors = defaultColors;
		}
		
		jq('.colorPicker-picker').remove();
		jq('#color1').colorPicker();
	}
</script>

<% ui.includeJavascript("chartsearch", "jquery.colorPicker.js") %>

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
		<b>NOTE:</b> Category Names are displayed by default, providing your preferred Display name will instead display your name than the default<br /><br />
		<table id="preferences-cats"></table>
	<br /><br />
</div>

<div class="pref-summary-item" id="pref-summary-item-notescolors">Notes Background Colors</div>
<div id="pref-summary-item-notescolors-details" class="pref-summary-item-details">
	<br />
	<div><label for="color1">Click and Select color to add</label>
		<input id="color1" name="color1" type="text" value="#333399" />
		<select id="choose-color-collection">
				<option>Color Collection To choose from</option>
				<option>Default Colors</option>
				<option>Red-based Colors</option>
				<option>Green-based Colors</option>
				<option>Blue-based Colors</option>
		</select><br />
		Selected Colors: <textarea id="selectedColors" disabled></textarea>
	</div>
</div>

<div id="submit-peferences-section">
	<br />
	<input type="button" value="Restore Default" id="restore-default-prefs" /> <input type="button" value="Apply and Save" id="apply-and-save-prefs" />
	<input type="button" value="Cancel" id="cancel-prefs-changing" />
</div>