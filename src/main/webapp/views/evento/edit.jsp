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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" href="styles/jquery.datetimepicker.css" />

</head>

<body>

<form:form modelAttribute="evento" action="evento/empresario/edit.do">    
       
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="usuarios"/>
	<form:hidden path="fechaRegistro"/>
	<form:hidden path="imagen"/>
	
	<acme:textbox code="evento.codigo" path="codigo" readonly = "true" /><br/>
	<acme:textbox code="evento.nombre" path="nombre" />	<br/>
	
		<div style="width:50%; margin: auto;" >
		<form:label path="fechaCelebracion">
			<spring:message code="evento.fechaCelebracion" />
		</form:label>
		<form:input path="fechaCelebracion" cssClass="form-control" id="datetimepicker"/>	
		<form:errors path="fechaCelebracion" cssClass="error" />
	</div><br/>
							
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="scripts/jquery.datetimepicker.full.js"></script>
	
	<script type="text/javascript">
		$('#datetimepicker').datetimepicker({format: 'd/m/Y H:i'});
	</script>
		<div style="width:50%; margin: auto;" >
	</div>
	<acme:textarea code="evento.descripcion" path="descripcion" /><br/>
	<acme:textbox code="evento.precio" path="precio" /><br/>
	
		<div style="width:50%; margin: auto;" >
		<form:label path="negocio">
			<spring:message code="evento.negocio" />
		</form:label>
		
		<form:select items="${negocios}" itemLabel="nombre" code="evento.negocio" path="negocio" class="form-control"/>
		<form:errors path="negocio" cssClass="error" />
	</div><br/>
		<div style="width:50%; margin: auto;" >
	<jstl:if test="${!hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="images/no-image.png" height="30%" width="30%" /><br /><br />
	</jstl:if>
	
	<jstl:if test="${hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="foto/displayImageEvento.do?eventoId=${evento.id}" height="30%" width="30%"/><br /><br />
 	</jstl:if><br/>
    	
    <security:authorize access="hasRole('EMPRESARIO')">
    	<jstl:if test="${evento.id != 0}">
			<a href="evento/empresario/uploadImageEvento.do?eventoId=${evento.id}" class="btn btn-info" ><spring:message code="evento.uploadImage" /></a>
		</jstl:if>
		<acme:submit name="save" code="evento.save"/>
	 </security:authorize>
	 
	<acme:cancel code="evento.cancel" url="/evento/list.do" /> 
	<br/> 
	<br/>
	</div>
</form:form>

</body>

</html>
						
					