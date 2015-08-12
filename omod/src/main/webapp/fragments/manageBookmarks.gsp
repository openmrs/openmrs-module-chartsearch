<style type="text/css">
	#grouped-functionality {
		float:right;
	}
	
	#selected-bookmark-dialog-content {
		display:none;
	}
	
	.ui-dialog > .ui-widget-header {
		background: #00463f;
		color:white;
	}
	
	.ui-widget-header .ui-icon {
		background-image: url(../scripts/jquery-ui/css/green/images/ui-icons_ffffff_256x240.png);
	}
	
	input[type='textbox'] {
		width:280px;
	}
	
</style>

<script type="text/javascript">
	var allReturnedBookmarks =' ${ allFoundBookmarks }';
    var bookmarksAfterparse = JSON.parse(allReturnedBookmarks).reverse();
	
	jq(document).ready(function() {
		displayExistingBookmarks();
	
		jq("body").on("click", "#bookmarks-section", function() {
			if(event.target.id === "bookmark-check-all") {
				checkOrUnAllOtherCheckBoxesInADiv("#bookmarks-section", "bookmark-check-all");
			}
		});
		
		jq("table").on('mouseenter', 'tr', function(event) {
			if(event.delegateTarget.id !== "manage-notes-display-table" && event.delegateTarget.id !== "patient-search-results-table" && event.target.localName !== "th" && event.target.localName !== "input" && event.target.localName !== "label" && event.target.offsetParent.id !== "todays-history" && event.target.offsetParent.id !== "preferences-cats" && event.target.offsetParent.id !== "this-weeks-history" && event.target.offsetParent.id !== "this-month-history" && event.target.offsetParent.id !== "other-history" && event.target.offsetParent.id !== "this-year-history") {
				jq(this).css("cursor", "pointer");
				jq(this).css('background', '#F0EAEA');
			}
		}).on('mouseleave', 'tr', function () {
			jq(this).css('background', '');
			jq(this).css("cursor", "");
		});
		
		jq("body").on("click", "#returned-search-bookmarks tr", function(event) {
			if(event.target.localName !== "input" && event.target.localName !== "label" && event.target.id !== "bookmarks-tb-header") {
				var bookmarkUuid = jq(this).attr("id");
				
				invokeBookmarkDetailsDialog(bookmarkUuid);
			}
		});
		
		jq("#delete-selected-bookmarks").click(function(event) {
			deleteAllSelectedBookmarks();
		});
		
		jq("#save-default-search").click(function(event) {
			setSelectedBookmarkAsDefaultSearch();
		});
		
		jq("#dialog-bookmark-save").click(function(event) {
			var bookmarkUuid = jq("#dialog-bookmark-uuid").val();
			var bkName = jq("#dialog-bookmark-name").val();
			var phrase = jq("#dialog-bookmark-phrase").val();
			var categories = [];
			jq("#dialog-bookmark-categories option:selected").each(function(event) {
				categories.push(jq(this).text());
			});
			var cats = categories.join(', ');
			
			if(confirm("Are you sure you want to Save Changes?")) {
				saveBookmarkProperties(bookmarkUuid, bkName, phrase, cats);
			}
		});
		
		jq("#dialog-bookmark-delete").click(function(event) {
			var bookmarkUuid = jq("#dialog-bookmark-uuid").val();
			
			if(confirm("Are you sure want to Delete This Bookmark?")) {
				deleteBookmarkInTheDialog(bookmarkUuid);
			}
		});
		
	});
	
		function invokeBookmarkDetailsDialog(bookmarkUuid) {
			if(isLoggedInSynchronousCheck()) {
				jq('#dialog-bookmark-categories').html("");
				
				if(bookmarkUuid) {
			    	jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('fetchBookmarkDetails') }",
						data: {"uuid":bookmarkUuid},
						dataType: "json",
						success: function(bookmarks) {
							var phrase = bookmarks.searchPhrase;
							var cats = bookmarks.categories;
							var bkName = bookmarks.bookmarkName;
							
							//TODO set dialog element values
							jq("#dialog-bookmark-uuid").val(bookmarkUuid);
							jq("#dialog-bookmark-name").val(bkName);
							jq("#dialog-bookmark-phrase").val(phrase);
							jq.each(cats, function(key, value) {   
								jq('#dialog-bookmark-categories').append(jq("<option></option>").attr("value",key).text(value)); 
							});
							
							if(jq('#dialog-bookmark-categories option').text() !== "") {
								jq('#dialog-bookmark-categories option').prop('selected', true);
							}
							invokeDialog("#selected-bookmark-dialog-content", "Editing '" + bkName + "' Bookmark", "450px");
						},
						error: function(e) {
						}
					});
		    	}
		    } else {
				location.reload();
			}
	    }
		
		function displayExistingBookmarks() {
			var trBookmarkEntries = "";
			var thBookmarks = "<tr id='bookmarks-tb-header'><th><label><input type='checkbox' id='bookmark-check-all' > Select (PatientFName)</label></th><th>Default Search</th><th>Bookmark Name </th><th>Search Phrase</th><th>Categories</th></tr>";
			
			if(bookmarksAfterparse.length != 0) {
				for(i = 0; i < bookmarksAfterparse.length; i++) {
					var bookmark = bookmarksAfterparse[i];
					var displayDefaultSearch = "";
					if(bookmark.isDefaultSearch) {
						displayDefaultSearch = "<input name='radiogroup' type='radio' id='" + bookmark.uuid + "' checked >";
					} else {
						displayDefaultSearch = "<input name='radiogroup' type='radio' id='" + bookmark.uuid + "' >";
					}
					
					trBookmarkEntries += "<tr id='" + bookmark.uuid + "'><td><label><input type='checkbox' class='bookmark-check' id='" + bookmark.uuid + "' > (" + bookmark.patientFamilyName + ")</label></td><td>" + displayDefaultSearch + "</td><td>" + bookmark.bookmarkName + "</td><td>" + bookmark.searchPhrase + "</td><td>" + bookmark.categories + "</td></tr>";
				}
			}
			
			if(trBookmarkEntries !== "") {
				jq("#returned-search-bookmarks").html(thBookmarks + trBookmarkEntries);
				jq("#grouped-functionality").show();
			} else {
				jq("#returned-search-bookmarks").html("You currently have no saved bookmarks to manage");
				jq("#grouped-functionality").hide();
			}
		}
		
		function deleteAllSelectedBookmarks() {
			if(isLoggedInSynchronousCheck()) {
	    		var selectedUuids = returnUuidsOfSeletedBookmarks();
	    		var deleteConfirmMsg = "Are you sure you want to delete " + selectedUuids.length + " Item(s)!";
	    		
	    		if(selectedUuids.length !== 0) {
		    		if(confirm(deleteConfirmMsg)) {
		    			jq.ajax({
							type: "POST",
							url: "${ ui.actionLink('deleteSelectedBookmarks') }",
							data: {"selectedUuids":selectedUuids},
							dataType: "json",
							success: function(remainingBookmarks) {
								bookmarksAfterparse = remainingBookmarks.reverse();
								
								displayExistingBookmarks();
							},
							error: function(e) {
							}
						});
		    		} else {
		    			//alert("DELETE Cancelled");
		    		}
	    		} else {
	    			alert("Select at-least one Bookmark to be deleted!");
	    		}
    		} else {
				location.reload();
			}
    	}
    	
    	function setSelectedBookmarkAsDefaultSearch() {
    		if(isLoggedInSynchronousCheck()) {
	    		var selectedDefaultBkuuid = returnUuidOfSelectedDefaultBookmark();
	    		
	    		if(selectedDefaultBkuuid) {
	    			jq.ajax({
							type: "POST",
							url: "${ ui.actionLink('setBookmarkAsDefaultSearch') }",
							data: {"selectedUuid":selectedDefaultBkuuid},
							dataType: "json",
							success: function(bookmarks) {
								bookmarksAfterparse = bookmarks.reverse();
								
								displayExistingBookmarks();
								alert("Successfully saved default search :-)");
							},
							error: function(e) {
							}
						});
	    		} else {
	    			alert("Please select a bookmark to set as a default search and try again!");
	    		}
	    	} else {
				location.reload();
			}
    	}
    	
    	function returnUuidsOfSeletedBookmarks() {
    		var selectedBookmarkUuids = [];
	    	
			jq('#bookmarks-section input:checked').each(function() {
				var selectedId = jq(this).attr("id");
				
				if(selectedId !== "bookmark-check-all" && jq(this).attr("type") !== "radio" && jq(this).attr("type") === "checkbox" && jq(this).attr("class") === "bookmark-check") {
			    	selectedBookmarkUuids.push(selectedId);
			    }
			});
			return selectedBookmarkUuids;
    	}
    	
    	function returnUuidOfSelectedDefaultBookmark() {
    		var selecteduuid;
    		
    		jq('#bookmarks-section input:checked').each(function() {
    			var selectedId = jq(this).attr("id");
    			
    			if(selectedId !== "bookmark-check-all" && jq(this).attr("name") === "radiogroup" && jq(this).attr("type") === "radio") {
    				selecteduuid = selectedId;
    			}
    		});
    		return selecteduuid;
    	}
    	
    	function saveBookmarkProperties(bookmarkUuid, bkName, phrase, categories) {
	    	if(isLoggedInSynchronousCheck()) {
	    		jq('#selected-bookmark-dialog-content').dialog('close');
	    		if(bookmarkUuid !== "" && bkName !== "" && phrase !== "") {
	    			jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('saveBookmarkProperties') }",
						data: { "bookmarkUuid":bookmarkUuid, "bookmarkName":bkName, "searchPhrase":phrase, "selectedCategories":categories },
						dataType: "json",
						success: function(remainingBookmarks) {
							bookmarksAfterparse = remainingBookmarks.reverse();
								
							displayExistingBookmarks();
						},
						error: function(e) {
						}
					});
	    		}
    		} else {
				location.reload();
			}
    	}
    	
    	function deleteBookmarkInTheDialog(bookmarkUuid) {
    		if(isLoggedInSynchronousCheck()) {
	    		jq('#selected-bookmark-dialog-content').dialog('close');
	    		if(bookmarkUuid !== "") {
	    			jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('deleteBookmarkInTheDialog') }",
						data: { "bookmarkUuid":bookmarkUuid },
						dataType: "json",
						success: function(remainingBookmarks) {
							bookmarksAfterparse = remainingBookmarks.reverse();
								
							displayExistingBookmarks();
						},
						error: function(e) {
						}
					});
	    		}
	    	} else {
				location.reload();
			}
    	}
		
