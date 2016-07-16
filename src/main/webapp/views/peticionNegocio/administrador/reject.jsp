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

<h2><spring:message code="peticionNegocio.administrador.rechazar.text" /></h2>

<form:form action="peticionNegocio/administrador/rechazar.do" modelAttribute="peticionNegocio">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="administrador" />
	<form:hidden path="empresario" />
	
	<acme:textbox code="peticionNegocio.empresario" path="empresario.nombre" readonly="true"/>
	<acme:textbox code="peticionNegocio.codigo" path="codigo" readonly="true"/>
	<acme:textbox code="peticionNegocio.fecha" path="fecha" readonly="true"/>
	<acme:textbox code="peticionNegocio.titulo" path="titulo" readonly="true"/>
	<acme:textbox code="peticionNegocio.estado" path="estado" readonly="true"/>
	<acme:textbox code="peticionNegocio.comentarios" path="comentarios"/>
				
	<spring:message code="peticionNegocio.rechazar.confirmar" var="confirmar"/>
	<acme:submit name="rechazar" code="peticionNegocio.reachazar.aceptar" onclick="return confirm('${confirmar}')"/>		
			
	<acme:cancel code="peticionNegocio.rechazar.cancelar" url="/peticionNegocio/administrador/listapendiente.do" />
	
</form:form>
