<script>
    var results ='<% results.each{ %> ${ it } <% } %>';
    var jsonAfterParse = JSON.parse(results);

    jQuery( document ).ready(function() {
        refresh_data();
    });
    
</script>