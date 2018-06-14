$(document).ready(function(){
	// **** File related script START **** //

	var usersRightsArray = new Array();
	var tempUserArray = new Array();
	
	function UsersRights(pUsersRights)
	{
		this.usersRights = pUsersRights;
	}
	
	function UserRight(pUserId, pUsageRights)
	{
		this.userId = pUserId;
		this.usageRights = pUsageRights;
	}
	
	// On load prepare existing users with their usage rights
	$("#users_rights tr.user_right").each(function() {
		var tdFirst = $(this).find("td:first");
		var userId = $(this).attr("userid");
		if( tdFirst == null || tdFirst.text() == null || tdFirst.text().trim() == "" )
		{
			tdFirst.text(userId);
		}
		var usageRights = $(this).attr("usagerights");
		tempUserArray.push(userId);
		var ur = new UserRight(userId, usageRights);
		usersRightsArray.push(ur);
		
	});
	
	
	$("#addUserBtn").click(function(){
		// Clear the old messages
		$("#emailId").css("border-color","#ccc");
		$("#AddUserErrorMsg").text("");
		$("#AddUserSuccessMsg").text("");
		
		var userId = $("#emailId").val();
		if ( userId == $("#emailId").attr("placeholder") )
        {
			userId = "";
        }
		
		if( userId == null || userId.trim().length == 0 )
		{
			$("#emailId").css("border-color","red");
			$("#AddUserErrorMsg").text("Please enter the user's email id");
			return;
		}
		
		userId = userId.trim();
		// check whether user already added or not
		if( tempUserArray.indexOf(userId) > -1)
		{
			$("#emailId").css("border-color","red");
			$("#AddUserErrorMsg").text("User already exists");
			return;
		}
		
		$.ajax({
			type:'POST',
			url:'searchUser.do',
			data:{emailId:userId},
			dataType:'json',
			beforeSend:function(){
				$("#InProgressEl").show();
				$("#addUserBtn").attr("disabled","disabled");
			},
		})
		.done(function(jsonData) {
			if( jsonData == undefined || jsonData == null )
			{
				$("#AddUserErrorMsg").text("Invalid response received!");
				return;
			}
			if( jsonData.status == -1)
			{
				$("#emailId").css("border-color","red");
				$("#AddUserErrorMsg").text(jsonData.errorMessage);
				return;
			}
			
			var name = jsonData.name;

			// To make consistent display email id at the place of name //
			var spanIcon = $('<span>').attr({"class":"glyphicon glyphicon-user","aria-hidden":"true"});
			var tdU = $("<td>").append(spanIcon).append(document.createTextNode(" "+userId)).attr("title",name);
			
			var defaultRights = $("#hiddenSelectEl .select_right").val();
			
			var rightId = defaultRights;
			var selectHtml = $("#hiddenSelectEl").html();
			
			var tdR = $("<td>").html(selectHtml);
			
			var span = $('<span>').attr({"title":"remove","class":"removeRight glyphicon glyphicon-remove","aria-hidden":"true","style":"color:red;cursor:pointer;"});
			var tdA = $("<td>").append(span);
			var tr = $("<tr>").append(tdU).append(tdR).append(tdA).attr({"userid":userId,"usagerights":rightId});
			
			$("#users_rights").append(tr);
			$("#emailId").val('');
			
			// Add user to Owner list if not exist
			if( $("#ownerEmaiId option[value='"+userId+"']").length == 0 )
			{
				$("#ownerEmaiId").append($('<option>', {
				    value: userId,
				    text: userId,
				    title: name
				}));
			}
			
			tempUserArray.push(userId);
			
			var ur = new UserRight(userId, rightId);
			usersRightsArray.push(ur);
		})
		.fail(function() {
			$("#AddUserErrorMsg").text("Error occured while processing the request");
		})
		.always(function() {
			$("#InProgressEl").hide();
			$("#addUserBtn").removeAttr("disabled");
		});
	});
	
	
	// On change user rights function call
	$("#users_rights").on("change",".select_right",function(){
		var rights = $(this).val();
		var tr =  $(this).parent().parent();
		var userId = tr.attr("userid");
		
		for(var i in usersRightsArray)
		{
			var ur = usersRightsArray[i];
			if ( ur.userId == userId )
			{
				ur.usageRights = rights;
				break;
			}
		}
	});
	
	/*
	$("#addAction").click(function(){
		var valid = true;
		var userId = $("#select_user").val();
		var rightId = $("#select_right").val();
		
		if(userId == null || userId.trim().length == 0)
		{
			$("#select_user").css("border-color","red");
			valid = false;
		}
		else
		{
			// check whether user already added or not
			if( tempUserArray.indexOf(userId) > -1)
			{
				$("#select_user").css("border-color","red");
				$("#select_user").attr("title","This user has been already added");
				valid = false;
			}
			else{
				$("#select_user").css("border-color","#ccc");
				$("#select_user").removeAttr("title");
			}
		}
		
		if(rightId == null || rightId.trim().length == 0)
		{
			$("#select_right").css("border-color","red");
			$("#select_right").attr("title","Select the rights");
			valid = false;
		}
		else{
			$("#select_right").css("border-color","#ccc");
			$("#select_right").removeAttr("title");
		}
		
		
		if( !valid )
		{
			return false;
		}
		
		
		var userName = $("#select_user option:selected").text();
		var rightName = $("#select_right option:selected").text();
		
		var tdU = $("<td>").text(userName);
		var tdR = $("<td>").text(rightName);
		
		 
		var span = $('<span>').attr({"title":"remove","class":"removeRight glyphicon glyphicon-remove","aria-hidden":"true","style":"color:red;cursor:pointer;"});
		var tdA = $("<td>").append(span);
		var tr = $("<tr>").append(tdU).append(tdR).append(tdA).attr({"userid":userId,"usagerights":rightId});
		
		$("#users_rights tr#select_user_right").before(tr);
		$("#select_user").val('');
		$("#select_right").val('');
		
		tempUserArray.push(userId);
		
		var ur = new UserRight(userId, rightId);
		usersRightsArray.push(ur);
	});
	*/
	
	$("#users_rights").on("click", ".removeRight", function(){
		
		var tr =  $(this).parent().parent();
		var userId = tr.attr("userid");
		var ti = tempUserArray.indexOf(userId);
		
		tempUserArray.splice(ti, 1);
		
		for(var i in usersRightsArray)
		{
			var ur = usersRightsArray[i];
			if ( ur.userId == userId )
			{
				usersRightsArray.splice(i, 1);
				break;
			}
		}
		
		tr.remove();
	});
	
	
	$("#fileSaveBtn").click(function(){
		
		try{
			var lUsersRights = new UsersRights(usersRightsArray);
			var jsonStr = JSON.stringify(lUsersRights);
			$("#usersRightsField").val(jsonStr);
		}
		catch(error){
			alert(error);
		}
		return true;
	});

	// **** File related script END **** //
});