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

<form:form modelAttribute="actor">

	<acme:textbox code="actor.name" path="nombre" readonly="true" /><br/>
	<acme:textbox code="actor.surname" path="apellidos" readonly="true" /><br/>
	<acme:textbox code="actor.email" path="email" readonly="true" /><br/>
		
	<jstl:if test="${isCliente == true}">
		<acme:textbox code="actor.phone" path="telefono" readonly="true"/><br/>
		<acme:textbox code="cliente.birthDate" path="fechaNacimiento" readonly="true"/><br/>
		<acme:textbox code="cliente.direccion" path="direccion" readonly="true"/><br/>
		<acme:textbox code="cliente.sexo" path="sexo" readonly="true"/><br/>
		<acme:textbox code="cliente.nacionalidad" path="nacionalidad" readonly="true"/><br/>
		
		<jstl:if test="${isUsuario == true}">
			<acme:textbox code="usuario.estadoActual" path="estadoActual" readonly="true"/><br/>
			<acme:textbox code="usuario.nivelColaborador" path="nivelColaborador" readonly="true"/><br/>
			<acme:textbox code="usuario.puntos" path="puntos" readonly="true"/><br/>
		</jstl:if>
		
		<jstl:if test="${isEmpresario == true}">
			<acme:textbox code="empresario.cif" path="cif" readonly="true"/><br/><br/>
		</jstl:if>
	</jstl:if>
	
	<jstl:if test="${isUsuario == true}">
		<a href="perfil/usuario/edit.do?usuarioId=${actor.id}"class="btn btn-danger"><spring:message code="profile.editar" /></a><br/>
	</jstl:if>
	
	<jstl:if test="${isEmpresario == true}">
		<a href="perfil/empresario/edit.do?empresarioId=${actor.id}"class="btn btn-danger"><spring:message code="profile.editar" /></a><br/>
	</jstl:if>
	
	<a href="#" class="btn btn-danger"><spring:message code="profile.return.link" /></a><br/>
	
</form:form>