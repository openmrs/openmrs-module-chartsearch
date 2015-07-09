$("#passwordPopup").hide();

var tryingToSubmit = false;
var tempJsonAfterParse;
var dates = {
	convert : function(d) {
		// Converts the date in d to a date-object. The input can be:
		// a date object: returned without modification
		// an array : Interpreted as [year,month,day]. NOTE: month is 0-11.
		// a number : Interpreted as number of milliseconds
		// since 1 Jan 1970 (a timestamp)
		// a string : Any format supported by the javascript engine, like
		// "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
		// an object : Interpreted as an object with year, month and date
		// attributes. **NOTE** month is 0-11.
		return (d.constructor === Date ? d
				: d.constructor === Array ? new Date(d[0], d[1], d[2])
						: d.constructor === Number ? new Date(d)
								: d.constructor === String ? new Date(
										format_date(d))
										: typeof d === "object" ? new Date(
												d.year, d.month, d.date) : NaN);
	},
	compare : function(a, b) {
		// Compare two dates (could be of any type supported by the convert
		// function above) and returns:
		// -1 : if a < b
		// 0 : if a = b
		// 1 : if a > b
		// NaN : if a or b is an illegal date
		// NOTE: The code inside isFinite does an assignment (=).
		return (isFinite(a = this.convert(a).valueOf())
				&& isFinite(b = this.convert(b).valueOf()) ? (a > b) - (a < b)
				: NaN);
	},
	inRange : function(d, start, end) {
		// Checks if date in d is between dates in start and end.
		// Returns a boolean or NaN:
		// true : if d is between start and end (inclusive)
		// false : if d is before start or after end
		// NaN : if one or more of the dates is illegal.
		// NOTE: The code inside isFinite does an assignment (=).
		return (isFinite(d = this.convert(d).valueOf())
				&& isFinite(start = this.convert(start).valueOf())
				&& isFinite(end = this.convert(end).valueOf()) ? start <= d
				&& d <= end : NaN);
	}
}

/*
 * Function that gets a string as a parameter The function capitalize the first
 * char in that string The function decapitalize the rest
 */

function capitalizeFirstLetter(string) {
	if (string) {
		return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
	}
}

var viewsFactory;
var doT;
var current_JSON_OBJECT;

viewsFactory = {

	result_row : doT
			.template('<div class="result_conainer"><div class="result_header">{{=it.head || \"\" }}</div><div class="result_body">{{=it.body || \"\" }}</div></div>')

}

function single_sort_func(a, b) {
	var first_date = new Date(parseInt(a.date));
	var second_date = new Date(parseInt(b.date));
	return dates.compare(first_date, second_date);
}

function addAllResultsFromJSONFromTheServer(json) {
	console.log(json);
	var resultText = '';
	var single_obsJSON = json.obs_singles;
	var allergies = jsonAfterParse.patientAllergies;
	var appointments = jsonAfterParse.patientAppointments;
	single_obsJSON.sort(single_sort_func);
	single_obsJSON.reverse();
	if (typeof single_obsJSON !== 'undefined') {
		resultText += '';
		for ( var i = 0; i < single_obsJSON.length; i++) {
			resultText += addSingleObsToResults(single_obsJSON[i], i);
		}
		for (i = 0; i < allergies.length; i++) {
			var allergen = allergies[i];

			if (!single_obsJSON.length || single_obsJSON.length === 0) {
				resultText += addAllergiesToResults(allergen, true);
			} else {
				resultText += addAllergiesToResults(allergen, false);
			}
		}
		for (i = 0; i < appointments.length; i++) {
			var app = appointments[i];
			resultText += addAppointmentsToResults(app);
		}
		document.getElementById('obsgroups_results').innerHTML += resultText;
	}
}

function addSingleObsToResults(obsJSON, i) {
	var obs_id_html = '';
	var ob_id;
	if (typeof obsJSON.observation_id !== 'undefined') {
		if (i == 0) {
			obs_id_html = 'id="first_obs_single"';
			ob_id = 'first_obs_single';
		} else {
			obs_id_html = 'id="obs_single_' + obsJSON.observation_id + '"';
			ob_id = 'obs_single_' + obsJSON.observation_id;
		}
	}
	var resultText = '';
	resultText += '<div class="obsgroup_wrap"' + obs_id_html
			+ ' onclick="updateNavigationIndicesToClicked('
			+ obsJSON.observation_id + ', ' + ob_id
			+ ');load_single_detailed_obs(' + obsJSON.observation_id + ');">';
	resultText += '<div class="obsgroup_first_row">';
	resultText += '<div class="obsgroup_titleBox">';
	resultText += '<h3 class="obsgroup_title">';
	resultText += capitalizeFirstLetter(obsJSON.concept_name);
	resultText += '</h3>';
	resultText += '<br><span class="obsgroup_date">';
	resultText += getDateStr(obsJSON.date, true);
	resultText += '</span></div>';
	if (obsJSON.value_type && obsJSON.value_type === "Text") {
		resultText += '<span class="obsgroup_valueText">';
		resultText += obsJSON.value.substring(0, 70) + "...";
		resultText += '</span>'
	} else {
		resultText += '<span class="obsgroup_value">';
		resultText += obsJSON.value;
		if (obsJSON.units_of_measurement) {
			resultText += '<span class="cs_span_measure">';
			resultText += ' ' + obsJSON.units_of_measurement;
			resultText += '</span>';
		}
		resultText += '</span>'
	}

	resultText += '<span class="obsgroup_range">';
	if (typeof obsJSON.normal_low !== 'undefined'
			&& typeof obsJSON.normal_high !== 'undefined') {
		resultText += '(' + obsJSON.normal_low + '-' + obsJSON.normal_high
				+ ')';
	}
	resultText += '</span>'
	resultText += '<div class="chart_serach_clear"></div>';
	resultText += '</div>';
	resultText += '</div>';
	return resultText;
}

function addAllergiesToResults(allergy, noObsSingle) {
	var allergyId = allergy.allergenId;
	var allergyUuid = allergy.allergenUuid;
	var allergyCodedName = allergy.allergenCodedName;
	var allergyNonCodedName = allergy.allergenNonCodedName;
	var allergySeverity = allergy.allergenSeverity;
	var allergyType = allergy.allergenType;
	var allergyCodedReaction = allergy.allergenCodedReaction;
	var allergyNonCodedReaction = allergy.allergenNonCodedReaction;
	var allergyComment = allergy.allergenComment;
	var allergyDate = allergy.allergenDate;
	var title = !allergyNonCodedName || allergyNonCodedName === "" ? allergyCodedName
			: allergyNonCodedName;
	var reaction = !allergyNonCodedReaction || allergyNonCodedReaction === "" ? allergyCodedReaction
			: allergyNonCodedReaction;
	var resultText = '';
	var allergyIdHtml;
	var allId;

	if (noObsSingle
			&& jsonAfterParse.patientAllergies[0].allergenId === allergy.allergenId) {
		allergyIdHtml = 'id="first_alergen"';
		allId = "first_alergen";
	} else {
		allergyIdHtml = 'id="allergen_' + allergyId + '"';
		allId = "allergen_" + allergyId;
	}

	resultText += '<div class="obsgroup_wrap" ' + allergyIdHtml
			+ ' onclick="updateNavigationIndicesToClicked(' + allergyId + ', '
			+ allId + '); load_allergen(' + allergyId + ');">';
	resultText += '<div class="obsgroup_first_row">';
	resultText += '<div class="obsgroup_titleBox">';
	resultText += '<h3 class="obsgroup_title">';
	resultText += title;
	resultText += '</h3>';
	resultText += '<br><span class="obsgroup_date">';
	resultText += getDateStr(allergyDate, true);
	resultText += '</span></div>';
	if (reaction) {
		resultText += '<span class="obsgroup_value">';
		resultText += reaction;
		resultText += '</span>'
	}

	if (allergySeverity) {
		resultText += '<span class="obsgroup_range">';
		resultText += allergySeverity;
		resultText += '</span>'
	}
	resultText += '<div class="chart_serach_clear"></div>';
	resultText += '</div>';
	resultText += '</div>';
	return resultText;
}

