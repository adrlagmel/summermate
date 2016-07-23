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

	<acme:textbox code="negocio.nombre" path="nombre" readonly="true"/>
	<acme:textbox code="negocio.tipo" path="tipo" readonly="true"/>
	<acme:textbox code="negocio.descripcion" path="descripcion" readonly="true"/>
	<acme:textbox code="negocio.telefono" path="telefono" readonly="true"/>
	<acme:textbox code="negocio.paginaweb" path="paginaWeb" readonly="true"/>
	
	<jstl:if test="${!hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="images/no-image.png" height="30%" width="30%" /><br /><br />
	</jstl:if>
	
	<jstl:if test="${hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="foto/displayImageNegocio.do?negocioId=${id}" height="30%" width="30%"/><br /><br />
 	</jstl:if>
	<fieldset>
	<legend><spring:message code="negocio.localizacion" /></legend>
	
	<acme:textbox code="negocio.localizacion.tipoVia" path="localizacion.via" readonly="true"/>
	<acme:textbox code="negocio.localizacion.nombreVia" path="localizacion.nombreVia" readonly="true"/>
	<acme:textbox code="negocio.localizacion.numero" path="localizacion.numeroVia" readonly="true"/>
	<acme:textbox code="negocio.localizacion.codigoPostal" path="localizacion.codigoPostal" readonly="true"/>
	<acme:textbox code="negocio.localizacion.provincia" path="localizacion.provincia" readonly="true"/>
	<acme:textbox code="negocio.localizacion.ciudad" path="localizacion.ciudad" readonly="true"/>
	</fieldset>
	
	<acme:textbox code="negocio.playa" path="nombre" readonly="true"/>
	
	<acme:cancel code="negocio.return" url="/negocio/list.do" /> 
</form:form>