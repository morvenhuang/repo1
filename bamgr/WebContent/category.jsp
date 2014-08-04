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
				actiontype: 'init'
			},
			dataType:'json',
			success:function(data){
				$("#tableIncomeCategory").find("tr:gt(0)").remove();//remove the tr which has row index greater than 0 (based on 0).
				$("#tableExpenseCategory").find("tr:gt(0)").remove();
				$.each(data, function (index, value) {
					var cr;
					//var tbl;
					if (value.isIncome == 1) {
						cr = $('#trICRowTmpl').clone();
						cr.find('.txtName').val(value.name);
						cr.find('.hdId').val(value.id);
						cr.prop('hidden', false);
						cr.appendTo('#tableIncomeCategory');
						
					} else {
						cr = $('#trECRowTmpl').clone();
						cr.find('.txtName').val(value.name);
						cr.find('.hdId').val(value.id);
						cr.prop('hidden', false);
						cr.appendTo('#tableExpenseCategory');
					}
					

				}); 
				//$('#tableExpenseCategory').find('tr').remove(1);
			}
		});
		
		
		$(document).on('click','.btnSave', function(){
			var tr = $(this).closest('tr');
			var category = tr.find('.txtName').eq(0).val();
			var id = tr.find('.hdId').eq(0).val();
			var desc ='';
			var isIncome=0;
			if(this.id=='btnSaveEC'){
				isIncome=0;
			} else{
				isIncome=1;
			}
			if(category){
				var obj = {'id':id, 'name':category, 'description':desc, 'isincome':isIncome};
				$.ajax({
					url:'categoryservlet',
					type:'post',
					data:{
						actiontype:'save',
						jsondata:JSON.stringify(obj)
					},
					dataType:'json',
					success:function(data){
						if (data.status == 'success') {
							if(id=='-1' && data.extra){
								tr.find('.hdId').val(data.extra);
							}
							alert('Congrats, your data is saved.');
						} else if (data.status == 'error') {
							alert(data.message);
						}
					}
				});
			}
		});
		
		
		$(document).on('click','.btnDelete', function(){
			var tr = $(this).closest('tr');
			var id = tr.find('.hdId').eq(0).val();
			if(id=='-1'){
				tr.remove();
			}else{
				$.ajax({
					url:'categoryservlet',
					type:'post',
					data:{
						actiontype:'remove',
						id:id
					},
					success:function(data){
						if (data.status == 'success') {
							tr.remove();
						} else if (data.status == 'error') {
							alert(data.message);
						}
					}
				});
			}
		});
		
		
		$('#btnBackToMain').on('click', function(){
			window.location='main.jsp';
		});
		
		
		$(document).on('click', '#btnAddExCategory', function(){
			var cr = $('#trECRowTmpl').clone();
			cr.prop('hidden', false);
			cr.appendTo('#tableExpenseCategory');
		});
		
		$(document).on('click', '#btnAddInCategory', function(){
			var cr = $('#trICRowTmpl').clone();
			cr.prop('hidden', false);
			cr.appendTo('#tableIncomeCategory');
		});
		
		
	});
</script>	

<body>
<form>
	<table width="100%">
			<tr>
				<td width="10%"></td>
				<td>
				<table width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;"><tr>
					<td width="100px"><input type="button" id="btnBackToMain" value="Back to Main page"></td>
				</tr></table>
				</td>
				<td width="10%"></td>
			</tr>
			
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
											<td width="260px"><input type="text" style="width:90%" class="txtName"></td>
											<td hidden="true"><input type="hidden" class="hdId" value="-1" width="1px" /></td>
											<td >
												<input type="button" value="√" id='btnSaveEC' class="btnSave">
												<input type="button" value="-" id='btnDeleteEC' class="btnDelete">
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
											<td width="260px"><input type="text" style="width:90%" class="txtName"></td>
											<td hidden="true"><input type="hidden" class="hdId" value="-1" width="1px" /></td>
											<td >
												<input type="button" value="√" id='btnSaveIC' class="btnSave">
												<input type="button" value="-" id='btnDeleteIC' class="btnDelete">
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