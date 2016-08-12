<%--
 * layout.jsp
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <meta name="viewport" content="width=device-width, initial-scale=1">

<script type="text/javascript" src="scripts/jquery.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jquery.cookie.js"></script>
<script type="text/javascript" src="scripts/jmenu.js"></script>
<script type="text/javascript" src="scripts/jquery.min.js"></script>
<script type="text/javascript" src="scripts/jquery-11.js"></script>
<script type="text/javascript" src="scripts/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="styles/maps.css" type="text/css">
<link rel="stylesheet" href="styles/common.css" type="text/css"/>
<link rel="stylesheet" href="styles/summermate.css" type="text/css"/>
<link rel="stylesheet" href="styles/jquery-ui.css" type="text/css"/>
<link rel="stylesheet" href="styles/bootstrap.min.css" />
<link rel="stylesheet" href="styles/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
 
  
  
<link rel="shortcut icon" href="favicon.ico"/> 

<title><tiles:insertAttribute name="title" ignore="true" /></title>

<script type="text/javascript">

$(document).ready(function() {
	$('.dropdown-toggle').dropdown();
	});

	function controlcookies() {
	    // si variable no existe se crea (al clicar en Aceptar)
			localStorage.controlcookie = (localStorage.controlcookie || 0);
	
			localStorage.controlcookie++; // incrementamos cuenta de la cookie
			cookie1.style.display='none'; // Esconde la política de cookies
	}
	
	function mostrarcookies() {
	    // si variable no existe se crea (al clicar en Aceptar)
			localStorage.controlcookie = 0;
			location.reload();
	}
	
	function askSubmission(msg, form) {
		if (confirm(msg))
			form.submit();
	}
	
	function showLargeText(text){
		$('#largeTextBody').html(text);
	}
	
	function centerModal() {
	    $(this).css('display', 'block');
	    var $dialog = $(this).find(".modal-dialog");
	    var offset = ($(window).height() - $dialog.height()) / 2;
	    // Center modal vertically in window
	    $dialog.css("margin-top", offset);
	}

	$('.modal').on('show.bs.modal', centerModal);
	$(window).on("resize", function () {
	    $('.modal:visible').each(centerModal);
	});

	
	function cargarMiniPlanner()
	{
		mesaId = $("select[name*=mesa]").val();
		date = $( 'input:text[name=fechaCreacion]').val();
		
		if(date == undefined || date ==null || date == '')
			date = $( 'input:text[name=fecha]').val();
		
		
		if(date != undefined && date !=null && date != '' && mesaId != undefined && mesaId != null && mesaId > 0){
		$('#miniplanner').load('mesa/miniplanner.do?date='+encodeURIComponent(date)+'&mesaId='+mesaId);
		}

	}
</script>
</head>

<body>

	<div>
		<tiles:insertAttribute name="header" />
	</div>
	
		<div id="contenedor">
			<h2><tiles:insertAttribute name="title" /></h2>
		</div>
		<div class="container">
		<tiles:insertAttribute name="terms" />
		<tiles:insertAttribute name="largeText" />
		<tiles:insertAttribute name="body" />	
		<jstl:if test="${message != null}">
			<br />
			<span class="message"><spring:message code="${message}" /></span>
		</jstl:if>	
		</div>
		
	<div class="" style="margin-top: 50px;">
		<nav class="navbar navbar-default navbar-fixed-bottom footer-summermate">
			<tiles:insertAttribute name="cookies" />
			<div class="container">
				<tiles:insertAttribute name="footer" />
			</div>
			
		</nav>
	</div>
</body>

</html>