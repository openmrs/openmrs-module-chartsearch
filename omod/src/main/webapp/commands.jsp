<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:htmlInclude file="/dwr/interface/DWRCommands.js" />

<openmrs:require privilege="Run Chart Search commands" otherwise="/login.htm" redirect="/module/chartsearch/commands.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/chartsearch/js/commands.js"/>

<%@ include file="template/localHeader.jsp"%>

<h2 style="text-align: center;">
	<spring:message code="chartsearch.manage.commands.title" />
</h2>

<div class="boxHeader"><spring:message code="chartsearch.commands.patientInfo"/></div>
<div class="box">
	<input type="button" id="patientInfoBtn" value="Get patient info" />
	<table>
		<tr>
			<td><spring:message code="chartsearch.commands.patientId"/></td>
			<td><input type="text" name="patientId" id="patientId" /></td>
		</tr>
	</table>	
	<span id="patientInfoResult"></span>
</div>
<div class="boxHeader"><spring:message code="chartsearch.commands.statistics"/></div>
<div class="box">
	<input type="button" id="statisticsBtn" value="Get statistics" /> <br />
	<span id="statisticsResult"></span>
</div>
<div class="boxHeader"><spring:message code="chartsearch.commands.clearIndex"/></div>
<div class="box">
	<input type="button" id="clearBtn" value="Clear index" />
	<table>
		<tr>
			<td><spring:message code="chartsearch.commands.clearStrategy"/></td>
			<td>
				<form:select id="clearStrategy" path="clearStrategies" >
					<form:options items="${clearStrategies}" />
				</form:select>
			</td>
		</tr>
		<tr>
			<td><spring:message code="chartsearch.commands.patientIdCommaDelimited"/></td>
			<td><input type="text" name="patientIds" id="patientIds" /></td>
		</tr>
		<tr>
			<td><spring:message code="chartsearch.commands.countMaxPaients"/></td>
			<td><input type="text" name="maxPatients" id="maxPatients" /></td>
		</tr>
		<tr>
			<td><spring:message code="chartsearch.commands.maxTime"/></td>
			<td><input type="text" name="ago" id="ago" /> </td>
		</tr>
	</table>
	<span id="clearResult"></span>
</div>
<div class="boxHeader"><spring:message code="chartsearch.commands.daemonManagement"/></div>
<div class="box">
	<input type="button" id="startDaemonBtn" value="Change daemons count" />
	<table>
		<tr>
			<td><spring:message code="chartsearch.commands.daemonsCount"/></td>
			<td><input type="text" name="daemonsCount" id="daemonsCount" /></td>
		</tr>
	</table>	
	<span id="daemonsManagementResult"></span>
</div>
<div class="boxHeader"><spring:message code="chartsearch.indexing.patientData.ui.index" /></div>
<div class="box">
	<input type="text" id="numberOfResults" placeholder="Number of Documents" size="22" /><br />
	<input type="button" id="indexPatientData" value="Index Patient Data" onclick="indexAllPatientData();" /><br />
	<div id="index_patientData_info"></div>
</div>

<script>

function indexAllPatientData() {
	document.getElementById("index_patientData_info").innerHTML = '<spring:message code="chartsearch.indexing.patientData.ui.starting" /> <br />';
	window.setInterval(function(){getIndexingProgress();}, 2000);
	var numberOfDocs = $j("#numberOfResults").val();
	DWRCommands.indexAllPatientData(numberOfDocs, function() {
		document.getElementById("index_patientData_info").innerHTML = '<b>' + numberOfDocs + ' <spring:message code="chartsearch.indexing.patientData.ui.finishedIndexingData" />' + '</b>';
	});
}

function getIndexingProgress() {
	DWRCommands.getIndexingProgressInfo(function(data) {
		if (data == "finished") {
			jQuery( "#indexPatientData").unbind( "click");
		} else {
			document.getElementById("index_patientData_info").innerHTML = data + '<br />';
		}
	});
}
</script>
 
<%@ include file="/WEB-INF/template/footer.jsp"%>