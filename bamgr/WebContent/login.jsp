<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Sign in</title>
</head>
<body>
	<form action="userloginservlet" method="post">
		<table align="center">
			<tr>
				<td height="200"></td>
			</tr>
			<tr>
				<td>
					<table align="center" width="300" bgcolor="lightgreen" border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
						<tr height="10">
							<td>
								Name:
							</td>
							<td>
								<input type="text" name="name">
							</td>
						</tr>
						<tr height="10">
							<td>
								Password:
							</td>
							<td>
								<input type="password" name="password">
								<input type="submit" name="login" value="Sign in">								
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right"><a href="userdetail.jsp">Create a new account...</a></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>