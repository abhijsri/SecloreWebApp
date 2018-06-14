<%@page import="com.seclore.sample.dms.util.LoggerUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ page isErrorPage="true" %>
<jsp:include page="header.jsp"/>

	<%LoggerUtil.logError("Server Error:"+exception.getMessage(), exception);%>
	<h3>
		The server encountered an internal error and was unable to complete your request.
	</h3>
	<h4>
		More information about this error may be available in the application error log.
	</h4>
	
	<br/>
	<a href="folderList.do">Back to Home</a>
	
<jsp:include page="footer.jsp"/>