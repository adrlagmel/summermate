<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" enctype="multipart/form-data" method="post"  modelAttribute="playa">
		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:if test="${createanddelete==true}">
 	
	<acme:textbox code="playa.nombre" path="nombre" readonly="false"/>	
	<acme:textbox code="playa.provincia" path="provincia" readonly="false"/>	
	<acme:textbox code="playa.municipio" path="municipio" readonly="false"/>	
	<acme:textarea code="playa.descripcion" path="descripcion" readonly="false"/>	
	<acme:textarea code="playa.composicion" path="tipoPlaya.composicion" readonly="false"/>	
	<acme:textarea code="playa.condiciones" path="tipoPlaya.condiciones" readonly="false"/>	
	<acme:textarea code="playa.ocupacion" path="tipoPlaya." readonly="false"/>	
	<acme:textarea code="playa.servicios" path="tipoPlaya.servicios" readonly="false"/>	
	<acme:textarea code="playa.transporte" path="tipoPlaya.transporte" readonly="false"/>	
	<acme:textarea code="playa.acceso" path="tipoPlaya.acceso" readonly="false"/>	
	<acme:textbox code="playa.longitud" path="tipoPlaya.longitud" readonly="false"/>	
	<acme:textbox code="playa.anchura" path="tipoPlaya.anchura" readonly="false"/>	
	<acme:textarea code="playa.paseoMaritimo" path="tipoPlaya.paseoMaritimo" readonly="false"/>	
	<acme:textarea code="playa.zonaFondeo" path="tipoPlaya.zonaFondeo" readonly="false"/>	
	<acme:textarea code="playa.vegetacion" path="tipoPlaya.vegetacion" readonly="false"/>	
	<acme:textarea code="playa.espacioProtegido" path="tipoPlaya.espacioProtegido" readonly="false"/>	
	

	<acme:submit name="save" code="playa.save"/>
	<acme:cancel code="playa.atras" url="/playa/admin/list.do" />
	
	
	</jstl:if>
	<jstl:if test="${createanddelete==false}">
	
	<acme:textarea code="playa.composicion" path="tipoPlaya.composicion" readonly="true"/>	
	<acme:textarea code="playa.condiciones" path="tipoPlaya.condiciones" readonly="true"/>	
	<acme:textarea code="playa.ocupacion" path="tipoPlaya." readonly="true"/>	
	<acme:textarea code="playa.servicios" path="tipoPlaya.servicios" readonly="true"/>	
	<acme:textarea code="playa.transporte" path="tipoPlaya.transporte" readonly="true"/>	
	<acme:textarea code="playa.acceso" path="tipoPlaya.acceso" readonly="true"/>	
	<acme:textbox code="playa.longitud" path="tipoPlaya.longitud" readonly="true"/>	
	<acme:textbox code="playa.anchura" path="tipoPlaya.anchura" readonly="true"/>	
	<acme:textarea code="playa.paseoMaritimo" path="tipoPlaya.paseoMaritimo" readonly="true"/>	
	<acme:textarea code="playa.zonaFondeo" path="tipoPlaya.zonaFondeo" readonly="true"/>	
	<acme:textarea code="playa.vegetacion" path="tipoPlaya.vegetacion" readonly="true"/>	
	<acme:textarea code="playa.espacioProtegido" path="tipoPlaya.espacioProtegido" readonly="true"/>	
	
	<security:authorize access="hasRole('USUARIO')">
 	<acme:cancel code="playa.atras" url="/playa/usuario/list.do" />
	</security:authorize>
			
	<security:authorize access="isAnonymous()">
 	<acme:cancel code="playa.atras" url="/playa/list.do" />
	</security:authorize>
	
	<security:authorize access="hasRole('EMPRESARIO')">
 	<acme:cancel code="playa.atras" url="/playa/empresario/list.do" />
	</security:authorize>
	
	<security:authorize access="hasRole('ADMINISTRADOR')">
 	<acme:cancel code="playa.atras" url="/playa/admin/list.do" />
	</security:authorize>
	
	</jstl:if>
</form:form>
