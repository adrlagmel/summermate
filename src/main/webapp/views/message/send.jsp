<%--
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


<form:form action="mensaje/actor/enviar.do" modelAttribute="m">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="remitente" />
	<form:hidden path="carpeta" />
	<form:hidden path="fechaEnvio" /><br><br>
	<div style="width:50%; margin: auto;" >
	<jstl:if test="${m.beneficiario == null}">
	
		<acme:select items="${actors}" itemLabel="email" code="msg.recipient" path="beneficiario"/>
	
	</jstl:if><br>
	
	<jstl:if test="${m.beneficiario != null}">
	
		<form:hidden path="beneficiario" />
		
		<b><spring:message code="msg.recipient"/>: </b><jstl:out value="${m.beneficiario.nombre}" />
		
	</jstl:if><br>
	</div>
	<acme:textbox code="msg.subject" path="asunto"/><br>
	<div style="width:50%; margin: auto;" >
	<acme:textarea code="msg.body" path="cuerpo"/><br>
				
					
	<acme:submit name="save" code="msg.send.link"/>				
	
	<acme:cancel url="/" code="msg.cancel.link"/>	
	
	</div>		

</form:form>
