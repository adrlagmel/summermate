<%--
 * edit.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div id="miniplanner" class="pull-right" style="width: 500px; height: 500px;"></div>

<form:form action="reserva/usuario/create.do" modelAttribute="form">

	<form:hidden path="reservaId"/>

	<acme:textbox code="booking.bookingDate" path="fecha" onchange="cargarMiniPlanner()" />
	<acme:textbox code="booking.price" path="precio" />
	<acme:textarea code="booking.comments" path="comentarios" />
		
	<acme:select items="${negocios}" itemLabel="nombre" code="booking.negocio" path="negocio" onchange="cargarMiniPlanner()"/>
	
	<acme:submit name="save" code="booking.pay"/>

	<acme:cancel code="booking.cancel" url="/reserva/usuario/lista.do" />

</form:form>

	