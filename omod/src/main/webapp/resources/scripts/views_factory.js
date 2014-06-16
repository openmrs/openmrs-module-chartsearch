/**
 * Views manipulations.
 * Created by Tallevi12
 */
var dates = {
    convert:function(d) {
        // Converts the date in d to a date-object. The input can be:
        //   a date object: returned without modification
        //  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
        //   a number     : Interpreted as number of milliseconds
        //                  since 1 Jan 1970 (a timestamp)
        //   a string     : Any format supported by the javascript engine, like
        //                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
        //  an object     : Interpreted as an object with year, month and date
        //                  attributes.  **NOTE** month is 0-11.
        return (
            d.constructor === Date ? d :
                d.constructor === Array ? new Date(d[0],d[1],d[2]) :
                    d.constructor === Number ? new Date(d) :
                        d.constructor === String ? new Date(format_date(d)) :
                            typeof d === "object" ? new Date(d.year,d.month,d.date) :
                                NaN
            );
    },
    compare:function(a,b) {
        // Compare two dates (could be of any type supported by the convert
        // function above) and returns:
        //  -1 : if a < b
        //   0 : if a = b
        //   1 : if a > b
        // NaN : if a or b is an illegal date
        // NOTE: The code inside isFinite does an assignment (=).
        return (
            isFinite(a=this.convert(a).valueOf()) &&
                isFinite(b=this.convert(b).valueOf()) ?
                (a>b)-(a<b) :
                NaN
            );
    },
    inRange:function(d,start,end) {
        // Checks if date in d is between dates in start and end.
        // Returns a boolean or NaN:
        //    true  : if d is between start and end (inclusive)
        //    false : if d is before start or after end
        //    NaN   : if one or more of the dates is illegal.
        // NOTE: The code inside isFinite does an assignment (=).
        return (
            isFinite(d=this.convert(d).valueOf()) &&
                isFinite(start=this.convert(start).valueOf()) &&
                isFinite(end=this.convert(end).valueOf()) ?
                start <= d && d <= end :
                NaN
            );
    }
}

        /*Function that gets a string as a parameter
        The function capitalize the first char in that string
        The function decapitalize the rest*/

function capitalizeFirstLetter(string)
{
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}


var viewsFactory;
var doT;
var current_JSON_OBJECT;

viewsFactory = {

    result_row: doT.template('<div class="result_conainer"><div class="result_header">{{=it.head || \"\" }}</div><div class="result_body">{{=it.body || \"\" }}</div></div>')

}

function addAllSingleObs(obsJSON)
{
    console.log(obsJSON);
    var resultText='';
    var single_obsJSON=obsJSON.obs_singles;
    if (typeof single_obsJSON !== 'undefined')
    {
        resultText+='<h2>Single observations</h2>';
        for(var i=0;i<single_obsJSON.length;i++){
            resultText+=addSingleObsToResults(single_obsJSON[i]);
        }
        document.getElementById('obsgroups_results').innerHTML+=resultText;
    }
}

function addSingleObsToResults(obsJSON)
{
    var obs_id_html = '';
    if(typeof obsJSON.observation_id !== 'undefined') {
        obs_id_html = 'id="obs_single_'+obsJSON.observation_id+'"';
    }
    var resultText = '';
    resultText+='<div class="obsgroup_wrap"' + obs_id_html +' onclick="load_single_detailed_obs('+obsJSON.observation_id+');">';
    resultText+='<div class="obsgroup_first_row">';
    resultText+='<div class="obsgroup_titleBox">';
    resultText+='<h3 class="obsgroup_title">';
    resultText+=capitalizeFirstLetter(obsJSON.concept_name);
    resultText+='</h3>';
    resultText+='<br><span class="obsgroup_date">';
    resultText+=getDateStr(obsJSON.date);
    resultText+='</span></div>';
    if (obsJSON.value_type && obsJSON.value_type === "Text") {
	    resultText+='<span class="obsgroup_valueText">';
	    resultText+=obsJSON.value.substring(0, 50) + "...";
	    resultText+='</span>'
    }
    else {
        resultText+='<span class="obsgroup_value">';
	    resultText+=obsJSON.value;
	    if (obsJSON.units_of_measurement) {
	        resultText+='<span class="cs_span_measure">';
	    	resultText+=' '+obsJSON.units_of_measurement;
	    	resultText+='</span>';
	    }
	    resultText+='</span>'
    }

    resultText+='<span class="obsgroup_range">';
    if (typeof obsJSON.absolute_low !== 'undefined' && typeof obsJSON.absolute_high !== 'undefined')
    {
        resultText+='('+obsJSON.absolute_low+'-'+obsJSON.absolute_high+')';
    }
    resultText+='</span>'
    resultText+='<div class="chart_serach_clear"></div>';
    resultText+='</div>';
    resultText+='</div>';
    return resultText;
}

