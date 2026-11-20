<%--
 * edit.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form modelAttribute="negocio" action="negocio/empresario/edit.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="negocio.nombre" path="nombre" /><br/>
	<acme:textbox code="negocio.tipo" path="tipo" /><br/>
	<div style="width:50%; margin: auto;">
		<acme:textarea code="negocio.descripcion" path="descripcion" /><br/>
	</div>
	<acme:textbox code="negocio.aforo" path="aforo" /><br/>
	<acme:textbox code="negocio.telefono" path="telefono" /><br/>
	<acme:textbox code="negocio.paginaweb" path="paginaWeb" /><br/>
	
	<div style="width:50%; margin: auto;">
		<form:label path="playa">
			<spring:message code="negocio.playa" />
		</form:label>
		
		<form:select items="${playas}" itemLabel="nombre" code="negocio.playa" path="playa" class="form-control"/>
		<form:errors path="playa" cssClass="error" />
	</div><br/>
	
		<div style="width:50%; margin: auto;" >
	<jstl:if test="${hasimage}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImageNegocio.do?negocioId=${negocio.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${!hasimage}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="images/no-image.png" height="350" width="590" />
	</jstl:if>
	<br>
	<jstl:if test="${negocio.id != 0}">
			<a href="negocio/empresario/uploadImageNegocio.do?negocioId=${negocio.id}" class="btn btn-info" ><spring:message code="negocio.actualizarImage" /></a>
		</jstl:if>		
	<acme:submit name="save" code="negocio.save"/>
	<acme:cancel code="negocio.cancel" url="/negocio/empresario/list.do" /> 
	</div>
	
</form:form>


						
					