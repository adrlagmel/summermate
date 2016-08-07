<%--
 * edit.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="evento">

	<acme:textbox code="evento.codigo" path="codigo" readonly="true"/>
	<acme:textbox code="evento.nombre" path="nombre" readonly="true"/>	
	<acme:textbox code="evento.descripcion" path="descripcion" readonly="true"/>
	<acme:textbox code="evento.precio" path="precio" readonly="true"/>
	
	<acme:textbox code="evento.negocio" path="negocio.nombre" readonly="true" />
	
	<jstl:if test="${!hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="images/no-image.png" height="30%" width="30%" /><br /><br />
	</jstl:if>
	
	<jstl:if test="${hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="foto/displayImageEvento.do?eventoId=${evento.id}" height="30%" width="30%"/><br /><br />
 	</jstl:if>
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<acme:cancel code="evento.return" url="/evento/empresario/list.do" /> 
	</security:authorize> 
	
	<security:authorize access="!hasRole('EMPRESARIO')">
		<acme:cancel code="evento.return" url="/evento/list.do" /> 
	</security:authorize> 
</form:form>