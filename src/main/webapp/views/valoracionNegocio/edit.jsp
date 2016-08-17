<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="valoracionNegocio/usuario/edit.do" modelAttribute="valoracionNegocio">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="reserva"/>
	
	<acme:textbox code="valoracionNegocio.titulo" path="titulo" />	<br/>
	<acme:textbox code="valoracionNegocio.fecha" path="fecha" /><br/>	
	<acme:textarea code="valoracionNegocio.comentario" path="comentario" />	<br/>
	<acme:textbox code="valoracionNegocio.puntuacion" path="puntuacion" />	<br/>
	<acme:textbox code="valoracionNegocio.comentarioUtil" path="comentarioUtil" />	<br/>
	
	<acme:submit name="save" code="valoracionNegocio.save"/><br/><br/>
	<acme:cancel code="valoracionNegocio.atras" url="/reserva/usuario/lista.do" />
	
	
</form:form>


