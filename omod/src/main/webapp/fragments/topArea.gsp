<script type="text/javascript">
    var jq = jQuery;
    var navigationIndex = 1;
    var peviousIndex = 0;
    var wasGoingNext = true;
    var categoryFilterLabel = "";
    var reversed = false;
    var prefs ='${preferences}';
    
    jq("#stored-preferences").val(prefs);
    
    jq(document).ready(function() {
		jq('#searchText').focus();
		jq("#ui-datepicker-start").datepicker();
		jq("#ui-datepicker-stop").datepicker();
		
		showHistorySuggestionsOnLoadTopArea();
		displayBothPersonalAndGlobalNotes(jsonAfterParse);
		updateCategeriesAtUIGlobally(jsonAfterParse.appliedCategories);
		
        jq( "#date_filter_title" ).click(function() {
            jq( "#date_filter_options" ).toggle();
        });
        
        jq( "#date_filter_options" ).click(function(e) {
            jq( "#date_filter_options" ).hide();
            var txt = jq(e.target).text();
            jq("#date_filter_title").text(txt);
        });
        
        jq('#selectAll_categories').click(function (event) {
		    console.log(jq('.category_check'));
		    jq('.category_check').prop('checked', true);
		    categoryFilterLabel = "All Categories";
		    jq("#category-filter_method").text(categoryFilterLabel);
		    submitChartSearchFormWithAjax();
		    
		    return false;
		});
		
		jq('#deselectAll_categories').click(function (event) {
		    jq('.category_check').prop('checked', false);
		    categoryFilterLabel = "All Categories";
		    jq("#category-filter_method").text(categoryFilterLabel);
		    submitChartSearchFormWithAjax();
		    
		    return false;
		});
		
		jq("body").on("click", "#inside_filter_categories", function (event) {
			var currCatLinkId = event.target.id;
			var currCatCheckId = currCatLinkId.replace("select_","");
			
			if(event.target.localName === "a") {
				var currCatLabel = currCatCheckId.replace("_category", "");
				
				jq('#inside_filter_categories').find('input[type=checkbox]:checked').prop('checked', false);
				jq("#category-filter_method").text(capitalizeFirstLetter(currCatLabel));
			    jq("#" + currCatCheckId).prop('checked', true);
			    submitChartSearchFormWithAjax();
				jq('#filter_categories_categories').removeClass('display_filter_onclick');
				
				return false;
			} else if(event.target.localName === "input" && currCatLinkId) {
					var cat = jq("#" + currCatCheckId).val();
					
					if(jq("#" + currCatCheckId).attr('checked')) {
						var checkedCats = jq('#inside_filter_categories :checked');
						checkedCats.each(function() {
							if(categoryFilterLabel === "All Categories") {
								categoryFilterLabel = "";
							}
							var bothCombined = categoryFilterLabel + cat;
							if(categoryFilterLabel.indexOf(capitalizeFirstLetter(cat)) < 0) {
								if(bothCombined.length <= 14) {
					    			categoryFilterLabel += capitalizeFirstLetter(cat) + ",";
					    		} else {
					    			if(categoryFilterLabel.indexOf(capitalizeFirstLetter("...")) < 0) {
					    				categoryFilterLabel += "...";
					    			}
					    		}
					    	}
					    });
			    } else {
			    	if(categoryFilterLabel.indexOf(capitalizeFirstLetter(cat)) >= 0) {
			    		var cat2 = capitalizeFirstLetter(cat) + ",";
			    		categoryFilterLabel = categoryFilterLabel.replace(cat2, "");
			    	}
			    }
			    
			    if(categoryFilterLabel.indexOf("...") >= 0) {
					categoryFilterLabel = categoryFilterLabel.replace("...", "") + "...";
				}
				
				if(categoryFilterLabel === "..." || categoryFilterLabel === "" || categoryFilterLabel === ",...") {
					categoryFilterLabel = "All Categories";
				}
				
				jq("#category-filter_method").text(categoryFilterLabel);
			}
		});
		
		jq('#searchBtn').click(function(event) {
			submitChartSearchFormWithAjax();
			return false;
		});
		
		jq('#submit_selected_categories').click(function(event) {
			submitChartSearchFormWithAjax();
			jq('#filter_categories_categories').removeClass('display_filter_onclick');
			
			return false;
		});
		
		jq('.filter_method').click(function(event) {
			return false;
		});
		
		jq('#category_dropdown, #category-filter_method').on('click', function(e){
			if(e.target.localName !== "a" && e.target.localName !== "input" || e.target.id === "category-filter_method") {
		    	jq('#filter_categories_categories').toggleClass('display_filter_onclick');
		    }
		});
		jq('#hide_categories').on('click', function(e){
		    jq('#filter_categories_categories').removeClass('display_filter_onclick');
		    return false;
		});
		
		jq('#time_dropdown, #time_anchor').on('click', function(e){
		    jq('#filter_categories_time').toggleClass('display_filter_onclick');
		});
		
		jq('#location_dropdown, #location_anchor').on('click', function(e){
		    jq('#locationOptions').toggleClass('display_filter_onclick');
		});
		
		jq('#provider_dropdown, #provider_anchor').on('click', function(e){
		    jq('#providersOptions').toggleClass('display_filter_onclick');
		});
		
		jq("#chart-previous-searches").click(function(event) {
			if(jq("#chart-previous-searches-display").is(':visible')) {
				jq("#chart-previous-searches-display").hide();
			} else {
				jq("#chart-previous-searches-display").show();
			}
		});
		
		jq("#searchText").keyup(function(key) {
			var searchText = document.getElementById('searchText').value;
			
			if(jq("#chart-previous-searches-display").is(':visible') && jsonAfterParse.searchHistory.length !== 0) {//Suggest History
				if(key.keyCode == 27) {
					submitChartSearchFormWithAjax();
				} else if(key.keyCode == 39 || key.keyCode == 37) {
			    	//DO NOTHING, since here we are doing
			    } else if ((key.keyCode >= 48 && key.keyCode <= 90) || key.keyCode != 13 || key.keyCode == 8) {//use numbers and letters plus backspace only
			    	delay(function() {
						if (searchText != "" && searchText.length >= 1) {
							updateSearchHistoryDisplay();
					 	} else {
					 		updateSearchHistoryDisplay();
					 	}
				    }, 50 );
			    }
			} else {//Suggest from default search suggestions
				updateSearchHistoryDisplay();
				if(key.keyCode == 27) {
			    	submitChartSearchFormWithAjax();
			    } else if(key.keyCode == 39 || key.keyCode == 37) {
			    	//DO NOTHING, since here we are doing
			    } else if ((key.keyCode >= 48 && key.keyCode <= 90) || key.keyCode != 13 || key.keyCode == 8) {//use numbers and letters plus backspace only
					delay(function() {
						if (searchText != "" && searchText.length >= 2) {
							showSearchSuggestions();
					 	} else {
					 		hideSearchSuggestions();
					 	}
				    }, 50 );
			    }
		    }
		    
			return false;
		});
		
		jq("body").on("click", "#chart-searches-suggestions", function (event) {
			if(event.target.localName === "a") {
				if(event.target.id === "hide-search-suggestions-ui") {
					jq("#chart-searches-suggestions").hide();
				} else {
					var selectedSuggestion = event.target.innerText;
					
					jq('#searchText').val(selectedSuggestion);
					submitChartSearchFormWithAjax();
				}
			}
			return false;
		});
		
		jq("body").on("click", "#chart-previous-searches-display", function (event) {
			if(event.target.localName === "a") {
				var selectedHistory = event.target.innerText;
				
				jq('#searchText').val(selectedHistory);
				unSelectAllCategories();
				submitChartSearchFormWithAjax();
			} else if(event.target.localName === "i") {
				var uuid = event.target.id;
				if(uuid) {
					deleteSearchHistory(uuid);
				}
			}
			return false;
		});
		
		var delay = (function() {
		  var timer = 0;
		  return function(callback, ms){
		    clearTimeout (timer);
		    timer = setTimeout(callback, ms);
		  };
		})();
		
		jq(document).keydown(function(key) {
			var single_obsJSON = getResultsJson().obs_singles;
			
			//if (typeof single_obsJSON !== 'undefined') {
				if(key.keyCode == 39) {// =>>
					var diffBtnIndecs = navigationIndex - peviousIndex;
					var numberOfResults = getResultsJson().obs_groups.length + getResultsJson().obs_singles.length + getResultsJson().patientAllergies.length + getResultsJson().patientAppointments.length;
					
					if(wasGoingNext) {
						if(navigationIndex != numberOfResults) {
							if(navigationIndex >= 0 && diffBtnIndecs == 1) {
								incrementNavigation(single_obsJSON);
							}
						}
					} else {
						navigationIndex  = navigationIndex + 2;
						if(peviousIndex == 0) {
							diffBtnIndecs = -1;
							navigationIndex = 1;
						}
						if(navigationIndex >= 0 && diffBtnIndecs == -1) {
							incrementNavigation(single_obsJSON);
						}
					}
				}
				if(key.keyCode == 37) {// <<=
					if(peviousIndex != 0) {
						var diffBtnIndecs = navigationIndex - peviousIndex;
						
						if(wasGoingNext) {
							navigationIndex  = navigationIndex - 2;
							
							if(navigationIndex >= 0 && diffBtnIndecs == 1) {
								decrementNavigation(single_obsJSON);
							}
						} else {
							if(navigationIndex >= 0 && diffBtnIndecs == -1) {
								decrementNavigation(single_obsJSON);
							}
						}
					}
				}
			//}
		});
		
		jq("#ui-datepicker-start").change(function(event) {
			enableOrDisableCustomFilterSubmitButton();
		});
		
		jq("#ui-datepicker-stop").change(function(event) {
			enableOrDisableCustomFilterSubmitButton();
		});
		
		jq("#submit-custom-date-filter").click(function(event) {
			if(!jq("#submit-custom-date-filter").is(":disabled")) {
				filterResultsUsingCustomTime();
				jq("#custom-date-dialog-content").dialog('close');
			}
		});
		
    });
    
    	function enableOrDisableCustomFilterSubmitButton() {
    		var start = jq("#ui-datepicker-start").val();
			var stop = jq("#ui-datepicker-stop").val();
			
			if(start && start !== "" && stop && stop !== "") {
				var startDate = new Date(start);
				var stopDate = new Date(stop);
				
				if(stopDate.setHours(0, 0, 0, 0) > startDate.setHours(0, 0, 0, 0)) {
					jq("#submit-custom-date-filter").removeAttr('disabled');
				} else {
					jq("#submit-custom-date-filter").attr('disabled','disabled');
				}
			}
    	}
    
		function submitChartSearchFormWithAjax() {
			if(isLoggedInSynchronousCheck()) {
				var searchText = document.getElementById('searchText');
				var searchHistory = jsonAfterParse.searchHistory;
				var patientId = jq("#patient_id").val().replace("Patient#", "");
				var categories = getAllCheckedCategoriesOrFacets();
				
				reInitializeGlobalVars();
				jq("#chart-previous-searches-display").hide();
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
					data: { "patientId":patientId, "phrase":searchText.value, "categories":categories },
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
								
						showHistorySuggestionsOnLoadTopArea();
						hideSearchSuggestions();
	    				displayBothPersonalAndGlobalNotes(jsonAfterParse);
	    				displayQuickSearches();
	    				updateBookmarksAndNotesUI();
	    				updateCategeriesAtUIGlobally(jsonAfterParse.appliedCategories);
					},
					error: function(e) {
					  //alert("Error occurred!!! " + e);
					}
				});
			} else {
				location.reload();
			}
		}
		
		function incrementNavigation(single_obsJSON) {
			updateNavigation("increment", single_obsJSON);
		}
		
		function decrementNavigation(single_obsJSON) {
			updateNavigation("decrement", single_obsJSON);
		}
		
		function updateNavigation(change, single_obsJSON) {//TODO logic may not work for some instances
			var obsGroup = getResultsJson().obs_groups[navigationIndex];
			var obsSingle = single_obsJSON[navigationIndex - getResultsJson().obs_groups.length];
			var allergy = getResultsJson().patientAllergies[navigationIndex - (getResultsJson().obs_groups.length + getResultsJson().obs_singles.length)];
			var appointment = getResultsJson().patientAppointments[navigationIndex - (getResultsJson().obs_groups.length + getResultsJson().obs_singles.length + getResultsJson().patientAllergies.length)];
			
			peviousIndex = navigationIndex;
			if (change === "increment") {
				navigationIndex++;
				wasGoingNext = true;
			} else {
				navigationIndex--;
				wasGoingNext = false;
			}
			
			if(obsGroup && obsGroup.group_Id) {
				focusOnNextObsGroupAndDisplayItsDetails(obsGroup.group_Id);
			} else if(obsSingle && obsSingle.observation_id > 0) {
				focusOnNextObsSingleAndDisplayItsDetails(obsSingle.observation_id);
			} else if(allergy && allergy.allergenId > 0) {
				focusOnNextAllergenAndDisplayItsDetails(allergy.allergenId);
			} else if(appointment && appointment.id > 0) {
				focusOnNextAppointmentAndDisplayItsDetails(appointment.id)
			}
		}
		
		function focusOnNextObsGroupAndDisplayItsDetails(groupId) {
			var elementId = 'obs_group_' + groupId;
			
			if(getResultsJson().obs_groups[0].group_Id === groupId) {
				elementId = "first_obs_group";
			}
			
			load_detailed_obs(groupId, elementId);
			updateNavigationIndicesToClicked(groupId, elementId);
		}
		
		function focusOnNextObsSingleAndDisplayItsDetails(obsId) {
			var elementId = 'obs_single_' + obsId;
			
			if(getResultsJson().obs_groups.length === 0 && getResultsJson().obs_singles[0].observation_id === obsId) {
				elementId = "first_obs_single";
			}
			
			load_single_detailed_obs(obsId, elementId);
			updateNavigationIndicesToClicked(obsId, elementId);
		}
		
		function focusOnNextAllergenAndDisplayItsDetails(allergenId) {
			var elementId = 'allergen_' + allergenId;
			
			if (getResultsJson().obs_groups.length === 0 && getResultsJson().obs_singles.length === 0 && getResultsJson().patientAllergies[0].allergenId === allergenId) {
				elementId = "first_alergen";
			}
			
			var clickedElement = jq("#" + elementId)[0];
			
			load_allergen(allergenId, clickedElement);
			updateNavigationIndicesToClicked(allergenId, clickedElement);
		}
		
		function focusOnNextAppointmentAndDisplayItsDetails(appId) {
			var elementId = 'appointment_' + appId;
			
			if (getResultsJson().obs_groups.length === 0 && getResultsJson().obs_singles.length === 0 && getResultsJson().patientAllergies.length === 0 && getResultsJson().patientAppointments[0].id === appId) {
				elementId = "first_appointment";
			}
			
			var clickedElement = jq("#" + elementId)[0];
			
			load_appointment(appId, clickedElement);
			updateNavigationIndicesToClicked(appId, clickedElement);
		}
		
		function hideSearchSuggestions() {
			jq("#chart-searches-suggestions").hide();
		}
		
		function showSearchSuggestions() {
			if(isLoggedInSynchronousCheck()) {
				var suggestionsArray = jsonAfterParse.searchSuggestions;
				var searchSuggestions = "";
				var searchText = jq('#searchText').val();
				
				searchSuggestions += "<a id='hide-search-suggestions-ui'>Close</a>";
				
				for(i = 0; i < suggestionsArray.length; i++) {
					var suggestion = suggestionsArray[i];
					
					if(strStartsWith(suggestion.toUpperCase(), searchText.toUpperCase()) && searchSuggestions.indexOf(suggestion) <= 0) {
						searchSuggestions += "<a class='search-text-suggestion' href=''>" + suggestion + "</a><br/>";
					}
				}
				
				document.getElementById('chart-searches-suggestions').innerHTML = searchSuggestions;
				if(searchSuggestions) {
					jq("#chart-searches-suggestions").show();
				} else {
					jq("#chart-searches-suggestions").hide();
				}
			} else {
				location.reload();
			}
		}
		
		function strStartsWith(str, prefix) {
		    return str.indexOf(prefix) === 0;
		}
		
		function showHistorySuggestionsOnLoadTopArea() {//TODO Rename this method to mention that it's for main page since we shall provide another for preference page
			var historyToDisplay = "";
			var history = jsonAfterParse.searchHistory.reverse();
			
			reversed = true;
			
			for(i = 0; i < history.length; i++) {
				historyToDisplay += "<div class='search-history-item'><a class='search-using-this-history' href=''>" + history[i].searchPhrase + "</a>&nbsp&nbsp-&nbsp&nbsp<em>" + history[i].formattedLastSearchedAt + "</em><i id='" + history[i].uuid + "' class='icon-remove delete-search-history' title='Delete This History'></i></div>"; 
			}
			
			jq("#chart-previous-searches-display").html(historyToDisplay);
		}
		
		function updateSearchHistoryDisplay() {
			if(isLoggedInSynchronousCheck()) {
				var historyArray;
				var historySuggestions = "";
				var searchText = jq('#searchText').val();
				
				if(reversed === true) {
					historyArray = jsonAfterParse.searchHistory
				} else {
					historyArray = jsonAfterParse.searchHistory.reverse();
					reversed = true;
				}
				
				for(i = 0; i < historyArray.length; i++) {
					var history = historyArray[i];
					
					if(strStartsWith(history.searchPhrase.toUpperCase(), searchText.toUpperCase()) && historySuggestions.indexOf(history) <= 0) {
						historySuggestions += "<div class='search-history-item'><a class='search-using-this-history' href=''>" + history.searchPhrase + "</a>&nbsp&nbsp-&nbsp&nbsp<em>" + history.formattedLastSearchedAt + "</em><i id='" + history.uuid + "' class='icon-remove delete-search-history'></i></div>";
					}
				}
				if(historySuggestions === "") {
					showSearchSuggestions();
				} else {
					hideSearchSuggestions();
				}
				
				document.getElementById('chart-previous-searches-display').innerHTML = historySuggestions;
			} else {
				location.reload();
			}
		}
		
		function deleteSearchHistory(historyUuid) {
			if(isLoggedInSynchronousCheck()) {
				if(historyUuid) {
					jq.ajax({
						type: "POST",
						url: "${ ui.actionLink('deleteSearchHistory') }",
						data: {"historyUuid":historyUuid},
						dataType: "json",
						success: function(updatedHistory) {
							var history = updatedHistory.searchHistory;
							
							jsonAfterParse.searchHistory = history;
							showHistorySuggestionsOnLoadTopArea();
						},
						error: function(e) {
							//DO Nothing
						}
					});
				}
			} else {
				location.reload();
			}
		}
