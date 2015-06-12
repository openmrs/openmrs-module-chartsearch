<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("chartsearch.refApp.title") ])
%>

<% ui.includeJavascript("chartsearch", "views_factory.js") %>

<script type="text/javascript">
	var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/systemadministration/systemAdministration.page'},
        { label: "${ ui.message("chartsearch.refApp.title") }" }
    ];
    
    jq = jQuery;
    
    jq(function() {
    	jq(".ui-tabs").tabs();
	});
</script>

<style>
input[type="radio"], input[type="checkbox"] {
	box-sizing: border-box;
	padding: 0;
	width: 15%;
	height: 1.5em;
}
</style>

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