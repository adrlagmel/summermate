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
	<form:hidden path="empresario"/><%-- 
	<form:hidden path="location.latitude" id="lat"/>
	<form:hidden path="location.longitude" id="lon"/> --%>
	
	<acme:textbox code="negocio.nombre" path="nombre" /><br/>
	<acme:textbox code="negocio.tipo" path="tipo" /><br/>
	<acme:textbox code="negocio.descripcion" path="descripcion" /><br/>
	<acme:textbox code="negocio.aforo" path="aforo" /><br/>
	<acme:textbox code="negocio.telefono" path="telefono" /><br/>
	<acme:textbox code="negocio.paginaweb" path="paginaWeb" /><br/>
	
	<jstl:if test="${negocio.imagen!=null}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImageNegocio.do?negocioId=${negocio.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${negocio.imagen==null}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="images/no-image.png" height="350" width="590" />
	</jstl:if>
	
	<fieldset>
	<legend><spring:message code="negocio.localizacion" /></legend>
	
	<div style="width:50%;">
			<form:label path="localizacion.via">
				<spring:message code="negocio.localizacion.tipoVia" />
			</form:label>
			
			<form:select path="localizacion.via" code="negocio.localizacion.tipoVia" class="form-control">
				<form:option value="Avenida" label="Avenida" />	
			 	<form:option value="Calle" label="Calle" />		
			 	<form:option value="Camino" label="Camino" />
				<form:option value="Carretera" label="Carretera" />	
				<form:option value="Plaza" label="Plaza" />				
			</form:select>
			<form:errors path="localizacion.via" cssClass="error" />
	</div><br/>
	
	<acme:textbox code="negocio.localizacion.nombreVia" path="localizacion.nombreVia" /><br/>
	<acme:textbox code="negocio.localizacion.numero" path="localizacion.numeroVia" /><br/>
	<acme:textbox code="negocio.localizacion.codigoPostal" path="localizacion.codigoPostal" /><br/>
	<acme:textbox code="negocio.localizacion.ciudad" path="localizacion.ciudad" /><br/>
	
	<div style="width:50%;">
		<form:label path="localizacion.provincia">
			<spring:message code="negocio.localizacion.provincia" />
		</form:label>
		
		<form:select path="localizacion.provincia" class="form-control" code="negocio.localizacion.provincia">
			<form:option value="Almeria" label="Almeria" />		
			<form:option value="C�diz" label="C�diz" />	
			<form:option value="Granada" label="Granada" />	
			<form:option value="Huelva" label="Huelva" />	
			<form:option value="M�laga" label="M�laga" />	
		</form:select>
		<form:errors path="localizacion.provincia" cssClass="error" />
	</div><br/>
	
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


						
					