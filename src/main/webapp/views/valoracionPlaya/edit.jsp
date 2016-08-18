<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="valoracionPlaya/usuario/edit.do" modelAttribute="valoracionPlaya">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="playa"/>
	<form:hidden path="cliente"/>
	<form:hidden path="fecha"/>
	
	<acme:textbox code="valoracionPlaya.titulo" path="titulo" /> <br/>
	<acme:textarea code="valoracionPlaya.comentario" path="comentario" /> <br/>	
	<acme:textbox code="valoracionPlaya.puntuacion" path="puntuacion" /> <br/>	
	
	<acme:submit name="save" code="valoracionPlaya.save"/>
	<acme:cancel code="valoracionPlaya.atras" url="/playa/list.do" />
	
</form:form>


