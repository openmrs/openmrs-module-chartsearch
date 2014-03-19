<% ui.includeJavascript("chartsearch", "jQAllRangeSliders-min.js") %>
<% ui.includeCss("chartsearch", "iThing.css") %>


<div class="left_side_form_panel">
<div class="form_header">Search Box</div>

<form class="simple-form-ui" id="searchbox" method="POST">
    <input class="form_input" type="text" placeholder="type a word to search">
    <input type="submit" class="button" value="${ ui.message("general.save") }"/>
    <!-- <div class="form_button" id="send_button">search</div> -->
</form>

<div class="form_header">Filters</div>
<!-- Date range 1 -->
<div class="filter_section">
    <label class="chartsearch_form_label">Date Range:</label>

    <div class="date_slider_middle">
        <div id="slider"></div>
    </div>
</div>
<!-- date range 2 -->
<div class="filter_section">
    <label class="chartsearch_form_label">Date Range 2:</label>

    <div class="date_range">
        <label for="from">From</label>
        <input type="text" id="from" name="from">
        <label for="to">to</label>
        <input type="text" id="to" name="to">
    </div>
</div>
<!-- time collected area -->
<div class="filter_section">
    <label class="chartsearch_form_label">Time collected:</label>

    <div class="date_slider_middle">
        <div id="time_slider"></div>
    </div>
</div>
<!-- value area -->
<div class="filter_section">
    <label class="chartsearch_form_label">value:</label>

    <div class="date_slider_middle">
        <div id="value_slider"></div>
    </div>
</div>
<!-- Location area -->
<div class="filter_section">
    <div class="form_hover_link">
        <label class="chartsearch_form_label_inline">location:</label>

        <div class="inline_div" id="select_all_locations">
            <a class="side_links" id="select_all_locations_link">Select all</a>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="pharamcy" name="locations" class="locations_item">

        <div class="checkbox_item" onclick="changeSelection('pharamcy')">
            <div class="select_only_div hidden_container">
                <a class="select_only">only</a>
            </div>
            <label class="checkbox_label">Pharmacy</label>
        </div>
        <input type="checkbox" id="Inpatient" name="locations" class="locations_item">

        <div class="checkbox_item" onclick="changeSelection('Inpatient')">
            <div class="select_only_div hidden_container">
                <a class="select_only">only</a>
            </div>
            <label class="checkbox_label">Inpatient ward</label>
        </div>
        <input type="checkbox" id="Isolation" name="locations" class="locations_item">

        <div class="checkbox_item" onclick="changeSelection('Isolation')">
            <div class="select_only_div hidden_container">
                <a class="select_only">only</a>
            </div>
            <label class="checkbox_label">Isolation ward</label>
        </div>
        <input type="checkbox" id="Laboratory" name="locations" class="locations_item">

        <div class="checkbox_item" onclick="changeSelection('Laboratory')">
            <div class="select_only_div hidden_container">
                <a class="select_only">only</a>
            </div>
            <label class="checkbox_label">Laboratory</label>
        </div>
        <input type="checkbox" id="Outpatient" name="locations" class="locations_item">

        <div class="checkbox_item" onclick="changeSelection('Outpatient')">
            <div class="select_only_div hidden_container">
                <a class="select_only">only</a>
            </div>
            <label class="checkbox_label">Outpatient clinic</label>
        </div>
    </div>
</div>
<!-- Providers area -->
<div class="filter_section">
    <div class="form_hover_link">
        <label class="chartsearch_form_label_inline">Providers:</label>

        <div class="inline_div" id="select_all_providers">
            <a class="side_links" id="select_all_providers_link">Select all</a>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="tallevi" name="providers" class="providers_item">

        <div class="checkbox_item" onclick="changeSelection('tallevi')">
            <div class="select_only_div hidden_container">
                <a class="select_only_prov">only</a>
            </div>
            <label class="checkbox_label">Tal Levi</label>
        </div>
        <input type="checkbox" id="elizilberman" name="providers" class="providers_item">

        <div class="checkbox_item" onclick="changeSelection('elizilberman')">
            <div class="select_only_div hidden_container">
                <a class="select_only_prov">only</a>
            </div>
            <label class="checkbox_label">Eli Zilberman</label>
        </div>
        <input type="checkbox" id="orhemi" name="providers" class="providers_item">

        <div class="checkbox_item" onclick="changeSelection('orhemi')">
            <div class="select_only_div hidden_container">
                <a class="select_only_prov">only</a>
            </div>
            <label class="checkbox_label">Or Hemi</label>
        </div>
    </div>
