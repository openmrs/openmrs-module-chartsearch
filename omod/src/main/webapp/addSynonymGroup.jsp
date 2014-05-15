<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Add synonym Groups" otherwise="/login.htm" redirect="/module/chartsearch/addsynonymgroup.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>


<script>
    var $=jQuery;
    var count =2;
    var boxName = 0;
    function addInput() {
        var boxName="synonym"+count;
        var checkboxName="category"+count;
        var rowId="row"+count;
        var deleteRowId="row"+(count-1);

        var newRow = "<p class=\"inputRow\" id=" + rowId + "> <input type=\"text\" name=" + boxName + ">" +
                "<button type=\"button\" class=\"addBtn\" onclick=\"addInput()\">Add</button> </p>";

        var deleteBtn = "<button type=\"button\" class=\"deleteBtn\" onclick=\"deleteInput(" + deleteRowId + ")\">Delete</button>";
        $('.addBtn').last().replaceWith(deleteBtn);
        $('.inputRow').last().after(newRow);
        count += 1;
    }

    function deleteInput(row) {
        $(row).remove();
    }

</script>

<div id="synonymDiv">
    <h3> Add synonym group page </h3>

    <form id="addSynonymGroupForm" method="post">
        <input type="text" name="groupName" placeholder="Group name">

        <input type="checkbox" name="category" value="true"> Category
        <br>
        <p class="inputRow" id="row1">
            <input type="text" name="synonym1">

            <button type="button" class="addBtn" onclick="addInput()">Add</button>
        </p>

        <br>
        <input type="submit" value="Submit">
        <input type="reset" value="Clear">
        <input type="button" value='<spring:message code="general.cancel"/>' onclick="javascript:window.location='<openmrs:contextPath />/admin'" />
    </form>

</div>





<%@ include file="/WEB-INF/template/footer.jsp"%>