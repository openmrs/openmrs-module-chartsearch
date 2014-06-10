<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.format(patient.patient.familyName) }, ${ ui.format(patient.patient.givenName) }" ,
        link: '${ui.pageLink("coreapps", "clinicianfacing/clinicianFacingPatientDashboard", [patientId: patient.patient.id])}'}
    ]
    var patient = { id: ${ patient.id } };
</script>

${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient.patient ]) }

<hr />
<% ui.includeJavascript("chartsearch", "doT.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.js") %>
<% ui.includeJavascript("chartsearch", "jquery.flot.time.js") %>
<% ui.includeJavascript("chartsearch", "views_factory.js") %>

${ ui.includeFragment("chartsearch", "cssIncludes") }
<div class="search_container">
    ${ ui.includeFragment("chartsearch", "resultBox") }
    ${ ui.includeFragment("chartsearch", "filters") }
    ${ ui.includeFragment("chartsearch", "topArea") }
    <div style="clear:both;"></div>
    
</div>