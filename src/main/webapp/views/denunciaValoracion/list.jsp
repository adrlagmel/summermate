
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="vDenuncias" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<spring:message code="denunciaValoracion.titulo" var="titulo" />
	<display:column property="titulo" title="${titulo}"
		sortable="true" />

	<spring:message code="denunciaValoracion.comentario" var="comentario" />
	<display:column property="comentario" title="${comentario}"
		sortable="true" />
		
	<spring:message code="denunciaValoracion.tipo" var="tipo" />
	<display:column property="tipo" title="${tipo}" 
		sortable="true" />
		
	<security:authorize access="hasRole('EMPRESARIO')">	
	<spring:message code="denunciaValoracion.editar" var="editar" />
	<display:column>
		<a  href="denunciaValoracion/empresario/edit.do?denunciaValoracionId=${row.id}" class="btn btn-warning">
			<spring:message code="valoracionNegocio.editar"/>
		</a>
	</display:column>
	
	<spring:message code="denunciaValoracion.borrar"  var="borrar" />
	<display:column sortable="false">
		<a href="denunciaValoracion/empresario/borrar.do?denunciaValoracionId=${row.id}" class="btn btn-danger" onclick="return confirm('<spring:message code="msg.delete.denunciaValoracion" />')"><spring:message code="valoracionNegocio.borrar" /></a>
	</display:column>
	</security:authorize>	
	<security:authorize access="hasRole('USUARIO')">
	<display:column sortable="false">
		<a href="valoracionNegocio/usuario/list.do" class="btn btn-danger" ><spring:message code="valoracionNegocio.atras" /></a>
	</display:column>
	</security:authorize>
		
</display:table>

</div>