<style type="text/css">
    .results_wrap {
        margin-top: 20px;
        background: rgba(0, 70, 63, 0.32);
        padding: 20px;
        display: block;
        border-radius: 3px;
        height: 600px;
    }
    .base_results {
         float: left;
         background: #FFFFFF;
         border-radius: 3px;
         width: 49%;
         min-height: 100%;
         box-shadow: 0 0 15px 0 black;
     }
    .detailed_results {
        float: right;
        background: #FFFFFF;
        border-radius: 3px;
        width: 49%;
        min-height: 100%;
        box-shadow: 0 0 15px 0 black;
    }
    .chart_serach_clear {
        clear: both;
    }
</style>

<div class="results_wrap">
    <div class="base_results">
    	<div id="failed_privileges"><script type="text/javascript">displayFailedPrivileges(jsonAfterParse);</script></div>
        ${ ui.includeFragment("chartsearch", "base_results") }
    </div>
    <div class="detailed_results">
        ${ ui.includeFragment("chartsearch", "detailed_results") }
    </div>
    <div class="chart_serach_clear"></div>
</div>