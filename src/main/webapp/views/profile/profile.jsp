<%--profile.jsp
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

<form:form modelAttribute="actor">
	
	<br/><br/>
	<jstl:if test="${isUsuario == true}">
			<jstl:if test="${actor.imagen!=null}">
				<img class="img-responsive img-circle" style = "margin: auto;" src="foto/displayImagePerfil.do?usuarioId=${actor.id}" height="250" width="250" />
			</jstl:if>
			<jstl:if test="${actor.imagen==null}">
				<img class="img-responsive img-rounded" style = "margin: auto;" src="images/no-image.png" height="350" width="590" />
			</jstl:if>
	<br/><br/>
	</jstl:if>
	
	
	<acme:textbox code="actor.name" path="nombre" readonly="true" /><br/>
	<acme:textbox code="actor.surname" path="apellidos" readonly="true" /><br/>
	<acme:textbox code="actor.email" path="email" readonly="true" /><br/>
		
	<jstl:if test="${isCliente == true}">
		<acme:textbox code="actor.phone" path="telefono" readonly="true"/><br/>
		<acme:textbox code="cliente.birthDate" path="fechaNacimiento" readonly="true"/><br/>
		<acme:textbox code="cliente.direccion" path="direccion" readonly="true"/><br/>
		<acme:textbox code="cliente.sexo" path="sexo" readonly="true"/><br/>
		<acme:textbox code="cliente.nacionalidad" path="nacionalidad" readonly="true"/><br/>
		
		<jstl:if test="${isUsuario == true}">
		<div style="width:50%; margin: auto;" >
			<acme:textarea code="usuario.estadoActual" path="estadoActual" readonly="true"/><br/>
		</div>
		</jstl:if>
		
		<jstl:if test="${isEmpresario == true}">
			<acme:textbox code="empresario.cif" path="cif" readonly="true"/><br/><br/>
		</jstl:if>
	</jstl:if>
	<div style="width:50%; margin: auto;" >
	<security:authorize access="hasRole('USUARIO')">
		<a href="usuario/uploadImageUsuario.do?usuarioId=${actor.id}" class="btn btn-success" style = "margin: auto;"><spring:message code="profile.uploadImage" /></a>
	</security:authorize>
		
	<jstl:if test="${isUsuario == true}">
		<a href="perfil/usuario/edit.do?usuarioId=${actor.id}"class="btn btn-primary" style = "margin: auto;"><spring:message code="profile.editar" /></a>
	</jstl:if>
	
	<jstl:if test="${isEmpresario == true}">
		<a href="perfil/empresario/edit.do?empresarioId=${actor.id}"class="btn btn-primary" style = "margin: auto;"><spring:message code="profile.editar" /></a>
	</jstl:if>
	
	<a href="#" class="btn btn-danger" style = "margin: auto;"><spring:message code="profile.return.link" /></a><br/><br/>
	</div>
</form:form>