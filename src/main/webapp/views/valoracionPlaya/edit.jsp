<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="valoracionPlaya/usuario/edit.do" modelAttribute="valoracionPlaya">		
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="playa"/>
	<form:hidden path="usuario"/>
	<form:hidden path="fecha"/>
	
	<acme:textbox code="valoracionPlaya.titulo" path="titulo" /> <br/>
	<acme:textarea code="valoracionPlaya.comentario" path="comentario" /> <br/>	
	
	<form:label path="puntuacion">
		<spring:message code="valoracionPlaya.puntuacion"/>
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
	<br/><br/>	
	
	<acme:submit name="save" code="valoracionPlaya.save"/>
	<acme:cancel code="valoracionPlaya.atras" url="/playa/list.do" />
	
</form:form>


