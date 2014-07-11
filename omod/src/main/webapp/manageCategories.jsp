<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<script type="text/javascript">
    var jq = jQuery;
    
    jq( document ).ready(function() {
    alert("dsdsds");
		jq('#searchBtn').click(function(event) {
			submitChartSearchFormWithAjax();
			return false;
		});
    });
    
	function submitChartSearchFormWithAjax() {
		
		var jsonData = "{  patientId: 2271,  phrase: \"blood pressure\",  [ {categories: question}, {categories: test}, {categories: finding}, {categories: misc} ]  }";

		alert(jsonData);
		jq.ajax({
			type: 'POST',
			dataType: 'json',
			contentType: "application/json; charset=utf-8",
			url: "/module/chartsearch/manageCategories.form",
			data:JSON.stringify(jsonData),
			success: function(data) {
			
				alert(data);
				jq("#getJSONFromServer").html(data);
			},
			error: function(jqXHR, textStatus, errorThrown) {
			  alert("Error occurred!!! " + errorThrown);
			}
		});
	}
</script>

<openmrs:require privilege="Register Categories" otherwise="/login.htm"
	redirect="/module/chartsearch/manageCategories.form" />

<h2 style="text-align: center;">
	<spring:message code="chartsearch.manage.manageCategories" />
</h2>

<div class="boxHeader">
	<spring:message code="chartsearch.manage.registerNewCategory" />
</div>
<div class="box">
	<form method="POST" id="chart-search-form-submit">
		<input type="submit" id="searchBtn" onclick="submitChartSearchFormWithAjax();"/>
	</form>
	<div id="getJSONFromServer"></div>
</div>

<div class="boxHeader">
	<spring:message code="chartsearch.manage.existingCategories" />
</div>
<div class="box"></div>

<%@ include file="/WEB-INF/template/footer.jsp"%>