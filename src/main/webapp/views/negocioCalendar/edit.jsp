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


<form:form modelAttribute="calendarioNegocio" action="calendarioNegocio/empresario/deleteDates.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="negocio"/>
		
	<acme:textbox code="negocio.fechaInicio" path="fechaInicio" />
	<acme:textbox code="negocio.fechaFin" path="fechaFin" />
	<acme:textbox code="negocio.anotacionesReserva" path="anotacionesReserva" />
	
	<acme:submit name="save" code="button.save"/>
	<acme:cancel code="button.cancel" url="/negocio/empresario/lista.do" /> 
</form:form>