<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    

<jsp:include page="header.jsp"/>

<div style="clear:both;">
	<ol class="breadcrumb">
		<li class="active"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Folder List</li>
		<li><a href="createFolder.do"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create New Folder</a></li>
	</ol>
</div>

<jsp:include page="message.jsp"/>

<div class="folder_list">	
	
	<div class="title">
		<p><b>Folder List</b></p>
	</div>
	
	<div class="table_outer">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Sr. No.</th>
					<th>Name</th>
					<th>IRM Enabled</th>
					<th>HTML Wrapping</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<core:if test="${folderList ne null}">
					<core:forEach items="${folderList}" var="folder" varStatus="counter">
						<tr>
							<td>${counter.count}</td>
							<td>
								<span class="glyphicon glyphicon-folder-open" aria-hidden="true" style="color:#337ab7;"></span>&nbsp;
								<a href="fileList.do?folderId=<core:out value="${folder.id}"/>" title="View files">
									<core:out value="${folder.name}"/>
									<%-- <span style="font-size:10px;"><core:out value="(${folder.id})"/></span> --%>
								</a>
								
							</td>
							<td>
								<core:choose>
									<core:when test="${folder.irmEnabled eq true}">
										<span class="glyphicon glyphicon-check" aria-hidden="true" style="color:#337ab7;" title="IRM Enabled"></span>
									</core:when>
									<core:otherwise>
										<span class="glyphicon glyphicon-unchecked" aria-hidden="true" style="color:#337ab7;" title="IRM not Enabled"></span>
									</core:otherwise>
								</core:choose> 
							</td>
							<td>
								<core:choose>
									<core:when test="${folder.htmlWrapEnabled eq true}">
										<span class="glyphicon glyphicon-check" aria-hidden="true" style="color:#337ab7;" title="IRM Enabled"></span>
									</core:when>
									<core:otherwise>
										<span class="glyphicon glyphicon-unchecked" aria-hidden="true" style="color:#337ab7;" title="IRM not Enabled"></span>
									</core:otherwise>
								</core:choose> 
							</td>
							
							<td> 
								<a href="editFolder.do?folderId=<core:out value="${folder.id}"/>" title="Edit the foler">
									<span class="glyphicon glyphicon-edit" aria-hidden="true" style="color:#337ab7;"></span>
								</a>
							 </td>
							<td>
								 <a href="deleteFolder.do?folderId=<core:out value="${folder.id}"/>" title="Delete the folder">
									<span class="glyphicon glyphicon-remove" aria-hidden="true" style="color:red;"></span>
								 </a>
							 </td>
						</tr>
					</core:forEach>
				</core:if>
			</tbody>
		</table>
	</div> <!--  CLOSED table_outer  -->
	
</div>

<jsp:include page="footer.jsp"/>