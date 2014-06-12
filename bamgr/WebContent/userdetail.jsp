<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>User detail</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/userdetailservlet" method="post">
	<input type="hidden" name="edittype" value="new"/>
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
							</td>
						</tr>
						<tr height="10">
							<td>
								Description:
							</td>
							<td>
								<input type="text" name="description">							
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<input type="submit" name="create" value="Create">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
</form>
</body>
</html>
