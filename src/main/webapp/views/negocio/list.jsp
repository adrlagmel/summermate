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


<form class="form-inline" action="${actionURI}">
	<div class="form-group">  
      <input type="text" style="width:250px;" class="form-control" name="s" placeholder="<spring:message code="negocio.searchByTown"/>">
     </div>
     
    <div class="form-group">  
      <button type="submit" class="btn btn-default"><spring:message code="negocio.search" /></button>
     </div><br/>
</form><br/>

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
<display:table pagesize="5" class="table table-condensed" keepStatus="false" name="negocios" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<security:authorize access="hasRole('EMPRESARIO')">
		<display:column>
		<security:authentication var="user" property="principal.id" />

			<jstl:if test="${row.empresario.userAccount.id == user}">
				<a href="negocio/empresario/edit.do?negocioId=${row.id}"><spring:message code="negocio.edit" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<display:column>
		<jstl:if test="${row.imagen != null}">
			<img class="img-responsive img-rounded" id="image" src="foto/displayImageNegocio.do?negocioId=${row.id}" height="75" width="75"/><br />
		</jstl:if>
	</display:column>
	
	<spring:message code="negocio.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}" sortable="true" />
	
	<spring:message code="negocio.ciudad" var="ciudad" />
	<display:column property="localizacion.ciudad" title="${ciudad}" sortable="true" />
		
	<spring:message code="negocio.playa" var="playa" />
	<display:column property="playa.nombre" title="${playa}" sortable="true" />
	
	<spring:message code="negocio.aforo" var="aforo" />
	<display:column property="aforo" title="${aforo}" sortable="true" />
	
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
	<security:authorize access="hasRole('USUARIO')">
		<display:column>
			<a href="reserva/usuario/create.do?negocioId=${row.id}"><spring:message code="negocio.reserva.create" /></a>
		</display:column>
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
</display:table>
</div>

	<security:authorize access="hasRole('EMPRESARIO')">
		<jstl:if test="${!negocios.isEmpty() || allowRegisternegocio == true}">
		<a href="negocio/empresario/register.do">
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
	