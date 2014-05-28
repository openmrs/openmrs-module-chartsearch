<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Register Categories" otherwise="/login.htm" redirect="/module/chartsearch/createCategories.form" />

<h2><spring:message code="chartsearch.manage.registerCategories" /></h2>


<style>

#categories_box .category_details, #categories_box .category_members_details {
	float: left;
	margin-right:5px;
	margin-bottom:5px;
} 

.category_details {
	border-radius: 8px;
	border: 1px solid #36b0d9;
	padding:5px 5px 5px 5px;
	height:170px;
}

.category_members_details {
	border-radius: 8px;
	border: 1px solid #36b0d9;
	padding: 5px 5px 5px 10px;
	height:170px;
}

.category_details p , .category_members_details p {
	text-align: center;
}

</style>

<div class="boxHeader"><spring:message code="chartsearch.manage.registerNewCategories" /></div>
<div class="box">
	<form method="post">
	    <table>
	        <tr>
	            <td>
	                <table>
	                    <tr><td><b>First Category</b></td></tr>
	                    <tr><td>Category Name:</td><td><input type="text" name="category_name" placeholder="Labs"></td></tr>
	                </table>
	            </td>
	            <td>
	                <table>
	                    <tr><b>Add Members of the first Category</b></tr>
	                    <tr><td>all observations of concept:</td><td><input type="text" name="category_name" placeholder="X"></td></tr>
	                    <tr><td>all observations groups of concept set:</td><td><input type="text" name="category_name" placeholder="Y"></td></tr>
	                    <tr><td>all forms of ID:</td><td><input type="text" name="category_name" placeholder="Z"></td></tr>
	                    <tr><td>text search for the term:</td><td><input type="text" name="category_name" placeholder="ABC"></td></tr>
	                </table>
	            </td>
	            </td>
	        </tr>
	    </table>
	    <hr />
	    <table>
	        <tr>
	            <td>
	                <table>
	                    <tr><td><b>Second Category</b></td></tr>
	                    <tr><td>Category Name:</td><td><input type="text" name="category_name" placeholder="Reports"></td></tr>
	                </table>
	            </td>
	            <td>
	                <table>
	                    <tr><b>Add Members of the Second Category</b></tr>
	                    <tr><td>all observations of concept:</td><td><input type="text" name="category_name" placeholder="X"></td></tr>
	                    <tr><td>all observations groups of concept set:</td><td><input type="text" name="category_name" placeholder="Y"></td></tr>
	                    <tr><td>all forms of ID:</td><td><input type="text" name="category_name" placeholder="Z"></td></tr>
	                    <tr><td>text search for the term:</td><td><input type="text" name="category_name" placeholder="ABC"></td></tr>
	                </table>
	            </td>
	        </tr>
	    </table>
	    <hr />
	    <input type="submit" class="submit_category" value="Register Categories">
	</form>
</div>

<div class="boxHeader"><spring:message code="chartsearch.manage.displayExistingCategories" /></div>
<div class="box">
	
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>