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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- <form action="${actionURI}">
      <input type="text" name="s" placeholder="<spring:message code="evento.searchByTown"/>">
      
      <button type="submit"><spring:message code="evento.search" /></button>
</form> --%>


<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true" name="participantes" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	
	<display:column>
		<jstl:if test="${row.imagen != null}">
			<img class="img-responsive img-rounded" id="image" src="foto/displayImagePerfil.do?playaId=${row.id}" height="75" width="75"/><br />
		</jstl:if>
	</display:column>
	
	<spring:message code="evento.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}" sortable="true" />
	
	<spring:message code="evento.apellidos" var="apellidos" />
	<display:column property="apellidos" title="${apellidos}" sortable="true" />
	
	<spring:message code="evento.email" var="email" />
	<display:column property="email" title="${email}" sortable="true" />
		
	<%-- <display:column>
			<a href="evento/display.do?eventoId=${row.id}"><spring:message code="evento.display" /></a>
	</display:column> --%>

</display:table>
</div>

	<acme:cancel code="evento.return.link" url="/evento/empresario/list.do" /> 
	
	