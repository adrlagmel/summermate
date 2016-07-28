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


<%-- <form action="${actionURI}">
      <input type="text" name="s" placeholder="<spring:message code="evento.searchByTown"/>">
      
      <button type="submit"><spring:message code="evento.search" /></button>
</form> --%>


<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true" name="eventos" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<display:column>
		<security:authentication var="user" property="principal.id" />

			<jstl:if test="${row.negocio.empresario.userAccount.id == user}">
				<a href="evento/empresario/edit.do?eventoId=${row.id}"><spring:message code="evento.edit" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<spring:message code="evento.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}" sortable="true" />
	
	<spring:message code="evento.provincia" var="provincia" />
	<display:column property="negocio.localizacion.provincia" title="${provincia}" sortable="true" />
	
	<spring:message code="evento.ciudad" var="ciudad" />
	<display:column property="negocio.localizacion.ciudad" title="${ciudad}" sortable="true" />
		
	<spring:message code="evento.playa" var="playa" />
	<display:column property="negocio.playa.nombre" title="${playa}" sortable="true" />
		
	<display:column>
			<a href="evento/empresario/display.do?eventoId=${row.id}"><spring:message code="evento.display" /></a>
	</display:column>

</display:table>
</div>

	<security:authorize access="hasRole('EMPRESARIO')">
		<a href="evento/empresario/create.do">
			<spring:message code="evento.create"/>
		</a> <br/>	
		<br />
	</security:authorize>