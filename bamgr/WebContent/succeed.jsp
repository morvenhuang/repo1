<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Succeed</title>
</head>

<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>
	$(document).ready(function () {
		window.setInterval(function () {
			var sec = $("#sec").html();
			if (eval(sec) == 0) {
				window.location = 'login.jsp';
			} else {
				$("#sec").html(eval(sec) - eval(1));
			}
		}, 1000);
	});
</script>

<body>
	<form >
		<table align="center">
			<tr>
				<td height="200"></td>
			</tr>
			<tr>
				<td>
					<table align="center" >
						<tr height="10">
							<td >
								<h1>Congrats, uou will be redirected after <span id="sec" >5</span> seconds, thanks</h1>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
