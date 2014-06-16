<script type="text/javascript">
    var jq = jQuery;


    jq( document ).ready(function() {
        jq( "#date_filter_title" ).click(function() {
            jq( "#date_filter_options" ).toggle();
        });

        jq( "#date_filter_options" ).click(function(e) {
            jq( "#date_filter_options" ).hide();
            var txt = jq(e.target).text();
            jq("#date_filter_title").text(txt);
        });
    });
</script>

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
    .filter_options {
        display: none;
        background: white;
        width: 90px;
        padding: 13px;
        position: absolute;
        border: 1px solid black;
        left:242px;
    }
    .date_filter_title {
        display: inline-block;
        white-space: nowrap;
        background-color: #ddd;
        background-image: -webkit-gradient(linear, left top, left bottom, from(#eee), to(#ccc));
        background-image: -webkit-linear-gradient(top, #eee, #ccc);
        background-image: -moz-linear-gradient(top, #eee, #ccc);
        background-image: -ms-linear-gradient(top, #eee, #ccc);
        background-image: -o-linear-gradient(top, #eee, #ccc);
        background-image: linear-gradient(top, #eee, #ccc);
        border: 1px solid #777;
        padding: 0 1.5em;
        margin: 0.5em 0;
        font: bold 1em/2em Arial, Helvetica;
        text-decoration: none;
        color: #333;
        text-shadow: 0 1px 0 rgba(255,255,255,.8);
        -moz-border-radius: .2em;
        -webkit-border-radius: .2em;
        border-radius: .2em;
        -moz-box-shadow: 0 0 1px 1px rgba(255,255,255,.8) inset, 0 1px 0 rgba(0,0,0,.3);
        -webkit-box-shadow: 0 0 1px 1px rgba(255,255,255,.8) inset, 0 1px 0 rgba(0,0,0,.3);
        box-shadow: 0 0 1px 1px rgba(255,255,255,.8) inset, 0 1px 0 rgba(0,0,0,.3);
    }
    .date_filter_title:hover
    {
        background-color: #eee;
        background-image: -webkit-gradient(linear, left top, left bottom, from(#fafafa), to(#ddd));
        background-image: -webkit-linear-gradient(top, #fafafa, #ddd);
        background-image: -moz-linear-gradient(top, #fafafa, #ddd);
        background-image: -ms-linear-gradient(top, #fafafa, #ddd);
        background-image: -o-linear-gradient(top, #fafafa, #ddd);
        background-image: linear-gradient(top, #fafafa, #ddd);
        cursor: pointer;
    }

    .date_filter_title:active
    {
        -moz-box-shadow: 0 0 4px 2px rgba(0,0,0,.3) inset;
        -webkit-box-shadow: 0 0 4px 2px rgba(0,0,0,.3) inset;
        box-shadow: 0 0 4px 2px rgba(0,0,0,.3) inset;
        position: relative;
        top: 1px;
    }
    .date_filter_title:after {
        content:' ↓'
    }
    .single_filter_option {
        display: block;
        cursor: pointer;
    }

    .demo-container {
        box-sizing: border-box;
        width: 400px;
        height: 300px;
        padding: 20px 15px 15px 15px;
        margin: 15px auto 30px auto;
        border: 1px solid #ddd;
        background: #fff;
        background: linear-gradient(#f6f6f6 0, #fff 50px);
        background: -o-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -ms-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -moz-linear-gradient(#f6f6f6 0, #fff 50px);
        background: -webkit-linear-gradient(#f6f6f6 0, #fff 50px);
        box-shadow: 0 3px 10px rgba(0,0,0,0.15);
        -o-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        -ms-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        -moz-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        -webkit-box-shadow: 0 3px 10px rgba(0,0,0,0.1);
    }

    .demo-placeholder {
        width: 100%;
        height: 100%;
        font-size: 14px;
        line-height: 1.2em;
    }

    .legend, .legend div {
        display: none;
    }

    .bold {
        font-weight: bold;
    }

</style>

<article id="search-box">
    <section>
        <div class="chart-search-wrapper">
            <form class="chart-search-form">
                <div class="chart-search-input">
                    <div class="chart_search_form_inputs">
                        <input type="text" name="patientId" value=${patientId} hidden>
                        <input type="text" id="searchText" name="phrase" class="chart_search_form_text_input inline ui-autocomplete-input" placeholder="${ ui.message("chartsearch.messageInSearchField") }" size="40">
                        <input type="submit" id="searchBtn" class="button inline chart_search_form_button" value="search"/>
                    </div>
                    <div class="filters_section">
                        <div class="dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a href="#" class="filter_method">Any Time</a>
                                    <i class="icon-sort-down" id="icon-arrow-dropdown"></i>
                                </span>
                                <div class="filter_categories">
                                    <hr />
                                        <a class="single_filter_option" onclick="time_filter(new Date(), )">Last Day</a>
                                        <a class="single_filter_option" onclick="time_filter(7)">Last Week</a>
                                        <a class="single_filter_option" onclick="time_filter(30)">Last Month</a>
                                        <a class="single_filter_option" onclick="time_filter(365)">Last Year</a>
                                        <a class="single_filter_option" onclick="refresh_data()">Any Time</a>
                                </div>
                            </div>
                        </div>
                        <div class="dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a href="#" class="filter_method">All Categories</a>
                                    <i class="icon-sort-down" id="icon-arrow-dropdown"></i>
                                </span>
                                <div class="filter_categories">
                                    <hr />
                                    <a class="single_filter_option" onclick="time_filter(0)">Last Day</a>
                                    <a class="single_filter_option" onclick="time_filter(7)">Last Week</a>
                                    <a class="single_filter_option" onclick="time_filter(30)">Last Month</a>
                                    <a class="single_filter_option" onclick="time_filter(365)">Last Year</a>
                                    <a class="single_filter_option" onclick="refresh_data()">Any Time</a>
                                </div>
                            </div>
                        </div>
                        <div class="dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a href="#" class="filter_method">All Locations</a>
                                    <i class="icon-sort-down" id="icon-arrow-dropdown"></i>
                                </span>
                                <div class="filter_categories">
                                    <hr />
                                    <a class="single_filter_option" onclick="time_filter(0)">Last Day</a>
                                    <a class="single_filter_option" onclick="time_filter(7)">Last Week</a>
                                    <a class="single_filter_option" onclick="time_filter(30)">Last Month</a>
                                    <a class="single_filter_option" onclick="time_filter(365)">Last Year</a>
                                    <a class="single_filter_option" onclick="refresh_data()">Any Time</a>
                                </div>
                            </div>
                        </div>
                        <div class="dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a href="#" class="filter_method">All Providers</a>
                                    <i class="icon-sort-down" id="icon-arrow-dropdown"></i>
                                </span>
                                <div class="filter_categories">
                                    <hr />
                                    <a class="single_filter_option" onclick="time_filter(0)">Last Day</a>
                                    <a class="single_filter_option" onclick="time_filter(7)">Last Week</a>
                                    <a class="single_filter_option" onclick="time_filter(30)">Last Month</a>
                                    <a class="single_filter_option" onclick="time_filter(365)">Last Year</a>
                                    <a class="single_filter_option" onclick="refresh_data()">Any Time</a>
                                </div>
                            </div>
                        </div>
                        <div class="dropdown">
                            <div class="inside_categories_filter">
                                <span class="dropdown-name" id="categories_label">
                                    <a href="#" class="filter_method">All Data Types</a>
                                    <i class="icon-sort-down" id="icon-arrow-dropdown"></i>
                                </span>
                                <div class="filter_categories">
                                    <hr />
                                    <a class="single_filter_option" onclick="time_filter(0)">Last Day</a>
                                    <a class="single_filter_option" onclick="time_filter(7)">Last Week</a>
                                    <a class="single_filter_option" onclick="time_filter(30)">Last Month</a>
                                    <a class="single_filter_option" onclick="time_filter(365)">Last Year</a>
                                    <a class="single_filter_option" onclick="refresh_data()">Any Time</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
    			${ ui.includeFragment("chartsearch", "main_results") }
            </form>
        </div>
    </section>
</article>