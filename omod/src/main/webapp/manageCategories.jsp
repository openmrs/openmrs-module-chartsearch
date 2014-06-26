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
	<spring:message code="chartsearch.manage.existingCategories" />
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
				<td></td><td></td><td><u><em>View In-Built Categories</em></u></td><td></td>
			</tr>
			<tr>
			 	<td><input type='checkbox' disabled></td>
			 	<td><input type='text' value='Diagnoses' disabled></td>
			 	<td><textarea disabled style='width:400px; height:37px;'></textarea></td>
			 	<td><textarea placeholder='inbuilt categories' disabled style='width:400px; height:37px;'>Category item for filtering Diagnoses</textarea></td>
			 </tr>
			 <tr>
			 	<td><input type='checkbox' disabled></td>
			 	<td><input type='text' value='Orders' disabled></td>
			 	<td><textarea disabled style='width:400px; height:37px;'></textarea></td>
			 	<td><textarea placeholder='inbuilt categories' disabled style='width:400px; height:37px;'>Category item for filtering Orders</textarea></td>
			 </tr>
			 <tr>
			 	<td><input type='checkbox' disabled></td>
			 	<td><input type='text' value='Reports' disabled></td>
			 	<td><textarea disabled style='width:400px; height:37px;'></textarea></td>
			 	<td><textarea placeholder='inbuilt categories' disabled style='width:400px; height:37px;'>Category item for filtering Reports</textarea></td>
			 </tr>
			 <tr>
			 	<td><input type='checkbox' disabled></td>
			 	<td><input type='text' value='Vitals' disabled></td>
			 	<td><textarea disabled style='width:400px; height:37px;'></textarea></td>
			 	<td><textarea placeholder='inbuilt categories' disabled style='width:400px; height:37px;'>Category item for filtering Vitals</textarea></td>
			 </tr>
			<tr>
				<td></td><td></td><td><u><em>Edit Custom Categories</em></u></td><td></td>
			</tr>
			<tr>
			 	<td><input type='checkbox'></td>
			 	<td><input type='text' value='Ids: 1 To 4000'></td>
			 	<td><textarea style='width:400px; height:37px;'>patient_id:1 To 4000</textarea></td>
			 	<td><textarea placeholder='inbuilt categories' style='width:400px; height:37px;'>Patients whose Id's are from 1 to 4000</textarea></td>
			 </tr>
			 <tr>
			 	<td><input type='checkbox'></td>
			 	<td><input type='text' value='CD4'></td>
			 	<td><textarea style='width:400px; height:37px;'>concept_name:CD4%</textarea></td>
			 	<td><textarea placeholder='inbuilt categories' style='width:400px; height:37px;'>Concept name cd4</textarea></td>
			 </tr>
		</table>
		<input type="submit" class="submit_category" value="Update Selected">
		<input type="submit" class="submit_category" value="Delete Selected">
	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>