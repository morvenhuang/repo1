<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Category</title>
</head>

<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>
	$(document).ready(function() {
		$.ajax({
			url:'categoryservlet',
			type:'post',
			data: {
				actiontype: 'xxx'
			},
			dataType:'json',
			success:function(data){
				$("#tableIncomeCategory").find("tr:gt(0)").remove();//remove the tr which has row index greater than 2 (based on 0).
				$("#tableExpenseCategory").find("tr:gt(0)").remove();
				$.each(data, function (index, value) {
					var cr;
					//var tbl;
					if (value.isIncome == 1) {
						cr = $('#trICRowTmpl').clone();
						cr.find('.txtIC').val(value.name);
						cr.prop('hidden', false);
						cr.appendTo('#tableIncomeCategory')
						
					} else {
						cr = $('#trECRowTmpl').clone();
						cr.find('.txtEC').val(value.name);
						cr.prop('hidden', false);
						cr.appendTo('#tableExpenseCategory')
					}
					

				}); 
				//$('#tableExpenseCategory').find('tr').remove(1);
			}
		});
	});
</script>	

<body>
<form>
	<table width="100%">
			<tr>
				<td width="10%"></td>
				
				<td>
				
				<table width="100%" id="tableCategory">
					<tr><td>
						<table width="100%"  border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
							<tr>
								<td width="140px">Expense Categories</td>
								<td></td>
								<td width="30px"><input type="button" id="btnAddExCategory" value="+"></td>
							</tr>
							<tr>
								<td colspan="3">
									<table id="tableExpenseCategory" width="100%">
										<tr id="trECRowTmpl" hidden="true">
											<td width="15px"></td>
											<td width="260px"><input type="text" style="width:90%" class="txtEC"></td>
											<td >
												<input type="button" value="√" class="btnSaveEC">
												<input type="button" value="-" class="btnDeleteEC">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					
					<tr><td>
						<table width="100%"  border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
							<tr>
								<td width="140px">Income Categories</td>
								<td></td>
								<td width="30px"><input type="button" id="btnAddInCategory" value="+"></td>
							</tr>
							<tr>
								<td colspan="3">
									<table id="tableIncomeCategory" width="100%">
										<tr id="trICRowTmpl" hidden="true">
											<td width="15px"></td>
											<td width="260px"><input type="text" style="width:90%" class="txtIC"></td>
											<td >
												<input type="button" value="√" class="btnSaveIC">
												<input type="button" value="-" class="btnDeleteIC">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
				</table>
				
				
				</td>
				
				<td width="10%"></td>
			</tr>
	</table>
</form>
</body>
</html>