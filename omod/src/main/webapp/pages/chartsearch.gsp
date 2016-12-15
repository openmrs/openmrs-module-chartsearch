<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    // RA-452 some elements require encodeHtmlContent to prevent XSS
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label:
            "${ ui.encodeHtmlContent(ui.format(patient.patient.familyName)) }, ${ ui.encodeHtmlContent(ui.format(patient.patient.givenName)) }",
            link: "${ ui.pageLink("coreapps", "patientdashboard/patientDashboard", [patientId: patient.patient.id]) }" },
        { label: "${ ui.message("chartsearch.chartSearch") }" }
    ];
</script>

${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient.patient ]) }

<hr />
<% ui.includeJavascript("chartsearch", "doT.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.navigate.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.time.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.autoMarkings.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.tickrotor.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.axislabels.js") %>
<% ui.includeJavascript("chartsearch", "jquery.sparkline.js") %>
<% ui.includeJavascript("chartsearch", "views_factory.js") %>
${ ui.includeFragment("chartsearch", "cssIncludes") }

<div class="search_container">
    <input type='hidden' id='json-stored-string' />
    <input type='hidden' id='json-filtered-string' />
    <input type='hidden' id='stored-preferences' />
    
    ${ ui.includeFragment("chartsearch", "resultBox") }
    ${ ui.includeFragment("chartsearch", "filters") }
    ${ ui.includeFragment("chartsearch", "topArea") }
    <div style="clear:both;"></div>
</div>