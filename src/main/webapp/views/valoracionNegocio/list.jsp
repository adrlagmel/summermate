
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
	<display:column property="fecha" title="${fecha}" format="{0,date,dd/MM/yyyy HH:mm}" sortable="true" />
		
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
	
	<display:column>
		<security:authentication var="user" property="principal.id" />
		<jstl:if test="${row.reserva.usuario.userAccount.id == user}">
			<a href="valoracionNegocio/usuario/edit.do?valoracionNegocioId=${row.id}" class="btn btn-info" ><spring:message code="valoracionNegocio.editar" /></a>
		</jstl:if>
	</display:column>
	
	<display:column sortable="false">
		<a href="valoracionNegocio/usuario/borrar.do?valoracionNegocioId=${row.id}" class="btn btn-danger" ><spring:message code="valoracionNegocio.borrar" /></a>
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('EMPRESARIO')">	
	<spring:message code="denunciaValoracion.create" var="create" />
	<display:column>
		<security:authentication var="user" property="principal.id" />
		<jstl:if test="${row.reserva.negocio.empresario.userAccount.id == user}">
			<a href="denunciaValoracion/empresario/create.do?valoracionNegocioId=${row.id}" class="btn btn-danger">
				<spring:message code="denunciaValoracion.create"/>
			</a>
		</jstl:if>
	</display:column>
	</security:authorize>	
	</display:table>

</div>