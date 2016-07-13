<%--
 * register.jsp
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

<form:form action="${actionURI}" modelAttribute="form">

	<jstl:if test="${isAdministrador==true}">
	<acme:textbox code="register.username" path="username"/>

	<acme:password code="register.password" path="password"/>
	<acme:password code="register.verify.password" path="verifyPassword"/>
	
	<form:errors path="registrationForm" cssClass="error" />
	
	<acme:textbox code="register.name" path="nombre"/>
	<acme:textbox code="register.surname" path="apellidos"/>
	<acme:textbox code="register.email" path="email"/>
	</jstl:if>

	<jstl:if test="${isAdministrador==false}">
	<acme:textbox code="register.username" path="registroForm.username"/>

	<acme:password code="register.password" path="registroForm.password"/>
	<acme:password code="register.verify.password" path="registroForm.verifyPassword"/>
	
	<form:errors path="registroForm" cssClass="error" />
	
	<acme:textbox code="register.name" path="registroForm.nombre"/>
	<acme:textbox code="register.surname" path="registroForm.apellidos"/>
	<acme:textbox code="register.email" path="registroForm.email"/>
	
	
	<acme:textbox code="register.phone" path="registroForm.telefono" />
	<acme:textbox code="register.birthDate" path="registroForm.fechaNacimiento" />
	<acme:textbox code="register.nacionality" path="registroForm.nacionalidad" />
	<acme:textbox code="register.address" path="registroForm.direccion" />
	<acme:textbox code="register.sex" path="registroForm.sexo" />
	</jstl:if>
	<jstl:if test="${isEmpresario==true}">
	<acme:textbox code="register.cif" path="cif" />
	</jstl:if>
	
	<jstl:if test="${isUsuario==true}">
	<acme:textbox code="register.actualstate" path="estadoActual" />
	<acme:textbox code="register.levelC" path="nivelColaborador" />
	<acme:textbox code="register.points" path="puntos" />
	</jstl:if>
	
	<jstl:if test="${isAdministrador==false}">
	<security:authorize access="!hasRole('ADMINISTRADOR')">
			<form:checkbox path="registroForm.contractAccepted" />
		<form:label path="registroForm.contractAccepted"><spring:message code="register.contract.accepted1" />
			<a href="javascript: void(0);" id="termsLink" data-toggle="modal" data-target="#terms"><spring:message code="register.contract.accepted2" /></a>
			<spring:message code="register.contract.accepted3" /></form:label>
	<br />
	</security:authorize>
	</jstl:if>
					
	<br />
		<acme:submit name="register" code="register.save"/>
		
		<security:authorize access="isAnonymous()">
		<acme:cancel url="/SummerMate" code="register.cancel"/>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
		<acme:cancel url="/SummerMate" code="register.cancel"/>
		</security:authorize>
		
</form:form>