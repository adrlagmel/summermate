
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="vPlayas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<spring:message code="valoracionPlaya.titulo" var="titulo" />
	<display:column property="titulo" title="${titulo}"
		sortable="true" />

	<spring:message code="valoracionPlaya.fecha" var="fecha" />
	<display:column property="fecha" title="${fecha}" format="{0,date,dd/MM/yyyy HH:mm}"
		sortable="true" />
		
	<spring:message code="valoracionPlaya.comentario" var="comentario" />
	<display:column property="comentario" title="${comentario}"
		sortable="true" />
		
	<spring:message code="valoracionPlaya.puntuacion" var="puntuacion" />
	<display:column property="puntuacion" title="${puntuacion}"
		sortable="true" />
		
	<spring:message code="valoracionPlaya.usuario" var="usuario" />
	<display:column property="usuario.userAccount.username" title="${usuario}"
		sortable="true" />
		
	<spring:message code="valoracionPlaya.playa" var="playa" />
	<display:column property="playa.nombre" title="${playa}"
		sortable="true" />
		
	<spring:message code="valoracionPlaya.editar" var="editar" />
	<display:column>
		<a href="valoracionPlaya/usuario/edit.do?valoracionPlayaId=${row.id}" class="btn btn-primary" >
			<spring:message code="valoracionPlaya.editar"/>
		</a>
	</display:column>
	
	<display:column sortable="false">
		<a href="valoracionPlaya/usuario/borrar.do?valoracionPlayaId=${row.id}" class="btn btn-danger" onclick="return confirm('<spring:message code="msg.delete.valoracion" />')"><spring:message code="valoracionPlaya.borrar" /></a>
	</display:column>
	
</display:table>
</div>