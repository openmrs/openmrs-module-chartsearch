<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:htmlInclude file="/dwr/interface/DWRCommands.js"/>

<%-- <openmrs:require privilege="Manage Global Properties" otherwise="/login.htm" redirect="/module/webservices/rest/settings.form" />--%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<script>

$j(document).ready(function() {
	$j('#patientInfoBtn').click(getPatientInfo);
});

function getPatientInfo(){
	DWRCommands.getPatientInfo($j("#patientId").val(), function(patientInfo) { 
	var time = patientInfo.lastIndexTime.toString();
    $j("#patientInfoResult").text("Last indexed time: " + time); 
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