</div>
<!-- end providers -->
<!-- Datatypes area -->
<div class="filter_section" id="datatypes_opener">
    <label class="chartsearch_form_label_inline">Data types</label>

    <div style="display: inline-block;" onclick="openSection('datatypes_opener', 'datatype_section');">&#9660;</div>
</div>

<div class="filter_section hidden_container" id="datatype_section">
    <div class="form_hover_link">
        <label class="chartsearch_form_label_inline">Data Type:</label>

        <div style="display: inline-block;"
             onclick="closeSection('datatype_section', 'datatypes_opener');">&#9650;</div>

        <div class="inline_div" id="select_all_dataTypes">
            <a class="side_links" id="select_all_dataTypes_link">Select all</a>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="numeric" name="datatypes" class="datatypes_item">

        <div class="checkbox_item" onclick="changeSelection('numeric')">
            <div class="select_only_div hidden_container">
                <a class="select_only_datatype">only</a>
            </div>
            <label class="checkbox_label">Numeric</label>
        </div>
        <input type="checkbox" id="coded" name="datatypes" class="datatypes_item">

        <div class="checkbox_item" onclick="changeSelection('coded')">
            <div class="select_only_div hidden_container">
                <a class="select_only_datatype">only</a>
            </div>
            <label class="checkbox_label">Coded</label>
        </div>
        <input type="checkbox" id="text_field" name="datatypes" class="datatypes_item">

        <div class="checkbox_item" onclick="changeSelection('text_field')">
            <div class="select_only_div hidden_container">
                <a class="select_only_datatype">only</a>
            </div>
            <label class="checkbox_label">Text</label>
        </div>
        <input type="checkbox" id="boolean_field" name="datatypes" class="datatypes_item">

        <div class="checkbox_item" onclick="changeSelection('boolean_field')">
            <div class="select_only_div hidden_container">
                <a class="select_only_datatype">only</a>
            </div>
            <label class="checkbox_label">Boolean</label>
        </div>
        <input type="checkbox" id="na_field" name="datatypes" class="datatypes_item">

        <div class="checkbox_item" onclick="changeSelection('na_field')">
            <div class="select_only_div hidden_container">
                <a class="select_only_datatype">only</a>
            </div>
            <label class="checkbox_label">N.A</label>
        </div>
        <input type="checkbox" id="datetime_field" name="datatypes" class="datatypes_item">

        <div class="checkbox_item" onclick="changeSelection('datetime_field')">
            <div class="select_only_div hidden_container">
                <a class="select_only_datatype">only</a>
            </div>
            <label class="checkbox_label">DateTime</label>
        </div>
    </div>
</div>
<!-- end Datatypes area -->
<!-- Concept classes -->
<div class="filter_section" id="concepts_opener">
    <label class="chartsearch_form_label_inline">Concept class</label>

    <div style="display: inline-block;" onclick="openSection('concepts_opener', 'Concept_section');">&#9660;</div>
</div>

<div class="filter_section hidden_container" id="Concept_section">
    <div class="form_hover_link">
        <label class="chartsearch_form_label_inline">Concept class:</label>

        <div style="display: inline-block;" onclick="closeSection('Concept_section', 'concepts_opener');">&#9650;</div>

        <div class="inline_div" id="select_all_concepts">
            <a class="side_links" id="select_all_concepts_link">Select all</a>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="hivtest" name="concepts" class="concepts_item">

        <div class="checkbox_item" onclick="changeSelection('hivtest')">
            <div class="select_only_div hidden_container">
                <a class="select_only_concepts">only</a>
            </div>
            <label class="checkbox_label">HIV Tests</label>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="bloodtests" name="concepts" class="concepts_item">

        <div class="checkbox_item" onclick="changeSelection('bloodtests')">
            <div class="select_only_div hidden_container">
                <a class="select_only_concepts">only</a>
            </div>
            <label class="checkbox_label">Blood Tests</label>
        </div>
    </div>
