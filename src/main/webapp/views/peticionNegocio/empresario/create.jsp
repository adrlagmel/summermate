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

<form:form action="peticionNegocio/empresario/enviar.do" modelAttribute="peticionNegocio">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="administrador" />
	<form:hidden path="empresario" />
	<form:hidden path="estado" />
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<form:hidden path="comentarios" />
	</security:authorize>

	<acme:textbox code="peticionNegocio.codigo" path="codigo" readonly="true"/>
	<acme:textbox code="peticionNegocio.fecha" path="fecha" readonly="true"/>
	<acme:textbox code="peticionNegocio.titulo" path="titulo"/>
	
	<security:authorize access="hasRole('ADMINISTRADOR')">
		<acme:textbox code="peticionNegocio.comentarios" path="comentarios"/>
	</security:authorize>
	
	<acme:submit name="save" code="peticionNegocio.guardar"/>
	<acme:cancel code="peticionNegocio.cancelar" url="/peticionNegocio/empresario/list.do" />
	
</form:form>