var tryingToSubmit = false;
var viewsFactory;
var doT;
var current_JSON_OBJECT;

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
	var allergies = json.patientAllergies;
	var appointments = json.patientAppointments;
	single_obsJSON.sort(single_sort_func);
	single_obsJSON.reverse();
	if (typeof single_obsJSON !== 'undefined') {
		resultText += '';
		var firstObsSingle = false;

		for ( var i = 0; i < single_obsJSON.length; i++) {
			if (i === 0 && json.obs_groups.length === 0) {
				firstObsSingle = true;
			} else {
				firstObsSingle = false;
			}
			resultText += addSingleObsToResults(single_obsJSON[i],
					firstObsSingle);
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

function addSingleObsToResults(obsJSON, firstObsSingle) {
	var obs_id_html = '';
	var ob_id;
	if (typeof obsJSON.observation_id !== 'undefined') {
		if (firstObsSingle) {
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
			+ ');load_single_detailed_obs(' + obsJSON.observation_id + ', '
			+ ob_id + ');">';
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
			+ allId + '); load_allergen(' + allergyId + ', this);">';
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
			+ '); load_appointment(' + id + ', this);">';
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
	var int_date2 = parseInt(observation_obj.date);
	var date_formmated2 = new Date(int_date2);
	var cur2 = [ date_formmated2.getTime(), observation_obj.value ];
	var allToBePlottedDates = [];
	var allToBePlottedVals = [];

	if (jsonAfterParse.duplicate_obs_singles != undefined) {
		data2.push(cur2);
		data2.sort(array_sort);
	}

	for (i = 0; i < data2.length; i++) {
		allToBePlottedDates.push(data2[i][0]);
		allToBePlottedVals.push(data2[i][1]);
	}

	var maxToBePlottedMilli = Math.max.apply(Math, allToBePlottedDates);
	var minToBePlottedMilli = Math.min.apply(Math, allToBePlottedDates);
	var maxToBePlottedVal = Math.max.apply(Math, allToBePlottedVals);
	var minToBePlottedVal = Math.min.apply(Math, allToBePlottedVals);
	var xaxisRange = maxToBePlottedMilli - minToBePlottedMilli;
	var yaxisRange = maxToBePlottedVal - minToBePlottedVal;
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
		yaxis : {
			axisLabel : 'Value',
			axisLabelUseCanvas : true,
			axisLabelFontSizePixels : 16,
			axisLabelFontFamily : 'Arial',
			zoomRange : [ 0.1, 3600000000 ],
			panRange : [ minToBePlottedVal, maxToBePlottedVal ]
		},
		xaxis : {
			mode : "time",
			minTickSize : [ 1, "minute" ],
			timezone : "browser",
			min : minToBePlottedMilli,
			max : maxToBePlottedMilli,
			axisLabel : 'Time',
			axisLabelUseCanvas : true,
			axisLabelFontSizePixels : 16,
			axisLabelFontFamily : 'Arial',
			zoomRange : [ 0.1, 3600000000 ],
			panRange : [ minToBePlottedMilli, maxToBePlottedMilli ]
		},
		zoom : {
			interactive : false
		},
		pan : {
			interactive : false
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

function load_single_detailed_obs(obs_id, obsIdElement) {
	if (obsIdElement) {
		removeAllHovering();

		if (obsIdElement !== "first_obs_single") {
			obsIdElement = obsIdElement.id;
		}

		if (obsIdElement !== "" && obsIdElement === "first_obs_single") {
			$("#first_obs_single").addClass("obsgroup_current");
		} else {
			$("#obs_single_" + obs_id).addClass("obsgroup_current");
		}
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
	resultText += load_single_obs_history(obs_id);
	resultText += '<div class="obsgroup_all_wrapper">';
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
	var obsJSONDups = get_obs_history_json_by_name(obsJSON.concept_name);

	if (obsJSON.value_type == 'Numeric' && obsJSONDups.length > 0) {
		enable_graph(obs_id);
	} else {
		$(".demo-container").hide();
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

function load_allergen(allergeId, clickedElement) {
	removeAllHovering();
	var clickedElementId;
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

	if (clickedElement.id) {
		clickedElementId = clickedElement.id;
	} else {
		clickedElementId = "first_alergen";
	}
	$("#" + clickedElementId).addClass("obsgroup_current");

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
	resultText += '<tr><th>Reaction(s):</th><td>' + reaction + '</td></tr>';
	resultText += '<tr><th>Comment:</th><td>' + comment + '</td></tr>';
	resultText += '<tr><th>Last Updated:</th><td>' + date + '</td></tr>';
	resultText += '</table>';
	resultText += '</div>';

	$("#obsgroup_view").html(resultText);
}

function load_appointment(appId, clickedElement) {
	removeAllHovering();
	var clickedElementId;
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

	if (clickedElement.id) {
		clickedElementId = clickedElement.id;
	} else {
		clickedElementId = "first_appointment";
	}
	$("#" + clickedElementId).addClass("obsgroup_current");

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
	var json = getResultsJson()
	var numberOfResults = getResultsJson().obs_groups.length
			+ json.obs_singles.length + json.patientAllergies.length
			+ json.patientAppointments.length;
	var allPossibleIndices = new Array(numberOfResults);

	if (clickedId && clickedId !== '') {
		for (i = 0; i < allPossibleIndices.length; i++) {
			if (json.obs_groups.length > 0
					&& json.obs_groups[i]
					&& json.obs_groups[i].group_Id === id
					&& (clickedId === "first_obs_group" || clickedId === "obs_group_"
							+ id)) {
				navigationIndicesUpdateLogic(i, numberOfResults);
			} else if (json.obs_singles.length > 0
					&& json.obs_singles[i]
					&& json.obs_singles[i].observation_id === id
					&& (clickedId === "first_obs_single" || clickedId === "obs_single_"
							+ id)) {
				navigationIndicesUpdateLogic(i + json.obs_groups.length,
						numberOfResults);
			} else if (json.patientAllergies.length > 0
					&& json.patientAllergies[i]
					&& json.patientAllergies[i].allergenId === id
					&& (clickedId === "first_alergen" || clickedId === "allergen_"
							+ id)) {
				navigationIndicesUpdateLogic(i + json.obs_groups.length
						+ json.obs_singles.length, numberOfResults);
			} else if (json.patientAppointments.length > 0
					&& json.patientAppointments.length[i]
					&& json.patientAppointments.length[i] === id
					&& (clickedId === "first_appointment" || clickedId === "appointment_"
							+ id)) {
				navigationIndicesUpdateLogic(i + json.obs_groups.length
						+ json.obs_singles.length
						+ json.patientAllergies.length, numberOfResults);
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
	var obs_obj = get_single_obs_by_id(obs_id);
	var obs_name = obs_obj.concept_name;
	var history_json = get_obs_history_json_by_name(obs_name);
	var oRed = '';
	var oAddition = '';
	var time1 = new Date(parseInt(obs_obj.date));
	var numberInHistory = history_json.length === 0 ? 1 : 1 + history_json.length;
		
	resultText = '<h3>History (' + numberInHistory + ')</h3>';
	
	if (obs_obj.value < obs_obj.normal_low) {
		oRed = ' red ';
		oAddition = ' less_then_normal ';
	} else if (obs_obj.value > obs_obj.normal_low) {
		oRed = ' red ';
		oAddition = ' more_then_normal ';
	}
	resultText += '<table><tr><th>Time</th><th>Value</th></tr>';
	resultText += '<tr class="' + oRed + '"><td>'
			+ getDateStr(obs_obj.date, true) + ' ' + time1.toTimeString()
			+ '</td><td><div class="' + oAddition + '">' + obs_obj.value
			+ '</td></tr>';

	for ( var i = 0; i < history_json.length; i++) {
		var red = '';
		var addition = '';
		var time2 = new Date(parseInt(history_json[i].date));

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
				+ getDateStr(history_json[i].date, true) + ' '
				+ time2.toTimeString() + '</td><td><div class="' + addition
				+ '">' + history_json[i].value + '</div></td></tr>';
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
	var single_obsJSON;

	if (jsonAfterParse.duplicate_obs_singles !== undefined) {
		single_obsJSON = jsonAfterParse.duplicate_obs_singles;
	} else {
		single_obsJSON = jsonAfterParse.obs_singles;
	}

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
		var firstObsgroup = false;

		for ( var i = 0; i < obsgroupJSON.length; i++) {
			if (i === 0) {
				firstObsgroup = true;
			} else {
				firstObsgroup = false;
			}
			resultText += addObsGroupToResults(obsgroupJSON[i], firstObsgroup);
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

function addObsGroupToResults(obsJSON, firstObsgroup) {
	var resultText = '';
	var obs_id_html = '';
	var obs_id = 'obs_group_' + obsJSON.group_Id;
	var clickedId = "";

	if (typeof obsJSON.group_Id !== 'undefined') {
		if (firstObsgroup) {
			obs_id_html = 'id="first_obs_group"';
			clickedId = "first_obs_group";
		} else {
			obs_id_html = 'id="obs_group_' + obsJSON.group_Id + '"';
			clickedId = "obs_group_" + obsJSON.group_Id;
		}
	}
	resultText += '<div class="obsgroup_wrap" ' + obs_id_html
			+ ' onclick="load_detailed_obs(' + obsJSON.group_Id + ', \''
			+ clickedId + '\');updateNavigationIndicesToClicked('
			+ obsJSON.group_Id + ', ' + clickedId + ')">';
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

function load_detailed_obs(obs_id, clickedElement) {
	var obsJSON = get_obs_by_id(obs_id);
	var resultText = '';

	removeAllHovering();

	if (!clickedElement) {
		clickedElement = "first_obs_group";
	}

	if (clickedElement === "first_obs_group") {
		$("#first_obs_group").addClass("obsgroup_current");
	} else {
		$("#obs_group_" + obs_id).addClass("obsgroup_current");
	}

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
				+ '" onclick="load_single_detailed_obs('
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
		resultText += '<span><i>' + singleObs[i].value_type + '</i></span>';
		resultText += '</div>';
	}
	resultText += '</div>';
	resultText += '</div>';

	$("#obsgroup_view").html(resultText);
}

function refresh_data(json) {
	$('#searchText').val(json.search_phrase);
	var searchText = document.getElementById('searchText');
	var filteredJson = $("#json-filtered-string").val();
	var pref = $("#stored-preferences").val();
	var preferences;

	if (pref !== "" && pref !== undefined) {
		preferences = JSON.parse(pref);
	}

	if (preferences.enableMultipleFiltering === false) {
		$("#time_anchor").text('Any Time');
		$("#location_anchor").text('All Locations');
		$("#provider_anchor").text('All Providers');
	}

	if (!json || json.noResults.foundNoResults) {
		if (json) {
			document.getElementById('obsgroups_results').innerHTML = "<div id='found_no_results'>"
					+ json.noResults.foundNoResultsMessage
					+ " <b> "
					+ searchText.value + "</b></div>";
		} else {
			alert("Still Indexing!");
		}
	} else {
		if (!filteredJson || filteredJson === "") {
			displayCategories(json);
			displayProviders(json);
			displayLocations(json);
		}
		searchText.value = json.search_phrase;
		var numberOfResults = json.obs_groups.length + json.obs_singles.length
				+ json.patientAllergies.length
				+ json.patientAppointments.length;
		var noResultsMessage = "<b>" + numberOfResults + "</b> ";
		if (json.duplicate_obs_singles !== undefined) {
			noResultsMessage += "Summarized ";
		}
		noResultsMessage += "Results (<b>" + json.retrievalTime
				+ "</b> seconds)";
		var noResultsMessageNote = "";

		if (numberOfResults === 0 && json.searchSuggestions.length === 0
				&& json.search_phrase === "") {
			noResultsMessageNote = "<br /><br /><br /><br /><p style='color:black;'><b>NOTE:</b> If this is the first time you are accessing this patient's chart, <b>Indexing patient data could still be in progress. </b>"
					+ "So refresh the page a few moments from now. Otherwise the <b>patient has no data</b> (observations, allergies and appointments) that matches the current search or filter tried</p>";
		}

		if (noResultsMessageNote !== "") {
			document.getElementById('found-results-summary').innerHTML = noResultsMessage
					+ noResultsMessageNote;
		} else {
			document.getElementById('found-results-summary').innerHTML = noResultsMessage;
		}

		addAllObsGroups(json);
		addAllResultsFromJSONFromTheServer(json);
		displayFailedPrivileges(json);
	}
}

/*
 * maintains categories state and displays new categories from the server
 */
function displayCategories(json) {
	var categories = document.getElementsByClassName('category_check');
	var checkedCategories = new Object();
	var allergies = json.patientAllergies;
	var appointments = json.patientAppointments;
	var catFilters = json.categoryFilters;

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

		displayNonFacetCategories(allergies, "allergies", json);
		displayNonFacetCategories(appointments, "appointments", json);

		for ( var i = 0; i < json.facets.length; i++) {
			var name = json.facets[i].facet.name;
			var count = json.facets[i].facet.count;
			var displaycheckBox;
			var displayDetail;
			var displayName = capitalizeFirstLetter(name);

			for (j = 0; j < json.categoryFilters.length; j++) {
				if (capitalizeFirstLetter(json.categoryFilters[j].name) === capitalizeFirstLetter(name)
						&& json.categoryFilters[j].displayName !== undefined
						&& json.categoryFilters[j].displayName !== "") {
					displayName = json.categoryFilters[j].displayName;
				}
			}

			if (count != 0 && name !== "convset") {
				displaycheckBox = "<div class='category_filter_item'><input class='category_check' id='"
						+ name
						+ "_category' type='checkbox' name='categories' value='"
						+ name;
				displayDetail = "<a href='' class='select_one_category' id='select_"
						+ name
						+ "_category'>"
						+ displayName
						+ "</a> ("
						+ count
						+ ") </div>";
			} else {
				displaycheckBox = "<div class='category_filter_item-disabled'><input class='category_check' id='"
						+ name
						+ "_category' type='checkbox' name='categories' value='"
						+ name;
				displayDetail = "<a href='' class='select_one_category' id='select_"
						+ name
						+ "_category'>"
						+ displayName
						+ "</a> ("
						+ count
						+ ") </div>";
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

function displayNonFacetCategories(cats, catName, json) {
	var displayNonFacetcheckBox;
	var displayNonFacetDetail;
	var displayName = capitalizeFirstLetter(catName);

	for (i = 0; i < json.categoryFilters.length; i++) {
		if (capitalizeFirstLetter(json.categoryFilters[i].name) === capitalizeFirstLetter(catName)
				&& json.categoryFilters[i].displayName !== undefined
				&& json.categoryFilters[i].displayName !== "") {
			displayName = json.categoryFilters[i].displayName;
		}
	}

	if (cats.length !== 0) {
		displayNonFacetcheckBox = "<div class='category_filter_item'><input class='category_check' id='"
				+ catName
				+ "_category' type='checkbox' name='categories' value='"
				+ catName + "' />";
		displayNonFacetDetail = "<a href='' class='select_one_category' id='select_"
				+ catName
				+ "_category'>"
				+ displayName
				+ "</a> ("
				+ cats.length + ") </div>";
	} else {
		displayNonFacetcheckBox = "<div class='category_filter_item-disabled'><input class='category_check' id='"
				+ catName
				+ "_category' type='checkbox' name='categories' value='"
				+ catName + "' />";
		displayNonFacetDetail = "<a href='' class='select_one_category' id='select_"
				+ catName
				+ "_category'>"
				+ displayName
				+ "</a> ("
				+ 0
				+ ") </div>";
	}
	document.getElementById('inside_filter_categories').innerHTML += displayNonFacetcheckBox
			+ displayNonFacetDetail;
}

/*
 * In-case the user has doesn't have privileges to view some results, they are
 * not returned and a message is instead displayed
 */
function displayFailedPrivileges(jsonAfterParse) {
	if (jsonAfterParse.failedPrivileges) {
		document.getElementById('failed_privileges').innerHTML == "";
		for ( var i = 0; i < jsonAfterParse.failedPrivileges.length; i++) {
			document.getElementById('failed_privileges').innerHTML += jsonAfterParse.failedPrivileges[i].message
					+ "<br />";
		}
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

function displayBothPersonalAndGlobalNotes(json) {
	var personalNotes = json.personalNotes;
	var globalNotes = json.globalNotes;
	var displayPersonalNotes = "";
	var displayGlobalNotes = "";
	var displayAllNotes;
	var owner;
	var curUser = json.currentUser;

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
	if (json.obs_groups.length && json.obs_groups.length > 0) {
		$('#first_obs_group').trigger('click');
	} else if ((!json.obs_groups.length || json.obs_groups.length === 0)
			&& json.obs_singles.length && json.obs_singles.length > 0) {
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
	var storedJsonAfterParse = JSON.parse($("#json-stored-string").val());
	var filtered = $("#json-filtered-string").val();
	var filteredJSONAfterParse;
	if (filtered !== "") {
		filteredJSONAfterParse = JSON.parse(filtered);
	}
	var pref = $("#stored-preferences").val();
	var preferences;
	if (pref !== "" && pref !== undefined) {
		preferences = JSON.parse(pref);
	}
	var newResultsJson;
	var new_obs_groups = [];
	var new_obs_singles = [];
	var new_patientAllergies = [];
	var new_patientAppointments = [];
	var selectedPeriodText = "Any Time";

	if (preferences !== undefined && preferences !== undefined
			&& preferences.enableMultipleFiltering === true
			&& filteredJSONAfterParse !== undefined) {
		newResultsJson = filteredJSONAfterParse;
	} else {
		newResultsJson = storedJsonAfterParse;
	}

	if (selectedPeriod === "custom") {
		$("#submit-custom-date-filter").attr('disabled', 'disabled');
		invokeDialog("#custom-date-dialog-content", "Choose Date Range",
				"245px")
	} else {
		if (selectedPeriod === "anyTime") {
			$("#json-filtered-string")
					.val(JSON.stringify(storedJsonAfterParse));
			refresh_data(newResultsJson);
			reInitializeGlobalVars();
		} else {
			if (newResultsJson.obs_groups.length > 0) {
				// TODO compare and form a new obs_groups object
				// new_obs_groups = newResultsJson.obs_groups;
			}
			if (newResultsJson.obs_singles.length > 0) {
				for (i = 0; i < newResultsJson.obs_singles.length; i++) {
					var curObjSingle = newResultsJson.obs_singles[i];
					var neededPeriod = getMatchedDatePeriod(curObjSingle.date,
							selectedPeriod);

					if (neededPeriod !== "anyTime") {
						new_obs_singles.push(curObjSingle);
					}
				}
			}
			if (newResultsJson.patientAllergies.length > 0) {
				for (i = 0; i < newResultsJson.patientAllergies.length; i++) {
					var curAllergy = newResultsJson.patientAllergies[i];
					var neededPeriod = getMatchedDatePeriod(
							curAllergy.allergenDate, selectedPeriod);

					if (neededPeriod !== "anyTime") {
						new_patientAllergies.push(curAllergy);
					}
				}
			}
			if (newResultsJson.patientAppointments.length > 0) {
				for (i = 0; i < newResultsJson.patientAppointments.length; i++) {
					var curAppointment = newResultsJson.patientAppointments[i];
					var neededPeriod = getMatchedDatePeriod(
							curAppointment.start, selectedPeriod);
					if (neededPeriod !== "anyTime") {
						new_patientAppointments.push(curAppointment);
					}
				}
			}

			newResultsJson.obs_groups = new_obs_groups;
			newResultsJson.obs_singles = new_obs_singles;
			newResultsJson.patientAllergies = new_patientAllergies;
			newResultsJson.patientAppointments = new_patientAppointments;
		}
		setResultsJsonAndApplySelectedFilter(newResultsJson);

		if (selectedPeriod === "today") {
			selectedPeriodText = "Today";
		} else if (selectedPeriod === "yesterday") {
			selectedPeriodText = "Yesterday";
		} else if (selectedPeriod === "thisWeek") {
			selectedPeriodText = "This Week";
		} else if (selectedPeriod === "thisMonth") {
			selectedPeriodText = "This Month";
		} else if (selectedPeriod === "last3Months") {
			selectedPeriodText = "Last 3 Months";
		} else if (selectedPeriod === "thisYear") {
			selectedPeriodText = "This Year";
		}
		$("#time_anchor").text(selectedPeriodText);
	}

}

function setResultsJsonAndApplySelectedFilter(newResultsJson) {
	document.getElementById('obsgroup_view').innerHTML = "";
	$("#obsgroups_results").html("");

	refresh_data(newResultsJson);
	reInitializeGlobalVars();
	$("#json-filtered-string").val(JSON.stringify(newResultsJson));
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

function checkIfDateIsInCurrentMonth(dateTime) {
	var date = new Date(dateTime);
	var now = new Date();

	if (date.getYear() === now.getYear() && date.getMonth() === now.getMonth()) {
		return true;
	} else {
		return false;
	}
}

function checkIfDateIsInLastThreeMonths(milliSecs) {
	// checks for last three months and includes the current
	var date = new Date(milliSecs);
	var now = new Date();
	var diff = now.getMonth() - date.getMonth();

	if (date.getYear() === now.getYear() && diff >= 0 && diff <= 3) {
		return true;
	} else {
		return false;
	}
}

function checkIfDateIsInCurrentYear(milliSecs) {
	var date = new Date(milliSecs);
	var now = new Date();

	if (date.getYear() === now.getYear()) {
		return true;
	} else {
		return false;
	}
}

/* Returns matched period */
function getMatchedDatePeriod(dateTime, period) {
	// period can be any of: (today, yesterday, thisWeek, thisMonth,
	// last3Months, thisYear, anyTime)
	if (typeof dateTime === 'string') {
		dateTime = parseInt(dateTime);
	}
	var date = new Date(dateTime);
	var matchedPeriod;

	if (period === "today" && checkIfDateIsToday(date.getTime())) { // today
		matchedPeriod = "today";
	} else if (period === "yesterday" && checkIfDateIsYesterday(date.getTime())) {
		matchedPeriod = "yesterday";
	} else if (period === "thisWeek"
			&& checkIfDateIsForThisWeek(date.getTime())) { // this week
		matchedPeriod = "thisWeek";
	} else if (period === "thisMonth"
			&& checkIfDateIsInCurrentMonth(date.getTime())) {
		matchedPeriod = "thisMonth";
	} else if (period === "last3Months"
			&& checkIfDateIsInLastThreeMonths(date.getTime())) {
		matchedPeriod = "last3Months";
	} else if (period === "thisYear"
			&& checkIfDateIsInCurrentYear(date.getTime())) {
		matchedPeriod = "thisYear";
	} else {
		matchedPeriod = "anyTime";
	}

	return matchedPeriod;
}

function storeJsonFromServer(json) {
	var jsonStringToStore = JSON.stringify(json);
	$("#json-stored-string").val(jsonStringToStore);
	$("#json-filtered-string").val("");
}

function getResultsJson() {
	var storedJsonString = $("#json-stored-string").val();
	var filteredJsonString = $("#json-filtered-string").val();

	if (filteredJsonString && filteredJsonString !== "") {
		return JSON.parse(filteredJsonString);
	} else {
		if (storedJsonString && storedJsonString !== "") {
			return JSON.parse(storedJsonString);
		} else {
			return jsonAfterParse;
		}
	}
}

function checkIfDateRangeExists(milliSecs, start, stop) {
	if (typeof milliSecs === 'string') {
		milliSecs = parseInt(milliSecs);
	}

	var serverDate = new Date(milliSecs);
	var startDate = new Date(start);
	var stopDate = new Date(stop);

	if (serverDate.setHours(0, 0, 0, 0) >= startDate.setHours(0, 0, 0, 0)
			&& serverDate.setHours(0, 0, 0, 0) <= stopDate.setHours(0, 0, 0, 0)) {
		return true;
	} else {
		return false;
	}
}

function filterResultsUsingCustomTime() {
	var start = $("#ui-datepicker-start").val();
	var stop = $("#ui-datepicker-stop").val();
	var storedJsonAfterParse = JSON.parse($("#json-stored-string").val());
	var filtered = $("#json-filtered-string").val();
	var filteredJSONAfterParse;
	if (filtered !== "") {
		filteredJSONAfterParse = JSON.parse(filtered);
	}
	var pref = $("#stored-preferences").val();
	var preferences;
	if (pref !== "" && pref !== undefined) {
		preferences = JSON.parse(pref);
	}
	var newResultsJson;

	if (preferences !== undefined
			&& preferences.enableMultipleFiltering === true
			&& filteredJSONAfterParse !== undefined) {
		newResultsJson = filteredJSONAfterParse;
	} else {
		newResultsJson = storedJsonAfterParse;
	}

	if (start && start !== "" && stop && stop !== "") {
		if (newResultsJson.obs_groups.length > 0) {
			// new_obs_groups = newResultsJson.obs_groups;
		}
		if (newResultsJson.obs_singles.length > 0) {
			for (i = 0; i < newResultsJson.obs_singles.length; i++) {
				var curObjSingle = newResultsJson.obs_singles[i];

				if (checkIfDateRangeExists(curObjSingle.date, start, stop)) {
					new_obs_singles.push(curObjSingle);
				}
			}
		}
		if (newResultsJson.patientAllergies.length > 0) {
			for (i = 0; i < newResultsJson.patientAllergies.length; i++) {
				var curAllergy = newResultsJson.patientAllergies[i];

				if (checkIfDateRangeExists(curAllergy.allergenDate, start, stop)) {
					new_patientAllergies.push(curAllergy);
				}
			}
		}
		if (newResultsJson.patientAppointments.length > 0) {
			for (i = 0; i < newResultsJson.patientAppointments.length; i++) {
				var curAppointment = newResultsJson.patientAppointments[i];

				if (checkIfDateRangeExists(curAppointment.start, start, stop)) {
					new_patientAppointments.push(curAppointment);
				}
			}
		}

		newResultsJson.obs_groups = new_obs_groups;
		newResultsJson.obs_singles = new_obs_singles;
		newResultsJson.patientAllergies = new_patientAllergies;
		newResultsJson.patientAppointments = new_patientAppointments;
	}
	setResultsJsonAndApplySelectedFilter(newResultsJson);
	$("#time_anchor").text("Custom");
}

function displayLocations(json) {
	var allLocations = json.allLocations;
	var locationsHtml = '<a class="single_filter_option" onclick="filterResultsUsingLocation(\'All Locations\')">All Locations</a>';

	document.getElementById('locationOptions').innerHTML = "";

	for (i = 0; i < allLocations.length; i++) {
		var location = allLocations[i];

		if (location) {
			locationsHtml += '<a class="single_filter_option" onclick="filterResultsUsingLocation(\''
					+ location + '\')">' + location + '</a>';
		}
	}

	document.getElementById('locationOptions').innerHTML = locationsHtml;
}

function displayProviders(json) {
	var allProviders = json.allProviders;
	var providersHtml = '<a class="single_filter_option" onclick="filterResultsUsingProvider(\'All Providers\')">All Providers</a>';

	document.getElementById('providersOptions').innerHTML = "";

	for (i = 0; i < allProviders.length; i++) {
		var provider = allProviders[i];

		if (provider) {
			providersHtml += '<a class="single_filter_option" onclick="filterResultsUsingProvider(\''
					+ provider + '\')">' + provider + '</a>';
		}
	}

	document.getElementById('providersOptions').innerHTML = providersHtml;
}

function filterResultsUsingLocation(location) {
	if (location && location != "") {
		var storedJsonAfterParse = JSON.parse($("#json-stored-string").val());
		var filtered = $("#json-filtered-string").val();
		var filteredJSONAfterParse;
		if (filtered !== "") {
			filteredJSONAfterParse = JSON.parse(filtered);
		}
		var pref = $("#stored-preferences").val();
		var preferences;
		if (pref !== "" && pref !== undefined) {
			preferences = JSON.parse(pref);
		}
		var newResultsJson;
		var new_obs_groups = [];
		var new_obs_singles = [];
		var new_patientAllergies = [];
		var new_patientAppointments = [];

		if (preferences !== undefined
				&& preferences.enableMultipleFiltering === true
				&& filteredJSONAfterParse !== undefined) {
			newResultsJson = filteredJSONAfterParse;
		} else {
			newResultsJson = storedJsonAfterParse;
		}

		if (location === "All Locations") {
			setResultsJsonAndApplySelectedFilter(newResultsJson);
		} else {
			if (newResultsJson.obs_groups.length > 0) {
				// TODO compare and form a new obs_groups object
				// new_obs_groups = newResultsJson.obs_groups;
			}
			if (newResultsJson.obs_singles.length > 0) {
				for (i = 0; i < newResultsJson.obs_singles.length; i++) {
					var curObjSingle = newResultsJson.obs_singles[i];

					if (curObjSingle.location === location) {
						new_obs_singles.push(curObjSingle);
					}
				}
			}
			if (newResultsJson.patientAllergies.length > 0) {
				// new_patientAllergies = newResultsJson.patientAllergies;
				// TODO patientAllergies are not related to locations
			}
			if (newResultsJson.patientAppointments.length > 0) {
				for (i = 0; i < newResultsJson.patientAppointments.length; i++) {
					var curAppointment = newResultsJson.patientAppointments[i];

					if (curAppointment.location === location) {
						new_patientAppointments.push(curAppointment);
					}
				}
			}

			newResultsJson.obs_groups = new_obs_groups;
			// newResultsJson.patientAllergies = new_patientAllergies;
			newResultsJson.obs_singles = new_obs_singles;
			newResultsJson.patientAppointments = new_patientAppointments;

			setResultsJsonAndApplySelectedFilter(newResultsJson);
		}
		$("#location_anchor").text(location);
	}
}

function filterResultsUsingProvider(provider) {
	if (provider && provider !== "") {
		var storedJsonAfterParse = JSON.parse($("#json-stored-string").val());
		var filtered = $("#json-filtered-string").val();
		var filteredJSONAfterParse;
		if (filtered !== "") {
			filteredJSONAfterParse = JSON.parse(filtered);
		}
		var pref = $("#stored-preferences").val();
		var preferences;
		if (pref !== "" && pref !== undefined) {
			preferences = JSON.parse(pref);
		}
		var newResultsJson;
		var new_obs_groups = [];
		var new_obs_singles = [];
		var new_patientAllergies = [];
		var new_patientAppointments = [];

		if (preferences !== undefined
				&& preferences.enableMultipleFiltering === true
				&& filteredJSONAfterParse !== undefined) {
			newResultsJson = filteredJSONAfterParse;
		} else {
			newResultsJson = storedJsonAfterParse;
		}

		if (provider === "All Providers") {
			setResultsJsonAndApplySelectedFilter(newResultsJson);
		} else {
			if (newResultsJson.obs_groups.length > 0) {
				// TODO compare and form a new obs_groups object
				// new_obs_groups = newResultsJson.obs_groups;
			}
			if (newResultsJson.obs_singles.length > 0) {
				for (i = 0; i < newResultsJson.obs_singles.length; i++) {
					var curObjSingle = newResultsJson.obs_singles[i];

					if (curObjSingle.provider === provider) {
						new_obs_singles.push(curObjSingle);
					}
				}
			}
			if (newResultsJson.patientAllergies.length > 0) {
				// new_patientAllergies = newResultsJson.patientAllergies;
				// TODO patientAllergies are not related to provider
			}
			if (newResultsJson.patientAppointments.length > 0) {
				for (i = 0; i < newResultsJson.patientAppointments.length; i++) {
					var curAppointment = newResultsJson.patientAppointments[i];

					if (curAppointment.provider === provider) {
						new_patientAppointments.push(curAppointment);
					}
				}
			}

			newResultsJson.obs_groups = new_obs_groups;
			// newResultsJson.patientAllergies = new_patientAllergies;
			newResultsJson.obs_singles = new_obs_singles;
			newResultsJson.patientAppointments = new_patientAppointments;

			setResultsJsonAndApplySelectedFilter(newResultsJson);
		}
		$("#provider_anchor").text(provider);
	}
}

function applyPreferencesToUIDisplays() {
	var preferences = $("#stored-preferences").val();
	var pref;

	if (preferences !== undefined && preferences !== "") {
		pref = JSON.parse(preferences);

		if (pref.enableQuickSearches === false) {
			$("#quick-searches").hide();
		} else {
			$("#quick-searches").show();
		}
		if (pref.enableBookmarks === false) {
			$("#favorite-search-record").hide();
		} else {
			$("#favorite-search-record").show();
		}
		if (pref.enableNotes === false) {
			$("#comment-on-search-record").hide();
		} else {
			$("#comment-on-search-record").show();
		}
		if (pref.enableHistory === false) {
			$("#chart-previous-searches").hide();
		} else {
			$("#chart-previous-searches").show();
		}
	}
}

function displayCategoriesForPreferences(cats) {
	var displayHtml = "<tr><th>Category Name</th><th>Category Description</th><th>Category Display Name</th></tr>";

	for (i = 0; i < cats.length; i++) {
		var displayName = cats[i].displayName === undefined ? ""
				: cats[i].displayName;

		displayHtml += "<tr><td>" + cats[i].name + "</td><td>"
				+ cats[i].description
				+ "</td><td><input type='textbox' class='pref-cat-names' id='"
				+ cats[i].uuid + "' value='" + displayName + "' title='"
				+ cats[i].name + "' /></td></tr>";
	}

	$("#preferences-cats").html(displayHtml);
}

function checkIfCatDisplayNamesHaveRightNumberOfCharacters() {
	var alright = true;

	$(".pref-cat-names").each(function(event) {
		var name = $(this).val();

		if (name.length > 12) {
			alright = false;
		}
	});

	return alright;
}
