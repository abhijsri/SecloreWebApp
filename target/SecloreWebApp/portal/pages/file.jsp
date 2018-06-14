<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    

<jsp:include page="header.jsp"/>

<div style="clear:both;">
	<ol class="breadcrumb">
	  <li><a href="folderList.do"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Folder List</a></li>
	  <li><a href="fileList.do?folderId=<core:out value="${folderId}"/>"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> File List</a></li>
	  <li class="active"><span class="glyphicon glyphicon-file" aria-hidden="true"></span> File</li>
	</ol>
</div>

<jsp:include page="message.jsp"/>

<div class="file">

	<div class="form_outer">
		<form action="saveFile.do?folderId=<core:out value="${folderId}"/>" method="POST" class="file_form form-horizontal" role="form"
				 enctype="multipart/form-data">
				 
			<div class="form-group">
			    <label class="col-sm-2 control-label">&nbsp;</label>
			    <div class="col-sm-10">
			    	<core:choose>
			    		<core:when test="${file.id ne null and fn:length( fn:trim(file.id) ) > 0 }">
							<span style="font-weight:bold;">Update File</span>
						</core:when>
						<core:otherwise>
							<span style="font-weight:bold;">Add New File</span>
						</core:otherwise>
			    	</core:choose>
			    </div>
			</div>
		
			<input type="hidden" name="usersRightsJson" id="usersRightsField" value="<core:out value="${usersRightsJson}"/>"/>
			<input type="hidden" name="fileId" value="<core:out value="${file.id}"/>" />
			
			<!-- Display Folder name -->
			<div class="form-group">
			    <label for="folderName" class="col-sm-2 control-label">Folder:</label>
			    <div class="col-sm-10">
			    	<div class="readonlyElement">
			    		<core:out value="${folderName}"/> <%-- <span style="font-size:11px;">(<core:out value="${folderId}"/>)</span> --%>
			    	</div>
			    </div>
			</div>
			
			<!-- File display of File Select field -->
			<div class="form-group">
			    <core:choose>
		    		<core:when test="${file.id ne null and fn:length( fn:trim(file.id) ) > 0 }">
						<label for="fileName" class="col-sm-2 control-label">File:</label>
					    <div class="col-sm-10">
					    	<div class="readonlyElement">
				    			<core:out value="${file.name}"/> <%-- <span style="font-size:11px;">(<core:out value="${file.id}"/>)</span> --%>
			      			</div>
					    </div>
					</core:when>
					<core:otherwise>
						<label for="file" class="col-sm-2 control-label">Select File:</label>
					    <div class="col-sm-10">
					      	<input type="file" name="file" id="file" placeholder="Select file name" title="Upload the file upto the size of 50MB"/>
					    </div>						
					</core:otherwise>
			    </core:choose>
			</div>
			
			<!-- File Owner -->
			<div class="form-group">
			    <label for="ownerEmailId" class="col-sm-2 control-label">Select Owner:</label>
				<div class="col-sm-10">
					<select name="ownerEmaiId" class="form-control input" id="ownerEmaiId" title="Enter owner email id for this folder">
						<option value="">Default</option>
						<core:forEach items="${usersMap}" var="entry">
							<option value="<core:out value="${entry.key}"/>" title="<core:out value="${entry.value.name}"/>"
								<core:out value="${file.owner.emailId eq entry.key?'selected=selected':''}"/> >
									<core:out value="${entry.key}"/>
							</option>
						</core:forEach>
					</select>
			    </div>
			</div>
			
			<!-- File Classification -->
			<div class="form-group">
			    <label for="classification" class="col-sm-2 control-label">Select Classification:</label>
				<div class="col-sm-10">
			      <select name="classification" class="form-control input" id="classification" title="Select classification for the files">
						<option value="">Default</option>
						<core:forEach items="${classifications}" var="classification">
							<option value="<core:out value="${classification.id}"/>" title="<core:out value="${classification.description}"/>"
								<core:out value="${file.classification.id eq classification.id?'selected=selected':''}"/> >
									<core:out value="${classification.name}"/>
							</option>
						</core:forEach>
				  </select>
			    </div>
			</div>
			
			<br/>
			<div class="form-group">
			    <label class="col-sm-2 control-label">Uses Rights:</label>
			    
				<div class="col-sm-10 form-inline">
				  <!-- <label for="emailId">Email ID:</label> -->
				  <div class="input-group">
				  	<div class="input-group-addon">Email Id</div>
				  	<input type="text" name="emailId" class="form-control" id="emailId" placeholder="Enter user's email Id" title="Enter user's email Id">
				  </div>
				  <span id="addUserBtn" class="btn btn-info">Add</span>
				  <span id="InProgressEl" style="display:none;"><img src="portal/images/ajax-loader.gif" alt="" style="width:25px;height:25px;"></span>
				  <span class="error" id="AddUserErrorMsg"></span>
				  <span class="success" id="AddUserSuccessMsg"></span>
				  <br/><br/>
				  
			      <table class="table table-bordered">
			      	<thead>
						<tr><th>Users</th><th>Rights</th><th>Action</th></tr>
					</thead>			      
					<tbody id="users_rights">
						<core:forEach items="${selectedUsersRights}" var="userRight">
							<tr class="user_right" userid="${userRight.userId}" usagerights="${userRight.usageRights}">
							
								<%-- <td title="${userRight.userId}"> <core:out value="${usersMap[userRight.userId].name}" /> </td> --%>
								<%-- To make consistent display email id at the place of name --%>
								<td title="<core:out value="${usersMap[userRight.userId].name}"/>">
									 <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
									 <core:out value="${userRight.userId}" />
								</td>
								
								<td>
									<select name="rights" class="select_right form-control">
										<core:forEach items="${rightsMap}" var="entry">
											<option value="<core:out value="${entry.key}"/>" ${entry.key==userRight.usageRights?'selected':'' }>
												<core:out value="${entry.value.text}"/>
											</option>
										</core:forEach>
									</select>
								 	<%-- <core:out value="${rightsMap[userRight.usageRights].text}" /> --%>
								</td>
								<td>
									<span class="removeRight glyphicon glyphicon-remove" aria-hidden="true" style="color:red;cursor:pointer;" title="remove"></span>
								</td>
							</tr>
						</core:forEach>
						<%--
						<tr id="select_user_right">
							<td>
								<select name="user" class="form-control" id="select_user">
									<option value="">--Select User--</option>
									<core:forEach items="${usersMap}" var="entry">
										<option value="<core:out value="${entry.key}"/>"><core:out value="${entry.value.name}"/></option>
									</core:forEach>
								</select>
							</td>
							<td>
								<select name="rights" class="form-control" id="select_right">
									<option value="">--Select Rights--</option>
									<core:forEach items="${rightsMap}" var="entry">
										<option value="<core:out value="${entry.key}"/>"><core:out value="${entry.value.text}"/></option>
									</core:forEach>
								</select>
							</td>
							<td>
								<span id="addAction" class="btn btn-info btn-xs">Add</span>
							</td>
						</tr>
						--%>
					</tbody>
			      </table>
			      
			      <div id="hiddenSelectEl" style="display:none;">
			      		<select name="rights" class="select_right form-control">
							<core:forEach items="${rightsMap}" var="entry">
								<option value="<core:out value="${entry.key}"/>"><core:out value="${entry.value.text}"/></option>
							</core:forEach>
						</select>
					</div>
			    </div>
			</div>
			
			<div class="form-group">
    			<div class="col-sm-offset-2 col-sm-10">
    				<input type="submit" id="fileSaveBtn" value="Save" class="btn btn-primary" />
    				<a style="margin-left:20px;" href="fileList.do?folderId=<core:out value="${folderId}"/>" class="btn btn-default">Cancel</a>
    			</div>
  			</div>
  			
		</form>
	
	</div>

	
</div> <!-- CLOSED file -->
<script src="portal/js/file.js" type="text/javascript" ></script>
<jsp:include page="footer.jsp"/>