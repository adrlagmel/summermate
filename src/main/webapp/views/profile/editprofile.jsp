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

<form:form action="${actionURI}" modelAttribute="actor">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="carpetas"/>
	
	<form:hidden path="userAccount.password"/>
	<form:hidden path="userAccount.username"/>
	<form:hidden path="userAccount.authorities" value="${authorities}" />
	
	<jstl:if test="${isCliente == true}">
		<form:hidden path="denuncias"/>
	</jstl:if>
	
	<jstl:if test="${isUsuario == true}">
		<form:hidden path="valoracionPlayas"/>
		<form:hidden path="eventos"/>
		<form:hidden path="reservas"/>
		<form:hidden path="puntos"/>
		<form:hidden path="nivelColaborador"/>
		<form:hidden path="imagen"/>
	</jstl:if>
	
	<jstl:if test="${isEmpresario == true}">
		<form:hidden path="negocios"/>
	</jstl:if>
	
	<acme:textbox code="actor.name" path="nombre" /><br/>
	<acme:textbox code="actor.surname" path="apellidos"  /><br/>
	<acme:textbox code="actor.email" path="email"  /><br/>
		
	<jstl:if test="${isCliente == true}">
		<acme:textbox code="actor.phone" path="telefono" /><br/>
		<acme:textbox code="cliente.birthDate" path="fechaNacimiento" /><br/>
		<acme:textbox code="cliente.direccion" path="direccion" /><br/>
		<acme:textbox code="cliente.sexo" path="sexo" /><br/>
		<acme:textbox code="cliente.nacionalidad" path="nacionalidad" /><br/>
		
		<jstl:if test="${isUsuario == true}">
			<acme:textbox code="usuario.estadoActual" path="estadoActual" /><br/>
		</jstl:if>
		
		<jstl:if test="${isEmpresario == true}">
			<acme:textbox code="empresario.cif" path="cif" /><br/><br/>
		</jstl:if>
	</jstl:if>
	
	<acme:submit name="save" code="register.save"/>
	
	<a href="#" class="btn btn-danger"><spring:message code="profile.return.link" /></a><br/>
	
</form:form>