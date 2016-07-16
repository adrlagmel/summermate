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

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="peticionNegocios" requestURI="peticionnegocio/administrador/listapendiente.do" id="row">
	<!-- Attributes -->

	<spring:message code="peticionNegocio.codigo" var="codigo" />
	<display:column property="codigo" title="${codigo}"
		sortable="true" />

	<spring:message code="peticionNegocio.fecha" var="fecha" />
	<display:column property="fecha" title="${fecha}"
		sortable="true" format="{0,date,dd/MM/yyyy}" />
		
	<spring:message code="peticionNegocio.titulo" var="titulo" />
	<display:column property="titulo" title="${titulo}"
		sortable="false" />
		
	<spring:message code="peticionNegocio.estado" var="estado" />
	<display:column property="estado" title="${estado}"
		sortable="false" />
		
	<spring:message code="peticionNegocio.comentarios" var="comentarios" />
	<display:column property="comentarios" title="${comentarios}"
		sortable="false" />
		
	
	<security:authorize access="hasRole('ADMINISTRADOR')">
	<display:column>
		<a href="peticionNegocio/administrador/aceptar.do?peticionNegocioId=${row.id}" ><spring:message code="peticionNegocio.aceptar.link" /></a>
	</display:column>
		
	<display:column>
		<a href="peticionNegocio/administrador/rechazar.do?peticionNegocioId=${row.id}" ><spring:message code="peticionNegocio.rechazar.link" /></a>
	</display:column>
	
	</security:authorize>
	
</display:table>
</div>
