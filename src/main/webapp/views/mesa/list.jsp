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
<display:table pagesize="5" class="table table-condensed" keepStatus="true" name="mesas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<display:column>
		<security:authentication var="user" property="principal.id" />

			<jstl:if test="${row.negocio.empresario.userAccount.id == user}">
				<a href="mesa/empresario/edit.do?mesaId=${row.id}"><spring:message code="negocio.edit" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<spring:message code="mesa.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}" sortable="true" />
	
	<spring:message code="mesa.personas" var="personas" />
	<display:column property="personas" title="${personas}" sortable="true" />
	
	<spring:message code="mesa.disponible" var="disponible" />
	<display:column property="disponible" title="${disponible}" sortable="true" />
	
	<spring:message code="mesa.fumadores" var="fumadores" />
	<display:column property="fumadores" title="${fumadores}" sortable="true" />
		
<%-- 	<security:authorize access="hasRole('EMPRESARIO')">
	<display:column>
	<security:authentication var="user" property="principal.id" />
		<jstl:if test="${row.negocio.empresario.userAccount.id == user}">
		<a href="mesa/empresario/display.do?mesaId=${row.id}"><spring:message code="mesa.empresario.display" /></a>
		</jstl:if>
	</display:column> --%>
	
	<%-- <display:column>
		<jstl:if test="${row.empresario.userAccount.id == user}">
		<a href="mesaCalendar/sportPartner/deleteDates.do?mesaId=${row.id}"><spring:message code="mesaCalendar.deleteDates" /></a>
	</jstl:if>
	</display:column> 
	
	</security:authorize> --%>
		
	<%-- <display:column>
			<a href="mesa/empresario/display.do?mesaId=${row.id}"><spring:message code="mesa.display" /></a>
	</display:column> --%>
	
<%-- 	<display:column>
		<a href="mesa/planner.do?mesaId=${row.id}"><spring:message code="mesa.displayPlanner" /></a>
	</display:column> --%>
	
</display:table>
</div>

	<security:authorize access="hasRole('EMPRESARIO')">
		<jstl:if test="${!mesas.isEmpty() || allowRegistermesa == true}">
		<a href="mesa/empresario/register.do?negocioId=${row.negocio.id}">
			<spring:message code="mesa.create"/>
		</a> <br/>	
		</jstl:if>
		<br />
	</security:authorize>