<div id="content" class="container">
   <fieldset>
       <legend>${ ui.message("chartsearch.searchBox") }</legend>
       <form class="simple-form-ui" id="searchbox" method="POST">
           ${ ui.includeFragment("uicommons", "field/text", [label:ui.message("chartsearch.searchBox"), formFieldName:"phrase"]) }
           <input type="submit" class="button" value="${ ui.message("general.save") }"/>
       </form>
   </fieldset>
</div>

<ul>
    <% resultList.each{ %>
    <li>${ it }</li>
    <% } %>
</ul>