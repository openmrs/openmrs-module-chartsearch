<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<div id="content" class="container">
    <h1>${ ui.message("chartsearchapp.addSynonymGroup") }</h1>
    <fieldset>
        <form class="simple-form-ui" id="addSynonymGroup" method="POST">
            ${ ui.includeFragment("uicommons", "field/text", [label:ui.message("Unique Synonym Group Name"), formFieldName:"groupName"]) }
            ${ ui.includeFragment("uicommons", "field/text", [label:ui.message("Synonym List separated by ';' "), formFieldName:"synonymList"]) }
            <input type="submit" class="button" value="${ ui.message("general.save") }"/>
        </form>
    </fieldset>
</div>