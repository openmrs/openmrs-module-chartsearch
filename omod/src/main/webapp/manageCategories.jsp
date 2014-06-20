<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<script type="text/javascript" charset="utf-8">
	
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
	<form method="post">
		<table>
			<tr>
				<td><b>Category Name</b></td>
				<td><b>Facet Query</b></td>
				<td><b>Category Description</b></td>
			</tr>
			<tr>
				<td><input type="text" class="category_name"
					placeholder="E.g. 'Diagnoses'"></td>
				<td><textarea class="category_name"
						placeholder="E.g. 'concept_name:CD4% & patient_id:2271'"
						style="width: 400px; height: 37px;"></textarea></td>
				<td><textarea class="category_description"
						placeholder="description"
						style="width: 400px; height: 37px;"></textarea></td>
			</tr>
			<tr>
				<td><spring:message code="chartsearch.manage.facetFollow" /></td>
				<td><a href="#"><spring:message
							code="chartsearch.manage.facetDocumentation" /></a></td>
			</tr>
		</table>
		<input type="submit" class="submit_category" value="Register Category">
	</form>
</div>

<div class="boxHeader">
	<spring:message code="chartsearch.manage.editCategories" />
</div>
<div class="box">
	<form method="post">
		<table>
			<tr>
				<td><b>Select Category</b></td>
				<td><b>Category Name</b></td>
				<td><b>Facet Query</b></td>
				<td><b>Category Description</b></td>
			</tr>
			<tr>
				<td><u><em>View In-Built Categories</em></u></td>
			</tr>
			${inBuiltCategories}
			<tr>
				<td><u><em>Edit Custom Categories</em></u></td>
			</tr>
			${customCategories}
		</table>
		<input type="submit" class="submit_category" value="Update Selected">
		<input type="submit" class="submit_category" value="Delete Selected">
	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>