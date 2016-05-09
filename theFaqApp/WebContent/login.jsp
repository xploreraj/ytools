<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Login to Admin Console</title>
<link rel="stylesheet" href="assets/form-mini.css">
</head>
<body>
<br><br>
<div class="main-content">
	<div class="form-mini-container">
		<h1>Admin Console</h1>
		<form class="form-mini" method="POST" action="admin" onsubmit="return validateForm();">
			<div class="form-row">
				<input type="password" id="password" name="password" placeholder="Enter password">
			</div>
			
			<div class="form-row form-last-row">
	        	<button type="submit">Login</button>
	        </div>
	        <span id="error" class="error-span">${requestScope.error}</span>
		</form>
		
	</div>
</div>


<%-- <div id ="loginForm" align="center">
	<h1>Admin Console</h1>
	<hr>
	<br><br>
	<form method="POST" action="admin" onsubmit="return validateForm();">
		<input type="password" id="password" name="password" placeholder="Enter password!">
		<br><br>
		<input type="submit" id="login" value="Login">
	</form>
	<br>
	<span id="error" style="color: red;">${requestScope.error}</span>
</div> --%>
</body>
<head>
<script type="text/javascript">
	function validateForm(){
		if(document.getElementById('password').value.trim()=='')
			return false;
		return true;
	}
</script>
</head>
</html>