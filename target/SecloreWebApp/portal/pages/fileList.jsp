<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    

<jsp:include page="header.jsp"/>

<div style="clear:both;">
	<ol class="breadcrumb">
	  <li><a href="folderList.do"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Folder List</a></li>
	  <li class="active"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> File List</li>
	  <li ><a href="addFile.do?folderId=<core:out value="${folder.id}"/>"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add New File</a></li>
	</ol>
</div>
	
<jsp:include page="message.jsp"/>

<div class="file_list">

	<div class="title">
		<p><b>Folder:</b>  <span title='Folder ID : <core:out value="${folder.id}"/>'><core:out value="${folder.name}"/></span></p>
		<p><b>IRM Enabled:</b> <core:out value="${folder.irmEnabled?'Yes':'No'}"/></p>
		  
		<p style="margin-top:15px;"><b>File List</b></p>
	</div>
	
	<div class="table_outer">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Sr. No.</th>
					<th>Name</th>
					<th>Owner</th>
					<th>Classification</th>
					<core:if test="${folder.irmEnabled eq true}">
						<th>View Online</th>
					</core:if>
					<th>Download</th>
					<th>Edit</th>
					<th>View Logs</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<core:if test="${fileList ne null}">
					<core:forEach items="${fileList}" var="file" varStatus="counter">
						<tr>
							<td>${counter.count}</td>
							<td title="File ID: <core:out value="${file.id}"/>">
								<span class="glyphicon glyphicon-file" aria-hidden="true" style="color:#337ab7;"></span>
								<core:out value="${file.name}"/>
								<%-- <span style="font-size:10px;"><core:out value="(${file.id})"/></span> --%>
							</td>
							
							<!-- File Owner-->
							<td>
								<core:choose>
									<core:when test="${file.owner.emailId ne null}">
										<div style="font-size:12px;clear:both;"><core:out value="${file.owner.name}"/></div>
										<div style="font-size:10px;margin-top:-4px;"><core:out value="(${file.owner.emailId})"/></div>
									</core:when>
									<core:otherwise>
										<core:out value="Default"></core:out>
									</core:otherwise>
								</core:choose> 
							</td>
							
							<!-- File Classification-->
							<td>
								<core:choose>
									<core:when test="${file.classification.name ne null}">
										<span title="<core:out value="${file.classification.description}"/>"><core:out value="${file.classification.name}"/></span>
										<span><core:out value="(${file.classification.id})"/></span>
									</core:when>
									<core:otherwise>
										<core:out value="Default"></core:out>
									</core:otherwise>
								</core:choose> 
							</td>
							  
							<!-- View File Online if folder if IRM Enabled-->
							<core:if test="${folder.irmEnabled eq true}">
								<td>
									<a href="viewFileOnline.do?folderId=<core:out value="${folder.id}"/>&fileId=<core:out value="${file.id}"/>" title="View the file online">
										<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
									</a>
								</td>
							</core:if>
							
							<!-- Download File -->
							<td> 
								<a href="downloadFile.do?folderId=<core:out value="${folder.id}"/>&fileId=<core:out value="${file.id}"/>" title="Download the file">
									<span class="glyphicon glyphicon-cloud-download" aria-hidden="true"></span>
								</a>
							 </td>
							
							<!-- Edit File -->
							<td>
								<a href="editFile.do?folderId=<core:out value="${folder.id}"/>&fileId=<core:out value="${file.id}"/>" title="Add/Edit users and rights on the file">
									<span class="glyphicon glyphicon-edit" aria-hidden="true" style="color:#337ab7;"></span>
								</a>
							</td>							
							
							<!-- View Logs -->
							<td>
								<a href="viewFileLogs.do?folderId=<core:out value="${folder.id}"/>&fileId=<core:out value="${file.id}"/>" title="View file log">
									<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
								</a>
							</td>
							
							<!-- Delete File -->
							<td>
								 <a href="deleteFile.do?folderId=<core:out value="${folder.id}"/>&fileId=<core:out value="${file.id}"/>" title="Delete the file">
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

<script type="text/javascript">
var PS_VIEW_FILE_URL = '<core:out value="${PS_VIEW_FILE_URL}"/>';

// Remove view url from session
<core:remove var="PS_VIEW_FILE_URL" scope="session"/>

if( PS_VIEW_FILE_URL != undefined && PS_VIEW_FILE_URL != null && PS_VIEW_FILE_URL.trim().length > 0 )
{
	var params = "toolbar=no, scrollbars=yes,resizable=yes,width="+screen.availWidth+",height="+screen.availHeight+"top=0, left=0,fullscreen=yes";
	var newWin = window.open(PS_VIEW_FILE_URL,'newWindow', params);
	if(newWin != undefined && newWin != null)
	{
		newWin.moveTo(0,0);
		newWin.resizeTo(screen.availWidth,screen.availHeight);
		newWin.focus();
	}
}
</script>

<jsp:include page="footer.jsp"/>