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
    var resultText = '';
    resultText+='<div class="obsgroup_wrap" onclick="load_single_detailed_obs('+obsJSON.observation_id+');">';
    resultText+='<div class="obsgroup_first_row">';
    resultText+='<h3 class="obsgroup_title">';
    resultText+=obsJSON.concept_name;
    resultText+='</h3>';
    resultText+='<span class="obsgroup_value">';
    resultText+=obsJSON.value;
    resultText+='</span>'
    resultText+='<span class="obsgroup_date">';
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

function get_obs_graph_points(obs_id) {
    var res = new Array();
    var cur;
    var obs_obj = get_single_obs_by_id(obs_id);
    var obs_name = obs_obj.concept_name;
    var history_json = get_obs_history_json_by_name(obs_name);
    for(var i=0;i<history_json.length;i++){
        console.log(format_date_2(history_json[i].date));
        cur = [(new Date(format_date_2(history_json[i].date))).getTime(), history_json[i].value];
        console.log(cur);
        res.push(cur);
    }
    return res;
}

function enable_graph(obs_id) {


        var d3 = get_obs_graph_points(obs_id);
        var options = {
            series: {
                lines: {
                    show: true
                },
                points: {
                    show: true
                }
            },
            grid: {
                hoverable: true,
                clickable: true
            },
            xaxis: {
                mode: "time",
                tickLength: 5
            }
        };
        $.plot("#placeholder", [ d3 ], options);
    $("<div id='tooltip'></div>").css({
        position: "absolute",
        display: "none",
        border: "1px solid #fdd",
        padding: "2px",
        "background-color": "#fee",
        opacity: 0.80
    }).appendTo("body");




}


function load_single_detailed_obs(obs_id){
    var obsJSON = get_single_obs_by_id(obs_id);
    var resultText='';
    resultText+='<div class="obsgroup_view">';
    resultText+='<h3 class="chartserach_center">';
    resultText+=obsJSON.concept_name;
    resultText+='</h3>';
    resultText+='<div class="obsgroup_all_wrapper">';

    resultText+='<label class="cs_label">';
    resultText+='Date: ';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=obsJSON.date;
    resultText+='</span>';
    resultText+='<br />';
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
    resultText+='<br />';
    resultText+='<label class="cs_label">';
    resultText+='Value:';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=obsJSON.value+' '+obsJSON.units_of_measurement;
    resultText+='</span>';
    resultText+='<br />';
    resultText+='<label class="cs_label">';
    resultText+='Absolute High:';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=obsJSON.value+' '+obsJSON.absolute_high;
    resultText+='</span>';
    resultText+='<br />';
    resultText+='<label class="cs_label">';
    resultText+='Absolute Low:';
    resultText+='</label>';
    resultText+='<span class="cs_span">';
    resultText+=obsJSON.value+' '+obsJSON.absolute_low;
    resultText+='</span>';
    resultText+='</div>';
    /*HISTORY*/
    resultText+='<div class="demo-container"><h1 class="graph_title">Graph</h1> <div id="placeholder" class="demo-placeholder" style="width:400px;height:140px"></div></div>';
    resultText+='<div class="obsgroup_all_wrapper">';
    resultText+=load_single_obs_history(obs_id);
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
        resultText+='<tr><td>'+history_json[i].date+'</td><td>'+history_json[i].value+'</td></tr>';
    }
    resultText+='</table>';
    return resultText;
}

function format_date(obs_date) {
    return obs_date.substring(3, 5)+'/'+obs_date.substring(0, 2)+'/'+obs_date.substring(6, 8);
}

