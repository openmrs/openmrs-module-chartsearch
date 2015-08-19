<style type="text/css">
	#search-saving-section {
		padding-top:8px;
	}
	
	#favorite-search-record, #comment-on-search-record, #possible-task-list {
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

	#dialogFailureMessage {
		display:none;
		color:red;
	}
	
	#bookmark-header {
	    text-align:center;
	    font-weight:bold;
	    margin-bottom:15px;
	}
	
	#remove-current-bookmark, #cancel-edit-bookmark {
		float:right;
	}
	
	#current-bookmark-name {
		width:364px;
	}
	
	#possible-task-list {
		padding-right:2px;
	}
	
	.detailed_results {
		z-index:3;
	}
	
	#lauche-other-chartsearch-features {
		position: absolute;
		z-index: 2;
		height: 300px;
		width: 220px;
		background-color: #eeeeee;
		margin-left: 688px;
		border-radius: 5px;
		box-shadow: -5px 5px 4px #888888;
		border-right: 1px solid #888888;
	}
	
	.possible-task-list-item {
		background-color: #F0EAEA;
		color:black;
		border: 1px solid white;
		overflow:hidden;
		height: 25px;
		cursor: pointer;
		padding-left: 10px;
	}
	
	.possible-task-list-item:hover, .possible-task-list-item:active {
		background-color: #d6d6d6;
	}
	
	#lauche-stored-bookmark {
		position: absolute;
		z-index: 2;
		height: 400px;
		width: 500px;
		background-color: #F0EAEA;
		margin-left: 185px;
		margin-top: 31px;
		border-radius: 5px;
		border: 1px solid #888888;
		overflow-y: scroll;
	}
	
	#bookmark-manager-lancher {
		float:right;
		margin-right: 10px;
	}
	
	#add-new-note-on-this-search {
		border-top: 1px solid #888888;
		padding-top: 8px;
	}
	
	#new-comment-or-note {
		margin-bottom: 5px;
	}
	
	#previous-notes-on-this-search {
		border: 1px solid #888888;
		border-radius: 5px;
		padding: 8px 8px 8px 8px;
		margin-bottom: 10px;
		height: 250px;
		overflow-y: scroll;
	}
	
	#refresh-notes-display, .remove-this-sNote, #new-note-color, #new-note-priority {
		cursor: pointer;
	}
	
	#lauche-stored-bookmark, #lauche-other-chartsearch-features {
		display:none;
	}
	
</style>

