<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("chartsearch.refApp.chartSearchApps.title") ])
%>

<script type="text/javascript">
	var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/systemadministration/systemAdministration.page'},
        { label: "${ ui.message("chartsearch.refApp.chartSearchApps.multi") }" }
    ];

    jq(function(){
		jq(".ui-tabs").tabs({ active: 2 });
	});
</script>

It's time to search data within your databases on this server and remotely connected to Databases<br />
Have you known the relevance of <a target="_blank" href="https://talk.openmrs.org/t/do-we-need-non-patient-data-searches-multi-search-module-proposal/1595"><b>MULTI SEARCHING</b> FOR EVERY THING WITHIN AND OUTSIDE OPENMRS DEFAULT DATABASE</a>!<br /><br />

${ ui.includeFragment("chartsearch", "chartsaerchUITabs") }


<style type="text/css">

	
</style>