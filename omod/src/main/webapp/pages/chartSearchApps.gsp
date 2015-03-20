<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("chartsearch.refApp.chartSearchApps.title") ])
%>

<script type="text/javascript">
	var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/systemadministration/systemAdministration.page'},
        { label: "${ ui.message("chartsearch.refApp.chartSearchApps") }" }
    ];

    jq(function(){
		jq(".ui-tabs").tabs();
	});
</script>

<!-- Just Informative, To be Removed -->
<b>ManageChartSearch will address all adminitration functionalities besides patient data search such as mentioned at: <a href="https://notes.openmrs.org/ChartSearch-Module-SupportOtherModulesSearches">Support Other Modules Searches</a></b>
<br /><br />

${ ui.includeFragment("chartsearch", "chartsaerchUITabs") }

