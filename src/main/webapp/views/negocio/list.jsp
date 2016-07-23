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


<form action="${actionURI}">
      <input type="text" name="s" placeholder="<spring:message code="negocio.searchByTown"/>">
      
      <button type="submit"><spring:message code="negocio.search" /></button>
</form>

<%-- <form action="negocio/nearToMe.do" method="get" name="nearToMe">
						<input type="text" name="lat" id="lat" class="hidden"/>
						<input type="text" name="lon" id="lon" class="hidden"/>
						
						<a href="javascript: nearToMe.submit()"> <spring:message code="master.page.negocios.nearToMe"/></a>
						<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=false"></script>
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
									</script></form> --%>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true" name="negocios" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<display:column>
		<security:authentication var="user" property="principal.id" />

			<jstl:if test="${row.empresario.userAccount.id == user}">
				<a href="negocio/empresario/edit.do?negocioId=${row.id}"><spring:message code="negocio.edit" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<spring:message code="negocio.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}" sortable="true" />
	
	<spring:message code="negocio.provincia" var="provincia" />
	<display:column property="localizacion.provincia" title="${provincia}" sortable="true" />
	
	<spring:message code="negocio.ciudad" var="ciudad" />
	<display:column property="localizacion.ciudad" title="${ciudad}" sortable="true" />
		
	<spring:message code="negocio.playa" var="playa" />
	<display:column property="playa.nombre" title="${playa}" sortable="true" />
	
	<security:authorize access="hasRole('EMPRESARIO')">
	<display:column>
	<security:authentication var="user" property="principal.id" />
		<jstl:if test="${row.empresario.userAccount.id == user}">
		<a href="valoracionNegocio/empresario/listValoraciones.do?negocioId=${row.id}"><spring:message code="negocio.valoracionesNegocio.list" /></a>
	</jstl:if>
	</display:column>
	
	<%-- <display:column>
		<jstl:if test="${row.empresario.userAccount.id == user}">
		<a href="payment/empresario/list.do?negocioId=${row.id}"><spring:message code="negocio.payments.list" /></a>
	</jstl:if>
	</display:column> --%>
	
	<%-- <display:column>
		<jstl:if test="${row.empresario.userAccount.id == user}">
		<a href="negocioCalendar/sportPartner/deleteDates.do?negocioId=${row.id}"><spring:message code="negocioCalendar.deleteDates" /></a>
	</jstl:if>
	</display:column> --%>
	</security:authorize> 
		
	<display:column>
			<a href="negocio/empresario/display.do?negocioId=${row.id}"><spring:message code="negocio.display" /></a>
	</display:column>
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<display:column>
			<security:authentication var="user" property="principal.id" />
			<jstl:if test="${row.empresario.userAccount.id == user}">
			<jstl:if test="${row.imagen==null}">
				<a href="negocio/empresario/uploadImageNegocio.do?negocioId=${row.id}"><spring:message code="negocio.uploadImage" /></a>
			</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>
	
<%-- 	<display:column>
		<a href="negocio/planner.do?negocioId=${row.id}"><spring:message code="negocio.displayPlanner" /></a>
	</display:column> --%>
	<display:column>
	<jstl:if test="${row.empresario.userAccount.id == user}">
				<a href="mesa/empresario/list.do"><spring:message code="negocio.mesas" /></a>
	</jstl:if>
	</display:column>
</display:table>
</div>

	<security:authorize access="hasRole('EMPRESARIO')">
		<jstl:if test="${!negocios.isEmpty() || allowRegisternegocio == true}">
		<a href="negocio/empresario/create.do">
			<spring:message code="negocio.create"/>
		</a> <br/>	
		</jstl:if>
		
		<jstl:if test="${negocios.isEmpty() && allowRequest == true}">
		<a href="peticionnegocio/empresario/send.do">
			<spring:message code="peticionnegocio.send"/>
		</a> <br/>	
		</jstl:if>
		<br />
		<jstl:if test="${allowRequest == false}">
		<a href="peticionegocio/empresario/list.do">
			<spring:message code="peticionnegocio.display"/>
		</a> <br/>	
		</jstl:if>
	</security:authorize>