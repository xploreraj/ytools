<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<% response.setHeader("Cache-Control","max-age=0"); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<style type="text/css">
table {
	width: 1000px;
	/* margin-left: 10%; */ /* raj */
	margin-bottom: 10px;
}
td {
	vertical-align: top;
	padding: 10px;
	/* margin-left: 20%; */
	/* padding-left: 20%; */
	/* text-align: center; raj */
}
.menuTd {
	vertical-align: top;
}
.infoPanelTD {
	/* float: right; */
}
.frame {

}
html, body {
    font-family: Verdana, sans-serif;
    font-size: 15px;
    line-height: 1.5;
    background-color: #f3f3f3;
    background: url("assets/images/background-8.jpg") no-repeat center fixed;
    background-size: cover;
}
.infoPanel {
	display: none;
	/* background: rgba(215,215,215,0.4); */
	border: none;
	width: 100%;
	overflow: auto;
}
.footer {
	text-align: center;
	vertical-align: bottom;
	position: relative;
	margin-top: -10px;
	height: 10px;
	clear: both;
}
</style>

<script type="text/javascript" src="assets/syntaxhighlighter/scripts/shCore.js"></script>
<script type="text/javascript" src="assets/syntaxhighlighter/scripts/shBrushJava.js"></script>
<script type="text/javascript" src="assets/syntaxhighlighter/scripts/shBrushSql.js"></script>
<script type="text/javascript" src="assets/syntaxhighlighter/scripts/shBrushJScript.js"></script>
<script type="text/javascript" src="assets/syntaxhighlighter/scripts/shBrushXml.js"></script>
<link href="assets/syntaxhighlighter/styles/shCoreMidnight.css" rel="stylesheet" type="text/css" />
<link href="assets/syntaxhighlighter/styles/shThemeMidnight.css" rel="stylesheet" type="text/css" />

</head>

<body>
<jsp:include page="header.jsp"></jsp:include>
<hr><br>
<table>
	<tr>
		<td class="menuTD" width="20px">
			<select name="module" id="module" style="width: 150px"
				title="Select the parent module">
					<option label="Select module">Select</option>
			</select>
			<select name="subModule" id="subModule"
				style="width: 150px; display: none" title="Select the sub module">
					<option label="Select submodule">Select</option>
			</select>
		</td>
		<td class="infoPanelTD" width="80%">
			<div class="infoPanel"><jsp:include page="info.jsp"></jsp:include></div>
		</td>
	</tr>
</table>

<br>
<%-- <jsp:include page="footer.jsp"></jsp:include> --%>

</body>
<head>
	  
<title>PFM Modules FAQ</title>
<script type="text/javascript" src="assets/js/jquery-2.1.3.min.js"></script>
<script type="text/javascript">
/*
 * This file has the AJAX calls and JQUERY page manipulation work functions for JSPs 
 */

function hideInfoTextAreas(emptyContents){
	if(emptyContents==true){
		$('#preChecksInfo').html('');
		$('#functionalInfo').html('');
		$('#technicalInfo').html('');
	}
	$('.infoPanel').hide();
 }


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

		hideInfoTextAreas(true);

		//on change, clear dropdown population
		$('#subModule').find('option:gt(0)').remove();
				
		var moduleName = $('select#module').val();
				
		if(moduleName == 'Select') {
			$('#subModule').hide();
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
		      	});

		      	$('#subModule').show();
			},
		});

	}); //2.

	//3. Populate 'info' section depending upon submodule selection
	$('#subModule').change(function(){

		var moduleName = $('select#module').val();
		var subModuleName = $('select#subModule').val();
		
		if(moduleName == 'Select' || subModuleName == 'Select') {
			hideInfoTextAreas(true);
			return;
		}

		var preChecksInfo = $('#preChecksInfo');
		var functionalInfo = $('#functionalInfo');
		var technicalInfo = $('#technicalInfo');
			
		$.ajax({
			type:'POST',
			data: {
				moduleName: moduleName,
				subModuleName: subModuleName,
				action: 'getSubModule',
				formatInfo: 'true'
			},
			dataType: 'json',
			url:'AjaxController',
			success: function(response){
				if (response != null) {
			      	//prepare information
					if(response.preChecksInfo)
			      		preChecksInfo.html(response.preChecksInfo);
			      	else
				      	preChecksInfo.html('NA');
				      	
			      	if(response.functionalInfo)
				      	functionalInfo.html(response.functionalInfo);
			      	else
				      	functionalInfo.html('NA');
			      	if(response.technicalInfo)
				      	technicalInfo.html(response.technicalInfo);
			      	else
				      	technicalInfo.html('NA');
			     	
			      	$('.infoPanel').show();

			      	//SyntaxHighlighter.all();
			      	SyntaxHighlighter.highlight();
				}
				
			}
		});
	}); //3.
});

</script>


</head>

</html>

	<div id="preChecksInfo"></div>
	<br>
	<div id="functionalInfo"></div>
	<br>
	<div id="technicalInfo"></div>
</fieldset>

</body>
</html>