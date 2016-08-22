<%--
 * register.jsp
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

<form:form action="${actionURI}" modelAttribute="form">

	<jstl:if test="${isAdministrador==true}">
	<acme:textbox code="register.username" path="username"/><br />

	<acme:password code="register.password" path="password"/><br />
	<acme:password code="register.verify.password" path="verifyPassword"/><br />
	
	<form:errors path="registrationForm" cssClass="error" />
	
	<acme:textbox code="register.name" path="nombre"/><br />
	<acme:textbox code="register.surname" path="apellidos"/><br />
	<acme:textbox code="register.email" path="email"/><br />
	</jstl:if>

	<jstl:if test="${isAdministrador==false}">
	<acme:textbox code="register.username" path="registroForm.username"/><br />

	<acme:password code="register.password" path="registroForm.password"/><br />
	<acme:password code="register.verify.password" path="registroForm.verifyPassword"/><br />
	
	<form:errors path="registroForm" cssClass="error" />
	
	<acme:textbox code="register.name" path="registroForm.nombre"/><br />
	<acme:textbox code="register.surname" path="registroForm.apellidos"/><br />
	<acme:textbox code="register.email" path="registroForm.email"/><br />
	
	
	<acme:textbox code="register.phone" path="registroForm.telefono" /><br />
	
	<div style="width:50%;">
		<form:label path="registroForm.fechaNacimiento">
			<spring:message code="register.birthDate" />
		</form:label>
		<form:input path="registroForm.fechaNacimiento" cssClass="form-control" id="datetimepicker"/>	
	</div><br/>
									
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="scripts/jquery.datetimepicker.full.js"></script>
	
	<script type="text/javascript">
		$('#datetimepicker').datetimepicker({format: 'd/m/Y'});
	</script>
	
	<acme:textbox code="register.nacionality" path="registroForm.nacionalidad" /><br />
	<acme:textbox code="register.address" path="registroForm.direccion" /><br />
	
	<div style="width:50%;">
		<form:label path="registroForm.sexo">
			<spring:message code="register.sex" />
		</form:label>
		<form:select path="registroForm.sexo" code="register.sex" cssClass="form-control">
			<form:option value="HOMBRE" label="HOMBRE" />		
			<form:option value="MUJER" label="MUJER" />
		</form:select>
	</div><br />
	
	<%-- <acme:textbox code="register.sex" path="registroForm.sexo" /> --%>
	</jstl:if>
	<jstl:if test="${isEmpresario==true}">
	<acme:textbox code="register.cif" path="cif" /><br />
	</jstl:if>
	
	<jstl:if test="${isUsuario==true}">
	<acme:textbox code="register.actualstate" path="estadoActual" /><br />
	</jstl:if>
	
	<jstl:if test="${isAdministrador==false}">
	<security:authorize access="!hasRole('ADMINISTRADOR')">
			<form:checkbox path="registroForm.contractAccepted" />
		<form:label path="registroForm.contractAccepted"><spring:message code="register.contract.accepted1" />
			<a href="javascript: void(0);" id="termsLink" data-toggle="modal" data-target="#terms"><spring:message code="register.contract.accepted2" /></a>
			<spring:message code="register.contract.accepted3" /></form:label>
	<br />
	</security:authorize>
	</jstl:if>
					
	<br />
		<acme:submit name="register" code="register.save"/>
		
		<security:authorize access="isAnonymous()">
		<acme:cancel url="/SummerMate" code="register.cancel"/>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
		<acme:cancel url="/SummerMate" code="register.cancel"/>
		</security:authorize>
		
</form:form>


</body>
</html>
