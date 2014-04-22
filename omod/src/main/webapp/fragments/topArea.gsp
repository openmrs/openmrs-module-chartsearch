<style type="text/css">
    .chart-search-input {
        background: #3f3d74;
        text-align: left;
        color: white;
        padding: 20px 30px;
        -moz-border-radius: 5px;
        -webkit-border-radius: 5px;
        -o-border-radius: 5px;
        -ms-border-radius: 5px;
        -khtml-border-radius: 5px;
        border-radius: 5px;
    }
    .chart_search_form_text_input {
        min-width: 82% !important;
    }
    .inline {
        display: inline-block;
    }
    .chart_search_form_button {
        margin-left: 30px;
    }
    .form_label_style {
        margin-bottom: 10px !important;
    }
</style>

<article id="search-box">
    <section>
        <div class="chart-search-wrapper">
            <form class="chart-search-form" method="post">
                <label class="form_label_style">
                    <i class="icon-search small"></i>
                    Search for a patient data:
                </label>
                <div class="chart-search-input">
                    <div class="chart_search_form_inputs">
                        <input type="text" name="phrase" class="chart_search_form_text_input inline ui-autocomplete-input" placeholder="Search for.." size="40">
                        <input type="submit" class="button inline chart_search_form_button" value="search"/>
                    </div>
                </div>
            </form>
        </div>
    </section>
</article>