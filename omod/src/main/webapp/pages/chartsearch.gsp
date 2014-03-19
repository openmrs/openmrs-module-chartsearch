<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<h2>Chart Search Page</h2>


${ ui.includeFragment("chartsearch", "cssIncludes") }
<div class="container">
    ${ ui.includeFragment("chartsearch", "leftPane") }
    ${ ui.includeFragment("chartsearch", "resultBox") }
</div>