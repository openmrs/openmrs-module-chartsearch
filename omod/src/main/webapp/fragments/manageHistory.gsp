<style type="text/css">
	#delete-selected-history {
		float:right;
	}
	
	#today-hide-or-show, #this-week-hide-or-show, #this-month-hide-or-show, #others-hide-or-show, #current-year-hide-or-show {
		cursor: pointer;
	}
</style>


<script type="text/javascript">
	var allReturnedhistory =' ${ allFoundHistory }';
    var historyAfterparse = JSON.parse(allReturnedhistory).reverse();
    
    jq(document).ready(function() {
    	displayHistoryExistingHistory();
    	jq("#this-weeks-history-section").hide();
    	jq("#this-month-history-section").hide();
    	jq("#others-history-section").hide();
    	jq("#this-year-history-section").hide();
    	
    	jq("#today-hide-or-show").click(function(event) {
    		hideOrShowHistorySection("#today-hide-or-show", "#todays-history-section");
    	});
    	
    	jq("#this-week-hide-or-show").click(function(event) {
    		hideOrShowHistorySection("#this-week-hide-or-show", "#this-weeks-history-section");
    	});
    	
    	jq("#this-month-hide-or-show").click(function(event) {
    		hideOrShowHistorySection("#this-month-hide-or-show", "#this-month-history-section");
    	});
    	
    	jq("#others-hide-or-show").click(function(event) {
    		hideOrShowHistorySection("#others-hide-or-show", "#others-history-section");
    	});
    	
    	jq("#current-year-hide-or-show").click(function(event) {
    		hideOrShowHistorySection("#current-year-hide-or-show", "#this-year-history-section");
    	});
    	
    	jq("body").on("click", "#todays-history-section", function() {
			if(event.target.id === "history-check-all-today") {
				checkOrUnAllOtherCheckBoxesInADiv("#todays-history-section", "history-check-all-today");
			}
		});
		
    	jq("body").on("click", "#this-weeks-history-section", function() {
			if(event.target.id === "history-check-all-week") {
				checkOrUnAllOtherCheckBoxesInADiv("#this-weeks-history-section", "history-check-all-week");
			}
		});
		
    	jq("body").on("click", "#this-month-history-section", function() {
			if(event.target.id === "history-check-all-month") {
				checkOrUnAllOtherCheckBoxesInADiv("#this-month-history-section", "history-check-all-month");
			}
		});
		
		jq("body").on("click", "#this-year-history-section", function() {
			if(event.target.id === "history-check-year") {
				checkOrUnAllOtherCheckBoxesInADiv("#this-year-history-section", "history-check-year");
			}
		});
		
    	jq("body").on("click", "#others-history-section", function() {
			if(event.target.id === "history-check-others") {
				checkOrUnAllOtherCheckBoxesInADiv("#others-history-section", "history-check-others");
			}
		});
    	
    	jq("#delete-selected-history").click(function(event) {
    		deleteAllSelectedHistory();
    	});
    	
    });

    	function displayHistoryExistingHistory() {
    		var thHistoryToday = "";
    		var thHistoryWeek = "";
    		var thHistoryMonth = "";
    		var thHistoryOther = "";
    		var thHistoryYear = "";
    		var tableTodayHeader = "<tr><th><label><input type='checkbox' id='history-check-all-today' > Select (PatientFName)</label></th><th>Time</th><th>Search Phrase</th></tr>";
    		var tableWeekHeader = "<tr><th><label><input type='checkbox' id='history-check-all-week' > Select (PatientFName)</label></th><th>Date && Time</th><th>Search Phrase</th></tr>";
    		var tableMonthHeader = "<tr><th><label><input type='checkbox' id='history-check-all-month' > Select (PatientFName)</label></th><th>Date && Time</th><th>Search Phrase</th></tr>";
    		var tableOthersHeader = "<tr><th><label><input type='checkbox' id='history-check-others' > Select (PatientFName)</label></th><th>Date && Time</th><th>Search Phrase</th></tr>";
    		var tableYearHeader = "<tr><th><label><input type='checkbox' id='history-check-year' > Select (PatientFName)</label></th><th>Date && Time</th><th>Search Phrase</th></tr>";
    		
    		if(historyAfterparse.length !== 0) {
    			for (i = 0; i < historyAfterparse.length; i++) {
    				var history = historyAfterparse[i];
    				var date = new Date(history.lastSearchedAt);
    				
    				if(checkIfDateIsToday(history.lastSearchedAt)) {
    					thHistoryToday += "<tr><td><label><input type='checkbox' id='" + history.uuid + "' class='history-check' > (" + history.patientFamilyName + ")</label></td><td>" + date.toTimeString() + "</td><td>" + history.searchPhrase + "</td></tr>";
    				}
    				if(checkIfDateIsForThisWeek(history.lastSearchedAt) && !checkIfDateIsToday(history.lastSearchedAt)) {
    					thHistoryWeek += "<tr><td><label><input type='checkbox' id='" + history.uuid + "' class='history-check' > (" + history.patientFamilyName + ")</label></td><td>" + date.toString() + "</td><td>" + history.searchPhrase + "</td></tr>";
    				}
    				if(checkIfDateIsInCurrentMonth(history.lastSearchedAt) && !checkIfDateIsForThisWeek(history.lastSearchedAt)) {
    					thHistoryMonth += "<tr><td><label><input type='checkbox' id='" + history.uuid + "' class='history-check' > (" + history.patientFamilyName + ")</label></td><td>" + date.toString() + "</td><td>" + history.searchPhrase + "</td></tr>";
    				}
    				if(checkIfDateIsInCurrentYear(history.lastSearchedAt) && !checkIfDateIsInCurrentMonth(history.lastSearchedAt)) {
    					thHistoryYear += "<tr><td><label><input type='checkbox' id='" + history.uuid + "' class='history-check' > (" + history.patientFamilyName + ")</label></td><td>" + date.toString() + "</td><td>" + history.searchPhrase + "</td></tr>";
    				}
    				if(!checkIfDateIsInCurrentYear(history.lastSearchedAt)) {
    					thHistoryOther += "<tr><td><label><input type='checkbox' id='" + history.uuid + "' class='history-check' > (" + history.patientFamilyName + ")</label></td><td>" + date.toString() + "</td><td>" + history.searchPhrase + "</td></tr>";
    				}
    			}
    		}
    		
    		if(thHistoryToday !== "") {
    			jq("#todays-history").html(tableTodayHeader + thHistoryToday);
    		}
    		if(thHistoryWeek !== "") {
    			jq("#this-weeks-history").html(tableWeekHeader + thHistoryWeek);
    		}
    		
    		if(thHistoryMonth !== "") {
    			jq("#this-month-history").html(tableMonthHeader + thHistoryMonth);
    		}
    		
    		if(thHistoryYear !== "") {
    			jq("#this-year-history").html(tableYearHeader + thHistoryYear);
    		}
    		
    		if(thHistoryOther !== "") {
    			jq("#other-history").html(tableOthersHeader + thHistoryOther);
    		}
    	}
    	
    	function hideOrShowHistorySection(iconElement, divElement) {
    		if(jq(divElement).is(':visible')) {
    			jq(iconElement).removeClass("icon-circle-arrow-down");
    			jq(iconElement).addClass("icon-circle-arrow-right");
    			jq(divElement + " :input").attr("disabled", true);
    			jq(divElement).hide();
    		} else {
    			jq(iconElement).removeClass("icon-circle-arrow-right");
    			jq(iconElement).addClass("icon-circle-arrow-down");
    			jq(divElement + " :input").attr("disabled", false);
    			jq(divElement).show();
    		}
    	}
    	
    	function returnuuidsOfSeletedHistory() {
	    	var selectedHistoryUuids = [];
	    	
			jq('#manage-history input:checked').each(function() {
				var selectedId = jq(this).attr("id");
				
				if(selectedId !== "history-check-all-today" && selectedId !== "history-check-all-week" && selectedId !== "history-check-others" && selectedId !== "history-check-all-month" && !jq("#" + selectedId).is(":disabled")) {
			    	selectedHistoryUuids.push(selectedId);
			    }
			});
			return selectedHistoryUuids;
    	}
    	
    	function deleteAllSelectedHistory() {
    		if(isLoggedInSynchronousCheck()) {
	    		var selectedUuids = returnuuidsOfSeletedHistory();
	    		var deleteConfirmMsg = "Are you sure you want to delete " + selectedUuids.length + " Item(s)!";
	    		
	    		if(selectedUuids.length !== 0) {
		    		if(confirm(deleteConfirmMsg)) {
		    			jq.ajax({
							type: "POST",
							url: "${ ui.actionLink('deleteSelectedHistory') }",
							data: {"selectedUuids":selectedUuids},
							dataType: "json",
							success: function(remainingHistory) {
								historyAfterparse = remainingHistory.reverse();
								
								displayHistoryExistingHistory();
							},
							error: function(e) {
							}
						});
		    		} else {
		    			//alert("DELETE Cancelled");
		    		}
	    		} else {
	    			alert("Select at-least one history to be deleted!");
	    		}
	    	} else {
				location.reload();
			}
    	}
