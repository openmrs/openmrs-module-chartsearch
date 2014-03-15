<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<h2>Chart Search Page</h2>

<div>Tal!</div>

${ ui.includeFragment("chartsearch", "searchBox") }
${ ui.includeFragment("chartsearch", "resultBox") }