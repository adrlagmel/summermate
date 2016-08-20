<%--
 * action-2.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" href="styles/jquery.datetimepicker.css" />

</head>

<body>

<security:authorize access="hasRole('EMPRESARIO')">	
<form class="form-inline" action="${actionURI}">
    
    <div class="form-group">  
      <input type="text" id = "datetimepicker" style="width:250px;" class="form-control" name="s" placeholder="<spring:message code="booking.searchByDate"/>">
     </div>
									
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="scripts/jquery.datetimepicker.full.js"></script>
	
	<script type="text/javascript">
		$('#datetimepicker').datetimepicker({format: 'd/m/Y'});
	</script>
     
    <div class="form-group">  
      <button type="submit" class="btn btn-default"><spring:message code="booking.search" /></button>
     </div><br/>
</form><br/>
</security:authorize>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="false" name="reservas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<spring:message code="booking.code" var="code" />
	<display:column property="codigo" title="${code}"
		sortable="false" />
		
	<spring:message code="booking.creationMoment" var="creationMoment" />
	<display:column property="fechaCreacion" title="${creationMoment}" format="{0,date,dd/MM/yyyy HH:mm}"
		sortable="true" />
		
	<spring:message code="booking.bookingDate" var="bookingDate" />
	<display:column property="fecha" title="${bookingDate}" format="{0,date,dd/MM/yyyy HH:mm}"
		sortable="true" />
		
	<spring:message code="booking.comensales" var="comensales" />
	<display:column property="comensales" title="${comensales}"
		sortable="false" />
		
	<spring:message code="booking.negocio" var="negocio" />
	<display:column property="negocio.nombre" title="${negocio}"
		sortable="true" />
		
	<spring:message code="booking.comments" var="comment" />
	<display:column title="${comment}" maxLength="20">
	<a href="javascript: void(0);" onclick="showLargeText('<jstl:out value="${row.comentarios}" />');" data-toggle="modal" data-target="#largeText"><jstl:out value="${row.comentarios}" /></a>
	</display:column>
	<spring:message code="valoracionNegocio.create" var="valoracionNegocio" />
	<display:column>
	<jsp:useBean id="now" class="java.util.Date"/>	
	<security:authentication var="user" property="principal.id" />
	<jstl:if test="${row.usuario.userAccount.id == user && now > row.fecha && row.valoracionNegocio.id == null}">
		<a href="valoracionNegocio/usuario/create.do?reservaId=${row.id}">
				<spring:message code="valoracionNegocio.create"/>
		</a> 
	</jstl:if>
	
	<jstl:if test="${row.usuario.userAccount.id == user && now > row.fecha && row.valoracionNegocio.id != null}">
		<a href="valoracionNegocio/usuario/display.do?reservaId=${row.id}">
				<spring:message code="valoracionNegocio.display"/>
		</a> 
	</jstl:if>
	</display:column>	
	
	<display:column>
		<spring:message code="booking.confirmDelete" var= "confirm" />
		<security:authentication var="use" property="principal.id" />
		<jstl:if test="${row.usuario.userAccount.id == use && now < row.fecha}">
			<a href="reserva/usuario/delete.do?reservaId=${row.id}" class="btn btn-danger" onclick="return confirm('${confirm}')"><spring:message code="booking.delete" /></a>
		</jstl:if>
	</display:column>
	
</display:table>
</div>
</body>
</html>