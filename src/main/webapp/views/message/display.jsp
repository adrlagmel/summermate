<%--
 * display.jsp
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
	<br>
	<form:form modelAttribute="mostrarMensaje">
	
	<acme:textbox code="msg.moment" path="fechaEnvio" readonly="true" /><br>
	<acme:textbox code="msg.sender" path="remitente.nombre" readonly="true" /><br>
	<acme:textbox code="msg.recipient" path="beneficiario.nombre" readonly="true" /><br>
	<acme:textbox code="msg.subject" path="asunto" readonly="true" /><br>
	<div style="width:50%; margin: auto;" >
	<acme:textarea code="msg.body" path="cuerpo" readonly="true" /><br>
	
		
	<security:authentication var="user" property="principal.id" />
	
	<jstl:if test="${mostrarMensaje.beneficiario.userAccount.id==user}">
	<input type="button" class="btn btn-success" value="<spring:message code="msg.reply.link" />"
	onclick="javascript: location.replace('mensaje/actor/responder.do?mensajeId=${mostrarMensaje.id}')" />
		</jstl:if>
	<br>
		<jstl:if test="${mostrarMensaje.beneficiario.userAccount.id==user || mostrarMensaje.remitente.userAccount.id==user}">
				<input type="button" class="btn btn-danger" value="<spring:message code="msg.delete" />"
				onclick="javascript: location.replace('mensaje/actor/borrar.do?mensajeId=${mostrarMensaje.id}');
				javascript: return confirm('<spring:message code="msg.delete.del" />')" />
		</jstl:if>
		
	<input type="button" class="btn btn-primary" value="<spring:message code="msg.return.link" />"
	onclick="javascript: location.replace('mensaje/actor/lista.do?carpetaId=${mostrarMensaje.carpeta.id}')" />
	</div>
	</form:form>
		
	