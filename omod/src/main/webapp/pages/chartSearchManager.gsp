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
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#history" >
	             ${ ui.message("chartsearch.refApp.manager.history") }
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#bookmarks">
	        	${ ui.message("chartsearch.refApp.manager.bookmarks") }
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#notes" >
	             ${ ui.message("chartsearch.refApp.manager.notes") }
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top">
	    	<a class="ui-tabs-anchor" href="#preferences">
	        	 ${ ui.message("chartsearch.refApp.manager.preferences") }
	        </a>
	    </li>
	    <li class="ui-state-default ui-corner-top ui-state-active" >
	        <a class="ui-tabs-anchor" href="#feedback" >
	              ${ ui.message("chartsearch.refApp.manager.feedback") }
	        </a>
	    </li>
    </ul>
    
    <div id="preferences">
    	${ ui.includeFragment("chartsearch", "managePreferences") }
    </div>
    <div id="bookmarks">
    	${ ui.includeFragment("chartsearch", "manageBookmarks") }
    </div>
    <div id="history">
    	${ ui.includeFragment("chartsearch", "manageHistory") }
    </div>
    <div id="notes">
    	${ ui.includeFragment("chartsearch", "manageNotes") }
    </div>
    <div id="feedback">
    	${ ui.includeFragment("chartsearch", "chartsearchFeedback") }
    </div>
    
</div>