function get_single_obs_by_id(obs_id)
{
    var obs_single_JSON=jsonAfterParse.obs_singles;
    for(var i=0;i<obs_single_JSON.length;i++){
        if(obs_single_JSON[i].observation_id==obs_id)
        {
            return obs_single_JSON[i];
        }
    }
    var obsgroupJSON=jsonAfterParse.obs_groups;
    for(var i=0;i<obsgroupJSON.length;i++){
        var singleGroup = obsgroupJSON[i].observations;
        for(var j=0;j<singleGroup.length;j++){
            if(singleGroup[j].observation_id==obs_id)
            {
                return singleGroup[j];
            }
        }
    }
    return -1;
}

function array_sort(a,b) {
    var first_date = new Date(a[0]);
    var second_date = new Date(b[0]);
    return dates.compare(first_date, second_date);
}

function get_obs_graph_points(obs_id) {
    var res = new Array();
    var cur, date_formmated, int_date;
    var obs_obj = get_single_obs_by_id(obs_id);
    var obs_name = obs_obj.concept_name;
    var history_json = get_obs_history_json_by_name(obs_name);
    for(var i=0;i<history_json.length;i++){
        int_date = parseInt(history_json[i].date);
        date_formmated = new Date (int_date);
        cur = [date_formmated.getTime(), history_json[i].value];
        res.push(cur);
    }
    res.sort(array_sort);
    return res;
}

function get_obs_spark_points(obs_id) {
    var res = new Array();
    var obs_obj = get_single_obs_by_id(obs_id);
    var obs_name = obs_obj.concept_name;
    var history_json = get_obs_history_json_by_name(obs_name);
    for(var i=0;i<history_json.length;i++){
        res.push(history_json[i].value);
    }
    return res;
}

function get_obs_ticks(obs_id) {
    var res = new Array();
    var obs_obj = get_single_obs_by_id(obs_id);
    var obs_name = obs_obj.concept_name;
    var history_json = get_obs_history_json_by_name(obs_name);
    for(var i=0;i<history_json.length;i++){
        res.push(history_json[i].date);
    }
    return res;
}


function enable_graph(obs_id) {

    var observation_obj = get_single_obs_by_id(obs_id);
    var data2 = get_obs_graph_points(obs_id);
    var mark = {
        enabled: true,
        showMinMax: false,
        avg:0
    };
    if (typeof observation_obj.normal_high !== 'undefined')
    {
        mark.max = parseInt(observation_obj.normal_high);
        mark.showMinMax = true;
    }
    if (typeof observation_obj.normal_low !== 'undefined')
    {
        mark.min = parseInt(observation_obj.normal_low);
        mark.showMinMax = true;
    }

    var plot = $.plot("#placeholder", [
        { data: data2, label: capitalizeFirstLetter(observation_obj.concept_name)}
    ], {
        series: {
            lines: {
                show: true
            },
            autoMarkings: mark,
            points: {
                show: true
            }
        },
        grid: {
            hoverable: true,
            clickable: true
        },
        yaxis: {
        },
        xaxis: {
            mode: "time",
            timeformat: "%d/%m",
            ticks: get_obs_ticks(obs_id)
        }
    });

    $("<div id='tooltip'></div>").css({
        position: "absolute",
        display: "none",
        border: "1px solid #fdd",
        padding: "2px",
        "background-color": "#fee",
        opacity: 0.80
    }).appendTo("body");

    $("#placeholder").bind("plothover", function (event, pos, item) {

            if (item) {
                var x = item.datapoint[0],
                    y = item.datapoint[1];

                $("#tooltip").html(item.series.label + ": "+ y + "<br /> Taken date:" + getDateStr(x))
                    .css({top: item.pageY+5, left: item.pageX+5})
                    .fadeIn(200);
            } else {
                $("#tooltip").hide();
            }
    });
}


