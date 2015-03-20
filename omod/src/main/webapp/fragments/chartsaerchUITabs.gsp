<div class="ui-tabs">
	<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
	    <li class="ui-state-default ui-corner-top ui-state-active">
	    	<a class="ui-tabs-anchor" href="#patient-searches">
	        	${ ui.message("chartsearch.chartsearch.refApp.patientSearches")}
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active">
	    	<a class="ui-tabs-anchor" href="#custom-indexing">
	        	${ ui.message("chartsearch.refApp.customIndexing")}
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor" href="#non-patient-searches" >
	             ${ ui.message("chartsearch.refApp.nonPatienDataSearches")}
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor" href="#synonym-group">
	             ${ ui.message("chartsearch.refApp.synonymGroups")}
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor" href="#filters">
	             ${ ui.message("chartsearch.refApp.filters")}
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top" >
	        <a class="ui-tabs-anchor" href="#settings">
	             ${ ui.message("chartsearch.manage.settings")}
	        </a>
	    </li>
    </ul>
    
    <div id="patient-searches">
    	${ ui.includeFragment("chartsearch", "patientDataSearches") }
    </div>
    <div id="custom-indexing">
    	${ ui.includeFragment("chartsearch", "customIndexing") }
    </div>
    
    <div id="non-patient-searches">
    	${ ui.includeFragment("chartsearch", "nonPatientSearches") }
    </div>
    
    <div id="synonym-group">
    	${ ui.includeFragment("chartsearch", "manageSynonymGroups") }
    </div>
    
    <div id="settings">
    	${ ui.includeFragment("chartsearch", "manageSettings") }
    </div>
    
    <div id="filters">
    	${ ui.includeFragment("chartsearch", "manageFilters") }
    </div>
</div>