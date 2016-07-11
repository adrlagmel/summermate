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
	
	<acme:textbox code="mesa.nombre" path="nombre" />
	<acme:textbox code="mesa.disponible" path="disponible" />
	<acme:textbox code="mesa.fumadores" path="fumadores" />
	<acme:textbox code="mesa.personas" path="personas" />
	
	<acme:submit name="save" code="mesa.save"/>
	<acme:cancel code="mesa.cancel" url="/mesa/empresario/list.do" /> 
	
</form:form>


						
					