
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="vNegocios" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<spring:message code="valoracionNegocio.titulo" var="titulo" />
	<display:column property="titulo" title="${titulo}"
		sortable="true" />

	<spring:message code="valoracionNegocio.fecha" var="fecha" />
	<display:column property="fecha" title="${fecha}" format="{0,date,dd/MM/yyyy HH:mm}"
		sortable="true" />
		
	<spring:message code="valoracionNegocio.comentario" var="comentario" />
	<display:column property="comentario" title="${comentario}"
		sortable="true" />
		
	<spring:message code="valoracionNegocio.puntuacion" var="puntuacion" />
	<display:column property="puntuacion" title="${puntuacion}"
		sortable="true" />
		
		<spring:message code="valoracionNegocio.comentarioUtil" var="comentarioUtil" />
	<display:column property="comentarioUtil" title="${comentarioUtil}"
		sortable="true" />
		
	<spring:message code="valoracionNegocio.reserva" var="reserva" />
	<display:column property="reserva.codigo" title="${reserva}"
		sortable="true" />
	
	<spring:message code="valoracionNegocio.negocio" var="negocio" />
	<display:column property="reserva.negocio.nombre" title="${negocio}"
		sortable="true" />
	<security:authorize access="hasRole('USUARIO')">	
	<spring:message code="valoracionNegocio.editar" var="editar" />
	<display:column>
		<a href="valoracionNegocio/usuario/edit.do?valoracionNegocioId=${row.id}">
			<spring:message code="valoracionNegocio.editar"/>
		</a>
	</display:column>
	
	<spring:message code="valoracionNegocio.borrar" var="borrar" />
	<display:column>
	<input type="button" value="<spring:message code="valoracionNegocio.borrar" />"
				onclick="javascript: location.replace('valoracionNegocio/usuario/borrar.do?valoracionNegocioId=${row.id}');
				javascript: return confirm('<spring:message code="msg.delete.valoracionNegocio" />')" />
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('EMPRESARIO')">	
	<spring:message code="denunciaValoracion.create" var="create" />
	<display:column>
	<a href="denunciaValoracion/empresario/create.do?valoracionNegocioId=${row.id}">
			<spring:message code="denunciaValoracion.create"/>
		</a> 
	</display:column>
	</security:authorize>	
	</display:table>

</div>