function load_single_detailed_obs(obs_id){
    removeAllHovering();
    $( "#obs_single_"+obs_id ).addClass( "obsgroup_current" );
    var obsJSON = get_single_obs_by_id(obs_id);
    var resultText='';
    resultText+='<div class="obsgroup_view">';
    resultText+='<h3 class="chartserach_center">';
    resultText+=capitalizeFirstLetter(obsJSON.concept_name);
    resultText+='</h3>';

    resultText+='<div class="demo-container">' +
        '<div id="placeholder" class="demo-placeholder"></div>' +
        '</div>';
    // resultText+='<div class="demo-container"><h1 class="graph_title">Graph</h1> <div id="placeholder" class="demo-placeholder" style="width:550px;height:300px;margin:0 auto;"></div></div>';
    /*resultText+='<div class="obsgroup_all_wrapper">';*/
    resultText+=load_single_obs_history(obs_id);
    /*resultText+='</div>';*/

    resultText+='<div class="obsgroup_all_wrapper">';

    /*resultText+='<label class="cs_label">';
    resultText+='Date: ';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=getDateStr(obsJSON.date);
    resultText+='</span>';
    resultText+='<br />';*/
    resultText+='<label class="cs_label">';
    resultText+='Value Type:';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=obsJSON.value_type;
    resultText+='</span>';
    resultText+='<br />';
    resultText+='<label class="cs_label">';
    resultText+='Location: ';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=obsJSON.location;
    resultText+='</span>';
    /*resultText+='<br />';*/
    /*resultText+='<label class="cs_label">';
    resultText+='Value:';
    resultText+='</label>';

    resultText+='<span class="cs_span">';
    resultText+=obsJSON.value;
    if (obsJSON.units_of_measurement) {
        resultText+='<span class="cs_span_measure">';
    	resultText+=' '+obsJSON.units_of_measurement;
    	resultText+='</span>';
    }
    resultText+='</span>';*/

    resultText+='<br />';
    if (obsJSON.absolute_high) {
	    resultText+='<label class="cs_label">';
	    resultText+='Absolute High:';
	    resultText+='</label>';
	    resultText+='<span class="cs_span">';
	    resultText+=obsJSON.absolute_high;
	    resultText+='</span>';
	    resultText+='<br />';
    }

    if (obsJSON.absolute_low) {
	    resultText+='<label class="cs_label">';
	    resultText+='Absolute Low:';
	    resultText+='</label>';
	    resultText+='<span class="cs_span">';
	    resultText+=obsJSON.absolute_low;
	    resultText+='</span>';
    }
    if (obsJSON.normal_high) {
        resultText+='<label class="cs_label">';
        resultText+='Normal High:';
        resultText+='</label>';
        resultText+='<span class="cs_span">';
        resultText+=obsJSON.normal_high;
        resultText+='</span>';
        resultText+='<br />';
    }

    if (obsJSON.normal_low) {
        resultText+='<label class="cs_label">';
        resultText+='Normal Low:';
        resultText+='</label>';
        resultText+='<span class="cs_span">';
        resultText+=obsJSON.normal_low;
        resultText+='</span>';
    }
    resultText+='</div>';
    resultText+='</div>';

    document.getElementById('obsgroup_view').innerHTML=resultText;
    enable_graph(obs_id);
}

function load_single_obs_history(obs_id) {
    resultText='<h3>History</h3>';
    var obs_obj = get_single_obs_by_id(obs_id);
    var obs_name = obs_obj.concept_name;
    var history_json = get_obs_history_json_by_name(obs_name);
    resultText+='<table><tr><th>Date</th><th>Value</th></tr>';
    for(var i=0;i<history_json.length;i++){
        var red = '';
        var addition ='';
        if (typeof history_json[i].normal_high !== 'undefined')
        {
            if(history_json[i].value > history_json[i].normal_high) {
                red=' red ';
                addition = ' more_then_normal ';
            }
        }
        if (typeof history_json[i].normal_low !== 'undefined')
        {
            if(history_json[i].value < history_json[i].normal_low) {
                red=' red ';
                addition = ' less_then_normal ';
            }
        }
        resultText+='<tr class="'+red+'"><td>'+getDateStr(history_json[i].date)+'</td><td><div class="'+addition+'">'+history_json[i].value+'</div></td></tr>';
    }
    resultText+='</table>';
    return resultText;
}

function format_date(obs_date) {
    return obs_date.substring(3, 5)+'/'+obs_date.substring(0, 2)+'/'+obs_date.substring(6, 8);
}

