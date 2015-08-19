<%@ include file="/WEB-INF/template/include.jsp" %>


<openmrs:require privilege="Manage synonym group" otherwise="/login.htm"
                 redirect="/module/chartsearch/managesynonymgroup.form"/>

<%@ include file="/WEB-INF/template/header.jsp" %>


<%@ include file="template/localHeader.jsp" %>


<script>
    var $ = jQuery;
    var count = 0;
    function addInput() {
        count += 1;
        var rowId = "rowAdd" + count;
        var boxName = "synonymNameAdd" + count;


        var newRow = "<p class=\"inputRow\" id=" + rowId + "> <input type=\"text\" name=" + boxName + ">" +
                " <button type=\"button\" class=\"deleteBtn\" onclick=\"deleteInput(" + rowId + ")\">Delete Synonym</button> </p>";

        $('.inputRow').last().after(newRow);

    }

    function deleteInput(row) {
        $(row).remove();
    }

</script>

<div id="synonymDiv">
    <h3><spring:message code="chartsearch.addSynonymGroupPage"/> </h3>


    <form id="ManageSynonymGroupForm" method="post">
        <button type="button" class="addBtn" onclick="addInput()">Add Synonym</button>
        <br> <br>
        <input type="text" name="groupName" value="${synonymGroup.groupName}" placeholder="Group name">

        <input type="checkbox" name="category" ${isCategory}><spring:message code="chartsearch.isCategory"/>
        <br>

        <c:forEach items="${synonymGroup.synonymSet}" var="synonym" varStatus="loop">
            <p class="inputRow" id="rowEdit${synonym.synonymId}">
                <input type="text" name="synonymNameEdit${synonym.synonymId}" value="${synonym.synonymName}">
                <button type="button" class="deleteBtn" onclick="deleteInput(rowEdit${synonym.synonymId})">
                	<spring:message code="chartsearch.deleteSynonym"/>
                </button>
            </p>
        </c:forEach>

        <p hidden class="inputRow"></p>

        <c:choose>
            <c:when test="${!empty synonymGroup.groupName}">
                <input type="text" name="save" id="groupName" value="${synonymGroup.groupName}" hidden>
            </c:when>
            <c:otherwise>
                <input type="text" name="save" value="save" hidden>
            </c:otherwise>
        </c:choose>

        <br>
        <input type="submit" name="button" id="saveGroup" value="Save Synonym Group">
        <input type="button" value='<spring:message code="general.cancel"/>'
               onclick="javascript:window.location='<openmrs:contextPath />/admin'"/>
        <c:choose>
            <c:when test="${!empty synonymGroup.groupName}">
                <input type="submit" id="deleteSynGrpBtn" name="button" value="Delete Synonym Group"/>
            </c:when>
        </c:choose>

    </form>

</div>


<%@ include file="/WEB-INF/template/footer.jsp" %>