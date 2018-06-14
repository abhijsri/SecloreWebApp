<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    

<jsp:include page="header.jsp"/>

<jsp:include page="message.jsp"/>

<div style="clear:both;"></div>
<div class="panel panel-info">
  <div class="panel-heading">
    <h3 class="panel-title">Add New User</h3>
  </div>
  <div class="panel-body">
    	<form class="form-inline" method="post" action="addUser.do">
			<div class="form-group">
			  <label for="userId">EXT ID/Email ID:</label>
			  <input type="text" name="userId" class="form-control" id="userId" placeholder="Enter user's ext Id or Email Id">
			</div>
			<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div class="form-group">
			  <label for="name">Name:</label>
			  <input type="text" name="name" class="form-control" id="name" placeholder="Enter User name">
			</div>
			<input type="submit" value="Add" class="btn btn-primary">
		</form>
  </div>
</div>

<div style="clear:both;"></div>

<div class="user_list">	
	
	<div class="title">
		<p><b>User List</b></p>
	</div>
	
	<div class="table_outer">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Sr. No.</th>
					<th>Name (User ID)</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<core:if test="${users ne null}">
					<core:forEach items="${users}" var="user" varStatus="counter">
						<tr>
							<td>${counter.count}</td>
							<td>
								<span class="glyphicon glyphicon-user" aria-hidden="true" style="color:#337ab7;"></span>&nbsp;
								<core:out value="${user.name}"/>
								<span><core:out value="(${user.id})"/></span>
							</td>
							
							<td> 
								<a href="#editUser.do?userId=<core:out value="${user.id}"/>" title="Edit User Name" class="editUserBtn" userid="<core:out value="${user.id}"/>">
									<span class="glyphicon glyphicon-edit" aria-hidden="true" style="color:#337ab7;"></span>
								</a>
							 </td>
							<td>
								 <a href="deleteUser.do?userId=<core:out value="${user.id}"/>" title="Delete the user">
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
	$(document).ready(function(){
		$(".editUserBtn").on("click",function(){
			console.log($(this).attr("userid"));			
		});
	});

</script>
<jsp:include page="footer.jsp"/>