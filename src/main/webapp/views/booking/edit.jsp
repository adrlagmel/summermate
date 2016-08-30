<%--
 * edit.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
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

		<form:form action="reserva/usuario/create.do" modelAttribute="form">

			<form:hidden path="reservaId"/>
			<form:hidden path="negocio"/>
			
			<div style="width:50%; margin: auto;">
				<form:label path="fecha">
					<spring:message code="booking.bookingDate" />
				</form:label>
				<form:input path="fecha" cssClass="form-control" id="datetimepicker"/>	
				<form:errors path="fecha" cssClass="error" />
			<br/>
									
			<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
			<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
			<script type="text/javascript" src="scripts/jquery.datetimepicker.full.js"></script>
			
			<script type="text/javascript">
				$('#datetimepicker').datetimepicker({format: 'd/m/Y H:00'});
			</script>
			</div>
			<acme:textarea code="booking.comments" path="comentarios" /><br/>
			<div style="width:50%; margin: auto;">
			<div>
				<form:label path="comensales">
					<spring:message code="booking.fumadores" />
				</form:label>
				
				<form:select path="fumadores" code="booking.fumadores" cssClass="form-control">
					<form:option value="true" label="Yes" />		
					<form:option value="false" label="No"/>
				</form:select>
			</div><br/>
			
			<div>
				<form:label path="comensales">
					<spring:message code="booking.comensales" />
				</form:label>
			
				<form:select path="comensales" code="booking.comensales" cssClass="form-control">
					<form:option value="1" label="1" />		
					<form:option value="2" label="2" />
					<form:option value="3" label="3" />		
					<form:option value="4" label="4" />
					<form:option value="5" label="5" />		
					<form:option value="6" label="6" />
					<form:option value="7" label="7" />		
					<form:option value="8" label="8" />
					<form:option value="9" label="9" />		
					<form:option value="10" label="10" />
				</form:select>
			</div><br/>
			
			<jstl:if test="${aforoCompleto}">	
				<div>
					  <h3><spring:message code="booking.seats" /> <jstl:out value="${comensales}" /></h3>
					  <p></p>
				</div>
			</jstl:if>
			
			<acme:submit name="save" code="booking.pay"/>
		
			<acme:cancel code="booking.cancel" url="/reserva/usuario/lista.do" />
		</div>
		</form:form>

</body>
</html>




	