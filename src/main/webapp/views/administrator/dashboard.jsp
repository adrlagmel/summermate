<%--
 * list.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<legend><spring:message code="dashboard.playa" /></legend>
<b><spring:message code="dashboard.playaMejorValorada"/></b>
<div class="table-responsive">
<display:table name="playaMejorValorada" class="table table-condensed" sort="list" pagesize="5" requestURI="${requestURI}" id="table1">
	<spring:message code="dashboard.playa.nombre" var="nombre"/>
	<display:column property="nombre" title="${nombre}" sortable="false"/>
	
	<spring:message code="dashboard.playa.valoracionMedia" var="valoracionMedia"/>
	<display:column property="valoracionMedia" title="${valoracionMedia}" sortable="false"/>
	
</display:table>
</div>

<b><spring:message code="dashboard.playaConMasValoraciones"/></b>
<div class="table-responsive">
<display:table name="playaConMasValoraciones" class="table table-condensed" sort="list" pagesize="5" requestURI="${requestURI}" id="table1">
	<spring:message code="dashboard.playa.nombre" var="nombre"/>
	<display:column property="nombre" title="${nombre}" sortable="false"/>
	
	<spring:message code="dashboard.playa.numeroValoraciones" var="valoracionPlayas"/>
	<display:column property="localizacion.provincia" title="${valoracionPlayas}" sortable="false"/>
	
</display:table>
</div>

<legend><spring:message code="dashboard.negocio" /></legend>
<b><spring:message code="dashboard.negocioMejorValorado"/></b>
<div class="table-responsive">
<display:table name="negocioMejorValorado" class="table table-condensed" sort="list" pagesize="5" requestURI="${requestURI}" id="table1">
	<spring:message code="dashboard.negocio.nombre" var="nombre"/>
	<display:column property="nombre" title="${nombre}" sortable="false"/>
	
	<spring:message code="dashboard.playa.valoracionMedia" var="valoracionMedia"/>
	<display:column property="valoracionMedia" title="${valoracionMedia}" sortable="false"/>
	
</display:table>
</div>

<b><spring:message code="dashboard.negocioMejorValorado"/></b>
<div class="table-responsive">
<display:table name="negocioMasReservas" class="table table-condensed" sort="list" pagesize="5" requestURI="${requestURI}" id="table1">
	<spring:message code="dashboard.negocio.nombre" var="nombre"/>
	<display:column property="nombre" title="${nombre}" sortable="false"/>
	
	<spring:message code="dashboard.negocio.empresario" var="valoracionMedia"/>
	<display:column property="empresario.nombre" title="${valoracionMedia}" sortable="false"/>
	
</display:table>
</div>

<legend><spring:message code="dashboard.usuario" /></legend>
<b><spring:message code="dashboard.usuariosTotales"/></b>
<div class="table-responsive">
<display:table name="usuariosTotales" class="table table-condensed" sort="list" pagesize="5" requestURI="${requestURI}" id="table1">
	<spring:message code="dashboard.usuario.nombre" var="nombre"/>
	<display:column property="nombre" title="${nombre}" sortable="false"/>
	
	<spring:message code="dashboard.usuario.apellidos" var="valoracionMedia"/>
	<display:column property="apellidos" title="${valoracionMedia}" sortable="false"/>
	
</display:table>
</div>
