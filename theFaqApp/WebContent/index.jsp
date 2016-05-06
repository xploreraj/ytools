<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% response.setHeader("Cache-Control","max-age=0"); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>PFM Modules FAQ</title>
<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
<script type="text/javascript">
/*
 * This file has the AJAX calls and JQUERY page manipulation work functions for JSPs 
 */


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
		        	$('<option>').val(value).text(value).appendTo($('#module'));
		      	});
			}
		}); //1.

		//2. Populate submodules in dropdown depending upon parent module selection
		$('#module').change(function(){

			//info section
			var module_submodule = $('#module_submodule');
			var info = $('#info');

			//on change, clear dropdown population and info
			$('#subModule').find('option:gt(0)').remove();
			//$('#subModule').find('option:gt(0):lt(-1)').remove();
			module_submodule.html('');
			info.html('<h3>Please select module and corresponding sub-module from above dropdowns, or create new!</h3>');
					
			var moduleName = $('select#module').val();
					
			if(moduleName == 'Select') {
				return;
			}
			
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
				        $('<option>').val(value).text(value).appendTo($('#subModule'));
			        	//if we have multiple options by default and want to insert before last option
			        	//$('#subModule option').eq(-1).before( $('<option>').val(value).text(value) );
			      	});
				}
			});

		}); //2.

		//3. Populate 'info' section depending upon submodule selection
		$('#subModule').change(function(){

			var moduleName = $('select#module').val();
			var subModuleName = $('select#subModule').val();

			//info area
			var module_submodule = $('#module_submodule');
			var info = $('#info');

			module_submodule.html('');
			
			if(moduleName == 'Select' || subModuleName == 'Select') {
				info.html('<h3>Please select module and corresponding sub-module from above dropdowns, or create new!</h3>');
				return;
			}
				
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
						/* $.each(response, function() {
				        	//$('#info').html('Sub-module Name: ' + response.name + '<br/>');
				        	$('#info').html('').after('Sub-module Name: ' + response.name + '<br/>');
				        	var i = 1;
				        	$.each(response.infoList, function(index, value) {
				        		$('#info').html('').after(i + '. ' + value + '<br/>');
				        		i += 1;
					        });
				      	}); */

				      	//prepare information
				      	
						
						module_submodule.append('<h3>' + moduleName + ': ' + response.name + '</h3>');
				      	info.html(response.info.replace(/\n/g,"<br>"));
			        	/* $.each(response.info, function(index, value) {
			        		//'index' is being used to number lines of info from 'infoList' in JSON
			        		info.append((index+1) + '. ' + value + '<br/>');
				        }); */
					}
					else {
						$('#info').html('<h2>NO DATA RETURNED</h2>');
					}
				}
			});//3.

		});

		/* $('#btnSum').click(function(){
			var num1 = $('#number1').val();
			var num2 = $('#number2').val();
			
			$.ajax({
				type:'POST',
				data: {
					number1: num1,
					number2: num2,
					action: 'demo2'
				},
				url:'AjaxController',
				success: function(result){
					$('#result2').html(result);
				}
			});
		}); */
	});

</script>

</head>
<body>

Step 1:
<select name="module" id="module" style="width: 150px" title="Select the parent module">
	<option label="Select module">Select</option>
</select>
Step 2:
<select name="subModule" id="subModule" style="width: 150px" title="Select the sub module">
	<option label="Select submodule">Select</option>
</select>

<b><a href="create_edit.jsp">Create New or Edit Info</a></b>

<br>
<br>
<fieldset>
	<legend>Info:</legend>
	<!-- <span id="info">
		<h3>Please select module and corresponding sub-module from above dropdowns, or create new!</h3>
	</span> -->
	<div id="module_submodule"></div>
	<br>
	<div id="info"></div>
	<br>
</fieldset>

</body>
</html>