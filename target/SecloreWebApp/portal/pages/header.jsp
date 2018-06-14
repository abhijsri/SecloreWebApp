<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My File Share</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="portal/bootstrap-3.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="portal/bootstrap-3.3.1/css/bootstrap-theme.min.css">
<!-- Our Application CSS must be after bootstrap CSS  -->
<link rel="stylesheet" href="portal/css/style.css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="portal/js/jquery.min-1.11.1.js"></script>
<script src="portal/bootstrap-3.3.1/js/bootstrap.min.js"></script>
<script src="portal/js/common.js" type="text/javascript"></script>

<body>

<div class="wrapper">  <!-- CLOSEd in footer.jsp -->

	<div class="header">
		
		<img alt=" " src="portal/images/seclore-header.png">
		<div class="header_inner">
			<div class="app_name">
				<span></span>
			</div>
			 <core:if test="${ext_id ne null and fn:length( fn:trim(ext_id) ) > 0 }">
				<div class="user_logout">
					<span><span class="glyphicon glyphicon-user" aria-hidden="true"></span> <core:out value="${name}"/></span> |
					<span><a href="logout" title="Logout"><span style="color:#146EA2;" class="glyphicon glyphicon-log-out" aria-hidden="true"></span></a></span>
				</div>
			</core:if>
		</div>
		
	</div>
	
	<div class="container"> <!-- CLOSEd in footer.jsp -->