</script>


<h1>${ ui.message("chartsearch.refApp.manage.bookmarks.title") }</h1>
<b>${ ui.message("chartsearch.note") }</b>
<p>${ ui.message("chartsearch.refApp.manage.bookmarks.searchNote") }</p>
<div id="selected-bookmark-dialog-content">
	<input type="hidden" id="dialog-bookmark-uuid" value="">
	${ ui.message("chartsearch.refApp.manage.bookmarks.bookmarkName") }: <input type="textbox" id="dialog-bookmark-name" value="" /><br /><br />
	${ ui.message("chartsearch.refApp.manage.bookmarks.searchPhrase") }: <input type="textbox" id="dialog-bookmark-phrase" disabled value="" /><br /><br />
	${ ui.message("chartsearch.refApp.manage.bookmarks.categories") }: 
		<select multiple id="dialog-bookmark-categories">
		</select> <b>${ ui.message("chartsearch.tip") }</b> ${ ui.message("chartsearch.refApp.manage.bookmarks.shortcutTip") }<br /><br />
	<input type="button" value="Save Changes" id="dialog-bookmark-save" />
	<input type="button" value="Delete This Bookmark" id="dialog-bookmark-delete" />
</div>

<div id="grouped-functionality">
	<input type="button" id="delete-selected-bookmarks" value="Delete Selected"/>
	<input type="button" id="save-default-search" value="Save Default Search"/><br /><br />
</div>

<div id="bookmarks-section">
	<table id="returned-search-bookmarks"></table>
</div>
