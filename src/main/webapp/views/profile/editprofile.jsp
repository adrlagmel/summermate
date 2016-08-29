<%--profile.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" href="styles/jquery.datetimepicker.css" />

</head>

<body>


<form:form action="${actionURI}" modelAttribute="actor">


	<acme:textbox code="actor.name" path="nombre" /><br/>
	<acme:textbox code="actor.surname" path="apellidos"  /><br/>
	<acme:textbox code="actor.email" path="email"  /><br/>
		
	<jstl:if test="${isCliente == true}">
		<acme:textbox code="actor.phone" path="telefono" /><br/>
		
			<div style="width:50%; margin: auto;" >
			<form:label path="fechaNacimiento">
				<spring:message code="cliente.birthDate" />
			</form:label>
		
			<form:input path="fechaNacimiento" cssClass="form-control" id="datetimepicker"/>	
		
		</div><br/>
								
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="scripts/jquery.datetimepicker.full.js"></script>
		
		<script type="text/javascript">
			$('#datetimepicker').datetimepicker({format: 'd/m/Y'});
		</script>
		
		<acme:textbox code="cliente.direccion" path="direccion" /><br/>
		<div style="width:50%; margin: auto;" >
			<form:label path="sexo">
				<spring:message code="cliente.sexo" />
			</form:label>
			<form:select path="sexo" code="cliente.sexo" cssClass="form-control">
				<form:option value="HOMBRE" label="HOMBRE" />		
				<form:option value="MUJER" label="MUJER" />
			</form:select>
		</div><br />
		
		
		<acme:textbox code="cliente.nacionalidad" path="nacionalidad" /><br/>
		
		<jstl:if test="${isUsuario == true}">
		
			<acme:textarea code="usuario.estadoActual" path="estadoActual" /><br/>
		
		</jstl:if>
		
		<jstl:if test="${isEmpresario == true}">
			<acme:textbox code="empresario.cif" path="cif" /><br/><br/>
		</jstl:if>
	</jstl:if>
	<div style="width:50%; margin: auto;" >
	<acme:submit name="save" code="edit.save"/>
	
	<a href="#" class="btn btn-danger"><spring:message code="profile.return.link" /></a><br/><br/>
	</div>
</form:form>


</body>
</html>