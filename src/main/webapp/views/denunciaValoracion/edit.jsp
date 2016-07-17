<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="denunciaValoracion/empresario/edit.do" modelAttribute="denunciaValoracion">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="valoracionNegocio"/>
	
	<acme:textbox code="denunciaValoracion.titulo" path="titulo" />	
	<acme:textarea code="denunciaValoracion.comentario" path="comentario" />	
	<acme:textbox code="denunciaValoracion.tipo" path="tipo" />	
	<acme:select items="${tiposDenuncia}" itemLabel="tipo" code="denunciaValoracion.tipo" path="tipo" onchange="cargarMiniPlanner()"/>
	
	
	<acme:submit name="save" code="denunciaValoracion.save"/>
	<acme:cancel code="denunciaValoracion.atras" url="/denunciaValoracion/empresario/list.do" />
	
	
</form:form>


