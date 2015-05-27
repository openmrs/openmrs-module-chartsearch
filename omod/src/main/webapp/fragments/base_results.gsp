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
        padding: 3px;
        margin-top: 2px;
    }
    .obsgroup_wrap:hover {
        background-color: #d6d6d6;
        cursor: pointer;
    }
    .obsgroup_current {
        background-color: #d6d6d6;
    }
    .obsgroup_titleBox {
		width: 65%; 
		float: left;
    }
    
    .obsgroup_title {
        margin: 0;
        color: #00463f;
        font-weight: bold;
		font-size: 14px;
		float: left;
    }
    
    .obsgroup_valueText {
		font-size: 14px;
		float: left;
    }
    
    .obsgroup_value {
        padding-top: 10px;
		float: left;
    }
    .obsgroup_date {
        color: #949494;
        font-size: 12px;
    }
    .obsgroup_range {
        float: right;
        color: #00463f;
        font-size: 14px;
        padding-top: 10px;
    }
    .obsgroup_rows {
        margin-top: 5px;
    }
    .obsgroup_row {
        margin-bottom: 5px;
    }
    .obsgroup_row_first_section {
         width: 50%;
         font-size: 13px;
     }
    .obsgroup_row_sec_section {
         font-size: 13px;
    }
    .obsgroup_row_trd_section {
        width: 25%;
        font-size: 13px;
        float: right;
    }
    .inline {
        display: inline-block;
        vertical-align: text-top;
    }
    .more_then_normal, .less_then_normal, .red {
        color: red;
    }
    .more_then_normal:after {
        content: '↑';
        position: relative;
        top: -2px;
        left: 2px;
    }
    .less_then_normal:after {
        content: '↓';
        position: relative;
        top: -2px;
        left: 2px;
    }

    .obsgroup_all_wrapper {
        font-size: 13px;
    }

</style>

<div class="results_box_container" id="results_box_container-id">
	<div id="found-results-summary"></div>
    <div class="results_table_wrap" id="obsgroups_results">

    </div>
</div>
