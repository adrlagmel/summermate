<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="playa" action="playa/admin/edit.do" >	
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="administrador"/>
	<form:hidden path="negocios"/>
	<form:hidden path="valoracionPlayas"/>
	<form:hidden path="localizacion.latitud" id="lat"/>
	<form:hidden path="localizacion.longitud" id="lon"/>
	 	
 	<acme:textarea code="playa.nombre" path="nombre" />
	<acme:textarea code="playa.descripcion" path="descripcion" />
	<acme:textarea code="playa.composicion" path="composicion" />	
	<acme:textarea code="playa.servicios" path="servicios" />	
	<acme:textbox  code="playa.extension" path="extension" />	
	
	<fieldset>
	<legend><spring:message code="playa.localizacion" /></legend>
		<acme:textbox code="playa.localizacion.tipoVia" path="localizacion.via" />
		<acme:textbox code="playa.localizacion.nombreVia" path="localizacion.nombreVia" />
		<acme:textbox code="playa.localizacion.numero" path="localizacion.numeroVia" />
		<acme:textbox code="playa.localizacion.codigoPostal" path="localizacion.codigoPostal" />
		<acme:textbox code="playa.localizacion.ciudad" path="localizacion.ciudad" />
		<acme:textbox code="playa.localizacion.provincia" path="localizacion.provincia" />
	</fieldset>

	<!-- <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=false"></script>
	<script>
		var x = document.getElementById("lat");
		var y = document.getElementById("lon");
   		
      			navigator.geolocation.getCurrentPosition();
      			x.value = position.coords.latitude;
			y.value = position.coords.longitude;
   		
   	</script> -->
   	
   	<jstl:if test="${playa.imagen != null}">
		<b><spring:message code="playa.foto" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImage.do?playaId=${row.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${playa.imagen == null}">
		<b><spring:message code="playa.foto" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="images/no-image.png" height="350" width="590" />
	</jstl:if>
   	
	<acme:submit name="save" code="playa.save"/>
	<acme:cancel code="playa.atras" url="/playa/list.do" />
	
</form:form>
