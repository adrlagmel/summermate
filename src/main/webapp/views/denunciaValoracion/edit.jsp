<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="denunciaValoracion/empresario/edit.do" modelAttribute="denunciaValoracion">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="valoracionNegocio"/>
	<form:hidden path="cliente"/>
	
	<acme:textbox code="denunciaValoracion.titulo" path="titulo" />	<br/>
	<acme:textarea code="denunciaValoracion.comentario" path="comentario" />	<br/>
	
	<div style="width:50%;">
		<form:label path="tipo">
			<spring:message code="denunciaValoracion.tipo" />
		</form:label>
		
		<form:select path="tipo" cssClass="form-control">
			<form:option label= "FALSO" value = "<spring:message code="denunciaValoracion.falso"/>" />		
			<form:option label= "RECHAZO" value = "<spring:message code="denunciaValoracion.burla"/>"/>
			<form:option label= "BURLA" value = "<spring:message code="denunciaValoracion.rechazo"/>"/>		
		</form:select>
	</div><br>
	
	<acme:submit name="save" code="denunciaValoracion.save"/>
	<acme:cancel code="denunciaValoracion.atras" url="/denunciaValoracion/empresario/list.do" />
	
</form:form>