function addAppointmentsToResults(app) {
	var status = app.status;
	var id = app.id;
	var reason = app.reason;
	var type = app.type;
	var start = new Date(app.start);
	var end = new Date(app.end);
	var typeDesc = app.typeDesc;
	var cancelReason = app.cancelReason;
	var provider = app.provider;

	var resultText = '';
	var appointmentIdHtml;
	var appId;

	if (jsonAfterParse.obs_singles.length === 0
			&& jsonAfterParse.patientAllergies.length === 0
			&& jsonAfterParse.patientAppointments[0].id === app.id) {
		appointmentIdHtml = 'id="first_appointment"';
		appId = "first_appointment";
	} else {
		appointmentIdHtml = 'id="appointment_' + id + '"';
		appId = "appointment_" + id;
	}

	resultText += '<div class="obsgroup_wrap" ' + appointmentIdHtml
			+ ' onclick="updateNavigationIndicesToClicked(' + id + ', ' + appId
			+ '); load_appointment(' + id + ');">';
	resultText += '<div class="obsgroup_first_row">';
	resultText += '<div class="obsgroup_titleBox">';
	resultText += '<h3 class="obsgroup_title">';
	resultText += type;
	resultText += '</h3>';
	resultText += '<br><span class="obsgroup_date">';
	resultText += start.toTimeString() + ' - ' + end.toTimeString();
	resultText += '</span></div>';
	if (typeDesc) {
		resultText += '<span class="obsgroup_value">';
		resultText += status;
		resultText += '</span>'
	}

	if (provider) {
		resultText += '<span class="obsgroup_range">';
		resultText += provider;
		resultText += '</span>'
	}
	resultText += '<div class="chart_serach_clear"></div>';
	resultText += '</div>';
	resultText += '</div>';

	return resultText;
}

function get_single_obs_by_id(obs_id) {
	var obs_single_JSON = jsonAfterParse.obs_singles;
	for ( var i = 0; i < obs_single_JSON.length; i++) {
		if (obs_single_JSON[i].observation_id == obs_id) {
			return obs_single_JSON[i];
		}
	}
	var obsgroupJSON = jsonAfterParse.obs_groups;
	for ( var i = 0; i < obsgroupJSON.length; i++) {
		var singleGroup = obsgroupJSON[i].observations;
		for ( var j = 0; j < singleGroup.length; j++) {
			if (singleGroup[j].observation_id == obs_id) {
				return singleGroup[j];
			}
		}
	}
	return -1;
}

