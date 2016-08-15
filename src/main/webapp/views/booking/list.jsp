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
	name="reservas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<spring:message code="booking.code" var="code" />
	<display:column property="codigo" title="${code}"
		sortable="false" />
		
	<spring:message code="booking.creationMoment" var="creationMoment" />
	<display:column property="fechaCreacion" title="${creationMoment}" format="{0,date,dd/MM/yyyy HH:mm}"
		sortable="true" />
		
	<spring:message code="booking.bookingDate" var="bookingDate" />
	<display:column property="fecha" title="${bookingDate}" format="{0,date,dd/MM/yyyy HH:mm}"
		sortable="true" />
		
	<spring:message code="booking.comensales" var="comensales" />
	<display:column property="comensales" title="${comensales}"
		sortable="false" />
		
	<spring:message code="booking.comments" var="comment" />
	<display:column title="${comment}" maxLength="20">
	<a href="javascript: void(0);" onclick="showLargeText('<jstl:out value="${row.comentarios}" />');" data-toggle="modal" data-target="#largeText"><jstl:out value="${row.comentarios}" /></a>
	</display:column>
	<spring:message code="valoracionNegocio.create" var="valoracionNegocio" />
	<display:column>
	<jsp:useBean id="now" class="java.util.Date"/>	
	<security:authentication var="user" property="principal.id" />
	<jstl:if test="${row.usuario.userAccount.id == user && now > row.fecha}">
		<a href="valoracionNegocio/usuario/create.do?reservaId=${row.id}">
				<spring:message code="valoracionNegocio.create"/>
		</a> 
	</jstl:if>
	</display:column>	
</display:table>
</div>
