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

<form:form modelAttribute="negocio">
	<br/>
		<div style="width:50%; margin: auto;" >
	<jstl:if test="${negocio.imagen!=null}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImageNegocio.do?negocioId=${negocio.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${negocio.imagen==null}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="images/no-image.png" height="350" width="590" />
	</jstl:if>
		<br/>
	</div>
	<acme:textbox code="negocio.nombre" path="nombre" readonly="true"/><br/>
	<acme:textbox code="negocio.tipo" path="tipo" readonly="true"/><br/>
	
		<acme:textarea code="negocio.descripcion" path="descripcion" readonly="true"/><br/>
	
	<acme:textbox code="negocio.aforo" path="aforo" readonly="true"/><br/>
	<acme:textbox code="negocio.telefono" path="telefono" readonly="true"/><br/>
	<acme:textbox code="negocio.paginaweb" path="paginaWeb" readonly="true"/><br/>
	<acme:textbox code="negocio.valoracionMedia" path="valoracionMedia" readonly="true"/><br/>
	
	<acme:textbox code="negocio.playa" path="nombre" readonly="true"/><br/>
		<div style="width:50%; margin: auto;" >
	<acme:cancel code="negocio.return" url="/negocio/list.do" /> 
	</div>
</form:form>