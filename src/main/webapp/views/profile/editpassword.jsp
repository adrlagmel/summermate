<%--profile.jsp
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

<form:form action="${actionURI}" modelAttribute="actor">

	<br>
	<acme:password code="edit.passwordOld" path="oldPassword"/><br/><br/>
	
	
	<fieldset>
		<div style="width:50%; margin: auto;" >
		<legend><spring:message code="edit.password.edicion" /></legend></div>
		<acme:password code="edit.password" path="password"/><br />
		<acme:password code="edit.verify.password" path="verifyPassword"/><br />
	</fieldset>
	<div style="width:50%; margin: auto;" >
	<acme:submit name="save" code="edit.save"/>
	<a href="#" class="btn btn-danger"><spring:message code="profile.return.link" /></a><br/><br/>
	</div>
</form:form>