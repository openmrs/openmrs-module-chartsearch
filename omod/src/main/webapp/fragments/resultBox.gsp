

<script>
    var resultJSON ='<% resultList.each{ %> ${ it } <% } %>';
    var jsonAfterParse = JSON.parse(resultJSON);

    jQuery( document ).ready(function() {
        addAllObsGroups(jsonAfterParse);
        addAllSingleObs(jsonAfterParse);
    });

/*    document.getElementById('result_main_box').innerHTML += resultText;*/
</script>