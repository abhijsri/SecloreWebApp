<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    

<jsp:include page="header.jsp"/>

<div style="text-align:center;">
	<p style="color:#666;font-size:18px;"></p>
</div>

<div style="text-align:center;color:#a94442;margin-top:10px;">
	<core:if test="${ERROR_MESSAGE ne null and fn:length( fn:trim(ERROR_MESSAGE) ) > 0}">
		<p><core:out value="${ERROR_MESSAGE}"/></p>
		<core:remove var="ERROR_MESSAGE" scope="session"/>
	</core:if>
	<core:if test="${SUCCESS_MESSAGE ne null and fn:length( fn:trim(SUCCESS_MESSAGE) ) > 0}">
		<p><core:out value="${SUCCESS_MESSAGE}"/></p>
		<core:remove var="SUCCESS_MESSAGE" scope="session"/>
	</core:if>
</div>


<div class="getstarted-box">
	<a class="redirect_link" href="RedirectToPS"> Click here to login </a><span>&nbsp;</span>
</div>

<jsp:include page="footer.jsp"/>
