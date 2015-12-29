<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>SAML Identity Provider</title>
	</head>
	<body>
		<center>
			<h2>SAML IDP Login</h2>
			<hr/>
			<form action="/saml-idp/saml-sso.jsp">
				<table border="0">
					<tr>
						<td>User Name:</td>
						<td><input name="username" type="text" /></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input name="password" type="password" /></td>
					</tr>
					<tr>
						<td colspan="2" align="right"><input type="submit" value="Login" /></td>
					</tr>
				</table>
			</form>
		</center>
	</body>
</html>