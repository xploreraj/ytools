<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create or Edit PFM Module Info</title>
<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
<script type="text/javascript">

	function showInfoTextAreas(preChecks, funcInfo, techInfo) {
		$('#preChecksInfo').val(preChecks).show();
		$('#functionalInfo').val(funcInfo).show();
		$('#technicalInfo').val(techInfo).show();
		//$("tr:gt(2)").show();
		$('#preChecksLegend').show();
		$('#funcInfoLegend').show();
		$('#techInfoLegend').show();
	}

	function hideInfoTextAreas() {
		$('#preChecksInfo').val('').hide();
		$('#functionalInfo').val('').hide();
		$('#technicalInfo').val('').hide();
		//$("tr:gt(2)").hide();
		$('#preChecksLegend').hide();
		$('#funcInfoLegend').hide();
		$('#techInfoLegend').hide();
	}

	function showConfirmCheckbox(show) {
		if(show) {
			$('#confirmSubmitTD').show();
			$('#confirmSubmit').prop('checked', false);
		}
		else {
			$('#confirmSubmitTD').hide();
			$('#confirmSubmit').prop('checked', false);
		}
	}

	function validateForm(currModuleName, newModuleName, currSubModuleName, newSubModuleName, infoConcatenated) {
		if(!newModuleName || (!newSubModuleName ? infoConcatenated : !infoConcatenated)) {
			//$('#submitMessageSpan').css('color','red').html('Inconsistent data, complete all the fields!');
			return false;
		}
		if(currModuleName.toLowerCase() == newModuleName.toLowerCase())
			if(!currSubModuleName && currSubModuleName.toLowerCase() == newSubModuleName.toLowerCase())
				return false;
		
		return true;
	}

	//pass a message to raise error, or pass nothing to remove existing error
	function errorHelper(isError, message) {
		if(isError == undefined && message == undefined) {
			//clear span
			$('#errorMessageSpan').html('');
		}
		else if(isError != undefined && message != undefined) {
			//populate error span
			$('#errorMessageSpan').html(message);
			if(isError)
				$('#errorMessageSpan').css('color','red');
			else
				$('#errorMessageSpan').css('color','blue');
		}
	}
/*
 * This file has the AJAX calls and JQUERY page manipulation work functions for JSPs 
 */

 	$(document).change(function(){
		errorHelper();
 	});
	$(document).ready(function(){
		
		//1. Populate parent modules into dropdown
		$.ajax({
			type:'POST',
			data: {
				action: 'getModuleNames'
			},
			dataType: 'json',
			url:'AjaxController',
			success: function(response){
		        $.each(response, function(index, value) {
		        	//$('<option>').val(value).text(value).appendTo($('#module'));
		        	$('#module option').eq(-1).before( $('<option>').val(value).text(value) );
		      	});
			}
		}); //1.

		//2. Populate submodules in dropdown depending upon parent module selection
		$('#module').change(function(){

			//on change, clear dropdown population and info
			//$('#subModule').find('option:gt(0)').remove();
			$('#subModule').find('option:gt(0):lt(-1)').remove();
			$('#newSubModuleName').val('').hide();
			hideInfoTextAreas();

			var moduleName = $('select#module').val();
					
			if(moduleName == 'Select') {
				$('#newModuleName').val('').hide();
				$('#subModule').hide();
				
				showConfirmCheckbox(false);
				return;
			}
			else if(moduleName == 'Create') {

				$('#newModuleName').val('Enter module name').show();
				$('#newSubModuleName').val('Enter submodule name').show();
				$('#currentModuleName').val('');
				$('#subModule').hide();
				$('#currentSubModuleName').val('');
				
				var preChecks = 'Enter pre checking steps here.\nSeparate lines using linebreaks.\n' +
						'Do not use any tags, use only plaintext';
				var funcInfo = 'Enter functional info here';
				var techInfo = 'Enter technical info here';
				showInfoTextAreas(preChecks, funcInfo, techInfo);
				showConfirmCheckbox(true);
				//$('#submit').show();
				
				return;
			}

			$('#currentModuleName').val(moduleName); //to store current module name or 'Create' for new module
			$('#newModuleName').val(moduleName).show();
			showConfirmCheckbox(true);
			
			$.ajax({
				type:'POST',
				data: {
					moduleName: moduleName,
					action: 'getSubModuleNames'
				},
				dataType: 'json',
				url:'AjaxController',
				success: function(response){
			        $.each(response, function(index, value) {
				        //$('<option>').val(value).text(value).appendTo($('#subModule'));
			        	//if we have multiple options by default and want to insert before last option
			        	$('#subModule option').eq(-1).before( $('<option>').val(value).text(value) );
			        	$('#subModule').show();
			      	});
				}
			});

		}); //2.

		//3. Populate 'info' section depending upon submodule selection
		$('#subModule').change(function(){

			var moduleName = $('select#module').val();
			var subModuleName = $('select#subModule').val();
			
			if(subModuleName == 'Select') {
				$('#newSubModuleName').hide();
				hideInfoTextAreas();
				return;
			}
			if(subModuleName == 'Create' && moduleName != 'Select') {
				$('#newSubModuleName').val('Enter submodule name').show();
				var preChecks = 'Enter pre checks steps here.\nSeparate lines using linebreaks.\nDo not use any tags, use only plaintext';
				var funcInfo = 'Enter functional info here.';
				var techInfo = 'Enter technical info here.';
				showInfoTextAreas(preChecks, funcInfo, techInfo);
				showConfirmCheckbox(true);
				return;
			}

			$('#currentSubModuleName').val(subModuleName); //to store current module name or 'Create' for new module
			$('#newSubModuleName').val(subModuleName);
			$('#newSubModuleName').show();
			
			$.ajax({
				type:'POST',
				data: {
					moduleName: moduleName,
					subModuleName: subModuleName,
					action: 'getSubModule'
				},
				dataType: 'json',
				url:'AjaxController',
				success: function(response){
					if (response != null) {
						showInfoTextAreas(response.preChecksInfo, response.functionalInfo, response.technicalInfo);
					}
					else {
						errorHelper(true, 'No Data Found');
					}
				}
			});//3.

		});

		$('#confirmSubmit').change(function(){
			if(this.checked)
				$('#submit').show();
			else
				$('#submit').hide();
		});

		$('#submit').click(function(){
			var currModuleName = $('#currentModuleName').val();
			var newModuleName = $('#newModuleName').val().trim();
			var currSubModuleName = $('#currentSubModuleName').val();
			var newSubModuleName = $('#newSubModuleName').val().trim();
			var preChecksInfo = $('#preChecksInfo').val().trim();
			var functionalInfo = $('#functionalInfo').val().trim();
			var technicalInfo = $('#technicalInfo').val().trim();;
			var infoConcatenated =  preChecksInfo + functionalInfo + technicalInfo;

			//TODO
			//submit validations **************
			if(!validateForm(currModuleName, newModuleName, currSubModuleName, newSubModuleName, infoConcatenated)) {
				errorHelper(true, 'Inconsistent data! New value(s) must be different from previous value(s), and not empty.');
				return;
			}

			//var proceed = confirm('Submitting will create new information or update existing infomation depending upon your form selection.' +
					//' Do you want to submit the information?');
			//if(!proceed)
				//return;
			
			$.ajax({
				type:'POST',
				data: {
					currModuleName : currModuleName,
					newModuleName : newModuleName,
					currSubModuleName : currSubModuleName,
					newSubModuleName : newSubModuleName,
					preChecksInfo : preChecksInfo,
					functionalInfo : functionalInfo,
					technicalInfo : technicalInfo,
					action : 'submitForm'
				},
				dataType: 'text',
				url:'AjaxController',
				success: function(response){
					//errorHelper(false, response + ' <a onclick="window.location.reload(true)">Refresh page.</a>');
					alert('Data sucessfully saved!');
					window.location.reload(true);
				},
				error: function(jqXHR, textStatus, message) {
					//alert(textStatus + ": " + message);
					errorHelper(true, message);
				},
				complete: function(){
					//window.location.reload(true);
				} 
			});
		});
	});
	
