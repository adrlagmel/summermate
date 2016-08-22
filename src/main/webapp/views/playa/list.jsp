<%--
 * action-2.jsp
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

<form action="playa/nearToMe.do" method="get" name="nearToMe">
						<input type="text" name="lat" id="lat" class="hidden"/>
						<input type="text" name="lon" id="lon" class="hidden"/>
						
						<a href="javascript: nearToMe.submit()"> <spring:message code="master.page.playa.nearToMe"/></a>
						<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&key=AIzaSyBBrx34wk6LRo3M4xsVJEe3U9umurfaMik" ></script>
									<script>
										var x = document.getElementById("lat");
										var y = document.getElementById("lon");
							    		if (navigator.geolocation) {
							       			navigator.geolocation.getCurrentPosition(initialize);
							    		}
										function initialize(position) {
											x.value = position.coords.latitude;
											y.value = position.coords.longitude;
											
										}
									</script></form>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="playas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<security:authentication var="user" property="principal.id" />
	<display:column>
		<jstl:if test="${row.administrador.userAccount.id == user}">
		<a href="playa/admin/edit.do?playaId=${row.id}" class="btn btn-warning">
			<spring:message code="playa.editar"/>
		</a>
	</jstl:if>
	</display:column>
		
	<display:column>
		<jstl:if test="${row.imagen != null}">
			<img class="img-responsive img-rounded" id="image" src="foto/displayImage.do?playaId=${row.id}" height="75" width="75"/><br />
		</jstl:if>
	</display:column>
	
	<spring:message code="playa.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}"
		sortable="true" />

	<spring:message code="playa.localizacion.provincia" var="provincia" />
	<display:column property="localizacion.provincia" title="${provincia}"
		sortable="true" />
		
	<spring:message code="playa.localizacion.ciudad" var="ciudad" />
	<display:column property="localizacion.ciudad" title="${ciudad}"
		sortable="true" />
		
		<spring:message code="playa.valoracionMedia" var="valoracionMedia" />
	<display:column property="valoracionMedia" title="${valoracionMedia}"
		sortable="true" />
		
	<spring:message code="playa.detallePlaya" var="detallePlaya" />
		<display:column sortable="false">
			<a href="playa/display.do?playaId=${row.id}" class="btn btn-info"><spring:message code="playa.detallePlaya" /></a>					
	</display:column>
	
	<security:authorize access="hasRole('ADMINISTRADOR')">
		<display:column>
			<jstl:if test="${row.imagen==null}">
				<a href="playa/admin/uploadImage.do?playaId=${row.id}" class="btn btn-primary"><spring:message code="playa.uploadImage" /></a>
			</jstl:if>
		</display:column>
		
		<display:column>
				<a href="playa/admin/uploadCoordenates.do?playaId=${row.id}" class="btn btn-success"><spring:message code="playa.uploadCoordenates" /></a>
		</display:column>
	
		<spring:message code="playa.delete" var="borrar" />
		<display:column>
		<jstl:if test="${row.administrador.userAccount.id == user && row.negocios.size() < 1}">
			<a href="playa/admin/delete.do?playaId=${row.id}" class="btn btn-danger" onclick="return confirm('<spring:message code="playa.confirmDelete" />')"><spring:message code="playa.delete" /></a>
		</jstl:if>
		</display:column>
	</security:authorize>
		
	<security:authorize access="hasRole('USUARIO')">
		<spring:message code="valoracionPlaya.valoracionPlaya" var="valorarPlaya" />
		<display:column sortable="false">
			<a href="valoracionPlaya/usuario/create.do?playaId=${row.id}" class="btn btn-success"><spring:message code="valoracionPlaya.valoracionPlaya" /></a>	
		</display:column>
	</security:authorize>
	
</display:table>
</div>