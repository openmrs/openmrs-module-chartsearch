<style type="text/css">
	#search-saving-section {
		padding-top:8px;
	}
	
	#delete-search-record, #favorite-search-record, #comment-on-search-record, #possible-task-list {
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
		z-index:1;
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
	
</style>

<script type="text/javascript">
	var jq = jQuery;
    
    jq(document).ready(function() {
    
    	jq("#lauche-stored-bookmark").hide();
    	jq("#lauche-other-chartsearch-features").hide();
    	
    	displayExistingBookmarks();
    	
    	jq("#delete-search-record").click(function(event) {
    		invokeDialog("#delete-search-record-dialog", "Delete this Search Record", "300px");
    	});
    
    	jq("#favorite-search-record").click(function(event) {
    		jq("#favorite-search-record").prop('disabled', true);
    		//TODO check if bookmarkExists, else run below
	    	var phrase = jq("#searchText").val();
		    var selectedCats = getSelectedCategoryNames();
		    var patientId = jq("#patient_id").val();
	    	
	    	if(!phrase) {
	    		failedToShowBookmark("A bookmark is only added after searching with a non blank phrase, Enter phrase and search first");
	    	} else {
		    	saveOrUpdateBookmark(selectedCats, phrase, phrase, patientId);
			}
    	});
    	
    	jq("#submit-edit-bookmark").click(function(event) {
    		var phrase = jq("#searchText").val();
    		var bookmarkName = jq("#current-bookmark-name").val();
			var selectedCats = getSelectedCategoryNames();
			var patientId = jq("#patient_id").val();
			
			saveOrUpdateBookmark(selectedCats, phrase, bookmarkName, patientId);
			jq("#favorite-search-record-dialog").dialog("close");
    	});
    	
    	jq("#remove-current-bookmark").click(function(event) {
    		var bookmarkUuid = jq("#current-bookmark-object").attr('name');
    		
    		if(bookmarkUuid) {
    			deleteSearchBookmark(bookmarkUuid, true);
    		}
    		return false;
    	});
    	
    	jq("#cancel-edit-bookmark").click(function(event) {
	    	jq("#favorite-search-record-dialog").dialog("close");
	    	jq("#favorite-search-record").prop('disabled', false);
	    	
    		return false;
    	});
    	
    	jq("#comment-on-search-record").click(function(event) {
    		var phrase = jq("#searchText").val();
    		
    		invokeDialog("#comment-on-search-record-dialog", "Add Notes to This Search");
    		/*
    		if(historyExists) {//TODO search should be automatically bookmarked
				invokeDialog("#comment-on-search-record-dialog", "Add Notes to This Search", "300px");
			} else {
				jq("#dialogFailureMessage").html("Search first to add Notes to your search");
				invokeDialog("#dialogFailureMessage", "Adding Notes is disabled Now", "300px");
			}
			*/
    	});
    	
    	jq("#quick-searches").click(function(event){
    		invokeDialog("#quick-searches-dialog-message", "Quick Searches", "300px");
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
    			window.open('../chartsearch/chartSearchManager.page?tab=2', '_blank');
    			return false;
    		} else if(event.target.localName === "div" || event.target.localName === "b" || event.target.localName === "em") {
    			var bookmarkUuid = event.target.id;
    			
    			if(bookmarkUuid !== "lauche-stored-bookmark") {
    				searchUsingBookmark(bookmarkUuid);
    			}
    		}
    	});
    	
    	jq("#history-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page?tab=3', '_blank');
    	});
    	
    	jq("#preferences-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page?tab=1', '_blank');
    	});
    	
    	jq("#commands-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page?tab=4', '_blank');
    	});
    	
    	jq("#settings-task-list-item").click(function(event) {
    		window.open('../chartsearch/chartSearchManager.page?tab=5', '_blank');
    	});
    	
    	function saveOrUpdateBookmark(selectedCats, phrase, bookmarkName, patientId) {
    		checkIfPhraseExisitsInHistory(phrase, function(exists) {
    			if(exists) {
	    			checkIfBookmarkExists(bookmarkName, phrase, selectedCats, function(bookmarkExists) {
	    				addBookmarkAtUIlayer(phrase, selectedCats, bookmarkName);
	    				
	    				if(!bookmarkExists) {
	    					saveBookmarkAtServerLayer(selectedCats, phrase, bookmarkName, patientId);
	    				}
	    			});
    			} else {
					failedToShowBookmark("A bookmark is only added after searching with a non blank phrase, Enter phrase and search first");
				}
			});
    	}
    	
    	function checkIfBookmarkExists(bookmarkName, phrase, categories, taskToRunOnSuccess) {
    		if(categories === "") {
				categories = "none";
			}
    		
    		if(bookmarkName !== "" && phrase) {
	    		jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('checkIfBookmarkExists') }",
					data: {"phrase":phrase, "bookmarkName":bookmarkName, "categories":categories},
					dataType: "json",
					success: function(exists) {
						taskToRunOnSuccess(exists);
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
							jq("#current-bookmark-object").attr('name', bkObjs.currentUuid);
							jq("#current-bookmark-object").val(bookmarkName);
							displayExistingBookmarks();
						}
						jq("#favorite-search-record").prop('disabled', false);
					},
					error: function(e) {
						//alert(e);
					}
				});
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
    	
    	/*Overrides jq.("").dialog()*/
    	function invokeDialog(dialogMessageElement, dialogTitle, dialogWidth) {
	    	jq(dialogMessageElement).dialog({
				title: dialogTitle,
				width:dialogWidth
			});
    	}
    	
    	function checkIfPhraseExisitsInHistory(searchPhrase, taskToRunOnSuccess) {
    		if(searchPhrase !== "") {
	    		jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('checkIfPhraseExisitsInHistory') }",
					data: {"searchPhrase":searchPhrase},
					dataType: "json",
					success: function(exists) {
						taskToRunOnSuccess(exists);
					},
					error: function(e) {
					}
				});
			}
    	}
    	
    	function failedToShowBookmark(message) {
	    	jq("#dialogFailureMessage").html(message);
			invokeDialog("#dialogFailureMessage", "Bookmarks are disabled!");
    	}
    	
    	function deleteSearchBookmark(bookmarkUuid, bookmarkIsOpen) {
    		if(bookmarkUuid) {
    			jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('deleteSearchBookmark') }",
					data: {"bookmarkUuid":bookmarkUuid},
					dataType: "json",
					success: function(bookmarks) {
						if(bookmarks) {
							jsonAfterParse.searchBookmarks = bookmarks;
							if(bookmarkIsOpen) {
								removeBookmarkAtUIlayer();
							}
							displayExistingBookmarks();
						}
					},
					error: function(e) {
					}
				});
			}
    	}
    	
	    function displayExistingBookmarks() {
	    	var bookmarks;
	    	var bookmarksToDisplay = "";
	    	
	    	//if(wholePageIsToBeLoaded) {
	    		bookmarks = jsonAfterParse.searchBookmarks.reverse();
	    	/*} else {
	    		bookmarks = jsonAfterParse.searchBookmarks;
	    	}*/
	    	
	    	for(i = 0; i < bookmarks.length; i++) {
	    		bookmarksToDisplay += "<div class='possible-task-list-item'  id='" + bookmarks[i].uuid + "' name=' "+ bookmarks[i].searchPhrase + "'><i class='icon-remove delete-this-bookmark' id='" + bookmarks[i].uuid + "'></i>&nbsp&nbsp<b id='" + bookmarks[i].uuid + "'>" + bookmarks[i].bookmarkName + "</b>&nbsp&nbsp-&nbsp&nbsp<em id='" + bookmarks[i].uuid + "'>" + bookmarks[i].categories + "</em></div>";
	    	}
	    	
	    	jq("#lauche-stored-bookmark").html(bookmarksToDisplay + "<a href='' id='bookmark-manager-lancher'>Bookmark Manager</a>");
	    }
	    
	    function searchUsingBookmark(bookmarkUuid) {
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
						submitChartSearchFormWithAjax2(phrase, cats);
					},
					error: function(e) {
					}
				});
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
		
		function submitChartSearchFormWithAjax2(phrase, cats, bkName) {
		    var searchText = document.getElementById('searchText');
		
		    jq("#lauche-stored-bookmark").hide();
		    jq("#lauche-other-chartsearch-features").hide();
		    jq("#chart-previous-searches-display").hide();
		    jq(".obsgroup_view").empty();
		    jq("#found-results-summary").html('');
		    jq("#obsgroups_results").html('<img class="search-spinner" src="../ms/uiframework/resource/uicommons/images/spinner.gif">');
		
		    jq.ajax({
		        type: "POST",
		        url: "${ ui.actionLink('getResultsFromTheServer') }",
		        data: jq('#chart-search-form-submit').serialize(),
		        dataType: "json",
		        success: function(results) {
		            jq("#obsgroups_results").html('');
		            jq(".inside_filter_categories").fadeOut(500);
		
		            jsonAfterParse = JSON.parse(results);
		            refresh_data();
		
		            jq(".results_table_wrap").fadeIn(500);
		            jq('#first_obs_single').trigger('click');
		            jq(".inside_filter_categories").fadeIn(500);
		            jq("#current-bookmark-name").val(bkName);
		            jq("#bookmark-category-names").text(cats);
		            jq("#bookmark-search-phrase").text(phrase);
		            jq("#favorite-search-record").removeClass("icon-star-empty");
		            jq("#favorite-search-record").addClass("icon-star");
		            if(cats.length !== 0) {
		            	if(cats[0] !== "") {
		            		jq("#category-filter_method").text(capitalizeFirstLetter(cats[0]) + "...");
		            	}
		            }
		        },
		        error: function(e) {}
		    });
		}
    
	});
    
       
