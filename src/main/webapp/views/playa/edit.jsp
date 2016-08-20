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
	 	
 	<acme:textbox code="playa.nombre" path="nombre" /><br/>
	<acme:textarea code="playa.descripcion" path="descripcion" /><br/>
	<acme:textbox code="playa.composicion" path="composicion" /><br/>
	<acme:textarea code="playa.servicios" path="servicios" /><br/>
	<acme:textbox  code="playa.extension" path="extension" /><br/>
	
	<fieldset>
	<legend><spring:message code="playa.localizacion" /></legend>
		<div style="width:50%;">
			<form:label path="localizacion.via">
				<spring:message code="playa.localizacion.tipoVia" />
			</form:label>
			
			<form:select path="localizacion.via" code="playa.localizacion.tipoVia" class="form-control">
				<form:option value="Avenida" label="Avenida" />	
			 	<form:option value="Calle" label="Calle" />		
			 	<form:option value="Camino" label="Camino" />
				<form:option value="Carretera" label="Carretera" />	
				<form:option value="Plaza" label="Plaza" />				
			</form:select>
			<form:errors path="localizacion.via" cssClass="error" />
		</div><br/>
		
		<acme:textbox code="playa.localizacion.nombreVia" path="localizacion.nombreVia" /><br/>
		<acme:textbox code="playa.localizacion.numero" path="localizacion.numeroVia" /><br/>
		<acme:textbox code="playa.localizacion.codigoPostal" path="localizacion.codigoPostal" /><br/>
		
		<div style="width:50%;">
			<form:label path="localizacion.provincia">
				<spring:message code="playa.localizacion.provincia" />
			</form:label>
			
			<form:select path="localizacion.provincia" class="form-control" code="playa.localizacion.provincia">
				<form:option value="Almeria" label="Almeria" />		
				<form:option value="Cádiz" label="Cádiz" />	
				<form:option value="Granada" label="Granada" />	
				<form:option value="Huelva" label="Huelva" />	
				<form:option value="Málaga" label="Málaga" />	
			</form:select>
			<form:errors path="localizacion.provincia" cssClass="error" />
		</div><br/>
		
		<acme:textbox code="playa.localizacion.ciudad" path="localizacion.ciudad" /><br/>
	</fieldset>

	<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&key=AIzaSyBBrx34wk6LRo3M4xsVJEe3U9umurfaMik" ></script>
	<script>
		var x = document.getElementById("lat");
		var y = document.getElementById("lon");
   		
      			navigator.geolocation.getCurrentPosition();
      			x.value = position.coords.latitude;
			y.value = position.coords.longitude;
   		
   	</script>
   	
   <jstl:if test="${playa.imagen!=null}">
		<b><spring:message code="playa.foto" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImage.do?playaId=${playa.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${playa.imagen==null}">
		<b><spring:message code="playa.foto" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="images/no-image.png" height="350" width="590" />
	</jstl:if>
   	<br/>
   	
	<acme:submit name="save" code="playa.save"/>
	<acme:cancel code="playa.atras" url="/playa/list.do" />
	
</form:form>
