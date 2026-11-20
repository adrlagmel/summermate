<%--
 * action-1.jsp
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="peticionNegocio/administrador/aceptar.do" modelAttribute="peticionNegocio">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<br/>
	<acme:readonly code="peticionNegocio.empresario" value="${peticion.empresario.nombre}"/><br/>
	<acme:readonly code="peticionNegocio.codigo" value="${peticion.codigo}"/><br/>
	<acme:readonly code="peticionNegocio.fecha" value="${peticion.fecha}"/><br/>
	<acme:readonly code="peticionNegocio.titulo" value="${peticion.titulo}"/><br/>
	<acme:readonly code="peticionNegocio.estado" value="${peticion.estado}"/><br/>
	<acme:textarea code="peticionNegocio.comentarios" path="comentarios"/><br/>
	
	<spring:message code="peticionNegocio.aceptar.confirmar" var="confirmar"/>
	<div style="width:50%; margin: auto;" >
		<acme:submit name="aceptar" code="peticionNegocio.aceptar.confirmar" onclick="return confirm('${confirmar}')"/>					
		<acme:cancel code="peticionNegocio.aceptar.cancelar" url="/peticionNegocio/administrador/listapendiente.do" />
	</div>
</form:form>