</script>



<i id="delete-search-record" class="icon-remove medium" title="Reset"></i>
<div class="search-record-dialog-content" id="delete-search-record-dialog">Do you want to delete?</div>

<i id="favorite-search-record" class="icon-star-empty medium" title="Bookmark Search"></i>
<div class="search-record-dialog-content" id="favorite-search-record-dialog">
	<div id="bookmark-header">Edit This Bookmark</div>
	<div id="bookmark-info">
        <div id="bookmark-icon-and-remove">
            <i class="icon-star medium"></i><a id="remove-current-bookmark" href="">Remove</a>
    	</div>
        <div id="bookmark-name-and-cats">
        	<input type="hidden" id="current-bookmark-object" value="" />
            <b>Name:</b>&nbsp&nbsp<input type="textbox" id="current-bookmark-name" value="" /><br /><br />
            <b>Search Phrase:</b> <label id="bookmark-search-phrase"></label><br /><br />
            <b>Categories:</b> <label id="bookmark-category-names"></label><br /><br />
            <input type="button" id="cancel-edit-bookmark" value="Cancel" />
            <input type="button" id="submit-edit-bookmark" value="Done" /><br />
            <a id="all-bookmarks" href="">All Bookmarks</a>
        </div>
	</div>
</div>

<i id="comment-on-search-record" class="icon-comment medium" title="Comment on Search"></i>
<div class="search-record-dialog-content" id="comment-on-search-record-dialog">Add Notes to a search</div>

<div id="dialogFailureMessage"></div>

<a id="quick-searches" href="" title="Quick Searches">Q.k-<i class="icon-search"></i></a>
<div class="search-record-dialog-content" id="quick-searches-dialog-message"></div>


<i id="possible-task-list" class="icon-reorder medium" title="List Items"></i>
<div id="lauche-stored-bookmark"></div>

<div id="lauche-other-chartsearch-features">
	<div class="possible-task-list-item" id="history-task-list-item">History Manager</div>
	<div class="possible-task-list-item" id="bookmark-task-list-item">Bookmarks</div>
	<div class="possible-task-list-item" id="preferences-task-list-item">Preferences</div>
	<div class="possible-task-list-item" id="commands-task-list-item">Commands</div>
	<div class="possible-task-list-item" id="settings-task-list-item">Settings</div>
</div>
