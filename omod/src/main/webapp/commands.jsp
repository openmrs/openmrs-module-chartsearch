<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:htmlInclude file="/dwr/interface/DWRCommands.js"/>

<%-- <openmrs:require privilege="Manage Global Properties" otherwise="/login.htm" redirect="/module/webservices/rest/settings.form" />--%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h2>
  <spring:message code="chartsearch.manage.commands.title" />
</h2>

<script type="text/javascript">

$j(document).ready(function() {
	$j('#patientInfoBtn').click(getPatientInfo);
	$j('#statisticsBtn').click(getStatistics);
});

function getPatientInfo(){
	DWRCommands.getPatientInfo($j("#patientId").val(), function(patientInfo) { 
		var text;
		if (!patientInfo) text="Patient is not in the index";
		else {
			var time = patientInfo.lastIndexTime.toString();
			text = "Last indexed time: " + time;
		}
		$j("#patientInfoResult").text(text); 
  	});	
}

function getStatistics(){
	DWRCommands.getStatistics(function(stats) { 
		var text;
		if (!stats) text="Error getting statistics";
		else {
			var strategyName = patientInfo.strategyName.toString();
			var pruneCount = patientInfo.pruneCount;
			console.log("Daemon States:" + patientInfo.daemonStates);
			for(int i=0; i < patientInfo.daemonStates.length; i++){
				console.log("Daemon State " + i + " " + patientInfo.daemonStates[0]);
			}
		}
		$j("#statisticsResult").text(text); 
  	});
}
</script>

<div class="boxHeader">Patient info (state and last index time)</div>
<div class="box">
	Patient id: <input type="text" name="patientId" id="patientId"/>
	<input type="button" id="patientInfoBtn" value="Get patient info"/>
	<br />
	<span id="patientInfoResult"></span>
</div>
<div class="boxHeader">Statistics</div>
<div class="box">
	<input type="button" id="statisticsBtn" value="Get statistics"/>
	<br />
	<span id="statisticsResult"></span>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>