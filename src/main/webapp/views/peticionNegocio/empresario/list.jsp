<%--
 * action-2.jsp
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

<form:form modelAttribute="peticionNegocio">

<jstl:if test="${peticionNegocio != null}">
	<acme:textbox code="peticionNegocio.codigo" path="codigo" readonly="true" /><br/>
	<acme:textbox code="peticionNegocio.fecha" path="fecha" readonly="true" /><br/>
	<acme:textbox code="peticionNegocio.titulo" path="titulo" readonly="true" /><br/>
	<acme:textbox code="peticionNegocio.estado" path="estado" readonly="true" /><br/>
	<acme:textbox code="peticionNegocio.comentarios" path="comentarios" readonly="true" /><br/>
</jstl:if>
	<jstl:if test="${peticionNegocio == null}">
	<security:authorize access="hasRole('EMPRESARIO')">
		<a href="peticionNegocio/empresario/enviar.do" class = "btn btn-primary"> <spring:message code="peticionNegocio.enviar" /></a>
	</security:authorize>
	</jstl:if>
	
	<acme:cancel url="/negocio/empresario/list.do" code="button.cancel"/>
</form:form>
