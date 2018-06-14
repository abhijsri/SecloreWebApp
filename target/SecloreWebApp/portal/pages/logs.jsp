<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 


<jsp:include page="header.jsp"/>

<div style="clear:both;">
	<ol class="breadcrumb">
	  <li><a href="folderList.do"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Folder List</a></li>
	  <li><a href="fileList.do?folderId=<core:out value="${folderId}"/>"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> File List</a></li>
	  <li class="active"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> Logs</li>
	</ol>
</div>

<b>Original File Name : </b><span title='File ID : <core:out value="${file.id}"/>'><core:out value="${file.name}"/></span>
<br>
<br>
<table class="table table-striped">
    <thead>
      <tr>
        <th>ID</th>
        <th>Current File Name</th>
        <th>User</th>
        <th>Activity</th>
        <th>Client Type</th>
        <th>Time</th>
        <th>Authorized?</th>
        <th>Mode</th>
      </tr>
    </thead>
    <tbody>
	    <core:forEach items="${fileactivitylog}" var="item">
	  			<tr>
	  			<td><core:out value="${item.activityId}"/></td>
	  			<td><core:out value="${item.currentFileName}"/></td>
	  			<td><core:out value="${item.user}"/></td>
	  			<td >
	  				<core:if test="${item.activity == 1}" >Protect</core:if>
	  				<core:if test="${item.activity == 2}" >View</core:if>
	  				<core:if test="${item.activity == 3}" >Edit</core:if>
	  				<core:if test="${item.activity == 4}" >Print</core:if>
	  				<core:if test="${item.activity == 5}" >Redistribute</core:if>
	  				<core:if test="${item.activity == 6}" >Unprotect</core:if>
	  				<core:if test="${item.activity == 7}" >Transfer Ownership</core:if>
	  				<core:if test="${item.activity == 8}" >Accessed Remotely</core:if>
	  				<core:if test="${item.activity == 9}" >Access on VM</core:if>
	  				<core:if test="${item.activity == 10}" >Capture Screen</core:if>
	  				<core:if test="${item.activity == 11}" >Copy Data</core:if>
	  				<core:if test="${item.activity == 12}" >Macro</core:if>
	  				<core:if test="${item.activity == 13}" >View in Lite Viewer</core:if>
	  				<core:if test="${item.activity == 14}" >Create Offline Package</core:if>
	  			
	  			</td>
	  			<td><core:out value="${item.client}"/></td>
	  			<td><core:out value="${item.timestamp}"/></td>
	  			<td style="text-align:center">
	  				<core:if test="${item.authorized == 1}" ><span style = "color:green;">Yes</span></core:if>
	  				<core:if test="${item.authorized == 0}" ><span style = "color:red;">No</span></core:if>
	  			</td>
	  			<td>
	  				<core:if test="${item.mode == 1}" >Online</core:if>
	  				<core:if test="${item.mode == 0}" >Offline</core:if>
	  			</td>
	  			</tr>
		</core:forEach>
    </tbody>
</table>
<jsp:include page="message.jsp"/>

<div class="logs">
	
</div>

<jsp:include page="footer.jsp"/>
