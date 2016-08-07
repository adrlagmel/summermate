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


<form:form modelAttribute="evento" action="evento/empresario/edit.do">    
       
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="usuarios"/>
	<form:hidden path="fechaRegistro"/>
	
	<acme:textbox code="evento.codigo" path="codigo" readonly = "true" />
	<acme:textbox code="evento.nombre" path="nombre" />	
	<acme:textbox code="evento.descripcion" path="descripcion" />
	<acme:textbox code="evento.precio" path="precio" />
	
	<acme:select items="${negocios}" itemLabel="nombre" code="evento.negocio" path="negocio"/>
	
	<jstl:if test="${!hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="images/no-image.png" height="30%" width="30%" /><br /><br />
	</jstl:if>
	
	<jstl:if test="${hasimage}">
		<img class="img-responsive img-rounded" id="foto" src="foto/displayImageEvento.do?playaId=${evento.id}" height="30%" width="30%"/><br /><br />
 	</jstl:if>
 	
 	<acme:textbox code="evento.fechaCelebracion" path="fechaCelebracion" /> <br/><br/>
 	
 	<!-- <div class='col-sm-6'>
            <div class="form-group">
                <div class='input-group date' id='fecha'>
                    <input type='text' name = "fechaCelebracion" class="form-control" />
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
    </div> -->
    
    <security:authorize access="hasRole('USUARIO')">
		<jstl:if test="${requestURI=='evento/usuario/list.do'}">
			<display:column>
			<form action="evento/usuario/signup.do" method="get" name="signup">
				<input type="text" name="eventoId" id="evento" class="hidden" value="${row.id}"/>	
				<input type="submit" value="<spring:message code="event.signup"/>"/>
			</form>
			</display:column>
		</jstl:if>	
	</security:authorize>
	
	<acme:submit name="save" code="evento.save"/>
	<security:authorize access="hasRole('EMPRESARIO')">
		<acme:cancel code="evento.cancel" url="/evento/empresario/list.do" /> 
	</security:authorize> 
	
	<security:authorize access="!hasRole('EMPRESARIO')">
		<acme:cancel code="evento.cancel" url="/evento/list.do" /> 
	</security:authorize> 
	
</form:form>


						
					