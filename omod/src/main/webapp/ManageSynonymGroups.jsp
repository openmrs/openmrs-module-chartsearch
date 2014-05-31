<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Synonym Groups" otherwise="/login.htm"
                 redirect="/module/chartsearch/managesynonymgroups.form"/>

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>


<div id="manageDiv">
    <h3>Manage Synonym Groups</h3>
    <a href="managesynonymgroup.form">Add Synonym Group</a>
    <br>
    <br>
    <b class="boxHeader">Current Synonym Groups</b>
    <table>
        <tbody>
        <tr>
            <th> Group Name</th>
            <th> Is Category</th>
            <th> Synonyms</th>
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