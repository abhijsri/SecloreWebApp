<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    

<jsp:include page="header.jsp"/>

<div style="clear:both;">
	<ol class="breadcrumb">
	  <li><a href="folderList.do"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Folder List</a></li>
	  <li class="active"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> Folder</li>
	</ol>
</div>

<jsp:include page="message.jsp"/>

<div class="folder">

	<div class="form_outer">
		<form action="saveFolder.do" method="POST" class="folder_form form-horizontal" role="form">
			<div class="form-group">
			    <label class="col-sm-2 control-label">&nbsp;</label>
			    <div class="col-sm-10">
			    	<core:choose>
			    		<core:when test="${folder.id ne null and fn:length( fn:trim(folder.id) ) > 0 }">
							<span style="font-weight:bold;">Update Folder</span>
						</core:when>
						<core:otherwise>
							<span style="font-weight:bold;">Create New Folder</span>
						</core:otherwise>
			    	</core:choose>
			    </div>
			</div>
		
			<input type="hidden" name="id" value="<core:out value="${folder.id}"/>" />
			
			<div class="form-group">
			    <label for="name" class="col-sm-2 control-label">Name:</label>
			    <div class="col-sm-10">
			      <input type="text" name="name" value="<core:out value="${folder.name}"/>" 
			      	class="form-control input" id="name" placeholder="Enter folder name" />
			    </div>
			</div>
  
			
			<div class="form-group" onclick="irmEnableClicked()">
			    <label for="irmEnabled" class="col-sm-2 control-label">IRM Enabled:</label>
				<div class="col-sm-10">
					<core:choose>
						<core:when test="${folder.irmEnabled eq true}">
							<input type="checkbox" name="irmEnabled" value="true" checked="checked" 
								class="checkbox" id="irmEnabled"/>
						</core:when>
						<core:otherwise>
							<input type="checkbox" name="irmEnabled" value="true" 
								class="checkbox" id="irmEnabled"/>
						</core:otherwise>
					</core:choose>
				</div>
			</div>
			<div class="form-group">
			    <label for="htmlWrapEnabled" class="col-sm-2 control-label">HTML Wrapping:</label>
				<div class="col-sm-10">
					<core:choose>
						<core:when test="${folder.irmEnabled eq true}">
							<core:choose>
								<core:when test="${folder.htmlWrapEnabled eq true}">
									<input type="checkbox" name="htmlWrapEnabled" value="true" checked="checked" 
										class="checkbox" id="htmlWrapEnabled"/>
								</core:when>
								<core:otherwise>
									<input type="checkbox" name="htmlWrapEnabled" value="true" 
										class="checkbox" id="htmlWrapEnabled"/>
								</core:otherwise>
							</core:choose>
						</core:when>
						<core:otherwise>
							<input type="checkbox" name="htmlWrapEnabled" value="true" 
								class="checkbox" id="htmlWrapEnabled" disabled/>
						</core:otherwise>
					</core:choose>
				</div>
			</div>
			
			<div class="form-group">
    			<div class="col-sm-offset-2 col-sm-10">
    				<input type="submit" class="btn btn-primary" value="Save"/>
      				<a style="margin-left:20px;" href="folderList.do" class="btn btn-default">Cancel</a>
    			</div>
  			</div>
  
		</form>
	</div>
	
</div>
<script>
	function irmEnableClicked()
	{
		if(document.getElementById('irmEnabled').checked == true)
		{
			document.getElementById('htmlWrapEnabled').disabled = false;
		}
		else
		{
			document.getElementById('htmlWrapEnabled').disabled = true;
		}	
	}
</script>

<jsp:include page="footer.jsp"/>