<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Synonym Groups" otherwise="/login.htm"
                 redirect="/module/chartsearch/managesynonymgroups.form"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>


<div id="manageDiv">
    <h3><spring:message code="chartsearch.manage.managesynonymgroups" /></h3>
    <a href="managesynonymgroup.form"><spring:message code="chartsearch.addSynonymGroup" /></a>
    <br>
    <br>
    <b class="boxHeader"><spring:message code="chartsearch.currentSynonymGroups" /></b>
    <table>
        <tbody>
        <tr>
            <th><spring:message code="chartsearch.groupName" /></th>
            <th><spring:message code="chartsearch.isCategory" /></th>
            <th><spring:message code="chartsearch.synonyms" /></th>
        </tr>


        <form name="manageSynonymGroupsForm" method="get">
            <c:forEach items="${synonymGroups}" var="synonymGroup">
                <tr>
                    <td>
                        <a href=managesynonymgroup.form?synonymGroupId=${synonymGroup.group_id}> ${synonymGroup.groupName}</a>
                    </td>
                    <td>${synonymGroup.isCategory}</td>
                    <td>
                        <c:forEach items="${synonymGroup.synonymSet}" var="synonym" varStatus="loop">
                            ${synonym.synonymName}
                            ${!loop.last ?',' : ''}
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </form>

        </tbody>
    </table>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>