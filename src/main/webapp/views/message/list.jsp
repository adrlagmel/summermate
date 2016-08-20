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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="carpeta.mensajes" requestURI="/mensaje/actor/lista.do" id="row">
	
	<!-- Attributes -->
	
	<spring:message code="msg.moment" var="moment" />
	<display:column property="fechaEnvio" title="${moment}"
		sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />

	<spring:message code="msg.sender" var="sender" />
	<display:column property="remitente.nombre" title="${sender}"
		sortable="true" />

	<spring:message code="msg.recipient" var="recipient" />
	<display:column property="beneficiario.nombre" title="${recipient}"
		sortable="true" />
		
	<spring:message code="msg.subject" var="subject" />
	<display:column property="asunto" title="${subject}"
		sortable="false" />
		
	<!-- Action links -->

	<display:column>
		<a href="mensaje/actor/mostrar.do?mensajeId=${row.id}" class="btn btn-info"><spring:message code="msg.display.link" /></a>
	</display:column>
	
</display:table>
</div>
	<a href="mensaje/actor/enviar.do" class="btn btn-primary"><spring:message code="msg.new.send.link" /></a>

	<acme:cancel code="msg.return.link" url="carpeta/actor/lista.do" /> 
	
	
		
	