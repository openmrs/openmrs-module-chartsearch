

<script>
    var resultJSON ='<% resultList.each{ %> ${ it } <% } %>';
    var jsonAfterParse = JSON.parse(resultJSON);


    jQuery( document ).ready(function() {
        setTimeout(function(){
            /*addAllObsGroups(jsonAfterParse);*/
            addAllSingleObs(jsonAfterParse);
        },100);

    });

/*    document.getElementById('result_main_box').innerHTML += resultText;*/
</script>