<%--
 * action-1.jsp
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="peticionNegocio/empresario/enviar.do" modelAttribute="peticionNegocio">

	<br>

	<%-- El código y la fecha se asignan al enviar la petición --%>
	<acme:textbox code="peticionNegocio.titulo" path="titulo"/><br/>
	
	<div style="width:50%; margin: auto;" >
	<acme:submit name="save" code="peticionNegocio.guardar"/>
	<acme:cancel code="peticionNegocio.cancelar" url="/peticionNegocio/empresario/list.do" />
	</div>
</form:form>