</div>
<!-- end concept classes -->
<!-- OpenMRS Data Model -->
<div class="filter_section" id="data_model_opener">
    <label class="chartsearch_form_label_inline">Data Model</label>

    <div style="display: inline-block;" onclick="openSection('data_model_opener', 'Data_Model_section');">&#9660;</div>
</div>

<div class="filter_section hidden_container" id="Data_Model_section">
    <div class="form_hover_link">
        <label class="chartsearch_form_label_inline">Data Model:</label>

        <div style="display: inline-block;"
             onclick="closeSection('Data_Model_section', 'data_model_opener');">&#9650;</div>

        <div class="inline_div" id="select_all_data_models">
            <a class="side_links" id="select_all_data_models_link">Select all</a>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="form_model" name="data_model" class="data_model_item">

        <div class="checkbox_item" onclick="changeSelection('form_model')">
            <div class="select_only_div hidden_container">
                <a class="select_only_data_model">only</a>
            </div>
            <label class="checkbox_label">Form</label>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="observation" name="data_model" class="data_model_item">

        <div class="checkbox_item" onclick="changeSelection('observation')">
            <div class="select_only_div hidden_container">
                <a class="select_only_data_model">only</a>
            </div>
            <label class="checkbox_label">Observation</label>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="visit" name="data_model" class="data_model_item">

        <div class="checkbox_item" onclick="changeSelection('visit')">
            <div class="select_only_div hidden_container">
                <a class="select_only_data_model">only</a>
            </div>
            <label class="checkbox_label">Visit</label>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="Encounter" name="data_model" class="data_model_item">

        <div class="checkbox_item" onclick="changeSelection('Encounter')">
            <div class="select_only_div hidden_container">
                <a class="select_only_data_model">only</a>
            </div>
            <label class="checkbox_label">Encounter</label>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="Orders" name="data_model" class="data_model_item">

        <div class="checkbox_item" onclick="changeSelection('Orders')">
            <div class="select_only_div hidden_container">
                <a class="select_only_data_model">only</a>
            </div>
            <label class="checkbox_label">Orders</label>
        </div>
    </div>

</div>
<!-- end data types -->
<!-- Category -->
<div class="filter_section">
    <div class="form_hover_link">
        <label class="chartsearch_form_label_inline">Category:</label>

        <div class="inline_div" id="select_all_categories">
            <a class="side_links" id="select_all_categories_link">Select all</a>
        </div>
    </div>

    <div class="checkbox_selection">
        <input type="checkbox" id="category_labs" name="categories" class="category_item">

        <div class="checkbox_item" onclick="changeSelection('category_labs')">
            <div class="select_only_div hidden_container">
                <a class="select_only_cat">only</a>
            </div>
            <label class="checkbox_label">Labs</label>
        </div>
        <input type="checkbox" id="category_orders" name="categories" class="category_item">

        <div class="checkbox_item" onclick="changeSelection('category_orders')">
            <div class="select_only_div hidden_container">
                <a class="select_only_cat">only</a>
            </div>
            <label class="checkbox_label">Orders</label>
        </div>
        <input type="checkbox" id="category_vitals" name="categories" class="category_item">

        <div class="checkbox_item" onclick="changeSelection('category_vitals')">
            <div class="select_only_div hidden_container">
                <a class="select_only_cat">only</a>
            </div>
            <label class="checkbox_label">Vitals</label>
        </div>
        <input type="checkbox" id="category_imaging" name="categories" class="category_item">

        <div class="checkbox_item" onclick="changeSelection('category_imaging')">
            <div class="select_only_div hidden_container">
                <a class="select_only_cat">only</a>
            </div>
            <label class="checkbox_label">Imaging</label>
        </div>
    </div>
</div>
<!-- end providers -->
</div>