</script>

<style type="text/css">
    .chart-search-input {
	    background: #00463f;
	    text-align: left;
	    color: white;
	    padding: 20px 30px;
	    -moz-border-radius: 5px;
	    -webkit-border-radius: 5px;
	    -o-border-radius: 5px;
	    -ms-border-radius: 5px;
	    -khtml-border-radius: 5px;
	    border-radius: 5px;
	}
	.chart_search_form_text_input {
	    min-width: 82% !important;
	}
	.inline {
	    display: inline-block;
	}
	.chart_search_form_button {
	    margin-left: 30px;
	}
	.form_label_style {
	    margin-bottom: 10px !important;
	}
	.filter_options {
	    display: none;
	    background: white;
	    width: 90px;
	    padding: 13px;
	    position: absolute;
	    border: 1px solid black;
	    left: 242px;
	}
	.single_filter_option {
	    display: block;
	    cursor: pointer;
	}
	.demo-container {
	    box-sizing: border-box;
	    width: 400px;
	    height: 300px;
	    padding: 20px 15px 15px 15px;
	    margin: 15px auto 30px auto;
	    border: 1px solid #ddd;
	    background: #fff;
	    background: linear-gradient(#f6f6f6 0, #fff 50px);
	    background: -o-linear-gradient(#f6f6f6 0, #fff 50px);
	    background: -ms-linear-gradient(#f6f6f6 0, #fff 50px);
	    background: -moz-linear-gradient(#f6f6f6 0, #fff 50px);
	    background: -webkit-linear-gradient(#f6f6f6 0, #fff 50px);
	    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.15);
	    -o-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
	    -ms-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
	    -moz-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
	    -webkit-box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
	}
	.demo-placeholder {
	    width: 100%;
	    height: 100%;
	    font-size: 14px;
	    line-height: 1.2em;
	}
	.legend,
	.legend div {
	    display: none;
	}
	.bold {
	    font-weight: bold;
	}
	.search-spinner {
	    display: block;
	    margin-left: auto;
	    margin-right: auto;
	    padding-top: 230px;
	    height: 120px;
	    width: 120px;
	}
	#found_no_results {
	    text-align: center;
	    font-size: 25px;
	}
	#chart_search_form_inputs-searchPhrase {
	    position: relative;
	}
	#chart-previous-searches {
	    position: absolute;
	}
	#searchBtn {
	    margin-left: 45px;
	}
	#chart-previous-searches {
	    cursor: pointer;
	}
	.filters_section,
	#category_dropdown,
	#time_dropdown,
	#location_dropdown,
	#provider_dropdown {
	    z-index: 1;
	}
	#chart-previous-searches-display {
	    position: absolute;
	    z-index: 2;
	    height: 250px;
	    width: 775px;
	    overflow: scroll;
	    background-color: white;
	    padding-left: 10px;
	    padding-right: 5px;
	    border-left: 2px solid #9C9A9A;
	    color: #949494;
	}
	#chart-searches-suggestions {
	    position: absolute;
	    z-index: 2;
	    height: 108px;
	    width: 764px;
	    background-color: white;
	    padding-left: 10px;
	    border: 2px solid #007fff;
	    color: black;
	    overflow: hidden;
	}
	.search-text-suggestion {
	    cursor: pointer;
	}
	.category_filter_item-disabled {
	    pointer-events: none;
	}
	#found-results-summary {
	    color: rgb(131, 128, 128);
	    text-align: center;
	}
	.search-history-item {
	    height: 25px;
	    border-bottom: 1px solid #A8ACAC;
	}
	.delete-search-history {
	    float: right;
	    cursor: pointer;
	}
	#hide-search-suggestions-ui {
	    float: right;
	    padding-right: 10px;
	    cursor: pointer;
	    color: rgb(79, 100, 155);
	}
	#chart-previous-searches-display, #custom-date-dialog-content, #chart-searches-suggestions {
		display:none;
	}
