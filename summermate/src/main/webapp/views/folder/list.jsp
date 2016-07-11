<%--
 * action-2.jsp
 *
 * Copyright (C) 2013 Universidad de Sevilla
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

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="carpetas" requestURI="carpeta/actor/lista.do" id="row">
	
	<!-- Action links -->

	
	<!-- Attributes -->

	<spring:message code="folder.name" var="nombre" />
	<display:column property="nombre" title="${nombre}"
		sortable="true" />
		
	<spring:message code="folder.numberMessages" var="numberMessages" />
	<display:column title="${numberMessages}">
		<jstl:out value="${row.mensajes.size()}"></jstl:out>
	</display:column>

	<display:column>
		<a href="mensaje/actor/lista.do?carpetaId=${row.id}" ><spring:message code="folder.messages.link" /></a>
	</display:column>
		
	
</display:table>
</div>
		<br />
		
		<input type="button" value="<spring:message code="folder.return.link" />" onclick="javascript: location.replace('actor/welcome/index.do')" />	