</script>
</head>
<body>
	<center><h1>Admin Console</h1></center>
	<div align="right" style="font-size: 15"><a href="logout">Logout</a></div>
	<hr>
	<table align="center">
		<tr>
			<td>
				<select name="module" id="module" style="width: 150px" title="Select the parent module">
					<option label="Select module" style="font-style: italic;">Select</option>
					<option label="Create new" style="font-style: italic;">Create</option>
				</select>
			</td>
			<td>
				<input type="hidden" id="currentModuleName" value="">
				<input type="text" style="display: none" id="newModuleName" style="width: 150px" maxlength="20" title="Enter Module Name">
			</td>
		</tr>
		<tr>
			<td>
				<select name="subModule" id="subModule" style="width: 150px; display:none;'" title="Select the sub module" >
					<option label="Select sub-module" style="font-style: italic;">Select</option>
					<option label="Create new" style="font-style: italic;">Create</option>
				</select>
			</td>
			<td>
				<input type="hidden" id="currentSubModuleName" value="">
				<input type="text" style="display: none" id="newSubModuleName" style="width: 150px" title="Enter Submodule Name">
			</td>
		</tr>
		<tr>
			<td id="preChecksLegend" style="display:none">Pre-checks</td>
			<td>
				<textarea style="display:none" cols="50" rows="10" id="preChecksInfo"></textarea>
			</td>
		</tr>
		<tr>
			<td id="funcInfoLegend" style="display:none">Functional Information</td>
			<td>
				<textarea style="display:none" cols="50" rows="10" id="functionalInfo"></textarea>
			</td>
		</tr>
		<tr>
			<td id="techInfoLegend" style="display:none">Technical Information</td>
			<td>
				<textarea style="display:none" cols="50" rows="10" id="technicalInfo"></textarea>
			</td>
		</tr>
		<tr>
			<td></td>
			<td id="confirmSubmitTD" style="display: none"><input style="color: blue" type="checkbox" id="confirmSubmit" value="">Confirm changes. This will create new information or update existing infomation.</td>
		</tr>
		<tr>
			<td></td>
			<td><input style="display:none" type="button" id="submit" value="Submit"></td>
		</tr>
		<tr>
			<td></td>
			<td><span id="errorMessageSpan"></span></td>
		</tr>
	</table>
	
</body>

<head>
	<style>
		td {vertical-align: top;}
	</style>
</head>
</html>
<%

%>