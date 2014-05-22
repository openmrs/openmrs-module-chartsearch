<%
    ui.decorateWith("appui", "standardEmrPage")
%>

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