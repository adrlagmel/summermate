<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="valoracionNegocio/usuario/edit.do" modelAttribute="valoracionNegocio">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="reserva"/>
	<form:hidden path="fecha"/>
	<form:hidden path="comentarioUtil"/>
	<br/>
	<acme:textbox code="valoracionNegocio.titulo" path="titulo" />	<br/>
	<acme:textarea code="valoracionNegocio.comentario" path="comentario" />	<br/>
	
	<div style = "margin: auto;">
		<form:label path="puntuacion">
			<spring:message code="valoracionNegocio.puntuacion"/>
		</form:label><br/>
		<label class="radio-inline"><input type="radio" value ="1" name="puntuacion">1</label>
		<label class="radio-inline"><input type="radio" value ="2" name="puntuacion">2</label>
		<label class="radio-inline"><input type="radio" value ="3" name="puntuacion">3</label>
		<label class="radio-inline"><input type="radio" value ="4" name="puntuacion">4</label>
		<label class="radio-inline"><input type="radio" value ="5" name="puntuacion">5</label>
		<label class="radio-inline"><input type="radio" value ="6" name="puntuacion">6</label>
		<label class="radio-inline"><input type="radio" value ="7" name="puntuacion">7</label>
		<label class="radio-inline"><input type="radio" value ="8" name="puntuacion">8</label>
		<label class="radio-inline"><input type="radio" value ="9" name="puntuacion">9</label>
		<label class="radio-inline"><input type="radio" value ="10" name="puntuacion">10</label>
	</div>
	<br/><br/>
		
	<acme:submit name="save" code="valoracionNegocio.save"/>
	<acme:cancel code="valoracionNegocio.atras" url="/valoracionNegocio/usuario/list.do" />
	
</form:form>


