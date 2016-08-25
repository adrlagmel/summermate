<%--
 * edit.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form modelAttribute="negocio" action="negocio/empresario/edit.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="eventos"/>
	<form:hidden path="reservas"/>
	<form:hidden path="empresario"/>
	
	<acme:textbox code="negocio.nombre" path="nombre" /><br/>
	<acme:textbox code="negocio.tipo" path="tipo" /><br/>
	<acme:textbox code="negocio.descripcion" path="descripcion" /><br/>
	<acme:textbox code="negocio.aforo" path="aforo" /><br/>
	<acme:textbox code="negocio.telefono" path="telefono" /><br/>
	<acme:textbox code="negocio.paginaweb" path="paginaWeb" /><br/>
		<div style="width:50%; margin: auto;" >
	<jstl:if test="${negocio.imagen!=null}">
		<b><spring:message code="negocio.imagen" /></b><br/><br/>
		<img class="img-responsive img-rounded" src="foto/displayImageNegocio.do?negocioId=${negocio.id}" height="350" width="590" />
	</jstl:if>
	<jstl:if test="${negocio.imagen==null}">
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


						
					