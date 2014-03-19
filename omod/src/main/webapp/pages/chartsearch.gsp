<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<h2>Chart Search Page</h2>

<% ui.includeJavascript("chartsearch", "doT.js") %>
<% ui.includeJavascript("chartsearch", "views_factory.js") %>

${ ui.includeFragment("chartsearch", "cssIncludes") }
<div class="search_container">
    ${ ui.includeFragment("chartsearch", "leftPane") }
    ${ ui.includeFragment("chartsearch", "resultBox") }
    <div style="clear:both;"></div>
</div>