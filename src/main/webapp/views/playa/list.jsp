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

	<spring:message code="playa.localizacion.provincia" var="provincia" />
	<display:column property="localizacion.provincia" title="${provincia}"
		sortable="true" />
		
	<spring:message code="playa.localizacion.ciudad" var="ciudad" />
	<display:column property="localizacion.ciudad" title="${ciudad}"
		sortable="true" />
		
	<spring:message code="playa.descripcion" var="descripcion" />
	<display:column property="descripcion" title="${descripcion}"
		sortable="true" />
		
	<spring:message code="playa.detallePlaya" var="detallePlaya" />
		<display:column title="${detallePlaya}" sortable="false">
			<a href="playa/display.do?playaId=${row.id}"> <spring:message code="playa.detallePlaya" />  </a>					
	</display:column>
	
	<security:authentication var="user" property="principal.id" />
	<display:column>
		<jstl:if test="${row.administrador.userAccount.id == user}">
		<a href="playa/admin/edit.do?playaId=${row.id}">
			<spring:message code="playa.editar"/>
		</a>
		
		</jstl:if>
		</display:column>
		<security:authorize access="hasRole('ADMINISTRADOR')">
		<display:column>
			<jstl:if test="${row.foto==null}">
				<a href="playa/admin/uploadImage.do?playaId=${row.id}"><spring:message code="playa.uploadImage" /></a>
			</jstl:if>
		</display:column>
		<spring:message code="playa.delete" var="borrar" />
			<display:column>
			<input type="button" value="<spring:message code="playa.delete" />"
						onclick="javascript: location.replace('playa/admin/borrar.do?playaId=${row.id}');
						javascript: return confirm('<spring:message code="playa.confirmDelete" />')" />
			</display:column>
		</security:authorize>
		<security:authorize access="hasRole('USUARIO')">
		<spring:message code="valoracionPlaya.valoracionPlaya" var="valorarPlaya" />
		<display:column title="${valorarPlaya}" sortable="false">
		<a href="valoracionPlaya/usuario/create.do?playaId=${row.id}"> 
					<spring:message code="valoracionPlaya.valoracionPlaya" />
					   </a>	
		</display:column>
		</security:authorize>
		
	
</display:table>
</div>