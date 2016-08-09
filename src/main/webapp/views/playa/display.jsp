<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="playa" >	
		
	<acme:textarea code="playa.nombre" path="nombre" readonly="true"/>
	<acme:textarea code="playa.descripcion" path="descripcion" readonly="true"/>
	<acme:textarea code="playa.composicion" path="composicion" readonly="true"/>	
	<acme:textarea code="playa.servicios" path="servicios" readonly="true"/>	
	<acme:textbox code="playa.extension" path="extension" readonly="true"/>	
	
	<fieldset>
	<legend><spring:message code="playa.localizacion" /></legend>
		<acme:textbox code="playa.localizacion.tipoVia" path="localizacion.via" readonly="true"/>
		<acme:textbox code="playa.localizacion.nombreVia" path="localizacion.nombreVia" readonly="true"/>
		<acme:textbox code="playa.localizacion.numero" path="localizacion.numeroVia" readonly="true"/>
		<acme:textbox code="playa.localizacion.codigoPostal" path="localizacion.codigoPostal" readonly="true"/>
		<acme:textbox code="playa.localizacion.ciudad" path="localizacion.ciudad" readonly="true"/>
		<acme:textbox code="playa.localizacion.provincia" path="localizacion.provincia" readonly="true"/>
	</fieldset>
	
	<jstl:if test="${playa.imagen!=null}">
		<b><spring:message code="playa.foto" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImage.do?playaId=${playa.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${playa.imagen==null}">
		<b><spring:message code="playa.foto" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="images/no-image.png" height="350" width="590" />
	</jstl:if>
		
	<%-- <security:authorize access="hasRole('ADMINISTRADOR')">
		<jstl:if test="${valoracionPlayas.size() = 0}">
			<spring:message code="playa.confirmDelete" var="confirm"/>
			<acme:submit name="delete" code="playa.delete" onclick="return confirm('${confirm}')"/>
		</jstl:if>
	</security:authorize> --%>
	
 	<acme:cancel code="playa.atras" url="/playa/list.do" />
		
</form:form>
