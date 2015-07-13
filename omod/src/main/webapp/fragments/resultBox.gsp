<script>
    var resultJSON ='<% resultList.each{ %> ${ it } <% } %>';
    var jsonAfterParse = JSON.parse(resultJSON);

    jQuery( document ).ready(function() {
        refresh_data(jsonAfterParse);
        autoClickFirstResultToShowItsDetails(jsonAfterParse);
        storeJsonFromServer(jsonAfterParse);
    });

</script>