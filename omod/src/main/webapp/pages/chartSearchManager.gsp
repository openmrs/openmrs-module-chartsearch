<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("chartsearch.refApp.title") ])
%>

<script type="text/javascript">
	var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/systemadministration/systemAdministration.page'},
        { label: "${ ui.message("chartsearch.refApp.title") }" }
    ];
    
    jq = jQuery;
    
    var tab = parseInt(getUrlParameter('tab')) - 1;
    
    jq(function() {
    	if(tab) {
	    	jq(".ui-tabs").tabs({ active: tab });
    	} else {
    		jq(".ui-tabs").tabs();
    	}
	});
	
	function getUrlParameter(sParam) {
	    var sPageURL = window.location.search.substring(1);
	    var sURLVariables = sPageURL.split('&');
	    for (var i = 0; i < sURLVariables.length; i++) {
	        var sParameterName = sURLVariables[i].split('=');
	        if (sParameterName[0] == sParam) 
	        {
	            return sParameterName[1];
	        }
	    }
	}      
</script>

<div class="ui-tabs">
	<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#launcher">
	        	Launcher
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#manage-preferences">
	        	Preferences
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#manage-bookmarks">
	        	Bookmarks
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#manage-history" >
	             History
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#manage-commands" >
	             Commands
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#manage-settings" >
	             Settings
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#manage-notes" >
	             Notes
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#aggregate-searches" >
	             Aggregate Searches
	        </a>
	    </li>
    </ul>
    
    <div id="launcher">
    	${ ui.includeFragment("chartsearch", "chartsearchLauncher") }
    </div>
    <div id="manage-preferences">
    	${ ui.includeFragment("chartsearch", "managePreferences") }
    </div>
    <div id="manage-bookmarks">
    	${ ui.includeFragment("chartsearch", "manageBookmarks") }
    </div>
    <div id="manage-history">
    	${ ui.includeFragment("chartsearch", "manageHistory") }
    </div>
    <div id="manage-commands">
    	${ ui.includeFragment("chartsearch", "manageCommands") }
    </div>
    <div id="manage-settings">
    	${ ui.includeFragment("chartsearch", "manageSettings") }
    </div>
    <div id="manage-notes">
    	${ ui.includeFragment("chartsearch", "manageNotes") }
    </div>
    <div id="aggregate-searches">
    	${ ui.includeFragment("chartsearch", "aggregateSearches") }
    </div>
    
</div>