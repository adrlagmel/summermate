<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="valoracionNegocio">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="reserva"/>
	<form:hidden path="fecha"/>
	
	<acme:textbox code="valoracionNegocio.titulo" path="titulo" readonly="true"/><br/>
	<acme:textarea code="valoracionNegocio.comentario" path="comentario" readonly="true"/>	<br/>
	<acme:textbox code="valoracionNegocio.puntuacion" path="puntuacion" readonly="true"/>	<br/>
	<acme:textbox code="valoracionNegocio.comentarioUtil" path="comentarioUtil" readonly="true"/>	<br/>
	
	<acme:cancel code="valoracionNegocio.atras" url="/reserva/usuario/lista.do" />
	
</form:form>


