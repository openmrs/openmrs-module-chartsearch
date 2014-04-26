<div class="resultBox_container" id="result_main_box">
    <div class="form_header">Search Results: </div>
</div>

<script>
    var resultJSON ='<% resultList.each{ %> ${ it } <% } %>';
    var jsonAfterParse = JSON.parse(resultJSON);
    var obs=jsonAfterParse.observations;
    var resultText = '';
    for(var i=0; i<obs.length; i++)
    {
        resultText+=viewsFactory.result_row({
            head: obs[i].date,
            body: obs[i].concept_name
        });
    }

/*    document.getElementById('result_main_box').innerHTML += resultText;*/
</script>