<script type="text/javascript">
	var jq = jQuery;
	var pColors = '${personalColorsNotes}'.replace("[", "").replace("]", "").split(", ");
    var preferences = JSON.parse(jq("#stored-preferences").val());
    
    jq(document).ready(function() {
    	displayExistingBookmarks();
    	displayBothPersonalAndGlobalNotes(jsonAfterParse);
    	updateBookmarksAndNotesUI();
    	displayQuickSearches();
    	applyPreferencesToUIDisplays();
    	addPersonalColorsToSelectColorElement();
    
    	jq("#favorite-search-record").click(function(event) {
    		jq("#favorite-search-record").prop('disabled', true);
    		//TODO check if bookmarkExists, else run below
	    	var phrase = jq("#searchText").val();
		    var selectedCats = getSelectedCategoryNames();
		    var patientId = jq("#patient_id").val().replace("Patient#", "");
			
	    	if(!phrase) {
	    		failedToShowBookmark("A bookmark is only added after searching with a non blank phrase, Enter phrase and search first");
	    	} else {
	    		if(preferences.enableBookmarks === true) {
		    		if(jq("#favorite-search-record").hasClass("icon-star-empty")) {
			    		saveOrUpdateBookmark(selectedCats, phrase, phrase, patientId);
				    } else {
				    	var bookmarks = jsonAfterParse.searchBookmarks;
				    	var bookmarkName;
				    	
					    for (i = 0; i < bookmarks.length; i++) {
							if (bookmarks[i].searchPhrase === phrase) {
								bookmarkName = bookmarks[i].bookmarkName;
								break;
							}
						}
				    	addBookmarkAtUIlayer(phrase, selectedCats, bookmarkName);
				    }
				    updateBookmarksAndNotesUI();
			    }
			}
    	});
    	
    	jq("#submit-edit-bookmark").click(function(event) {
    		var phrase = jq("#searchText").val();
    		var bookmarkName = jq("#current-bookmark-name").val();
			var selectedCats = getSelectedCategoryNames();
			var patientId = jq("#patient_id").val().replace("Patient#", "");
			
			if(preferences.enableBookmarks === true) {
				saveOrUpdateBookmark(selectedCats, phrase, bookmarkName, patientId);
				jq("#favorite-search-record-dialog").dialog("close");
				updateBookmarksAndNotesUI();
			}
    	});
    	
    	jq("#remove-current-bookmark").click(function(event) {
    		var bookmarkUuid = jq("#current-bookmark-object").val();
    		
    		if(bookmarkUuid) {
    			deleteSearchBookmark(bookmarkUuid, true);
    			jq("#favorite-search-record-dialog").dialog("close");
    			removeBookmarkAtUIlayer();
    		}
    		return false;
    	});
    	
    	jq("#cancel-edit-bookmark").click(function(event) {
	    	jq("#favorite-search-record-dialog").dialog("close");
	    	
    		return false;
    	});
    	
    	jq("#comment-on-search-record").click(function(event) {
    		var phrase = jq("#searchText").val();
    		var patientId = jq("#patient_id").val().replace("Patient#", "");
    		refreshSearchNotes();
    		
    		if(phrase) {
	    		checkIfPhraseExisitsInHistory(phrase, patientId, function(exists) {
		    		if(exists) {
		    			displayNotesAtUILevel();
	    			} else {
						failedToShowNotes("Notes are only added and accessed for a search, enter search phrase and search first");
					}
				});
			} else {
				failedToShowNotes("Notes are only added and accessed for a search, enter search phrase and search first");
			}
			updateBookmarksAndNotesUI();
    	});
    	
    	function displayNotesAtUILevel() {
    		invokeDialog("#comment-on-search-record-dialog", "Notes on this Search for this Patient", "550px");
    	}
    	
    	jq("#quick-searches").click(function(event){
    		invokeDialog("#quick-searches-dialog-message", "Quick Searches", "400px");
    		return false;
    	});
    	
    	jq("#possible-task-list").click(function(event) {
    		if(jq("#lauche-other-chartsearch-features").is(':visible')) {
				jq("#lauche-other-chartsearch-features").hide();
				jq("#lauche-stored-bookmark").hide();
			} else {
				jq("#lauche-other-chartsearch-features").show();
			}
    	});
    	
    	jq("#bookmark-task-list-item").click(function(event) {
    		if(jq("#lauche-stored-bookmark").is(':visible')) {
				jq("#lauche-stored-bookmark").hide();
				jq("#bookmark-task-list-item").css('background-color', '#F0EAEA');
			} else {
				jq("#lauche-stored-bookmark").show();
				jq("#bookmark-task-list-item").css('background-color', '#d6d6d6');
			}
    	});
    	
    	jq("body").on("click", "#lauche-stored-bookmark", "#lauche-stored-bookmark.possible-task-list-item", function (event) {
    		if(event.target.localName === "i") {
    			var bookmarkUuid = event.target.id;
    			deleteSearchBookmark(bookmarkUuid, false);
    		} else if(event.target.localName === "a") {
    			window.open('../chartsearch/chartSearchManager.page#bookmarks', '_blank');
    			return false;
    		} else if(event.target.localName === "div" || event.target.localName === "b" || event.target.localName === "em") {
    			var bookmarkUuid = event.target.id;
    			
    			if(bookmarkUuid !== "lauche-stored-bookmark") {
    				searchUsingBookmark(bookmarkUuid);
    			}
    		}
    	});
    	
    	jq("#history-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page#history', '_blank');
    	});
    	
    	jq("#preferences-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page#preferences', '_blank');
    	});
    	
    	jq("#notes-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page#notes', '_blank');
    	});
    	
    	jq("#feedback").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page#feedback', '_blank');
    	});
    	
    	jq("#new-note-color").change(function(event) {
    		var bgColor = jq("#new-note-color option:selected").text();
    		if(bgColor !== "Color") {
    			changeNotesBgColor("#new-comment-or-note", bgColor);
    		} else {
    			changeNotesBgColor("#new-comment-or-note", "white");
    		}
    	});
    	
    	jq("#save-a-new-note").click(function(event) {
    		saveSearchNote();
    	});
    	
    	jq("body").on("click", "#previous-notes-on-this-search", function (event) {
    		if(event.target.localName === "i") {
    			var noteUuid = event.target.id;
    			deleteSearchNote(noteUuid);
    		}
    	});
    	
    	jq("#refresh-notes-display").click(function(event) {
    		refreshSearchNotes();
    	});
    	
    	jq("body").on("click", "#quick-searches-dialog-message", function (event) {
    		if(event.target.localName === "a") {
    			if(event.target.className === "quick-searches-history") {
	    			var searchPhrase = event.target.text;
	    			if(searchPhrase) {
	    				jq("#searchText").val(searchPhrase);
	    				unSelectAllCategories();
	    				submitChartSearchFormWithAjax2(searchPhrase, "");
	    			}
    			} else if(event.target.className === "quick-searches-bookmark") {
    				var uuid = event.target.id;
    				
    				searchUsingBookmark(uuid);
    			}
    		}
    		return false;
    	});
    
	});
	
    function saveOrUpdateBookmark(selectedCats, phrase, bookmarkName, patientId) {
    		checkIfPhraseExisitsInHistory(phrase, patientId, function(exists) {
    			if(exists === true) {
	    			checkIfBookmarkExists(bookmarkName, phrase, selectedCats, patientId, function(bookmarkUuid) {
	    				saveBookmarkAtServerLayer(selectedCats, phrase, bookmarkName, patientId);
	    				jq("#current-bookmark-object").val(bookmarkUuid);
	    				addBookmarkAtUIlayer(phrase, selectedCats, bookmarkName);
	    			});
    			} else {
					failedToShowBookmark("A bookmark is only added after searching with a non blank phrase, Enter phrase and search first");
				}
			});
    	}
    	
    	function checkIfBookmarkExists(bookmarkName, phrase, categories, patientId, taskToRunOnSuccess) {
    		if(categories === "") {
				categories = "none";
			}
    		
    		if(bookmarkName !== "" && phrase) {
	    		jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('checkIfBookmarkExists') }",
					data: {"phrase":phrase, "bookmarkName":bookmarkName, "categories":categories, "patientId":patientId},
					dataType: "json",
					success: function(bookmarkUuid) {
						taskToRunOnSuccess(bookmarkUuid);
						
					},
					error: function(e) {
					}
				});
			}
    	}
    	
    	function getSelectedCategoryNames() {
    		var checkedCat = [];
    		var checkedCagegoryNames = "";
    		
    		jq('input:checkbox.category_check:checked').each(function() {
    			checkedCat.push(jq(this).attr('id').replace('_category', ''));
    		});
    		
    		for(i = 0; i < checkedCat.length; i++) {
    			if(i === checkedCat.length - 1) {
    				checkedCagegoryNames += checkedCat[i];
    			} else {
    				checkedCagegoryNames += checkedCat[i] + ", ";
    			}
    		}
    		return checkedCagegoryNames;
    	}
		
		function saveBookmarkAtServerLayer(selectedCategories, phrase, bookmarkName, patientId) {
			if(isLoggedInSynchronousCheck()) {
				if(phrase && bookmarkName && patientId) {
					if(selectedCategories === "") {
						selectedCategories = "none";
					}
					jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('saveOrUpdateBookmark') }",
						data: {"selectedCategories":selectedCategories, "searchPhrase":phrase, "bookmarkName":bookmarkName, "patientId":patientId},
						dataType: "json",
						success: function(bkObjs) {
							if(bkObjs) {
								jsonAfterParse.searchBookmarks = bkObjs.allBookmarks;
								jq("#current-bookmark-object").val(bkObjs.currentUuid);
								displayExistingBookmarks();
							}
							jq("#favorite-search-record").prop('disabled', false);
						},
						error: function(e) {
							//alert(e);
						}
					});
				}  	
    		} else {
				location.reload();
			}
    	}
    	
    	function addBookmarkAtUIlayer(phrase, selectedCats, bookmarkName) {
	    	jq("#current-bookmark-name").val(bookmarkName);
	    	jq("#bookmark-category-names").text(selectedCats);
	    	jq("#bookmark-search-phrase").text(phrase);
		    	
    		jq("#favorite-search-record").removeClass("icon-star-empty");
		    jq("#favorite-search-record").addClass("icon-star");
			invokeDialog("#favorite-search-record-dialog", "Bookmark '" + phrase + "'", "400px"); 
    	}
    	
    	function removeBookmarkAtUIlayer() {
    		jq("#favorite-search-record").removeClass("icon-star");
	    	jq("#favorite-search-record").addClass("icon-star-empty");
	    	jq("#favorite-search-record-dialog").dialog("close");
    	}
    	
    	function checkIfPhraseExisitsInHistory(searchPhrase, patientId, taskToRunOnSuccess) {
    		if(isLoggedInSynchronousCheck()) {
	    		if(searchPhrase !== "") {
		    		jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('checkIfPhraseExisitsInHistory') }",
						data: {"searchPhrase":searchPhrase, "patientId":patientId},
						dataType: "json",
						success: function(exists) {
							taskToRunOnSuccess(exists);
						},
						error: function(e) {
						}
					});
				}
			} else {
				location.reload();
			}
    	}
    	
    	function failedToShowBookmark(message) {
	    	jq("#dialogFailureMessage").html(message);
			invokeDialog("#dialogFailureMessage", "Bookmarks are disabled!");
    	}
    	
    	function failedToShowNotes(message) {
	    	jq("#dialogFailureMessage").html(message);
			invokeDialog("#dialogFailureMessage", "Notes are disabled!");
    	}
    	
    	function deleteSearchBookmark(bookmarkUuid, bookmarkIsOpen) {
    		if(isLoggedInSynchronousCheck()) {
	    		if(bookmarkUuid) {
	    			jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('deleteSearchBookmark') }",
						data: {"bookmarkUuid":bookmarkUuid},
						dataType: "json",
						success: function(bookmarks) {
							//removeBookmarkAtUIlayer();
							if(bookmarks) {
								jsonAfterParse.searchBookmarks = bookmarks;
								displayExistingBookmarks();
							}
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
	    	var bookmarks;
	    	var bookmarksToDisplay = "";
	    	bookmarks = jsonAfterParse.searchBookmarks.reverse();
	    	
	    	for(i = 0; i < bookmarks.length; i++) {
	    		bookmarksToDisplay += "<div class='possible-task-list-item'  id='" + bookmarks[i].uuid + "' name=' "+ bookmarks[i].bookmarkName + "'><i class='icon-remove delete-this-bookmark' id='" + bookmarks[i].uuid + "' title='Delete This Bookmark'></i>&nbsp&nbsp<b id='" + bookmarks[i].uuid + "'>" + bookmarks[i].bookmarkName + "</b>&nbsp&nbsp-&nbsp&nbsp<em id='" + bookmarks[i].uuid + "'>" + bookmarks[i].categories + "</em></div>";
	    	}
	    	
	    	jq("#lauche-stored-bookmark").html(bookmarksToDisplay + "<a href='' id='bookmark-manager-lancher'>Bookmark Manager</a>");
	    }
	    
	    function searchUsingBookmark(bookmarkUuid) {
	    	if(isLoggedInSynchronousCheck()) {
		    	if(bookmarkUuid) {
		    		jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('getSearchBookmarkSearchDetailsByUuid') }",
						data: {"uuid":bookmarkUuid},
						dataType: "json",
						success: function(bookmarks) {
							var phrase = bookmarks.searchPhrase;
							var cats = bookmarks.categories;
							var bkName = bookmarks.bookmarkName;
							var commaCats = bookmarks.commaCategories;
							
							autoFillSearchForm(phrase, cats, bkName);
							jq("#current-bookmark-name").val(bkName);
							
							submitChartSearchFormWithAjax2(phrase, cats);
						},
						error: function(e) {
						}
					});
		    	}
	    	} else {
				location.reload();
			}
	    }
	    
	    function autoFillSearchForm(searchPhrase, categories) {
		    if (searchPhrase) {
		        jq("#searchText").val(searchPhrase);
		
		        if (categories && categories.length !== 0) {
		            jq('input:checkbox.category_check').each(function() {
		                jq(this).prop('checked', false);
		            });
		
		            for (i = 0; i < categories.length; i++) {
		                jq('input:checkbox.category_check').each(function() {
		                    if (categories[i] === jq(this).val()) {
		                        jq(this).prop('checked', true);
		                    }
		                });
		            }
		        }
		    }
		}
		
		function submitChartSearchFormWithAjax2(phrase, cats) {
			if(isLoggedInSynchronousCheck()) {
			    var patientId = jq("#patient_id").val().replace("Patient#", "");
				var categories = getAllCheckedCategoriesOrFacets();
				var searchText = document.getElementById('searchText');
				
				if(phrase === "") {
					phrase = searchText;
				}
				if(cats === "") {
					cats = categories;
				}
				reInitializeGlobalVars();
				jq(".obsgroup_view").empty();
				jq("#found-results-summary").html('');
				jq("#obsgroups_results").html('<img class="search-spinner" src="../ms/uiframework/resource/uicommons/images/spinner.gif">');
				jq('.ui-dialog-content').dialog('close');	
				jq("#lauche-other-chartsearch-features").hide();
				jq("#lauche-stored-bookmark").hide();
				jq("#chart-previous-searches-display").hide();
				jq('#filter_categories_categories').removeClass('display_filter_onclick');
				
				jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('getResultsFromTheServer') }",
					data: { "patientId":patientId, "phrase":phrase, "categories":cats },
					dataType: "json",
			        success: function(results) {
			            jq("#obsgroups_results").html('');
			            jq(".inside_filter_categories").fadeOut(500);
			
			            jsonAfterParse = JSON.parse(results);
			            
			            storeJsonFromServer(jsonAfterParse);
			            refresh_data(jsonAfterParse);
			            autoClickFirstResultToShowItsDetails(jsonAfterParse);
			
			            jq(".results_table_wrap").fadeIn(500);
			            jq(".inside_filter_categories").fadeIn(500);
			            jq("#bookmark-category-names").text(cats);
			            jq("#bookmark-search-phrase").text(phrase);
			            
			            updateBookmarksAndNotesUI();
			            displayQuickSearches();
			            updateCategeriesAtUIGlobally(jsonAfterParse.appliedCategories);
			        },
			        error: function(e) {}
			    });
		    } else {
				location.reload();
			}
		}
    	
    	function changeNotesBgColor(element, color) {
    		jq(element).css('color', 'black');
    		jq(element).css('background-color', color);
    	}
    	
    	function saveSearchNote() {
    		if(isLoggedInSynchronousCheck()) {
	    		var searchPhrase = jq("#searchText").val();
	    		var patientId = jq("#patient_id").val().replace("Patient#", "");
				var comment = jq("#new-comment-or-note").val();
	    		var priority = jq("#new-note-priority option:selected").text();
	    		var backgroundColor = jq("#new-note-color option:selected").text();
	    		
	    		if(searchPhrase && patientId && comment) {
		    		jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('saveANewNoteOnToASearch') }",
						data: {"searchPhrase":searchPhrase, "patientId":patientId, "comment":comment, "priority":priority, "backgroundColor":backgroundColor},
						dataType: "json",
						success: function(allNotes) {
							if(allNotes) {
								jsonAfterParse.personalNotes = allNotes.personalNotes;
								jsonAfterParse.globalNotes = allNotes.globalNotes;
								jsonAfterParse.currentUser = allNotes.currentUser;
								
								displayBothPersonalAndGlobalNotes(jsonAfterParse);
								jq("#new-comment-or-note").val("");
	    						jq("#new-comment-or-note").css("border", "");
							}
						},
						error: function(e) {
						}
					});
				} else {
					jq("#new-comment-or-note").css("border", "2px solid rgb(252, 0, 27)");
					jq("#new-comment-or-note").focus();
				}
			} else {
				location.reload();
			}
    	}
    	
    	function formatTimeToShow(dateObj) {
    		var ctdDate = new Date(parseInt(dateObj));
		    var hour = ctdDate.getHours() + 1;
		    var min = ctdDate.getMinutes() + 1;
		    var sec = ctdDate.getSeconds() + 1;
		    var timeStr = hour + ":" + min + ":" + sec;
		    		//toUTCString
		    return ctdDate.toTimeString();
    	}
    	
    	function deleteSearchNote(uuid) {
    		if(isLoggedInSynchronousCheck()) {
	    		var searchPhrase = jq("#searchText").val();
	    		var patientId = jq("#patient_id").val().replace("Patient#", "");
				
	    		if(uuid) {
		    		jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('deleteSearchNote') }",
						data: {"uuid":uuid, "searchPhrase":searchPhrase, "patientId":patientId},
						dataType: "json",
						success: function(allNotes) {
							if(allNotes) {
								jsonAfterParse.personalNotes = allNotes.personalNotes;
								jsonAfterParse.globalNotes = allNotes.globalNotes;
								jsonAfterParse.currentUser = allNotes.currentUser;
								
								displayBothPersonalAndGlobalNotes(jsonAfterParse);
							}
						},
						error: function(e) {
						}
					});
				}
			} else {
				location.reload();
			}
    	}
    	
    	function refreshSearchNotes() {
    		if(isLoggedInSynchronousCheck()) {
	    		var searchPhrase = jq("#searchText").val();
	    		var patientId = jq("#patient_id").val().replace("Patient#", "");
				jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('refreshSearchNotes') }",
					data: {"searchPhrase":searchPhrase, "patientId":patientId},
					dataType: "json",
					success: function(allNotes) {
						if(allNotes) {
							jsonAfterParse.personalNotes = allNotes.personalNotes;
							jsonAfterParse.globalNotes = allNotes.globalNotes;
							jsonAfterParse.currentUser = allNotes.currentUser;
							
							displayBothPersonalAndGlobalNotes(jsonAfterParse);
						}
					},
					error: function(e) {
					}
				});
			} else {
				location.reload();
			}
    	}
    	
    	function addPersonalColorsToSelectColorElement() {
    		if(pColors !== undefined && pColors !== "null") {
    			for(i = 0; i < pColors.length; i++) {
    				if(pColors[i] !== null && pColors[i] !== "") {
    					jq("#new-note-color").append("<option>" + pColors[i] + "</option>");
    				}
    			}
    		}
    	}
       
