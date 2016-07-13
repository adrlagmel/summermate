<%--
 * header.jsp
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="container">

      <!-- Static navbar -->
      <nav class="navbar navbar-default">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
		         <a class="navbar-brand" href="#" style="padding-top: 4px; padding-bottom: 0; margin-left: 4px;">
					<img src="images/logo.png" width='40%' height='auto' />
				</a>
			</div>
<div id="navbar" class="navbar-collapse collapse">
  <ul class="nav navbar-nav">

		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		
		
		
		<security:authorize access="hasRole('EMPRESARIO')">
			
			<li><a href="playa/list.do"><spring:message code="master.page.playa.listAll" /></a></li>
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" ><spring:message code="master.page.negocios" /><b class="caret"></b></a>
              <ul class="dropdown-menu" >
              		<li><a href="negocio/register.do"><spring:message code="master.page.negocio.register" /></a></li>
					<li><a href="negocio/empresario/list.do"><spring:message code="master.page.negocio.list" /></a></li>
				</ul>
			</li>	
			
		</security:authorize>
		
		
		
		
		<security:authorize access="isAnonymous()">
			<li><a href="security/login.do"><spring:message code="master.page.login" /></a></li>
						
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown"><spring:message code="master.page.register" /><span class="caret"></span></a>
              <ul class="dropdown-menu">	
					<li><a href="usuario/register.do"><spring:message code="master.page.register.usuario" /></a></li>
					<li><a href="empresario/register.do"><spring:message code="master.page.register.empresario" /></a></li>
					</ul>
			</li>
			<li><a href="playa/list.do"><spring:message code="master.page.playa.listAll" /></a></li>
				
		</security:authorize>
		
		<security:authorize access="hasRole('USUARIO')">
			<li><a href="playa/list.do"><spring:message code="master.page.playa.listAll" /></a></li>
			
			
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" ><spring:message code="master.page.reservas" /><b class="caret"></b></a>
              <ul class="dropdown-menu" >
					<li><a href="reserva/usuario/create.do"><spring:message code="master.page.reservas.crear" /></a></li>
					<li><a href="reserva/usuario/lista.do"><spring:message code="master.page.reservas.listar"/></a></li>		
				</ul>
			</li>	
			
		</security:authorize>	
		
		
		<security:authorize access="hasRole('ADMINISTRADOR')">
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.playa" /><span class="caret"></span></a>
              <ul class="dropdown-menu" >
					<li><a href="playa/list.do"><spring:message code="master.page.playa.listAll" /></a></li>
					<li><a href="playa/admin/create.do"><spring:message code="master.page.playa.create" /></a></li>
			  </ul>
			</li>
			
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">			
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.messages" /><span class="caret"></span></a>
              <ul class="dropdown-menu" >
					<li><a href="carpeta/actor/lista.do"><spring:message code="master.page.folders.list" /></a></li>
					<li><a href="mensaje/actor/enviar.do"><spring:message code="master.page.messages.send" /></a></li>
			  </ul>
			</li>
			<li><a href="negocio/list.do"><spring:message code="master.page.negocio.mylist" /></a></li>
			
			<li class="dropdown">
	              <a class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.profile" /><span class="caret"></span></a>
	              <ul class="dropdown-menu" >
						<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
					</ul>
			</li>
		</security:authorize>
		
	</ul>
	</div><!--/.nav-collapse -->
  </div><!--/.container-fluid -->
</nav>

	<div class="pull-right" style="margin-right: 5px;">
		<a href="?language=en">en</a> | <a href="?language=es">es</a>
	</div>

</div> <!-- /container -->