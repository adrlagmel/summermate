<%--
 * action-1.jsp
 *
 * Copyright (C) 2013 Universidad de Sevilla
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

<form:form action="peticionNegocio/administrador/aceptar.do" modelAttribute="peticionNegocio">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="administrador" />
	<form:hidden path="empresario" />
	<br/>
	<acme:textbox code="peticionNegocio.empresario" path="empresario.nombre" readonly="true"/><br/>
	<acme:textbox code="peticionNegocio.codigo" path="codigo" readonly="true"/><br/>
	<acme:textbox code="peticionNegocio.fecha" path="fecha" readonly="true"/><br/>
	<acme:textbox code="peticionNegocio.titulo" path="titulo" readonly="true"/><br/>
	<acme:textbox code="peticionNegocio.estado" path="estado" readonly="true"/><br/>
	<acme:textarea code="peticionNegocio.comentarios" path="comentarios"/><br/>
	
	<spring:message code="peticionNegocio.aceptar.confirmar" var="confirmar"/>
	<div style="width:50%; margin: auto;" >
		<acme:submit name="aceptar" code="peticionNegocio.aceptar.confirmar" onclick="return confirm('${confirmar}')"/>					
		<acme:cancel code="peticionNegocio.aceptar.cancelar" url="/peticionNegocio/administrador/listapendiente.do" />
	</div>
</form:form>