</script>

<i id="favorite-search-record" class="icon-star-empty medium" title="Bookmark Search"></i>
<div class="search-record-dialog-content" id="favorite-search-record-dialog">
	<div id="bookmark-header">${ ui.message("chartsearch.searchSaving.editBookmark") }</div>
	<div id="bookmark-info">
        <div id="bookmark-icon-and-remove">
            <i class="icon-star medium"></i><a id="remove-current-bookmark" href="">${ ui.message("chartsearch.searchSaving.remove") }</a>
    	</div>
        <div id="bookmark-name-and-cats">
        	<input type="hidden" id="current-bookmark-object" value="" />
            <b>${ ui.message("chartsearch.searchSaving.name") }:</b>&nbsp&nbsp<input type="textbox" id="current-bookmark-name" value="" /><br /><br />
            <b>${ ui.message("chartsearch.searchSaving.searchPhrase") }:</b> <label id="bookmark-search-phrase"></label><br /><br />
            <b>${ ui.message("chartsearch.searchSaving.categories") }:</b> <label id="bookmark-category-names"></label><br /><br />
            <input type="button" id="cancel-edit-bookmark" value="Cancel" />
            <input type="button" id="submit-edit-bookmark" value="Done" /><br />
        </div>
	</div>
</div>

<i id="comment-on-search-record" class="icon-comment-alt medium" title="Comment on Search"></i>
<div class="search-record-dialog-content" id="comment-on-search-record-dialog">
	<b>${ ui.message("chartsearch.searchSaving.previousNotes") }:</b>
	<div id="previous-notes-on-this-search">
		
	</div>
	<div id="add-new-note-on-this-search">
		<b>${ ui.message("chartsearch.searchSaving.addNote") }:</b><br />
		${ ui.message("chartsearch.searchSaving.comment") }:
		<textarea id="new-comment-or-note" style="height: 80px; width: 512px;"></textarea>
		<br />
		<i class="icon-user-md medium"></i>&nbsp&nbsp&nbsp&nbsp&nbsp
		<select id="new-note-priority" title="Priority/Severity of this note">
			<option>${ ui.message("chartsearch.searchSaving.priority") }</option>
			<option>${ ui.message("chartsearch.searchSaving.priority.low") }</option>
			<option>${ ui.message("chartsearch.searchSaving.priority.high") }</option>
		</select>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		<select id="new-note-color" title="Background color for this Note">
			<option>${ ui.message("chartsearch.searchSaving.note.color") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.orange") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.yellow") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.violet") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.lime") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.beige") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.cyan") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.lightgreen") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.deeppink") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.magenta") }</option>
			<option>${ ui.message("chartsearch.searchSaving.note.red") }</option>
		</select>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		<i class="icon-refresh medium" id="refresh-notes-display"></i>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		<input type="button" id="save-a-new-note" value="Save Note" />
	</div>
</div>

<div id="dialogFailureMessage"></div>

<a id="quick-searches" href="" title="Quick Searches">QuickSearch</a>
<div class="search-record-dialog-content" id="quick-searches-dialog-message"></div>


<i id="possible-task-list" class="icon-reorder medium" title="List Items"></i>
<div id="lauche-stored-bookmark"></div>

<div id="lauche-other-chartsearch-features">
	<div class="possible-task-list-item" id="history-task-list-item">${ ui.message("chartsearch.searchSaving.history") }</div>
	<div class="possible-task-list-item" id="bookmark-task-list-item">${ ui.message("chartsearch.searchSaving.bookmarks") }</div>
	<div class="possible-task-list-item" id="preferences-task-list-item">${ ui.message("chartsearch.searchSaving.preferences") }</div>
	<div class="possible-task-list-item" id="notes-task-list-item">${ ui.message("chartsearch.searchSaving.notes") }</div>
	<div class="possible-task-list-item" id="feedback">${ ui.message("chartsearch.searchSaving.feedback") }</div>
</div>