function array_sort(a, b) {
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
	for ( var i = 0; i < history_json.length; i++) {
		int_date = parseInt(history_json[i].date);
		date_formmated = new Date(int_date);
		cur = [ date_formmated.getTime(), history_json[i].value ];
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
	for ( var i = 0; i < history_json.length; i++) {
		res.push(history_json[i].value);
	}
	return res;
}

function get_obs_ticks(obs_id) {
	var res = new Array();
	var obs_obj = get_single_obs_by_id(obs_id);
	var obs_name = obs_obj.concept_name;
	var history_json = get_obs_history_json_by_name(obs_name);
	for ( var i = 0; i < history_json.length; i++) {
		res.push(history_json[i].date);
	}
	return res;
}

function enable_graph(obs_id) {

	var observation_obj = get_single_obs_by_id(obs_id);
	var data2 = get_obs_graph_points(obs_id);
	var mark = {
		enabled : true,
		showMinMax : false,
		color : "rgb(6,191,2)",
		avg : 0
	};
	if (typeof observation_obj.normal_high !== 'undefined') {
		mark.max = parseInt(observation_obj.normal_high);
		mark.showMinMax = true;
	}
	if (typeof observation_obj.normal_low !== 'undefined') {
		mark.min = parseInt(observation_obj.normal_low);
		mark.showMinMax = true;
	}

	var plot = $.plot("#placeholder", [ {
		data : data2,
		label : capitalizeFirstLetter(observation_obj.concept_name),
		color : '#0D4F8B'
	} ], {
		series : {
			lines : {
				show : true
			},
			autoMarkings : mark,
			points : {
				show : true
			}
		},
		grid : {
			hoverable : true,
			clickable : true
		},
		yaxis : {},
		xaxis : {
			mode : "time",
			minTickSize : [ 1, "month" ],
			timeformat : "%b <br/> %y"
		}
	});

	$("<div id='tooltip'></div>").css({
		position : "absolute",
		display : "none",
		border : "1px solid #fdd",
		padding : "2px",
		"background-color" : "#fee",
		opacity : 0.80
	}).appendTo("body");

	$("#placeholder").bind(
			"plothover",
			function(event, pos, item) {

				if (item) {
					var x = item.datapoint[0], y = item.datapoint[1];

					$("#tooltip").html(
							item.series.label + ": " + y + "<br /> Taken date:"
									+ getDateStr(x)).css({
						top : item.pageY + 5,
						left : item.pageX + 5
					}).fadeIn(200);
				} else {
					$("#tooltip").hide();
				}
			});
}

function load_single_detailed_obs(obs_id) {
	removeAllHovering();
	if (jsonAfterParse.obs_singles[0].observation_id == obs_id) {
		$("#first_obs_single").addClass("obsgroup_current");
	} else {
		$("#obs_single_" + obs_id).addClass("obsgroup_current");
	}
	var obsJSON = get_single_obs_by_id(obs_id);
	var resultText = '';
	resultText += '<div class="obsgroup_view">';
	resultText += '<h3 class="chartserach_center">';
	resultText += capitalizeFirstLetter(obsJSON.concept_name);
	if (obsJSON.units_of_measurement != undefined) {
		resultText += '<span style="font-weight: normal; display: inline;"> ('
				+ obsJSON.units_of_measurement + ') </span>';
	}
	resultText += '</h3>';
	resultText += '<div class="demo-container">'
			+ '<div id="placeholder" class="demo-placeholder"></div>'
			+ '</div>';
	// resultText+='<div class="demo-container"><h1
	// class="graph_title">Graph</h1> <div id="placeholder"
	// class="demo-placeholder" style="width:550px;height:300px;margin:0
	// auto;"></div></div>';
	/* resultText+='<div class="obsgroup_all_wrapper">'; */
	resultText += load_single_obs_history(obs_id);
	/* resultText+='</div>'; */

	resultText += '<div class="obsgroup_all_wrapper">';

	/*
	 * resultText+='<label class="cs_label">'; resultText+='Date: ';
	 * resultText+='</label>'; resultText+='<span class="cs_span">';
	 * resultText+=getDateStr(obsJSON.date); resultText+='</span>';
	 * resultText+='<br />';
	 */
	resultText += '<label class="cs_label">';
	resultText += 'Value Type:';
	resultText += '</label>';
	resultText += '<span class="cs_span">';
	resultText += obsJSON.value_type;
	resultText += '</span>';
	resultText += '<br />';
	resultText += '<label class="cs_label">';
	resultText += 'Location: ';
	resultText += '</label>';
	resultText += '<span class="cs_span">';
	resultText += obsJSON.location;
	resultText += '</span>';
	/* resultText+='<br />'; */
	/*
	 * resultText+='<label class="cs_label">'; resultText+='Value:';
	 * resultText+='</label>';
	 * 
	 * resultText+='<span class="cs_span">'; resultText+=obsJSON.value; if
	 * (obsJSON.units_of_measurement) { resultText+='<span
	 * class="cs_span_measure">'; resultText+=' '+obsJSON.units_of_measurement;
	 * resultText+='</span>'; } resultText+='</span>';
	 */

	resultText += '<br />';
	if (obsJSON.absolute_high) {
		resultText += '<label class="cs_label">';
		resultText += 'Absolute High:';
		resultText += '</label>';
		resultText += '<span class="cs_span">';
		resultText += obsJSON.absolute_high;
		resultText += '</span>';
		resultText += '<br />';
	}

	if (obsJSON.absolute_low) {
		resultText += '<label class="cs_label">';
		resultText += 'Absolute Low:';
		resultText += '</label>';
		resultText += '<span class="cs_span">';
		resultText += obsJSON.absolute_low;
		resultText += '</span>';
	}
	if (obsJSON.normal_high) {
		resultText += '<label class="cs_label">';
		resultText += 'Normal High:';
		resultText += '</label>';
		resultText += '<span class="cs_span">';
		resultText += obsJSON.normal_high;
		resultText += '</span>';
		resultText += '<br />';
	}

	if (obsJSON.normal_low) {
		resultText += '<label class="cs_label">';
		resultText += 'Normal Low:';
		resultText += '</label>';
		resultText += '<span class="cs_span">';
		resultText += obsJSON.normal_low;
		resultText += '</span>';
	}
	resultText += '</div>';
	resultText += '</div>';

	document.getElementById('obsgroup_view').innerHTML = resultText;
	if (obsJSON.value_type == 'Numeric') {
		enable_graph(obs_id);
	}
}

function getAllergy(allergyId) {
	for (i = 0; i < jsonAfterParse.patientAllergies.length; i++) {
		var allergy = jsonAfterParse.patientAllergies[i];
		if (allergy.allergenId === allergyId) {
			return allergy;
		}
	}
}

function getAppointment(appId) {
	for (i = 0; i < jsonAfterParse.patientAppointments.length; i++) {
		var appointment = jsonAfterParse.patientAppointments[i];
		if (appointment.id === appId) {
			return appointment;
		}
	}
}

function load_allergen(allergeId) {
	removeAllHovering();
	if (jsonAfterParse.obs_singles.length === 0
			&& jsonAfterParse.patientAllergies[0].allergenId === allergeId) {
		$("#first_alergen").addClass("obsgroup_current");
	} else {
		$("#allergen_" + allergeId).addClass("obsgroup_current");
	}
	var resultText = '';
	var allergy = getAllergy(allergeId);
	var allergen = (!allergy.allergenNonCodedName || allergy.allergenNonCodedName === "") ? allergy.allergenCodedName
			: allergy.allergenNonCodedName;
	var reaction = (!allergy.allergenNonCodedReaction || allergy.allergenNonCodedReaction === "") ? allergy.allergenCodedReaction
			: allergy.allergenNonCodedReaction;
	var severity = allergy.allergenSeverity;
	var type = allergy.allergenType;
	var comment = allergy.allergenComment;
	var dt = new Date(allergy.allergenDate);
	var date = getDateStr(allergy.allergenDate, true) + " " + dt.toTimeString();

	if (!severity) {
		severity = "";
	} else {
		severity = capitalizeFirstLetter(severity);
	}

	resultText += '<div class="obsgroup_view">';
	resultText += '<h3 class="chartserach_center">';
	resultText += allergen;
	resultText += '</h3>';
	resultText += '<table>';
	resultText += '<tr><th>Allergen:</th><td>' + allergen + '</td></tr>';
	resultText += '<tr><th>Type:</th><td>' + type + '</td></tr>';
	resultText += '<tr><th>Severity:</th><td>' + severity + '</td></tr>';
	resultText += '<tr><th>Reaction:</th><td>' + reaction + '</td></tr>';
	resultText += '<tr><th>Comment:</th><td>' + comment + '</td></tr>';
	resultText += '<tr><th>Last Updated:</th><td>' + date + '</td></tr>';
	resultText += '</table>';
	resultText += '</div>';

	$("#obsgroup_view").html(resultText);
}

function load_appointment(appId) {
	removeAllHovering();
	if (jsonAfterParse.obs_singles.length === 0
			&& jsonAfterParse.patientAllergies.length === 0
			&& jsonAfterParse.patientAppointments[0].id === appId) {
		$("#first_appointment").addClass("obsgroup_current");
	} else {
		$("#appointment_" + appId).addClass("obsgroup_current");
	}
	var resultText = '';
	var app = getAppointment(appId);
	var status = app.status;
	var reason = app.reason;
	var type = app.type;
	var start = new Date(app.start);
	var end = new Date(app.end);
	var typeDesc = app.typeDesc;
	var cancelReason = app.cancelReason;
	var provider = app.provider;
	var date;

	typeDesc = !typeDesc ? "" : typeDesc;
	provider = !provider ? "" : provider;
	reason = !reason ? "" : reason;
	status = !status ? "" : status;
	if (getDateStr(app.start, true) === getDateStr(app.end, true)) {
		date = getDateStr(app.start, true) + " " + start.toTimeString() + " - "
				+ end.toTimeString();
	} else {
		date = getDateStr(app.start, true) + " " + start.toTimeString() + " - "
		getDateStr(app.end, true) + " " + end.toTimeString();
	}

	resultText += '<div class="obsgroup_view">';
	resultText += '<h3 class="chartserach_center">';
	resultText += type;
	resultText += '</h3>';
	resultText += '<table>';
	resultText += '<tr><th>Appointment Service Type:</th><td>' + type
			+ '</td></tr>';
	resultText += '<tr><th>Description:</th><td>' + typeDesc + '</td></tr>';
	resultText += '<tr><th>Provider:</th><td>' + provider + '</td></tr>';
	resultText += '<tr><th>Reason:</th><td>' + reason + '</td></tr>';
	resultText += '<tr><th>Status:</th><td>' + status + '</td></tr>';
	if (cancelReason && cancelReason !== "") {
		resultText += '<tr><th>Cancel Reason:</th><td>' + cancelReason
				+ '</td></tr>'
	}
	resultText += '<tr><th>Period:</th><td>' + date + '</td></tr>';

	resultText += '</table>'
	resultText += '</div>';

	$("#obsgroup_view").html(resultText);
}

function updateNavigationIndicesToClicked(id, clickedId) {
	clickedId = clickedId.id;
	var numberOfResults = jsonAfterParse.obs_groups.length
			+ jsonAfterParse.obs_singles.length
			+ jsonAfterParse.patientAllergies.length
			+ jsonAfterParse.patientAppointments.length;
	var allPossibleIndices = new Array(numberOfResults);

	if (clickedId && clickedId !== '') {
		for (i = 0; i < allPossibleIndices.length; i++) {
			if (i < jsonAfterParse.obs_singles.length
					&& jsonAfterParse.obs_singles[i].observation_id === id
					&& (clickedId === "first_obs_single" || clickedId
							.indexOf("obs_single_") === 0)) {
				navigationIndicesUpdateLogic(i, numberOfResults);
			} else if (i < jsonAfterParse.patientAllergies.length
					&& jsonAfterParse.patientAllergies[i].allergenId === id
					&& (clickedId === "first_alergen" || clickedId
							.indexOf("allergen_") === 0)) {
				if (jsonAfterParse.obs_singles.length
						&& jsonAfterParse.obs_singles.length > 0) {
					navigationIndicesUpdateLogic(i
							+ jsonAfterParse.obs_singles.length,
							numberOfResults);
				} else {
					navigationIndicesUpdateLogic(i, numberOfResults);
				}
			} else if (i < jsonAfterParse.patientAppointments.length
					&& jsonAfterParse.patientAppointments.length[i] === id
					&& (clickedId === "first_appointment" || clickedId
							.indexOf("appointment_") === 0)) {

			}
		}
	}

}

function navigationIndicesUpdateLogic(i, numberOfResults) {
	peviousIndex = i;
	if (wasGoingNext) {
		if (peviousIndex === numberOfResults) {
			peviousIndex = -1;
			navigationIndex = 0;
		} else {
			navigationIndex = i + 1;
		}
	} else {
		if (peviousIndex !== 0) {
			navigationIndex = i - 1;
			wasGoingNext = false;
		}
	}
}

function load_single_obs_history(obs_id) {
	resultText = '<h3>History</h3>';
	var obs_obj = get_single_obs_by_id(obs_id);
	var obs_name = obs_obj.concept_name;
	var history_json = get_obs_history_json_by_name(obs_name);
	resultText += '<table><tr><th>Date</th><th>Value</th></tr>';
	for ( var i = 0; i < history_json.length; i++) {
		var red = '';
		var addition = '';
		if (typeof history_json[i].normal_high !== 'undefined') {
			if (history_json[i].value > history_json[i].normal_high) {
				red = ' red ';
				addition = ' more_then_normal ';
			}
		}
		if (typeof history_json[i].normal_low !== 'undefined') {
			if (history_json[i].value < history_json[i].normal_low) {
				red = ' red ';
				addition = ' less_then_normal ';
			}
		}
		resultText += '<tr class="' + red + '"><td>'
				+ getDateStr(history_json[i].date, true)
				+ '</td><td><div class="' + addition + '">'
				+ history_json[i].value + '</div></td></tr>';
	}
	resultText += '</table>';
	return resultText;
}

function format_date(obs_date) {
	return obs_date.substring(3, 5) + '/' + obs_date.substring(0, 2) + '/'
			+ obs_date.substring(6, 8);
}

function get_obs_history_json_by_name(obs_name) {
	var result = new Array();
	var single_obsJSON = jsonAfterParse.obs_singles;
	if (typeof single_obsJSON !== 'undefined') {
		for ( var i = 0; i < single_obsJSON.length; i++) {
			if (single_obsJSON[i].concept_name === obs_name) {
				result.push(single_obsJSON[i]);
			}
		}
	}

	var obsgroupJSON = jsonAfterParse.obs_groups;
	for ( var i = 0; i < obsgroupJSON.length; i++) {
		var singleGroup = obsgroupJSON[i].observations;
		for ( var j = 0; j < singleGroup.length; j++) {
			if (singleGroup[j].concept_name == obs_name) {
				result.push(singleGroup[j]);
			}
		}
	}

	result.sort(compare);
	return result;
}

function compare(a, b) {
	var first_date = new Date(parseInt(a.date));
	var second_date = new Date(parseInt(b.date));
	/*
	 * console.log(first_date.toLocaleString() + ' against: ' +
	 * second_date.toLocaleString()); console.log(dates.compare(first_date,
	 * second_date));
	 */
	return dates.compare(second_date, first_date);
}

function groupSortFunc(a, b) {
	var first_date = new Date(parseInt(a.last_taken_date));
	var second_date = new Date(parseInt(b.last_taken_date));
	return dates.compare(first_date, second_date);
}

function addAllObsGroups(obsJSON) {
	var resultText = '';
	var obsgroupJSON = obsJSON.obs_groups;
	obsgroupJSON.sort(groupSortFunc);
	obsgroupJSON.reverse();
	if (typeof obsgroupJSON !== 'undefined') {
		resultText += '';
		for ( var i = 0; i < obsgroupJSON.length; i++) {
			resultText += addObsGroupToResults(obsgroupJSON[i]);
		}
		document.getElementById('obsgroups_results').innerHTML += resultText;
	}
}

function getDateStr(date_str, onlyDate) {
	date_str = parseInt(date_str);
	var date_obj = new Date(date_str);
	var ans = date_obj.toLocaleString('he-IL');
	if (onlyDate) {
		ans = date_obj.toLocaleDateString('he-IL');
	}
	return ans;
}

function addObsGroupToResults(obsJSON) {
	var resultText = '';
	var obs_id_html = '';
	var obs_id = 'obs_group_' + obsJSON.group_Id;
	if (typeof obsJSON.group_Id !== 'undefined') {
		obs_id_html = 'id="obs_group_' + obsJSON.group_Id + '"';
	}
	resultText += '<div class="obsgroup_wrap" ' + obs_id_html
			+ ' onclick="load_detailed_obs(' + obsJSON.group_Id + ');">';
	resultText += '<div class="obsgroup_first_row">';
	resultText += '<div class="obsgroup_titleBox">';
	if (typeof obsJSON.group_name !== 'undefined') {
		resultText += '<h3 class="obsgroup_title">';
		resultText += obsJSON.group_name;
		resultText += '</h3>';
	}
	if (typeof obsJSON.last_taken_date !== 'undefined') {
		resultText += '<br><span class="obsgroup_date">';
		resultText += getDateStr(obsJSON.last_taken_date, true);
		resultText += '</span>'
	}
	resultText += '</div>'
	resultText += '<div class="chart_serach_clear"></div>';
	resultText += '</div>';
	resultText += innerObservationsHTML(obsJSON.observations);
	resultText += '</div>';
	return resultText;
}

function innerObservationsHTML(obsJSON) {
	var resultText = '';
	resultText += '<div class="obsgroup_rows">';
	for ( var i = 0; i < obsJSON.length; i++) {
		resultText += single_obs_html(obsJSON[i]);
	}
	resultText += '</div>';
	return resultText;
}

function displayOnlyIfDef(display_opt) {
	if (typeof display_opt !== 'undefined') {
		return display_opt;
	} else {
		return '';
	}
}

function single_obs_html(obsJSON) {
	console.log('====single obs html function=========');
	var resultText = '';
	var red = '';
	if (typeof obsJSON.normal_high !== 'undefined') {
		if (obsJSON.value > obsJSON.normal_high) {
			red = ' red ';
		}
	}
	if (typeof obsJSON.normal_low !== 'undefined') {
		if (obsJSON.value < obsJSON.normal_low) {
			red = ' red ';
		}
	}
	if (typeof obsJSON.chosen !== 'undefined') {
		red += ' bold ';
	}
	resultText += '<div class="obsgroup_row ' + red + '">';
	if (typeof obsJSON.concept_name !== 'undefined') {
		resultText += '<div class="obsgroup_row_first_section inline">';
		resultText += capitalizeFirstLetter(obsJSON.concept_name);
		resultText += '</div>';
	}
	if (typeof obsJSON.value !== 'undefined') {
		var change = '';
		if (typeof obsJSON.normal_high !== 'undefined') {
			if (obsJSON.value > obsJSON.normal_high) {
				change += ' more_then_normal ';
			}
		}
		if (typeof obsJSON.normal_low !== 'undefined') {
			if (obsJSON.value < obsJSON.normal_low) {
				change += ' less_then_normal ';
			}
		}
		resultText += '<div class="obsgroup_row_sec_section inline ' + change
				+ '">';
		resultText += obsJSON.value + ' '
				+ displayOnlyIfDef(obsJSON.units_of_measurement);
		resultText += '</div>';
	}
	if (typeof obsJSON.normal_low !== 'undefined'
			&& typeof obsJSON.normal_high !== 'undefined') {
		resultText += '<div class="obsgroup_row_trd_section inline ">';
		resultText += '(' + obsJSON.normal_low + ' - ' + obsJSON.normal_high
				+ ')';
		resultText += '</div>';
	}
	resultText += '</div>';
	return resultText;
}

function get_obs_by_id(id) {
	var obsgroupJSON = jsonAfterParse.obs_groups;
	for ( var i = 0; i < obsgroupJSON.length; i++) {
		if (obsgroupJSON[i].group_Id == id) {
			return obsgroupJSON[i];
		}
	}
	return -1;
}

function removeAllHovering() {
	$(".obsgroup_wrap").removeClass("obsgroup_current");
}

function showOnlyIfDef(str) {
	if (typeof str !== 'undefined') {
		return str;
	}
	return '';
}

function load_detailed_obs(obs_id) {
	removeAllHovering();
	$("#obs_group_" + obs_id).addClass("obsgroup_current");
	var obsJSON = get_obs_by_id(obs_id);
	var resultText = '';
	resultText += '<div class="obsgroup_view">';
	resultText += '<h3 class="chartserach_center">';
	resultText += obsJSON.group_name;
	resultText += '</h3>';
	resultText += '<div class="obsgroup_all_wrapper">';
	var singleObs = obsJSON.observations;
	for ( var i = 0; i < singleObs.length; i++) {
		var isBold = '';
		if (typeof singleObs[i].chosen !== 'undefined') {
			isBold = ' bold ';
		}
		resultText += '<div class="obsgroup_item_row' + isBold
				+ '" onclick="updateNavigationIndicesToClicked('
				+ singleObs[i].observation_id
				+ ',"");load_single_detailed_obs('
				+ singleObs[i].observation_id + ');">';
		resultText += '<div class="obsgroup_item_first inline">';
		resultText += capitalizeFirstLetter(singleObs[i].concept_name);
		resultText += '</div>';
		resultText += '<div class="obsgroup_item_sec inline">';
		resultText += '<span style="display: inline; font-weight: bold;">'
				+ singleObs[i].value + "</span> "
				+ showOnlyIfDef(singleObs[i].units_of_measurement);
		resultText += '</div>';
		resultText += '<div class="obsgroup_item_frth inline">';
		resultText += getDateStr(singleObs[i].date, true);
		resultText += '</div>';
		resultText += '<span id="single_spark_' + singleObs[i].observation_id
				+ '">Load</span>';
		resultText += '</div>';
	}
	resultText += '</div>';
	resultText += '</div>';
	$("#obsgroup_view").html(resultText);
	var singleObs = obsJSON.observations;
	for ( var i = 0; i < singleObs.length; i++) {
		var spark = get_obs_spark_points(singleObs[i].observation_id);
		$("#single_spark_" + singleObs[i].observation_id).sparkline(spark, {
			type : 'line',
			width : '150',
			normalRangeMin : singleObs[i].normal_low,
			normalRangeMax : singleObs[i].normal_high,
			normalRangeColor : '#d3ffa8',
			fillColor : false,
			drawNormalOnTop : true
		});
	}
}

/* ############# Filters ############### */

function get_timeback_date(time_back) {
	var today = new Date();
	today.setDate(today.getDate() - time_back);
	return today;
}

function time_filter(time_back, lbl) {
	$("#time_anchor").text(lbl);
	$("#location_anchor").text('All Locations');
	$("#provider_anchor").text('All Providers');
	$("#dataType_anchor").text('All Data Types');
	var today = get_timeback_date(time_back), myDate;
	var single_obsJSON = jsonAfterParse.obs_singles;
	var group_obsJSON = jsonAfterParse.obs_groups;
	var json_counter = 0;
	var newJSON = {
		'obs_groups' : new Array(),
		'obs_singles' : new Array()
	};
	if (typeof single_obsJSON !== 'undefined') {
		for ( var i = 0; i < single_obsJSON.length; i++) {
			myDate = new Date(parseInt(single_obsJSON[i].date));
			/* console.log('try to compare today: '+today+' with: '+ myDate); */
			if (dates.compare(today, myDate) <= 0) {
				console.log('pass!!');
				newJSON.obs_singles[json_counter] = single_obsJSON[i];
				json_counter++;
			}
		}
	}

	json_counter = 0;
	if (group_obsJSON) {
		for ( var i = 0; i < group_obsJSON.length; i++) {
			var observations = group_obsJSON[i].observations;
			for ( var j = 0; j < observations.length; j++) {
				myDate = new Date(parseInt(observations[j].date));
				if (dates.compare(today, myDate) <= 0) {
					newJSON.obs_groups[json_counter] = group_obsJSON[i];
					json_counter++;
					break;
				}
			}
		}
	}
	addAllObsGroups(newJSON);
	addAllResultsFromJSONFromTheServer(newJSON);
	currentJson = newJSON;

}

function location_filter(location, lbl) {
	$("#location_anchor").text(lbl);
	$("#time_anchor").text('Any Time');
	$("#provider_anchor").text('All Providers');
	$("#dataType_anchor").text('All Data Types');
	var single_obsJSON = jsonAfterParse.obs_singles;
	var group_obsJSON = jsonAfterParse.obs_groups;
	var json_counter = 0;
	var newJSON = {
		'obs_groups' : new Array(),
		'obs_singles' : new Array()
	};
	if (typeof single_obsJSON !== 'undefined') {
		for ( var i = 0; i < single_obsJSON.length; i++) {
			if (single_obsJSON[i].location === location) {
				newJSON.obs_singles[json_counter] = single_obsJSON[i];
				json_counter++;
			}
		}
	}

	json_counter = 0;
	if (group_obsJSON) {
		for ( var i = 0; i < group_obsJSON.length; i++) {
			var observations = group_obsJSON[i].observations;
			for ( var j = 0; j < observations.length; j++) {
				if (observations[j].location
						&& observations[j].location === location) {
					newJSON.obs_groups[json_counter] = group_obsJSON[i];
					json_counter++;
					break;
				}
			}
		}
	}
	addAllObsGroups(newJSON);
	addAllResultsFromJSONFromTheServer(newJSON);
	currentJson = newJSON;

}

function provider_filter(provider, lbl) {
	$("#provider_anchor").text(lbl);
	$("#time_anchor").text('Any Time');
	$("#location_anchor").text('All Locations');
	$("#dataType_anchor").text('All Data Types');

	var single_obsJSON = jsonAfterParse.obs_singles;
	var group_obsJSON = jsonAfterParse.obs_groups;
	var json_counter = 0;
	var newJSON = {
		'obs_groups' : new Array(),
		'obs_singles' : new Array()
	};
	if (typeof single_obsJSON !== 'undefined') {
		for ( var i = 0; i < single_obsJSON.length; i++) {
			if (single_obsJSON[i].provider
					&& single_obsJSON[i].provider === provider) {
				newJSON.obs_singles[json_counter] = single_obsJSON[i];
				json_counter++;
			}
		}
	}

	json_counter = 0;
	if (group_obsJSON) {
		for ( var i = 0; i < group_obsJSON.length; i++) {
			var observations = group_obsJSON[i].observations;
			for ( var j = 0; j < observations.length; j++) {
				if (observations[j].provider
						&& observations[j].provider === provider) {
					newJSON.obs_groups[json_counter] = group_obsJSON[i];
					json_counter++;
					break;
				}
			}
		}
	}
	addAllObsGroups(newJSON);
	addAllResultsFromJSONFromTheServer(newJSON);
	currentJson = newJSON;

}

function dataType_filter(type, lbl) {
	$("#dataType_anchor").text(lbl);
	$("#time_anchor").text('Any Time');
	$("#location_anchor").text('All Locations');
	$("#provider_anchor").text('All Providers');

	var single_obsJSON = jsonAfterParse.obs_singles;
	var group_obsJSON = jsonAfterParse.obs_groups;
	var json_counter = 0;
	var newJSON = {
		'obs_groups' : new Array(),
		'obs_singles' : new Array()
	};

	if (typeof single_obsJSON !== 'undefined') {
		for ( var i = 0; i < single_obsJSON.length; i++) {
			if (single_obsJSON[i].value_type
					&& single_obsJSON[i].value_type === type) {
				newJSON.obs_singles[json_counter] = single_obsJSON[i];
				json_counter++;
			}
		}
	}

	json_counter = 0;
	if (group_obsJSON) {
		for ( var i = 0; i < group_obsJSON.length; i++) {
			var observations = group_obsJSON[i].observations;
			for ( var j = 0; j < observations.length; j++) {
				if (observations[j].value_type
						&& observations[j].value_type === type) {
					newJSON.obs_groups[json_counter] = group_obsJSON[i];
					json_counter++;
					break;
				}
			}
		}
	}
	addAllObsGroups(newJSON);
	addAllResultsFromJSONFromTheServer(newJSON);
	currentJson = newJSON;
}

function filterOptions_providers() {
	var providers = jsonAfterParse.providers;
	var result = '<hr />';
	for ( var i = 0; i < providers.length; i++) {
		var tmpProvider = providers[i].provider;
		result += '<a class="single_filter_option" onclick="provider_filter(\''
				+ tmpProvider + '\', \'' + tmpProvider + '\')">' + tmpProvider
				+ '</a>';
	}

	result += '<a class="single_filter_option" onclick="refresh_data(json)">All Providers</a>';
	document.getElementById('providersOptions').innerHTML = result;
}

function filterOptions_locations() {
	var locations = jsonAfterParse.locations;
	var result = '<hr />';
	for ( var i = 0; i < locations.length; i++) {
		var tmpLocation = locations[i].location;
		result += '<a class="single_filter_option" onclick="location_filter(\''
				+ tmpLocation + '\', \'' + tmpLocation + '\')">' + tmpLocation
				+ '</a>';
	}

	result += '<a class="single_filter_option" onclick="refresh_data(json)">All Locations</a>';
	document.getElementById('locationOptions').innerHTML = result;
}

function filterOptions_datatypes() {
	var datatypes = jsonAfterParse.datatypes;
	var result = '<hr />';
	for ( var i = 0; i < datatypes.length; i++) {
		var tmpdatatypes = datatypes[i].datatype;
		if (tmpdatatypes !== 'N/A') {
			result += '<a class="single_filter_option" onclick="dataType_filter(\''
					+ tmpdatatypes
					+ '\', \''
					+ tmpdatatypes
					+ '\')">'
					+ tmpdatatypes + '</a>';

		}
	}

	result += '<a class="single_filter_option" onclick="refresh_data(json)">All Data Types</a>';
	document.getElementById('datatypesOptions').innerHTML = result;
}

function refresh_data(json) {
	$('#searchText').val(json.search_phrase);
	var searchText = document.getElementById('searchText');

	$("#time_anchor").text('Any Time');
	$("#location_anchor").text('All Locations');
	$("#provider_anchor").text('All Providers');
	currentJson = json;
	displayCategories(json);
	if (json.noResults.foundNoResults) {
		document.getElementById('obsgroups_results').innerHTML = "<div id='found_no_results'>"
				+ json.noResults.foundNoResultsMessage
				+ " <b> "
				+ searchText.value + "</b></div>";
	} else {
		searchText.value = json.search_phrase;
		var numberOfResults = json.obs_groups.length + json.obs_singles.length
				+ json.patientAllergies.length
				+ json.patientAppointments.length;
		document.getElementById('found-results-summary').innerHTML = "<b>"
				+ numberOfResults + "</b> Results (<b>" + json.retrievalTime
				+ "</b> seconds)";
		// TODO remove filters and instead call this method within filter
		// methods
		filterOptions_providers();
		filterOptions_locations();
		addAllObsGroups(json);
		addAllResultsFromJSONFromTheServer(json);
		displayFailedPrivileges(json);
	}
	displayBothPersonalAndGlobalNotes();
}

/*
 * maintains categories state and displays new categories from the server
 */
function displayCategories(json) {
	var categories = document.getElementsByClassName('category_check');
	var checkedCategories = new Object();
	var allergies = json.patientAllergies;
	var appointments = json.patientAppointments;

	if (json.noResults.foundNoResults) {
		document.getElementById('inside_filter_categories').innerHTML = "";
	} else {
		// record previous state
		for ( var i = 0; i < categories.length; i++) {
			var catId = "#" + categories[i].id;
			if ($(catId).prop('checked')) {
				checkedCategories[i] = catId;
			}
		}

		// delete all categories being shown on the page
		document.getElementById('inside_filter_categories').innerHTML = "";

		// now fetch and display new categories from the server

		displayNonFacetCategories(allergies, "allergies");
		displayNonFacetCategories(appointments, "appointments");

		for ( var i = 0; i < json.facets.length; i++) {
			var name = json.facets[i].facet.name;
			var count = json.facets[i].facet.count;
			var displaycheckBox;
			var displayDetail;

			if (count != 0) {
				displaycheckBox = "<div class='category_filter_item'><input class='category_check' id='"
						+ name
						+ "_category' type='checkbox' name='categories' value='"
						+ name;
				displayDetail = "<a href='' class='select_one_category' id='select_"
						+ name
						+ "_category'>"
						+ capitalizeFirstLetter(name)
						+ "</a> (" + count + ") </div>";
			} else {
				displaycheckBox = "<div class='category_filter_item-disabled'><input class='category_check' id='"
						+ name
						+ "_category' type='checkbox' name='categories' value='"
						+ name;
				displayDetail = "<a href='' class='select_one_category' id='select_"
						+ name
						+ "_category'>"
						+ capitalizeFirstLetter(name)
						+ "</a> (" + count + ") </div>";
			}

			document.getElementById('inside_filter_categories').innerHTML += displaycheckBox
					+ "' />" + displayDetail;
		}

		// now check all previously checked categories
		for (index in checkedCategories) {
			$(checkedCategories[index]).prop('checked', true);
		}
	}
}

function displayNonFacetCategories(cat, catName) {
	var displayNonFacetcheckBox;
	var displayNonFacetDetail;

	if (cat.length !== 0) {
		displayNonFacetcheckBox = "<div class='category_filter_item'><input class='category_check' id='"
				+ catName
				+ "_category' type='checkbox' name='categories' value='"
				+ catName + "' />";
		displayNonFacetDetail = "<a href='' class='select_one_category' id='select_"
				+ catName
				+ "_category'>"
				+ capitalizeFirstLetter(catName)
				+ "</a> (" + cat.length + ") </div>";
	} else {
		displayNonFacetcheckBox = "<div class='category_filter_item-disabled'><input class='category_check' id='"
				+ catName
				+ "_category' type='checkbox' name='categories' value='"
				+ catName + "' />";
		displayNonFacetDetail = "<a href='' class='select_one_category' id='select_"
				+ catName
				+ "_category'>"
				+ capitalizeFirstLetter(catName)
				+ "</a> (" + 0 + ") </div>";
	}
	document.getElementById('inside_filter_categories').innerHTML += displayNonFacetcheckBox
			+ displayNonFacetDetail;
}

/*
 * In-case the has doesn't have privileges to view some results, they are not
 * returned and a message is instead displayed
 */
function displayFailedPrivileges(jsonAfterParse) {
	document.getElementById('failed_privileges').innerHTML == "";
	for ( var i = 0; i < jsonAfterParse.failedPrivileges.length; i++) {
		document.getElementById('failed_privileges').innerHTML += jsonAfterParse.failedPrivileges[i].message
				+ "<br />";
	}
}

function updateBookmarksAndNotesUI() {
	var bookmarks = jsonAfterParse.searchBookmarks;
	var pNotes = jsonAfterParse.personalNotes;
	var gNotes = jsonAfterParse.globalNotes;
	var bookmarkDoesnotExist = true;
	var pNoteDoesnotExist = true;
	var gNoteDoesnotExist = true;
	var sPhrase = jsonAfterParse.search_phrase;

	for (i = 0; i < bookmarks.length; i++) {
		if (bookmarks[i].searchPhrase === sPhrase) {
			bookmarkDoesnotExist = false;
		}
	}
	for (i = 0; i < pNotes.length; i++) {
		if (pNotes[i].searchPhrase === sPhrase) {
			pNoteDoesnotExist = false;
		}
	}
	for (i = 0; i < gNotes.length; i++) {
		if (gNotes[i].searchPhrase === sPhrase) {
			gNoteDoesnotExist = false;
		}
	}

	if (bookmarkDoesnotExist) {
		$("#favorite-search-record").removeClass("icon-star");
		$("#favorite-search-record").addClass("icon-star-empty");
	} else {
		$("#favorite-search-record").removeClass("icon-star-empty");
		$("#favorite-search-record").addClass("icon-star");
	}

	if (pNoteDoesnotExist && gNoteDoesnotExist) {
		$("#comment-on-search-record").removeClass("icon-comment");
		$("#comment-on-search-record").addClass("icon-comment-alt");
	} else {
		$("#comment-on-search-record").removeClass("icon-comment-alt");
		$("#comment-on-search-record").addClass("icon-comment");
	}
}

function displayBothPersonalAndGlobalNotes() {
	var personalNotes = jsonAfterParse.personalNotes;
	var globalNotes = jsonAfterParse.globalNotes;
	var displayPersonalNotes = "";
	var displayGlobalNotes = "";
	var displayAllNotes;
	var owner;
	var curUser = jsonAfterParse.currentUser;

	if (personalNotes && personalNotes.length !== 0) {
		displayPersonalNotes += "<u>Personal Notes (LOW priority):</u><br />";

		for (i = 0; i < personalNotes.length; i++) {
			var note = personalNotes[i];
			var date = new Date(note.createdOrLastModifiedAt);
			var time = date.toTimeString();
			owner = note.noteOwner;

			if (owner === curUser) {
				displayPersonalNotes += "On: <em>"
						+ note.formatedCreatedOrLastModifiedAt + " " + time
						+ "</em><p style='background-color:"
						+ note.backgroundColor + ";'>" + note.comment
						+ " <i class='icon-remove remove-this-sNote' id='"
						+ note.uuid + "'></i></p><hr>";
			}
		}
	}

	if (globalNotes && globalNotes.length !== 0) {
		displayGlobalNotes += "<u>Global Notes (HIGH priority):</u><br />";

		for (i = 0; i < globalNotes.length; i++) {
			var note = globalNotes[i];
			var date = new Date(note.createdOrLastModifiedAt);
			var time = date.toTimeString();
			owner = note.noteOwner;

			displayGlobalNotes += "<b>" + note.noteOwner + "</b> On: <em>"
					+ note.formatedCreatedOrLastModifiedAt + " " + time
					+ "</em><p style='background-color:" + note.backgroundColor
					+ ";'>" + note.comment;

			if (owner === curUser) {
				displayGlobalNotes += "<i class='icon-remove remove-this-sNote' id='"
						+ note.uuid + "'></i>";
			}

			displayGlobalNotes += "</p><hr>";
		}
	}
	if (displayPersonalNotes || displayGlobalNotes) {
		displayAllNotes = displayPersonalNotes + "<br /><hr />"
				+ displayGlobalNotes;

		$("#comment-on-search-record").removeClass("icon-comment-alt");
		$("#comment-on-search-record").addClass("icon-comment");
		$("#previous-notes-on-this-search").html(displayAllNotes);
		scrollToBottomOfDiv("#previous-notes-on-this-search");
	} else {
		$("#comment-on-search-record").removeClass("icon-comment");
		$("#comment-on-search-record").addClass("icon-comment-alt");
		$("#previous-notes-on-this-search").html("");
	}
}

function scrollToBottomOfDiv(element) {
	var wtf = $(element);
	var height = wtf[0].scrollHeight;
	wtf.scrollTop(height);
}

function closeAllActiveDialogs() {
	$("#favorite-search-record-dialog").dialog("close");
	$("#comment-on-search-record-dialog").dialog("close");
}

function displayQuickSearches() {
	var history = jsonAfterParse.searchHistory;
	var bookmarks = jsonAfterParse.searchBookmarks;
	var quickSearchDisplay = "";
	var hLength = history.length;
	var bLength = bookmarks.length;

	if (history && hLength > 0) {
		quickSearchDisplay += "<b>2 Most Recent & 3 Last bookmarked Searches</b><br /><u>History</u><br />";
		for (i = 0; i < hLength; i++) {
			var no = i + 1;
			if (i === 0 || i === 1) {
				quickSearchDisplay += no
						+ ". <a href='' class='quick-searches-history' id='"
						+ history[i].uuid + "'>" + history[i].searchPhrase
						+ "</a><br />";
			}
		}
	}

	if (bookmarks && bLength > 0) {
		quickSearchDisplay += "<u>Bookmarks</u><br />";
		for (i = 0; i < bLength; i++) {
			var no = i + 1;
			if (i === 0 || i === 1 || i === 2) {
				quickSearchDisplay += no
						+ ". <a href='' class='quick-searches-bookmark' id='"
						+ bookmarks[i].uuid + "'>" + bookmarks[i].searchPhrase
						+ "</a> [ <em>" + bookmarks[i].categories
						+ "</em> ]<br />";
			}
		}
	}

	if (quickSearchDisplay !== "") {
		$("#quick-searches-dialog-message").html(quickSearchDisplay);
	}
}

function getAllCheckedCategoriesOrFacets() {
	var categories = [];
	$("input:checkbox[name=categories]:checked").each(function() {
		categories.push($(this).val());
	});
	return categories;
}

function unSelectAllCategories() {
	$("input:checkbox[name=categories]:checked").each(function() {
		$(this).prop('checked', false);
	});
}

function updateCategeriesAtUIGlobally(categories) {
	if (categories && categories.length === 0) {
		$("#category-filter_method").text("All Categories");
	} else {
		if (categories.length === 1) {
			if (categories[0] && categories[0] !== "") {
				$("#category-filter_method").text(
						capitalizeFirstLetter(categories[0]));
			}
		} else if (categories.length >= 2) {
			if (categories[0] && categories[0] !== "" && categories[1]
					&& categories[1] !== "") {
				var catsLabel = capitalizeFirstLetter(categories[0]) + ","
						+ capitalizeFirstLetter(categories[1]);

				if (catsLabel.length <= 14) {
					$("#category-filter_method").text(catsLabel + "...");
				} else {
					$("#category-filter_method").text(
							capitalizeFirstLetter(categories[0]) + "...");
				}
			}
		}
		$("input:checkbox[class=category_check]").each(function() {
			var uiCat = $(this).val();

			for (i = 0; i < categories.length; i++) {
				var cat = categories[i];
				if (uiCat === cat) {
					$(this).prop('checked', true);
				}
			}
		});
	}
}

function checkOrUnAllOtherCheckBoxesInADiv(divElement, skipId) {
	$(divElement + " input").each(function(event) {
		var selectedId = $(this).attr("id");

		if (selectedId !== skipId && $(this).attr("type") !== "radio") {
			if ($("#" + skipId).is(":checked")) {
				$(this).prop("checked", true);
			} else {
				$(this).prop("checked", false);
			}
		}
	});
}

/* Overrides $.("").dialog() */
function invokeDialog(dialogMessageElement, dialogTitle, dialogWidth) {
	$(dialogMessageElement).dialog({
		title : dialogTitle,
		width : dialogWidth
	});
}

function reInitializeGlobalVars() {
	navigationIndex = 1;
	peviousIndex = 0;
	wasGoingNext = true;
}

function autoClickFirstResultToShowItsDetails(json) {
	if (json.obs_singles.length && json.obs_singles.length > 0) {
		$('#first_obs_single').trigger('click');
	} else if ((!json.obs_singles.length || json.obs_singles.length === 0)
			&& json.patientAllergies.length && json.patientAllergies.length > 0) {
		$('#first_alergen').trigger('click');
	} else if ((!json.obs_singles.length || json.obs_singles.length === 0)
			&& (!json.patientAllergies.length || json.patientAllergies.length === 0)
			&& json.patientAppointments.length
			&& json.patientAppointments.length > 0) {
		$('#first_appointment').trigger('click');
	}
}

function isLoggedInSynchronousCheck() {// $.getJSON(emr.fragmentActionLink('htmlformentryui',
	// 'htmlform/enterHtmlForm', 'checkIfLoggedIn'), function(result) {} but
	// synchronously
	var isLoggedIn = false;
	$.ajax({
		url : emr.fragmentActionLink('htmlformentryui',
				'htmlform/enterHtmlForm', 'checkIfLoggedIn'),
		dataType : 'json',
		async : false,
		success : function(loggedIn) {
			isLoggedIn = loggedIn.isLoggedIn;
		}
	});
	return isLoggedIn;
}

function filterResultsUsingTime(selectedPeriod) {
	var serverPeriod = getMatchedDatePeriod("1436333189000", "yesterday");

	alert(serverPeriod);
}

function checkIfDateIsToday(dateTime) {
	var inputDate = new Date(dateTime);
	var todaysDate = new Date();

	if (inputDate.setHours(0, 0, 0, 0) === todaysDate.setHours(0, 0, 0, 0)) {
		return true;
	} else {
		return false;
	}
}

function checkIfDateIsYesterday(milliSecs) {
	var inputDate = new Date(milliSecs);
	var todaysDate = new Date();
	todaysDate.setDate(todaysDate.getDate() - 1);// set today to yesterday
	// instead
	if (inputDate.setHours(0, 0, 0, 0) === todaysDate.setHours(0, 0, 0, 0)) {
		return true;
	} else {
		return false;
	}
}

function getWeek(date) {
	// Obtained from: http://weeknumber.net/how-to/javascript
	date.setHours(0, 0, 0, 0);
	// Thursday in current week decides the year.
	date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
	// January 4 is always in week 1.
	var week1 = new Date(date.getFullYear(), 0, 4);
	// Adjust to Thursday in week 1 and count number of weeks from date to
	// week1.
	return 1 + Math
			.round(((date.getTime() - week1.getTime()) / 86400000 - 3 + (week1
					.getDay() + 6) % 7) / 7);
}

function checkIfDateIsForThisWeek(dateTime) {
	var date = new Date(dateTime);
	var now = new Date();

	if (date.getFullYear() === now.getFullYear()
			&& date.getMonth() === now.getMonth()
			&& getWeek(date) === getWeek(now)) {
		return true;
	} else {
		return false;
	}
}

/* Returns matched period */
function getMatchedDatePeriod(dateTime, period) {
	if (typeof dateTime === 'string') {
		dateTime = parseInt(dateTime);
	}
	var date = new Date(dateTime);
	var matchedPeriod;

	if (period === "today" && checkIfDateIsToday(date.getTime())) { // today
		matchedPeriod = "today";
	} else if (period === "thisWeek"
			&& checkIfDateIsForThisWeek(date.getTime())) { // this week
		matchedPeriod = "thisWeek";
	} else if (period = "yesterday" && checkIfDateIsYesterday(date.getTime())) {
		matchedPeriod = "yesterday";
	}

	return matchedPeriod;
}
