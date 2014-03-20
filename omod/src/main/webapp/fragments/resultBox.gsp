<div class="resultBox_container" id="result_main_box">
    <div class="form_header">Search Results: </div>
</div>

<script>
    var resultJSON ='<% resultList.each{ %> ${ it } <% } %>';
    var jsonAfterParse = JSON.parse(resultJSON);
    var resultText = viewsFactory.result_row({
        head: "direct_contact_wrap",
        body: "cont_image"
    });
    document.getElementById('result_main_box').innerHTML += resultText;
</script>