<script>
    // RA-452 encodeHtmlContent to prevent XSS
    var resultJSON ='<% resultList.each{ %> ${ ui.encodeHtmlContent(it) } <% } %>';
    var jsonAfterParse = JSON.parse(resultJSON);

    jQuery( document ).ready(function() {
        refresh_data(jsonAfterParse);
        autoClickFirstResultToShowItsDetails(jsonAfterParse);
        storeJsonFromServer(jsonAfterParse);
    });

</script>