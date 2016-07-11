<%--
 * action-2.jsp
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

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="playas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<spring:message code="playa.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}"
		sortable="true" />

	<spring:message code="playa.provincia" var="provincia" />
	<display:column property="provincia" title="${provincia}"
		sortable="true" />
		
		<spring:message code="playa.municipio" var="municipio" />
	<display:column property="municipio" title="${municipio}"
		sortable="true" />
		
		<spring:message code="playa.descripcion" var="descripcion" />
	<display:column property="descripcion" title="${descripcion}"
		sortable="true" />
	<spring:message code="playa.detallePlaya" var="detallePlaya" />
		<display:column title="${detallePlaya}" sortable="false">
		
					<a href="playa/edit.do?playaId=${row.id}"> 
					<spring:message code="playa.detallePlaya" />
					   </a>					
	
		</display:column>
	
</display:table>
</div>