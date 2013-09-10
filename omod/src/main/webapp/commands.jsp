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
</script>

<div class="boxHeader">Patient info (state and last index time)</div>

<div class="box">
	Patient id: <input type="text" name="patientId" id="patientId"/>
	<input type="button" id="patientInfoBtn" value="Get patient info"/>
	<br />
	<span id="patientInfoResult"></span>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>