</script>

<h1>${ ui.message("chartsearch.refApp.manage.history.title") }</h1>
<input type="button" id="delete-selected-history" value="Delete Selected"/><br /><br />
<div id="manage-history">
	<i class="icon-circle-arrow-down" id="today-hide-or-show"> ${ ui.message("chartsearch.refApp.manage.history.today") }</i><br />
	<div id="todays-history-section">
		<table id="todays-history"></table>
	</div>
	<br />
	<i class="icon-circle-arrow-right" id="this-week-hide-or-show"> ${ ui.message("chartsearch.refApp.manage.history.week") }</i><br />
	<div id="this-weeks-history-section">
		<table id="this-weeks-history"></table>
	</div>
	<br />
	<i class="icon-circle-arrow-right" id="this-month-hide-or-show"> ${ ui.message("chartsearch.refApp.manage.history.month") }</i><br />	
	<div id="this-month-history-section">
		<table id="this-month-history"></table>
	</div>
	<br />
	<i class="icon-circle-arrow-right" id="current-year-hide-or-show">${ ui.message("chartsearch.refApp.manage.history.year") }</i><br />	
	<div id="this-year-history-section">
		<table id="this-year-history"></table>
	</div>
	<br />
	<i class="icon-circle-arrow-right" id="others-hide-or-show"> ${ ui.message("chartsearch.refApp.manage.history.otherYears") }</i><br />	
	<div id="others-history-section">
		<table id="other-history"></table>
	</div>
</div>