function get_obs_history_json_by_name(obs_name) {
    var result = new Array();
    var single_obsJSON=jsonAfterParse.obs_singles;
    if (typeof single_obsJSON !== 'undefined')
    {
        for(var i=0;i<single_obsJSON.length;i++){
            if(single_obsJSON[i].concept_name === obs_name) {
                result.push(single_obsJSON[i]);
            }
        }
    }

    var obsgroupJSON=jsonAfterParse.obs_groups;
    for(var i=0;i<obsgroupJSON.length;i++){
        var singleGroup = obsgroupJSON[i].observations;
        for(var j=0;j<singleGroup.length;j++){
            if(singleGroup[j].concept_name==obs_name)
            {
                result.push(singleGroup[j]);
            }
        }
    }

    result.sort(compare);
    return result;
}

function compare(a,b) {
    return dates.compare(a.date, b.date);
}


function addAllObsGroups(obsJSON)
{
    var resultText='';
    var obsgroupJSON=obsJSON.obs_groups;
    if (typeof obsgroupJSON !== 'undefined')
    {
        resultText+='<h2>Observation Groups</h2>';
        for(var i=0;i<obsgroupJSON.length;i++){
            resultText+=addObsGroupToResults(obsgroupJSON[i]);
        }
        document.getElementById('obsgroups_results').innerHTML+=resultText;
    }
}

function getDateStr(date_str, onlyDate) {
    date_str = parseInt(date_str);
    var date_obj = new Date(date_str);
    var ans = date_obj.toLocaleString('he-IL');
    if(onlyDate) {
        ans = date_obj.toLocaleDateString('he-IL');
    }
    return ans;
}

function addObsGroupToResults(obsJSON)
{
    var resultText = '';
    var obs_id_html = '';
    var obs_id = 'obs_group_'+obsJSON.group_Id;
    if(typeof obsJSON.group_Id !== 'undefined') {
        obs_id_html = 'id="obs_group_'+obsJSON.group_Id+'"';
    }
    resultText+='<div class="obsgroup_wrap" '+obs_id_html+' onclick="load_detailed_obs('+obsJSON.group_Id+');">';
    resultText+='<div class="obsgroup_first_row">';
	resultText+='<div class="obsgroup_titleBox">';
    if (typeof obsJSON.group_name !== 'undefined')
    {
        resultText+='<h3 class="obsgroup_title">';
        resultText+=obsJSON.group_name;
        resultText+='</h3>';
    }
    if (typeof obsJSON.last_taken_date !== 'undefined')
    {
        resultText+='<br><span class="obsgroup_date">';
        resultText+=getDateStr(obsJSON.last_taken_date);
        resultText+='</span>'
    }
    resultText+='</div>'
    resultText+='<div class="chart_serach_clear"></div>';
    resultText+='</div>';
    resultText+=innerObservationsHTML(obsJSON.observations);
    resultText+='</div>';
    return resultText;
}

function innerObservationsHTML(obsJSON) {
    var resultText='';
    resultText+='<div class="obsgroup_rows">';
    for(var i=0;i<obsJSON.length;i++){
        resultText+=single_obs_html(obsJSON[i]);
    }
    resultText+='</div>';
    return resultText;
}

function displayOnlyIfDef(display_opt) {
    if (typeof display_opt !== 'undefined')
    {
        return display_opt;
    }
    else {
        return '';
    }
}

function single_obs_html(obsJSON) {
    console.log('====single obs html function=========');
    var resultText='';
    var red = '';
    if (typeof obsJSON.normal_high !== 'undefined')
    {
        if(obsJSON.value > obsJSON.normal_high) {
            red=' red ';
        }
    }
    if (typeof obsJSON.normal_low !== 'undefined')
    {
        if(obsJSON.value < obsJSON.normal_low) {
            red=' red ';
        }
    }
    if (typeof obsJSON.chosen !== 'undefined')
    {
        red+=' bold ';
    }
    resultText+='<div class="obsgroup_row ' + red + '">';
    if (typeof obsJSON.concept_name !== 'undefined')
    {
        resultText+='<div class="obsgroup_row_first_section inline">';
        resultText+=capitalizeFirstLetter(obsJSON.concept_name);
        resultText+='</div>';
    }
    if (typeof obsJSON.value !== 'undefined')
    {
        var change = '';
        if (typeof obsJSON.normal_high !== 'undefined')
        {
            if(obsJSON.value > obsJSON.normal_high) {
                change+=' more_then_normal ';
            }
        }
        if (typeof obsJSON.normal_low !== 'undefined')
        {
            if(obsJSON.value < obsJSON.normal_low) {
                change+=' less_then_normal ';
            }
        }
        resultText+='<div class="obsgroup_row_sec_section inline '+ change + '">';
        resultText+=obsJSON.value + ' ' + displayOnlyIfDef(obsJSON.units_of_measurement);
        resultText+='</div>';
    }
    if (typeof obsJSON.normal_low !== 'undefined' && typeof obsJSON.normal_high !== 'undefined')
    {
        resultText+='<div class="obsgroup_row_trd_section inline ">';
        resultText+='(' + obsJSON.normal_low + ' - ' + obsJSON.normal_high + ')';
        resultText+='</div>';
    }
    resultText+='</div>';
    return resultText;
}

