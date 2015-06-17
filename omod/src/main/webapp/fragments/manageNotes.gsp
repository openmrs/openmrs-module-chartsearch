<style type="text/css">

</style>


<script type="text/javascript">
	var allNotes =' ${ allFoundNotes }';
    var notesAfterparse = JSON.parse(allNotes);
    
    jq(document).ready(function(event) {
    	displayExistingNotes();
    	
    	function displayExistingNotes() {
    		var displayNotes;
    		if(notesAfterparse && notesAfterparse.length !== 0) {
    			displayNotes = "<tr><th><input type='checkbox' id='select-all-notes' style='width:55%;height:1.5em;'/>Patient</th><th>Search Phrase</th><th>Comment/Note</th><th>Priority</th><th>Last Updated</th></tr>";
    			
    			for(i = 0; i < notesAfterparse.length; i++) {
    				var note = notesAfterparse[i];
    				if(note) {
    					var uuid = note.uuid;
	    				var patient = "<td><input type='checkbox' class='select-this-note' id='" + uuid + "' style='width:55%;height:1.5em;'/>" + note.patientFName + "</td>";
	    				var phrase = "<td>" + note.searchPhrase + "</td>";
	    				var comment = "<td><textarea class='m-notes-comment' style='width: 488px;'>" + note.comment + "</textarea></td>";
	    				var priority = setPriorityDisplay(note.priority);
	    				var date = new Date(note.createdOrLastModifiedAt);
	    				
	    				displayNotes += "<tr>" + patient + phrase + comment + priority + "<td>" + date.toString() + "</td></tr>";
    				}
    			}
    		}
    		
    		if(displayNotes && displayNotes !== "") {
    			jq("#manage-notes-display-table").html(displayNotes);
    		}
    	}
    	
    	
    	function setPriorityDisplay(priority) {
    		var allPiorities = ["LOW", "HIGH"];
    		var priorityDisplay = "";
    		
    		if(priority === allPiorities[1]) {
    			priorityDisplay = "<td><select class='m-notes-priority'><option>" + allPiorities[1] + "</option><option>" + allPiorities[0] + "</option></select></td>";
    		} else {
    			priorityDisplay = "<td><select class='m-notes-priority'><option>" + allPiorities[0] + "</option><option>" + allPiorities[1] + "</option></select></td>";
    		}
    		
    		return priorityDisplay;
    	}
    	
    });
   	
</script>








<h1>Manage Notes</h1>
<div id="manage-notes-display">
	<table id="manage-notes-display-table"></table>
</div>