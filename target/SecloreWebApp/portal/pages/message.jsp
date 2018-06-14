<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="message_div" id="messageDiv">
	<core:if test="${ERROR_MESSAGE ne null and fn:length( fn:trim(ERROR_MESSAGE) ) > 0}">
		<div class="alert alert-danger" role="alert">
			<core:out value="${ERROR_MESSAGE}"/>
		</div>
		<core:remove var="ERROR_MESSAGE" scope="session"/>
	</core:if>
	
	<core:if test="${SUCCESS_MESSAGE ne null and fn:length( fn:trim(SUCCESS_MESSAGE) ) > 0}">
		<div class="alert alert-success" role="alert">
			<core:out value="${SUCCESS_MESSAGE}"/>
		</div>
		<core:remove var="SUCCESS_MESSAGE" scope="session"/>
	</core:if>
</div>
