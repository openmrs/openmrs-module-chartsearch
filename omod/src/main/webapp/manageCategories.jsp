<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Register Categories" otherwise="/login.htm"
	redirect="/module/chartsearch/manageCategories.form" />

<h2 style="text-align: center;">
	<spring:message code="chartsearch.manage.manageCategories" />
</h2>

<div class="boxHeader">
	<spring:message code="chartsearch.manage.registerNewCategory" />
</div>
<div class="box">
</div>

<div class="boxHeader">
	<spring:message code="chartsearch.manage.existingCategories" />
</div>
<div class="box">
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>