function format_date_2(obs_date) {
    return '20'+obs_date.substring(6, 8)+'/'+obs_date.substring(3, 5)+'/'+obs_date.substring(0, 2);
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
    return dates.compare(b.date, a.date);
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

function addObsGroupToResults(obsJSON)
{
    var resultText = '';
    resultText+='<div class="obsgroup_wrap" onclick="load_detailed_obs('+obsJSON.group_Id+');">';
    resultText+='<div class="obsgroup_first_row">';
    if (typeof obsJSON.group_name !== 'undefined')
    {
        resultText+='<h3 class="obsgroup_title">';
        resultText+=obsJSON.group_name;
        resultText+='</h3>';
    }
    if (typeof obsJSON.last_taken_date !== 'undefined')
    {
        resultText+='<span class="obsgroup_date">';
        resultText+=obsJSON.last_taken_date;
        resultText+='</span>'
    }
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
    resultText+='<div class="obsgroup_row">';
    if (typeof obsJSON.concept_name !== 'undefined')
    {
        resultText+='<div class="obsgroup_row_first_section inline">';
        resultText+=obsJSON.concept_name;
        resultText+='</div>';
    }
    if (typeof obsJSON.value !== 'undefined')
    {
        resultText+='<div class="obsgroup_row_sec_section inline">';
        resultText+=obsJSON.value + ' ' + displayOnlyIfDef(obsJSON.units_of_measurement);
        resultText+='</div>';
    }
    if (typeof obsJSON.absolute_low !== 'undefined')
    {
        resultText+='<div class="obsgroup_row_trd_section inline">';
        resultText+='(' + obsJSON.absolute_low + ' - ' + obsJSON.absolute_high + ')';
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

function load_detailed_obs(obs_id)
{
    var obsJSON = get_obs_by_id(obs_id);
    console.log(obs_id);
    console.log(obsJSON);
    var resultText='';
    resultText+='<div class="obsgroup_view">';
    resultText+='<h3 class="chartserach_center">';
    resultText+=obsJSON.group_name;
    resultText+='</h3>';
    resultText+='<div class="obsgroup_all_wrapper">';
    var singleObs = obsJSON.observations;
    for(var i=0;i<singleObs.length;i++){
        resultText+='<div class="obsgroup_item_row" onclick="load_single_detailed_obs('+singleObs[i].observation_id+')">';
        resultText+='<div class="obsgroup_item_first inline">';
        resultText+=singleObs[i].concept_name;
        resultText+='</div>';
        resultText+='<div class="obsgroup_item_sec inline">';
        resultText+=singleObs[i].value +" "+ singleObs[i].units_of_measurement;
        resultText+='</div>';
        resultText+='<div class="obsgroup_item_frth inline">';
        resultText+=singleObs[i].date;
        resultText+='</div>';
        resultText+='</div>';
    }
    resultText+='</div>';
    resultText+='</div>';
    document.getElementById('obsgroup_view').innerHTML=resultText;
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
    addAllObsGroups(jsonAfterParse);
    addAllSingleObs(jsonAfterParse);
}

/*
var resultJSON =' {"obs_groups":[],"obs_singles":[{"observation_id":18,"concept_name":"Pulse","date":"28/04/14","value_type":"Numeric","units_of_measurement":"rate/min","absolute_high":230,"absolute_low":0,"value":"28.0","location":"Registration Desk"},{"observation_id":27,"concept_name":"SYSTOLIC BLOOD PRESSURE","date":"28/04/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":250,"absolute_low":0,"value":"80.0","location":"Registration Desk"},{"observation_id":12,"concept_name":"Blood oxygen saturation","date":"23/04/14","value_type":"Numeric","units_of_measurement":"%","absolute_high":100,"absolute_low":0,"value":"32.0","location":"Laboratory"},{"observation_id":13,"concept_name":"DIASTOLIC BLOOD PRESSURE","date":"23/04/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":150,"absolute_low":0,"value":"100.0","location":"Laboratory"},{"observation_id":14,"concept_name":"Height (cm)","date":"23/04/14","value_type":"Numeric","units_of_measurement":"cm","absolute_high":228,"absolute_low":10,"value":"170.0","location":"Laboratory"},{"observation_id":30,"concept_name":"Respiratory rate","date":"28/04/14","value_type":"Numeric","units_of_measurement":"","value":"23.0","location":"Registration Desk"},{"observation_id":8,"concept_name":"DIASTOLIC BLOOD PRESSURE","date":"20/03/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":150,"absolute_low":0,"value":"120.0","location":"Laboratory"},{"observation_id":9,"concept_name":"Pulse","date":"23/04/14","value_type":"Numeric","units_of_measurement":"rate/min","absolute_high":230,"absolute_low":0,"value":"80.0","location":"Laboratory"},{"observation_id":25,"concept_name":"Blood oxygen saturation","date":"28/04/14","value_type":"Numeric","units_of_measurement":"%","absolute_high":100,"absolute_low":0,"value":"20.0","location":"Registration Desk"},{"observation_id":11,"concept_name":"Weight (kg)","date":"23/04/14","value_type":"Numeric","units_of_measurement":"kg","absolute_high":250,"absolute_low":0,"value":"80.0","location":"Laboratory"},{"observation_id":6,"concept_name":"Pulse","date":"20/03/14","value_type":"Numeric","units_of_measurement":"rate/min","absolute_high":230,"absolute_low":0,"value":"80.0","location":"Laboratory"},{"observation_id":7,"concept_name":"Height (cm)","date":"20/03/14","value_type":"Numeric","units_of_measurement":"cm","absolute_high":228,"absolute_low":10,"value":"170.0","location":"Laboratory"},{"observation_id":24,"concept_name":"Temperature (C)","date":"28/04/14","value_type":"Numeric","units_of_measurement":"DEG C","absolute_high":43,"absolute_low":25,"value":"27.0","location":"Registration Desk"},{"observation_id":17,"concept_name":"SYSTOLIC BLOOD PRESSURE","date":"28/04/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":250,"absolute_low":0,"value":"89.0","location":"Registration Desk"},{"observation_id":26,"concept_name":"Pulse","date":"28/04/14","value_type":"Numeric","units_of_measurement":"rate/min","absolute_high":230,"absolute_low":0,"value":"23.0","location":"Registration Desk"},{"observation_id":2,"concept_name":"Temperature (C)","date":"20/03/14","value_type":"Numeric","units_of_measurement":"DEG C","absolute_high":43,"absolute_low":25,"value":"36.0","location":"Laboratory"},{"observation_id":1,"concept_name":"Blood oxygen saturation","date":"20/03/14","value_type":"Numeric","units_of_measurement":"%","absolute_high":100,"absolute_low":0,"value":"10.0","location":"Laboratory"},{"observation_id":28,"concept_name":"DIASTOLIC BLOOD PRESSURE","date":"28/04/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":150,"absolute_low":0,"value":"120.0","location":"Registration Desk"},{"observation_id":20,"concept_name":"Weight (kg)","date":"28/04/14","value_type":"Numeric","units_of_measurement":"kg","absolute_high":250,"absolute_low":0,"value":"189.0","location":"Registration Desk"},{"observation_id":5,"concept_name":"Respiratory rate","date":"20/03/14","value_type":"Numeric","units_of_measurement":"","value":"80.0","location":"Laboratory"},{"observation_id":4,"concept_name":"SYSTOLIC BLOOD PRESSURE","date":"20/03/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":250,"absolute_low":0,"value":"80.0","location":"Laboratory"},{"observation_id":15,"concept_name":"Temperature (C)","date":"23/04/14","value_type":"Numeric","units_of_measurement":"DEG C","absolute_high":43,"absolute_low":25,"value":"30.0","location":"Laboratory"},{"observation_id":23,"concept_name":"Height (cm)","date":"28/04/14","value_type":"Numeric","units_of_measurement":"cm","absolute_high":228,"absolute_low":10,"value":"29.0","location":"Registration Desk"},{"observation_id":32,"concept_name":"Weight (kg)","date":"28/04/14","value_type":"Numeric","units_of_measurement":"kg","absolute_high":250,"absolute_low":0,"value":"20.0","location":"Registration Desk"},{"observation_id":10,"concept_name":"SYSTOLIC BLOOD PRESSURE","date":"23/04/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":250,"absolute_low":0,"value":"80.0","location":"Laboratory"},{"observation_id":3,"concept_name":"Weight (kg)","date":"20/03/14","value_type":"Numeric","units_of_measurement":"kg","absolute_high":250,"absolute_low":0,"value":"80.0","location":"Laboratory"},{"observation_id":19,"concept_name":"DIASTOLIC BLOOD PRESSURE","date":"28/04/14","value_type":"Numeric","units_of_measurement":"mmHg","absolute_high":150,"absolute_low":0,"value":"89.0","location":"Registration Desk"},{"observation_id":29,"concept_name":"Height (cm)","date":"28/04/14","value_type":"Numeric","units_of_measurement":"cm","absolute_high":228,"absolute_low":10,"value":"20.0","location":"Registration Desk"},{"observation_id":21,"concept_name":"Blood oxygen saturation","date":"28/04/14","value_type":"Numeric","units_of_measurement":"%","absolute_high":100,"absolute_low":0,"value":"89.0","location":"Registration Desk"},{"observation_id":22,"concept_name":"Respiratory rate","date":"28/04/14","value_type":"Numeric","units_of_measurement":"","value":"82.0","location":"Registration Desk"},{"observation_id":16,"concept_name":"Respiratory rate","date":"23/04/14","value_type":"Numeric","units_of_measurement":"","value":"80.0","location":"Laboratory"},{"observation_id":31,"concept_name":"Temperature (C)","date":"28/04/14","value_type":"Numeric","units_of_measurement":"DEG C","absolute_high":43,"absolute_low":25,"value":"35.0","location":"Registration Desk"}]} ';
*/


