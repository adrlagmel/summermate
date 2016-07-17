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

<form:form modelAttribute="negocio" action="negocio/empresario/edit.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="eventos"/>
	<form:hidden path="reservas"/>
	<form:hidden path="empresario"/>
	<form:hidden path="mesas"/><%-- 
	<form:hidden path="location.latitude" id="lat"/>
	<form:hidden path="location.longitude" id="lon"/> --%>
	
	<acme:textbox code="negocio.nombre" path="nombre" />
	<acme:textbox code="negocio.tipo" path="tipo" />
	<acme:textbox code="negocio.descripcion" path="descripcion" />
	<acme:textbox code="negocio.telefono" path="telefono" />
	<acme:textbox code="negocio.paginaweb" path="paginaWeb" />
	
	<fieldset>
	<legend><spring:message code="negocio.localizacion" /></legend>
	
	<acme:textbox code="negocio.localizacion.tipoVia" path="localizacion.via" />
	<acme:textbox code="negocio.localizacion.nombreVia" path="localizacion.nombreVia" />
	<acme:textbox code="negocio.localizacion.numero" path="localizacion.numeroVia" />
	<acme:textbox code="negocio.localizacion.codigoPostal" path="localizacion.codigoPostal" />
	<acme:textbox code="negocio.localizacion.ciudad" path="localizacion.ciudad" />
	<acme:textbox code="negocio.localizacion.provincia" path="localizacion.provincia" />
	</fieldset>
		
	<acme:submit name="save" code="negocio.save"/>
	<acme:cancel code="negocio.cancel" url="/negocio/empresario/list.do" /> 
	
	<!-- <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=false"></script>
	<script>
		var x = document.getElementById("lat");
		var y = document.getElementById("lon");
   		
      			navigator.geolocation.getCurrentPosition();
      			x.value = position.coords.latitude;
			y.value = position.coords.longitude;
   		
   	</script> -->
</form:form>


						
					