function get_obs_by_id(id)
{
    var obsgroupJSON=jsonAfterParse.obs_groups;
    for(var i=0;i<obsgroupJSON.length;i++){
        if(obsgroupJSON[i].group_Id==id)
        {
            return obsgroupJSON[i];
        }
    }
    return -1;
}

function removeAllHovering() {
    $( ".obsgroup_wrap" ).removeClass( "obsgroup_current" );
}

function load_detailed_obs(obs_id)
{
    removeAllHovering();
    $( "#obs_group_"+obs_id ).addClass( "obsgroup_current" );
    var obsJSON = get_obs_by_id(obs_id);
    var resultText='';
    resultText+='<div class="obsgroup_view">';
    resultText+='<h3 class="chartserach_center">';
    resultText+=obsJSON.group_name;
    resultText+='</h3>';
    resultText+='<div class="obsgroup_all_wrapper">';
    var singleObs = obsJSON.observations;
    for(var i=0;i<singleObs.length;i++){
        var isBold = '';
        if (typeof singleObs[i].chosen !== 'undefined')
        {
            isBold=' bold ';
        }
        resultText+='<div class="obsgroup_item_row' + isBold +'" onclick="load_single_detailed_obs('+singleObs[i].observation_id+')">';
        resultText+='<div class="obsgroup_item_first inline">';
        resultText+=capitalizeFirstLetter(singleObs[i].concept_name);
        resultText+='</div>';
        resultText+='<div class="obsgroup_item_sec inline">';
        resultText+=singleObs[i].value +" "+ singleObs[i].units_of_measurement;
        resultText+='</div>';
        resultText+='<div class="obsgroup_item_frth inline">';
        resultText+=getDateStr(singleObs[i].date, true);
        resultText+='</div>';
        resultText+='<span id="single_spark_'+singleObs[i].observation_id+'">Load</span>';
        resultText+='</div>';
    }
    resultText+='</div>';
    resultText+='</div>';
    $("#obsgroup_view").html(resultText);
    var singleObs = obsJSON.observations;
    for(var i=0;i<singleObs.length;i++){
        var spark = get_obs_spark_points(singleObs[i].observation_id);
        $("#single_spark_"+singleObs[i].observation_id).sparkline(spark, {
            type: 'line',
            width: '150',
            normalRangeMin: singleObs[i].normal_low,
            normalRangeMax: singleObs[i].normal_high,
            normalRangeColor: '#d3ffa8',
            drawNormalOnTop: true});
    }
}

function get_today_date(time_back) {
    var today = new Date();
    today.setDate(today.getDate()-time_back);
    var dd = today.getDate();
    var mm = today.getMonth()+1;
    var yyyy = today.getFullYear()+'';
    yyyy = yyyy.substring(2, 4);
    if(dd<10) {
        dd='0'+dd
    }
    if(mm<10) {
        mm='0'+mm
    }
    today = mm+'/'+dd+'/'+yyyy;
    return today;
}

function time_filter(time_back) {
    var today = get_today_date(time_back);
    today = format_date(today);
    var single_obsJSON=jsonAfterParse.obs_singles;
    var json_counter = 0;
    var newJSON = {
        'Obsgroubs':new Array(),
        'obs_singles': new Array()
    };
    if (typeof single_obsJSON !== 'undefined')
    {
        for(var i=0;i<single_obsJSON.length;i++){
            console.log('try to compare today: '+today+' with: '+single_obsJSON[i].date);
            if(dates.compare(today, single_obsJSON[i].date) <= 0) {
                console.log('pass!!');
                newJSON.obs_singles[json_counter]=single_obsJSON[i];
                json_counter++;
            }
        }
        document.getElementById('obsgroups_results').innerHTML='';
        addAllSingleObs(newJSON);
    }
}

function refresh_data() {
	var searchText = document.getElementById('searchText');
	searchText.value = jsonAfterParse.search_phrase;

    addAllObsGroups(jsonAfterParse);
    addAllSingleObs(jsonAfterParse);
}


