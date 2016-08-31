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
      <input type="text" style="width:250px;" class="form-control" name="s" placeholder="<spring:message code="usuario.buscarPorApellidos"/>">
     </div>
     
    <div class="form-group">  
      <button type="submit" class="btn btn-default"><spring:message code="usuario.search" /></button>
     </div><br/>
</form><br/>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="false" name="usuarios" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	

	<spring:message code="usuario.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}" sortable="true" />	
	
	<spring:message code="usuario.apellidos" var="apellidos" />
	<display:column property="apellidos" title="${apellidos}" sortable="true" />	
	

	<spring:message code="usuario.email" var="email" />
	<display:column property="email" title="${email}" sortable="true" />	
</display:table>
</div>

	
	
	