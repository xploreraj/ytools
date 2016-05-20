<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<script src="assets/js/jquery-2.1.3.min.js"></script>
<script type="text/javascript">


	/**
	 * The collapsible code is taken from : https://jsfiddle.net/kutec/6x3qb1zc/
	 */
	$(document).ready(function() {
		$("#secondary").on('click', '.widget-title', function(e) {
			//alert($(this).next('.widget-content'));
			$(this).next('.widget-content').slideToggle(200);
			$(this).closest('.widget').toggleClass('active');
		});

	});

	/* (function ($) {
	    $("#secondary").on('click', '.widget-title', function (e) {
	        $(this).next('.widget-content').toggle(200);
	        $(this).parents('.widget').toggleClass('active');
	    });
	})(jQuery); */
</script>

<style type="text/css">
#secondary .widget-title {
	margin: 0;
	font-size: 22px;
	/* background: #eee; */
	/* background: #0F1A69; */
	background: #FE9F43;
	color: #fff;
	padding: 10px;
	cursor: pointer;
	text-align: left;
}

#secondary .widget .widget-content {
	display: none;
	/*background-color: white; /* #CFE880 */
	background: rgba(255,255,255,0.7);
	text-align: left;
	padding: 20px;
}

#secondary .widget.active .widget-title,#secondary .widget.active ul {
	background: #6ce26c; /* #666 */
	color: #fff;
}

/* html, body {
    font-family: /* Verdana, sans-serif, */ cursive;
    font-size: 15px;
    line-height: 1.5;
} */


</style>

</head>

<body>
	<aside id="secondary" class="col-md-3 col-sm-3">
	<div class="row">
		<!-- .widget STARTS -->
		<div class="widget">
			<div class="widget-inner">
				<h3 class="widget-title">PRE-CHECKS</h3>
				<div id="preChecksInfo" class="widget-content"></div>
			</div>
		</div>
		<!-- .widget ENDS -->
		<br>
		<!-- .widget STARTS -->
		<div class="widget">
			<div class="widget-inner">
				<h3 class="widget-title">FUNCTIONAL INFO</h3>
				<div id="functionalInfo" class="widget-content"></div>
			</div>
		</div>
		<!-- .widget ENDS -->
		<br>
		<!-- .widget STARTS -->
		<div class="widget">
			<div class="widget-inner">
				<h3 class="widget-title">TECHNICAL INFO</h3>
				<div id="technicalInfo" class="widget-content"></div>
			</div>
		</div>
		<!-- .widget ENDS -->
	</div>
	</aside>
</body>
</html>
