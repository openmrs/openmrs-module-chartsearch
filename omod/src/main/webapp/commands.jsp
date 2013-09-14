<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:htmlInclude file="/dwr/interface/DWRCommands.js" />

<%-- <openmrs:require privilege="Manage Global Properties" otherwise="/login.htm" redirect="/module/webservices/rest/settings.form" />--%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="chartsearch.manage.commands.title" />
</h2>

<script type="text/javascript">
	$j(document).ready(function() {
		console.log("Document is ready");
		$j('#patientInfoBtn').click(getPatientInfo);
		$j('#statisticsBtn').click(getStatistics);
		$j('#clearBtn').click(clearIndex);
	});

	function getPatientInfo() {
		DWRCommands.getPatientInfo($j("#patientId").val(),
				function(patientInfo) {
					var text;
					if (!patientInfo)
						text = "Patient is not in the index";
					else {
						var time = patientInfo.lastIndexTime.toString();
						text = "Last indexed time: " + time;
					}
					$j("#patientInfoResult").text(text);
				});
	}

	function getStatistics() {
		DWRCommands
				.getStatistics(function(stats) {
					if (!stats)
						$j("#statisticsResult").text(
								"Error on getting statistics");
					else {
						$j("#statisticsResult").empty();
						var strategyName = stats.strategyName.toString();
						$j("<div/>", {
							text : "Strategy: " + strategyName
						}).appendTo("#statisticsResult");
						var pruneCount = stats.pruneCount;
						$j("<div/>", {
							text : "Prune patients count: " + pruneCount
						}).appendTo("#statisticsResult");
						$j("<div/>", {
							id : "daemonStates",
							text : "Daemon states:"
						}).appendTo("#statisticsResult");
						for ( var i = 0; i < stats.daemonStates.length; i++) {
							var daemonId = stats.daemonStates[i]['daemon id'];
							var daemonStatus = stats.daemonStates[i]['daemon status'];
							var daemonSuccessCount = stats.daemonStates[i]['daemon success count'];
							var daemonFailCount = stats.daemonStates[i]['daemon fail count'];
							$j(
									"<div/>",
									{
										id : "daemonState" + i,
										text : "Daemon Id: " + daemonId + ", "
												+ "Status:" + daemonStatus
												+ ", " + "Processed:"
												+ daemonSuccessCount
									//TODO Fail doesn't implemented
									/*+ "Success:" + daemonSuccessCount + ", " 
									+ "Fail:"+ daemonFailCount */
									}).appendTo("#daemonStates");
						}
					}
				});

	}

	function clearIndex() {
		DWRCommands.clearIndex($j("#clearStrategy").val(), $j("#patientIds").val(), $j("#maxPatients").val(), $j("#ago").val(),
				function(pruneCount) {
					console.log("Begin clearIndex");
					console.log("pruneCount: " + pruneCount);
					if (pruneCount == null)
						$j("#clearResult").text("Error on pruning patients");
					else
						$j("#clearResult").text(
								"Sent request to delete " + pruneCount
										+ " patient(s)");
					console.log("End clearIndex");
				});
	}
	
</script>

<div class="boxHeader">Patient info (state and last index time)</div>
<div class="box">
	<input type="button" id="patientInfoBtn" value="Get patient info" />
	Patient id: <input type="text" name="patientId" id="patientId" /> <br />
	<span id="patientInfoResult"></span>
</div>
<div class="boxHeader">Statistics</div>
<div class="box">
	<input type="button" id="statisticsBtn" value="Get statistics" /> <br />
	<span id="statisticsResult"></span>
</div>
<div class="boxHeader">Clear index</div>
<div class="box">
	<input type="button" id="clearBtn" value="Clear index" /> <br />
	Clear strategy: 
	<form:select id="clearStrategy" path="clearStrategies" >
		<form:options items="${clearStrategies}" />
	</form:select>
	<br /> 
	Patient ids: <input type="text" name="patientIds" id="patientIds" /> <br />Count of maximum patients to left: <input type="text"
		name="maxPatients" id="maxPatients" /> <br /> Max time patient can
	store: <input type="text" name="ago" id="ago" /> <br /> <span
		id="clearResult"></span>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>