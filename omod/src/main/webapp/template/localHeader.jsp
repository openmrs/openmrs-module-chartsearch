<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<openmrs:hasPrivilege privilege="Manage Global Properties">
		<li
			<c:if test='<%=request.getRequestURI().contains("/settings")%>'>class="active"</c:if>>
			<a
			href="${pageContext.request.contextPath}/module/chartsearch/settings.form"><spring:message
					code="chartsearch.manage.settings" /></a>
		</li>
	</openmrs:hasPrivilege>

	<openmrs:hasPrivilege privilege="Run Chart Search commands">
		<li
			<c:if test='<%=request.getRequestURI().contains("/commands")%>'>class="active"</c:if>>
			<a
			href="${pageContext.request.contextPath}/module/chartsearch/commands.form"><spring:message
					code="chartsearch.manage.commands" /></a>
		</li>
	</openmrs:hasPrivilege>

	<!-- Add further links here -->



    <openmrs:hasPrivilege privilege="Manage Synonym Groups">
        <li
        <c:if test='<%=request.getRequestURI().contains("/managesynonymgroups")%>'>class="active"</c:if>>
        <a
                href="${pageContext.request.contextPath}/module/chartsearch/managesynonymgroups.form"><spring:message
                code="chartsearch.manage.managesynonymgroups" /></a>
        </li>
    </openmrs:hasPrivilege>
	
	<li
		<c:if test='<%=request.getRequestURI().contains("/manageCategories")%>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/chartsearch/manageCategories.form">
			<spring:message code="chartsearch.manage.manageCategories" />
		</a>
	</li>

</ul>
<h2>
	<spring:message code="chartsearch.title" />
</h2>
