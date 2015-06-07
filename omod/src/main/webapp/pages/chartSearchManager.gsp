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
    	<h1>Select patient to launch their chart search page</h1>
    </div>
    <div id="manage-preferences">
    	<h1>Manage Preferences</h1>
    </div>
    <div id="manage-bookmarks">
    	<h1>Manage Bookmarks</h1>
    </div>
    <div id="manage-history">
    	<h1>Manage History</h1>
    </div>
    <div id="manage-commands">
    	<h1>Manage Commands</h1>
    </div>
    <div id="manage-settings">
    	<h1>Manage Settings</h1>
    </div>
    <div id="manage-notes">
    	<h1>Manage Notes</h1>
    </div>
    <div id="aggregate-searches">
    	<h1>Aggregate Searches</h1>
    	(e.g., find all visits across all patients during 2014 with a new diagnosis of diabetes).
    </div>
    
</div>