<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@page import="mh.bamgr.models.UserBean"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Main</title>
</head>

<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
<!-- <script src="http://code.jquery.com/jquery-1.8.3.js"></script> -->
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
<script src="scripts/Highcharts-4.0.1/js/highcharts.js"></script>
<script src="scripts/Highcharts-4.0.1/js/modules/exporting.js"></script>
<script src="scripts/smartpaginator.js" type="text/javascript"></script>
<link href="scripts/smartpaginator.css" rel="stylesheet" type="text/css" />




<script>
	$(document).ready(function() {
		
		
		$.ajaxSetup({
			statusCode: {
				401: function(xhr){
					alert('Session expired, you will be redirected to login page.');
					window.location = xhr.getResponseHeader("Location");
				}
			}
		});
		
		$('.selClass').css({'width':'150px'});
		$('#btnAddExpense','#btnAddIncome').prop("disabled",true);
		//
		$.ajax({
			url:'actionservlet',
			type:'post',
			data:{
				actiontype:'init'
			},
			dataType:'json',
			success:function(data){
				$.each(data, function (index, value) {
					var r;
					if (value.isIncome == 1) {
						r = $('#rowIncome');
					} else {
						r = $('#rowExpense');
					}
					r.find('.selClass').append('<option value="' + value.id + '">' + value.name + '</option>');

				}); 
				$('#btnAddExpense','#btnAddIncome').prop("disabled",false);
			}
		}); 
		
		$('#btnLogout').on('click', function(){
			if(confirm('You sure you want to logout?')){
				$.ajax({
					url: 'userlogoutservlet',
					type: 'post',
					success:function(data){
						window.location='login.jsp';
					}
				});
			}
		});
		
		
		$('#btnManageCategories').on('click', function(){
			window.location='category.jsp';
		});
		
		
		
		
		var frDate;
		var toDate;
		var cnt;
		
		function retrieveCount(){
			$.ajax({
				url:'actionservlet',
				type:'post',
				data:{
					actiontype:'retrievecount',
					fr:frDate,
					to:toDate
				},
				success:function(data){
					cnt=data.total;
					//alert(data);
					$('#pg').smartpaginator({
						totalrecords: cnt,
						recordsperpage: data.perPage,
						theme: 'black',
						onchange: function (page) {
							retrieve(page);
						}
					});
				}
			});
		}
		
		function retrieve(pageNum){
			$.ajax({
				url:'actionservlet',
				type:'post',
				data:{
					actiontype:'retrieve',
					fr:frDate,
					to:toDate,
					page:pageNum
				},
				//dataType:'json',
				success:function(data){
					$("#tableRetrieve").find("tr").remove();
					$.each(data, function (index, value) {
						var cr;
						if (value.isIncome == 1) {
							cr = $('#rowIncome').clone();
						} else {
							cr = $('#rowExpense').clone();
						}
						cr.find('.dpClass').datepicker().datepicker("setDate",value.eiDate);
						cr.find('.selClass').val(value.categoryId);
						cr.find('.txtAmountClass').val(value.amount);
						cr.find('.txtDescClass').val(value.description);
						cr.find('.hdEiIdClass').val(value.id);
						cr.prop('hidden', false);
						cr.appendTo('#tableRetrieve');
					});
				}
			});
		}
		
		//		
		$('.dpFromToClass').datepicker().datepicker("setDate",new Date()).on('change', function(){
			frDate=$('#dpFrom').val();
			toDate=$('#dpTo').val();
			retrieveCount();
			retrieve('1');//have to nest the function in another function, can not directly use a function which has arg			
		});
		
		
		
		
		
		//clone row
		$(document).on('click', '#btnAddExpense', function(){
			var cr = $('#rowExpense').clone();
			cr.find('.dpClass').datepicker();
			cr.prop('hidden', false);
			cr.appendTo('#tableAdd');
		});
		
		$(document).on('click', '#btnAddIncome', function(){
			var cr = $('#rowIncome').clone();
			cr.find('.dpClass').datepicker();
			cr.prop('hidden', false);
			cr.appendTo('#tableAdd');
		});
		
		
		
		

		var specialKeys = new Array();
		specialKeys.push(8); //Backspace
		// Have to use $(document) here, cannot use $(.txtAmountClass) since it's dynamically created and doesn't exist in the beginning.
		$(document).on('keypress', '.txtAmountClass', function (e) {
			var keyCode = e.which ? e.which : e.keyCode;
			var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys.indexOf(keyCode) != -1);
			return ret;
		});
		$(document).on('paste', '.txtAmountClass', function (e) {
			return false;
		});
		$(document).on('drop', '.txtAmountClass', function (e) {
			return false;
		});
		
		
		
		
		$(document).on('click','.btnSaveClass', function(){
			var tr = $(this).closest('tr');
			var dt = tr.find('.dpClass').eq(0).val();
			var id = tr.find('.hdEiIdClass').eq(0).val();
			var category = tr.find('.selClass').eq(0).val();
			var amount = tr.find('.txtAmountClass').eq(0).val();
			var desc = tr.find('.txtDescClass').eq(0).val();
			if(dt && category!=0 && amount) {
				var obj = {'id':id, 'categoryId':category, 'amount':amount, 'eiDate':dt, 'description':desc};
				$.ajax({
					url:'actionservlet',
					type:'post',
					data:{
						actiontype:'save',
						jsondata:JSON.stringify(obj)
					},
					dataType:'json',
					success:function(data){
						if (data.status == 'success') {
							alert('Congrats, your data is saved.');
						} else if (data.status == 'error') {
							alert(data.message);
						}
					}
				});
			} else {
				alert('Hi man, input some value into mandatory fields!');
			}
			
		});
		
		$(document).on('click','.btnRemoveClass', function(){
			var tr = $(this).closest('tr');
			var id = tr.find('.hdEiIdClass').eq(0).val();
			if(id=='-1'){
				tr.remove();
			}else{
				$.ajax({
					url:'actionservlet',
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
		
		
		function drawChart(){
		var ym=[];
		var expense=[];
		var income=[];
		$.ajax({
			url: 'actionservlet',
			type: 'post',
			data: {
				actiontype: 'monthlysumchart'
			},
			dataType: 'json',
			success: function (data) {
				$.each(data, function (index, value) {
					ym.push(value.ym);
					expense.push(value.expense * -1);
					income.push(value.income);
				});
				//alert(sa_expense);
				$('#container').highcharts({
					chart: {
						type: 'column'
					},
					title: {
						text: 'Expense/Income Monthly Summary'
					},
					xAxis: {
						categories: ym
					},
					credits: {
						enabled: false
					},
					series: [{
						name: 'Expense',
						data: expense
					}, {
						name: 'Income',
						data: income
					}]
				});
			}
		});
		}
		
		drawChart();
		$('#btnRefresh').on('click', function(){
			drawChart();
		});
		
		
		
		
		
	});
</script>


<body>
	<form action="" method="post">
		<table width="100%">
			<tr>
				<td width="10%"></td>
				<td>
				<table width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;"><tr>
					<td>Welcome, ${user.getName() }</td>
					<td width="100px"><input type="button" id="btnManageCategories" value="Manage Categories"></td>
					<td width="60px"><input type="button" id="btnLogout" value="Logout"></td>
				</tr></table>
				</td>
				<td width="10%"></td>
			</tr>
			
			<tr height="17px"></tr>
			
			<tr>
				<td width="10%"></td>
				<td>
				<table id="middle" width="100%">
				<tr>
				<td>
					<table id="table1" width="100%" >
						<tr id="rowExpense" hidden="true">
							<td>Date: <input type="text" class="dpClass" readonly="readonly" />(*)</td>
							<td>Category: <select class="selClass">
									<option value="select one">(Select One)</option>
							</select>(*)</td>
							<td>Amount: <input type="text" name="amount" class="txtAmountClass">(*)</td>
							<td>Description: <input type="text" name="description" class="txtDescClass"></td>
							<td><input type="button" class="btnSaveClass" value="Save" /></td>
							<td><input type="button" class="btnRemoveClass" value="Remove" /></td>
							<td hidden="true"><input type="hidden" class="hdEiIdClass" value="-1" width="1px" /></td>
						</tr>
						<tr id="rowIncome" hidden="true">
							<td>Date: <input type="text" class="dpClass" readonly="readonly" />(*)</td>
							<td>Category: <select class="selClass">
									<option value="select one">(Select One)</option>
							</select>(*)</td>
							<td>Amount: <input type="text" name="amount" class="txtAmountClass">(*)</td>
							<td>Description: <input type="text" name="description" class="txtDescClass"></td>
							<td><input type="button" class="btnSaveClass" value="Save" /></td>
							<td><input type="button" class="btnRemoveClass" value="Remove" /></td>
							<td hidden="true"><input type="hidden" class="hdEiIdClass" value="-1" width="1px"  /></td>
						</tr>
					</table>
				</td>
				</tr>
				
				<tr><td>
								<table id="table2" width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
									<tr width="100%">
										<td width="100px"><input type="button" id="btnAddExpense" value="Add Expense"></td>
										<td width="100px"><input type="button" id="btnAddIncome" value="Add Income"></td>
										<td></td>
									</tr>
									<tr>
										<td colspan="3">
											<table id="tableAdd"></table>
										</td>
									</tr>
								</table>
				</td></tr>
				
				<tr height="20px"></tr>
				
				<tr><td>
								<table id="table3" width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
									<tr>
										<td width="200px">From: <input type="text" id="dpFrom" class="dpFromToClass" /></td>
										<td width="200px">To: <input type="text" id="dpTo" class="dpFromToClass" readonly="readonly" /></td>
										<td></td>
									</tr>
									<tr>
										<td colspan="3">
										<table>
										<tr>
										<td>
											<table id="tableRetrieve"></table>
										</td>
										</tr>
										<!-- paging -->
										<tr>
										<td><div id="pg"  style="width:99%"></td>
										</tr>
										</table>
										</td>
									</tr>
								</table>
				</td></tr>
				
				<tr height="20px"></tr>
				
				<tr><td>
								<table id="table4" width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
									<tr>
										<td width="200px">Monthly Summary</td>
										<td></td>
										
										<td width="60px"><input type="button" id="btnRefresh" value="Refresh" /></td>
									</tr>
									<tr>
										<td colspan="3">
											<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
										</td>
									</tr>
								</table>
				</td></tr>
				
				</table>
				</td>
				<td width="10%"></td>
			</tr>		
			
			<tr>
				<td width="10%"></td>
				<td>
				</td>
				<td width="10%"></td>
			</tr>
		</table>
	</form>
</body>
</html>
