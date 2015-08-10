<style type="text/css">
	#notes-grouped-functionality {
		float:right;
	}
	
	.m-notes-actions {
		cursor:pointer;
	}	
	
</style>


<script type="text/javascript">
	var allNotes =' ${ allFoundNotes }';
    var notesAfterparse = JSON.parse(allNotes);
    
    jq(document).ready(function(event) {
    	displayExistingNotes();
    	
    	jq("body").on("click", "#manage-notes-display", function(event) {
    		if(event.target.id === "select-all-notes") {
    			checkOrUnAllOtherCheckBoxesInADiv("#manage-notes-display", "select-all-notes");
    		}
    		if(event.target.className.indexOf("m-notes-actions") >= 0) {
    			if(event.target.id && event.target.title === "Delete") {
    				if(confirm("Do you really want to delete this Note?")) {
    					deleteSelectedNotes(event.target.id);
    				} else {
    					//Do nothing
    				}
    			} else if (event.target.id && event.target.title === "Save") {
    				if(confirm("Have you finished Editing?")) {
			    		//TODO save the edited Note
			    		saveEdittedNote(event.target.id);		
    				}
    			}
    		}
    	});
    	
		jq("table").on('mouseenter', 'tr', function(event) {
			if(event.delegateTarget.id === "manage-notes-display-table" && event.target.localName !== "th") {
				jq(this).css('background', '#F0EAEA');
			}
		}).on('mouseleave', 'tr', function () {
			jq(this).css('background', '');
		});
		
		jq("#delete-selected-notes").click(function(event) {
			deleteSelectedNotes();
		});
    	
    });
    
    function displayExistingNotes() {
    		var displayNotes;
    		if(notesAfterparse && notesAfterparse.length !== 0) {
    			displayNotes = "<tr><th><input type='checkbox' id='select-all-notes' style='width:55%;height:1.5em;'/>Patient</th><th>Search Phrase</th><th>Comment/Note</th><th>Priority</th><th>Last Updated</th><th>Action</th></tr>";
    			
    			for(i = 0; i < notesAfterparse.length; i++) {
    				var note = notesAfterparse[i];
    				if(note && note.searchPhrase) {
    					var uuid = note.uuid;
	    				var patient = "<td><input type='checkbox' class='select-this-note' id='" + uuid + "' style='width:55%;height:1.5em;'/>" + note.patientFName + "</td>";
	    				var phrase = "<td>" + note.searchPhrase + "</td>";
	    				var comment = "<td><textarea class='m-notes-comment " + uuid + "' style='width: 488px;height:80px;'>" + note.comment + "</textarea></td>";
	    				var priority = setPriorityDisplay(note.priority, uuid);
	    				var date = new Date(note.createdOrLastModifiedAt);
	    				
	    				displayNotes += "<tr>" + patient + phrase + comment + priority + "<td>" + date.toString() + "</td><td><i class='icon-remove medium m-notes-actions' id='" + uuid + "' title='Delete'></i><i class='icon-save medium m-notes-actions' title='Save' id='" + uuid + "'></i></td></tr>";
    				}
    			}
    		}
    		
    		if(displayNotes && displayNotes !== "") {
    			jq("#manage-notes-display-table").html(displayNotes);
    		}
    	}
    	
    	
    	function setPriorityDisplay(priority, uuid) {
    		var allPiorities = ["LOW", "HIGH"];
    		var priorityDisplay = "";
    		
    		if(priority === allPiorities[1]) {
    			priorityDisplay = "<td><select class='m-notes-priority " + uuid + "'><option>" + allPiorities[1] + "</option><option>" + allPiorities[0] + "</option></select></td>";
    		} else {
    			priorityDisplay = "<td><select class='m-notes-priority " + uuid + "'><option>" + allPiorities[0] + "</option><option>" + allPiorities[1] + "</option></select></td>";
    		}
    		
    		return priorityDisplay;
    	}
    	
    	function deleteSelectedNotes(onlyOneNoteUuid) {
    		if(isLoggedInSynchronousCheck()) {
	    		var selectedUuids = returnUuidsOfSeletedNotes(onlyOneNoteUuid);
	    		if(selectedUuids.length !== 0) {
	    			if(onlyOneNoteUuid || (!onlyOneNoteUuid && confirm("Are you sure you want to delete " + selectedUuids.length + " items?"))) {
			    		jq.ajax({
							type: "POST",
							url: "${ ui.actionLink('deleteSelectedNotes') }",
							data: {"selectedUuids":selectedUuids},
							dataType: "json",
							success: function(remainingNotes) {
								notesAfterparse = remainingNotes;
								displayExistingNotes();
									
								alert("Successfully Deleted Note");
							},
							error: function(e) {
							}
						});
					} else {
						//do nothing
					}
		    	} else {
		    		alert("No selected Note to be Deleted");
		    	}
	    	} else {
				location.reload();
			}
    	}
    	
    	function returnUuidsOfSeletedNotes(onlyOneNoteUuid) {
    		var selectedUuids = [];
    		
    		if(onlyOneNoteUuid) {
    			selectedUuids.push(onlyOneNoteUuid);
    		} else {
    			jq('#manage-notes-display input:checked').each(function() {
					var selectedId = jq(this).attr("id");
					
					if(selectedId !== "select-all-notes") {
				    	selectedUuids.push(selectedId);
				    }
				});
    		}
    		
    		return selectedUuids;
    	}
    	
    	function saveEdittedNote(uuid) {
    		if(isLoggedInSynchronousCheck()) {
	    		var comment;
	    		var priority;
	    		
	    		jq(".m-notes-priority").each(function(event) {
	    			if(jq(this).attr("class").indexOf(uuid) >= 0) {
	    				priority = jq(this).find('option:selected').text();
	    			}
	    		});
	    		jq(".m-notes-comment").each(function(event) {
	    			if(jq(this).attr("class").indexOf(uuid) >= 0) {
	    				comment = jq(this).val();
	    			}
	    		});
	    		
	    		if(uuid && comment && priority) {
	    			jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('saveEdittedNote') }",
						data: {"uuid":uuid, "comment":comment, "priority":priority},
						dataType: "json",
						success: function(allExistingNotes) {
							if(allExistingNotes) {
								notesAfterparse = allExistingNotes;
								displayExistingNotes();
										
								alert("Successfully Edited Note");
							} else {
								alert("Note is not edited to be saved!");
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
   	
</script>






<h1>${ ui.message("chartsearch.refApp.manage.notes.title") }</h1>
<div id="notes-grouped-functionality">
	<input type="button" id="delete-selected-notes" value="Delete Selected"/>
</div><br /><br />
<div id="manage-notes-display">
	<table id="manage-notes-display-table"></table>
</div>