<script>
    jQuery(".select_only_cat").click(function () {
        checkOnlyCat(this);
    });

    function checkOnlyCat(currentDiv) {
        jQuery(".category_item").each(function () {
            this.checked = false;
        });
    }

    //for concepts only button
    jQuery(".select_only_data_model").click(function () {
        checkOnlyDataModel(this);
    });

    function checkOnlyDataModel(currentDiv) {
        jQuery(".data_model_item").each(function () {
            this.checked = false;
        });
    }

    function openSection(divID1, divID2) {
        jQuery("#" + divID1).fadeOut("fast", function () {
            jQuery("#" + divID2).fadeIn("fast", function () {

            });
        });
    }

    function closeSection(divID1, divID2) {
        jQuery("#" + divID1).fadeOut("fast", function () {
            jQuery("#" + divID2).fadeIn("fast", function () {

            });
        });
    }

    //for concepts only button
    jQuery(".select_only_concepts").click(function () {
        checkOnlyCencept(this);
    });

    function checkOnlyCencept(currentDiv) {
        jQuery(".concepts_item").each(function () {
            this.checked = false;
        });
    }

    //for datatype only button
    jQuery(".select_only_datatype").click(function () {
        checkOnlyDataType(this);
    });
    function checkOnlyDataType(currentDiv) {
        jQuery(".datatypes_item").each(function () {
            this.checked = false;
        });
    }
    //for providers only button
    jQuery(".select_only_prov").click(function () {
        checkOnlyProv(this);
    });
    function checkOnlyProv(currentDiv) {
        jQuery(".providers_item").each(function () {
            this.checked = false;
        });
    }

    // for locations only button
    jQuery(".select_only").click(function () {
        checkOnly(this);
    });
    function checkOnly(currentDiv) {
        jQuery(".locations_item").each(function () {
            this.checked = false;
        });
    }
    /*
     function changeSelection
     @param divID
     gets a divID and checks/unchecks his coresponding checkbox
     */
    function changeSelection(divID) {
        var isChecked = jQuery("#" + divID);
        if (isChecked.is(':checked')) {
            jQuery("#" + divID).prop("checked", false);
        }
        else {
            jQuery("#" + divID).prop("checked", true);
        }

    }

    /*
     *   select all button - check all of the list
     */

    jQuery("#select_all_categories_link").click(function (event) {
        jQuery(".category_item").each(function () {
            this.checked = true;
        });
    });

    jQuery("#select_all_data_models_link").click(function (event) {
        jQuery(".data_model_item").each(function () {
            this.checked = true;
        });
    });

    jQuery("#select_all_concepts_link").click(function (event) {
        jQuery(".concepts_item").each(function () {
            this.checked = true;
        });
    });

    jQuery("#select_all_locations_link").click(function (event) {
        jQuery(".locations_item").each(function () {
            this.checked = true;
        });
    });

    jQuery("#select_all_providers_link").click(function (event) {
        jQuery(".providers_item").each(function () {
            this.checked = true;
        });
    });

    jQuery("#select_all_dataTypes_link").click(function (event) {
        jQuery(".datatypes_item").each(function () {
            this.checked = true;
        });
    });


    /*
     *   open "only" div on some line hover
     */
    jQuery(".checkbox_item").hover(function () {
        jQuery(this).find("div.select_only_div").css("display", "inline-block");
    }, function () {
        jQuery(this).find("div.select_only_div").css("display", "none");
    });


    /*
     function Two digits -
     @param val - hour to add 0 before
     @return fix number with leading zero
     */
    function TwoDigits(val) {
        if (val < 10) {
            return "0" + val;
        }
        return val;
    }
    jQuery("#slider").dateRangeSlider();
    jQuery("#time_slider").dateRangeSlider({
        bounds: {min: new Date(2013, 0, 1), max: new Date(2013, 0, 1, 23, 59, 59)},
        defaultValues: {min: new Date(2013, 0, 1, 8), max: new Date(2013, 0, 1, 18)},
        formatter: function (value) {
            var hours = value.getHours(),
                    minutes = value.getMinutes();
            return TwoDigits(hours) + ":" + TwoDigits(minutes);
        }
    });
    jQuery("#value_slider").editRangeSlider({type: "number"});
    jQuery(function () {
        jQuery("#from").datepicker({
            defaultDate: "+1w",
            changeMonth: true,
            numberOfMonths: 2,
            width: 300,
            onClose: function (selectedDate) {
                jQuery("#to").datepicker("option", "minDate", selectedDate);
            }
        });
        jQuery("#to").datepicker({
            defaultDate: "+1w",
            changeMonth: true,
            numberOfMonths: 2,
            onClose: function (selectedDate) {
                jQuery("#from").datepicker("option", "maxDate", selectedDate);
            }
        });
    });
</script>