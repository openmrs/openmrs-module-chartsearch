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
	
</style>

<script type="text/javascript">
	var jq = jQuery;
    
    jq(document).ready(function() {
    
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
    		deleteBookmark();
    		
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
    		var dataO;
    		
    		if(categories) {
    			dataO = {"phrase":phrase, "bookmarkName":bookmarkName, "categories":categories};
    		} else {
    			dataO = {"phrase":phrase, "bookmarkName":bookmarkName};
    		}
    		if(bookmarkName !== "" && phrase) {
	    		jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('checkIfBookmarkExists') }",
					data: dataO,
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
    	
    	function deleteBookmark() {
    		//TODO removeBookmarkAtServerLayer();
    		removeBookmarkAtUIlayer();
    	}
		
		function saveBookmarkAtServerLayer(selectedCategories, phrase, bookmarkName, patientId) {
			if(phrase && bookmarkName && patientId) {
				var dataObj;
				if(selectedCategories) {
					dataObj = {"selectedCategories":selectedCategories, "searchPhrase":phrase, "bookmarkName":bookmarkName, "patientId":patientId};
				} else {
					dataObj = {"searchPhrase":phrase, "bookmarkName":bookmarkName, "patientId":patientId};
				}
				
				jq.ajax({
					type: "POST",
					url: "${ ui.actionLink('saveOrUpdateBookmark') }",
					data: dataObj,
					dataType: "json",
					success: function(bookmarkUuid) {
						if(bookmarkUuid) {
							jq("#current-bookmark-object").attr('name', bookmarkUuid);
							jq("#current-bookmark-object").val(bookmarkName);
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
