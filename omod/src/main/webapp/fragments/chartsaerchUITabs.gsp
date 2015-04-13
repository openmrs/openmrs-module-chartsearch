<div class="ui-tabs">
	<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#import-database">
	        	Import Database
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#custom-indexing">
	        	${ ui.message("chartsearch.refApp.customIndexing")}
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#non-patient-searches" >
	             ${ ui.message("chartsearch.refApp.nonPatienDataSearches")}
	        </a>
	    </li>
    </ul>
    
    <div id="import-database">
    	${ ui.includeFragment("chartsearch", "importDatabase") }
    </div>
    <div id="custom-indexing">
    	${ ui.includeFragment("chartsearch", "customIndexing") }
    </div>
    <div id="non-patient-searches">
    	${ ui.includeFragment("chartsearch", "nonPatientSearches") }
    </div>
    
</div>