</style>

<article id="search-box">
    <section>
        <div class="chart-search-wrapper">
            <form class="chart-search-form" id="chart-search-form-submit">
                <div class="chart-search-input">
                    <div class="chart_search_form_inputs">
                        <input type="text" name="patientId" id="patient_id" value="${patientId.id}" hidden>
                        <div id="chart_search_form_inputs-searchPhrase">
                        	<input type="text" id="searchText" name="phrase" class="chart_search_form_text_input inline ui-autocomplete-input" placeholder="${ ui.message("chartsearch.messageInSearchField") }" size="40">
                        	<i id="chart-previous-searches" class="icon-arrow-down medium" title="History"></i>
                        	<input type="submit" id="searchBtn" class="button inline chart_search_form_button" value="search"/>
                        </div>
                        <div id="chart-previous-searches-display">
                        	<!-- All, search phrases searched to be stored and displayed here -->
                        </div>
                        <div id="chart-searches-suggestions">
                        </div>
                    </div>
                    <div class="filters_section">
                    	<div class="dropdown" id="category_dropdown">
	                     	<div class="inside_categories_filter">
								<span class="dropdown-name" id="categories_label">
								<a class="filter_method" id="category-filter_method">${ ui.message("chartsearch.topArea.allCategories") }</a>
								<i class="icon-sort-down"></i>
								</span>
								<div class="filter_categories" id="filter_categories_categories">
									<a href="" id="selectAll_categories" class="disabled_link">${ ui.message("chartsearch.topArea.selectAll") }</a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href="" id="deselectAll_categories" class="disabled_link">Clear</a>
									<br /><hr />
									<div id="inside_filter_categories">
										<script type="text/javascript">
											displayCategories(jsonAfterParse);
										</script>
									</div>
									<hr />
									<input id="submit_selected_categories" type="submit" value="OK" />&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href="" id="hide_categories">${ ui.message("chartsearch.topArea.cancel") }</a>
								</div>
							</div>
						</div>
                        <div class="dropdown" id="time_dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="time_label">
                                    <a class="filter_method" id="time_anchor">${ ui.message("chartsearch.topArea.anyTime") }</a>
                                    <i class="icon-sort-down"></i>
                                </span>
                                <div class="filter_categories" id="filter_categories_time">
                                    <hr />
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('today')">${ ui.message("chartsearch.topArea.today") }</a>
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('yesterday')">${ ui.message("chartsearch.topArea.yesterday") }</a>
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('thisWeek')">${ ui.message("chartsearch.topArea.week") }</a>
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('thisMonth')">${ ui.message("chartsearch.topArea.month") }</a>
										<a class="single_filter_option" onclick="filterResultsUsingTime('last3Months')">${ ui.message("chartsearch.topArea.threeMonths") }</a>
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('thisYear')">${ ui.message("chartsearch.topArea.year") }</a>
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('custom')">${ ui.message("chartsearch.topArea.custom") }</a>
                                        <a class="single_filter_option" onclick="filterResultsUsingTime('anyTime')">${ ui.message("chartsearch.topArea.anyTime") }</a>
                                </div>
                                <div id="custom-date-dialog-content">
                                	<p>Start: <input type="text" id="ui-datepicker-start"></p><br />
									<p>Stop: <input type="text" id="ui-datepicker-stop"></p><br />
									<p><input type="button" id="submit-custom-date-filter" value="Submit" disabled /></p>
                                </div>
                            </div>
                        </div>
                        <div class="dropdown" id="location_dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a class="filter_method" id="location_anchor">${ ui.message("chartsearch.topArea.allLocations") }</a>
                                    <i class="icon-sort-down"></i>
                                </span>
                                <div class="filter_categories" id="locationOptions">
									<script type="text/javascript">
											displayLocations(jsonAfterParse);
									</script>
                                </div>
                            </div>
                        </div>
                        <div class="dropdown" id="provider_dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a class="filter_method" id="provider_anchor">${ ui.message("chartsearch.topArea.allProviders") }</a>
                                    <i class="icon-sort-down"></i>
                                </span>
                                <div class="filter_categories" id="providersOptions">
                                    <script type="text/javascript">
											displayProviders(jsonAfterParse);
									</script>
                                </div>
                            </div>
                        </div>
                    	<div id="search-saving-section">
							${ ui.includeFragment("chartsearch", "searchSavingSection") }
						</div>
                    </div>
                </div>
    			${ ui.includeFragment("chartsearch", "main_results") }
            </form>
        </div>
    </section>
</article>