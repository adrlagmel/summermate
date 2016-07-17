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


<form:form modelAttribute="mesa" action="mesa/empresario/edit.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="negocio"/>
	<form:hidden path="calendarioNegocios"/>
	
	<acme:textbox code="mesa.nombre" path="nombre" /><br>
	<spring:message code="mesa.afirmativo" var="positivo" />
	<spring:message code="mesa.negativo" var="negativo" />
	<spring:message code="mesa.disponible" />
	<div style="width:30%;">
		<form:select path="disponible" cssClass="form-control">
			<form:option value= "true" label = "${positivo}" />		
			<form:option value= "false" label = "${negativo}"/>
		</form:select>
	</div>	<br>
	<spring:message code="mesa.fumadores" />
	<div style="width:30%;">
		<form:select path="fumadores" cssClass="form-control">
			<form:option value= "true" label = "${positivo}" />		
			<form:option value= "false" label = "${negativo}"/>
		</form:select>
	</div><br>
	
	<spring:message code="mesa.personas" />
	<div style="width:30%;">
		<form:select path="personas" cssClass="form-control">
			<form:option value= "1" label = "1" />		
			<form:option value= "2" label = "2"/>
			<form:option value= "3" label = "3" />		
			<form:option value= "4" label = "4"/>
			<form:option value= "5" label = "5" />		
			<form:option value= "6" label = "6"/>
			<form:option value= "7" label = "7" />		
			<form:option value= "8" label = "8"/>
			<form:option value= "9" label = "9" />		
			<form:option value= "10" label = "10"/>
			<form:option value= "11" label = "11" />		
			<form:option value= "12" label = "12"/>
			<form:option value= "13" label = "13"/>
			<form:option value= "14" label = "14" />		
			<form:option value= "15" label = "15"/>
		</form:select>
	</div><br>
	
	<acme:submit name="save" code="mesa.save"/>
	<acme:cancel code="mesa.cancel" url="/mesa/empresario/list.do" /> 
	
</form:form>

					