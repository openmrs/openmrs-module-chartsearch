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
	console.log("Document is ready");
	$j('#patientInfoBtn').click(getPatientInfo);
	$j('#statisticsBtn').click(getStatistics);
});

function getPatientInfo(){
	console.log("Begin getPatientInfo");
	DWRCommands.getPatientInfo($j("#patientId").val(), function(patientInfo) { 
		var text;
		if (!patientInfo) text="Patient is not in the index";
		else {
			var time = patientInfo.lastIndexTime.toString();
			text = "Last indexed time: " + time;
		}
		$j("#patientInfoResult").text(text); 
  	});	
	console.log("End getPatientInfo");
}

function getStatistics(){
	console.log("Begin getStatistics");
	DWRCommands.getStatistics(function(stats) {
		if (!stats) $j("#statisticsResult").text("Error getting statistics");		
		else {
			$j("#statisticsResult").empty();
			var strategyName = stats.strategyName.toString();
			console.log("Strategy: " + strategyName);
			$j("<div/>", {
				text: "Strategy: " + strategyName
			}).appendTo("#statisticsResult");
			var pruneCount = stats.pruneCount;
			console.log("Prune count: " + pruneCount);			
			$j("<div/>", {
				text: "Prune patients count: " + pruneCount
			}).appendTo("#statisticsResult");
			console.log("Daemon States:" + stats.daemonStates);
			$j("<div/>", {
				id: "daemonStates",
				text: "Daemon states:"
			}).appendTo("#statisticsResult");
			for(var i=0; i < stats.daemonStates.length; i++){
				var daemonId = stats.daemonStates[i]['daemon id'];
				var daemonStatus = stats.daemonStates[i]['daemon status'];
				var daemonSuccessCount = stats.daemonStates[i]['daemon success count'];
				var daemonFailCount = stats.daemonStates[i]['daemon fail count'];
				console.log("Daemon State " + stats.daemonStates[i]['daemon id'] + " " + stats.daemonStates[i]['daemon status'] + " "
					+ stats.daemonStates[i]['daemon success count'] + " " + stats.daemonStates[i]['daemon fail count']);
				$j("<div/>", {
					id: "daemonState" + i,
					text: "Daemon Id: " + daemonId + ", " 
						  + "Status:" + daemonStatus + ", " 
						  + "Processed:" + daemonSuccessCount 
						  //TODO Fail doesn't implemented
						  /*+ "Success:" + daemonSuccessCount + ", " 
						  + "Fail:"+ daemonFailCount */
				}).appendTo("#daemonStates");
			}
		}
  	});
	console.log("End statistics");
	
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