<style type="text/css">
    .base_results {
        height: 480px;
        overflow-x: scroll;
    }
    .results_box_container {
        padding: 10px 20px;
    }
    .border_bottom {
        border-bottom: 1px black solid;
    }
    .results_table_wrap {
        height: 100%;
        /*overflow-y: scroll;*/
    }
    .obsgroup_wrap {
        border: 1px solid #bebebe;
        padding: 5px;
        margin-top: 2px;
    }
    .obsgroup_wrap:hover {
        background-color: #d6d6d6;
        cursor: pointer;
    }
    .obsgroup_title {
        margin: 0;
        color: #00463f;
        font-weight: bold;
        font-size: 13px;
        float: left;
        width: 190px;
    }
    .obsgroup_date {
        float: right;
        color: #00463f;
        font-size: 14px;
    }
    .obsgroup_rows {
        margin-top: 10px;
    }
    .obsgroup_row_first_section {
         width: 40%;
     }
    .obsgroup_row_sec_section {
        width: 36%;
    }
    .obsgroup_row_trd_section {
        width: 20%;
    }
    .inline {
        display: inline-block;
    }
</style>

<div class="results_box_container">
    <h3 class="border_bottom">Results: </h3>
    <div class="results_table_wrap" id="obsgroups_results">

    </div>
</div>
