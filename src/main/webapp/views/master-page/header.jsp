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
		         
	         <a class="navbar-brand" href="#" style="padding-top: 0px; padding-bottom: 0; margin-left: 4px;">
				<img src="images/logo.png" width='40%' height='auto' />
			 </a>
		</div>
<div id="navbar" class="navbar-collapse collapse">
  <ul class="nav navbar-nav">

		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		
		<security:authorize access="hasRole('EMPRESARIO')">
			
			<li><a href="playa/list.do"><spring:message code="master.page.playa" /></a></li>
			<li><a href="denunciaValoracion/empresario/list.do"><spring:message code="master.page.denuncia.listAll" /></a></li>
			
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" ><span class="glyphicon glyphicon-cutlery"></span>&nbsp;<spring:message code="master.page.negocios" /><b class="caret"></b></a>
              <ul class="dropdown-menu" >
              		<li><a href="negocio/empresario/register.do"><spring:message code="master.page.negocio.register" /></a></li>
					<li><a href="negocio/list.do"><spring:message code="master.page.negocio.list" /></a></li>
					<li><a href="negocio/empresario/list.do"><spring:message code="master.page.negocio.mylist" /></a></li>
					<li><a href="peticionNegocio/empresario/list.do"><spring:message code="master.page.peticionnegocio.empresario.list" /></a></li>
				</ul>
			</li>			
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" ><spring:message code="master.page.eventos" /><b class="caret"></b></a>
              <ul class="dropdown-menu" >
              		<li><a href="evento/empresario/register.do"><spring:message code="master.page.eventos.register" /></a></li>
					<li><a href="evento/list.do"><spring:message code="master.page.eventos.list" /></a></li>
					<li><a href="evento/empresario/list.do"><spring:message code="master.page.eventos.mylist" /></a></li>
			  </ul>
			</li>	
			<li><a href="reserva/empresario/lista.do"><spring:message code="master.page.reservas"/></a></li>				
		</security:authorize>
		
		
		<security:authorize access="isAnonymous()">
		  	<ul class="nav navbar-nav navbar-right">
		  		<li class="dropdown">
	      			<a class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span>&nbsp;<spring:message code="master.page.registro" /></a>
	      				<ul class="dropdown-menu">	
							<li><a href="usuario/register.do"><span class="glyphicon glyphicon-user"></span>&nbsp;<spring:message code="master.page.register.usuario" /></a></li>
							<li><a href="empresario/register.do"><span class="glyphicon glyphicon-briefcase"></span>&nbsp;<spring:message code="master.page.register.empresario" /></a></li>
						</ul>
				</li>
      			<li><a href="security/login.do"> <span class="glyphicon glyphicon-log-in"></span>&nbsp;<spring:message code="master.page.login" /> </a></li>
    		</ul>
						
		</security:authorize>
		
		<security:authorize access="hasRole('USUARIO')">
			<li><a href="playa/list.do"><spring:message code="master.page.playa" /></a></li>
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" ><spring:message code="master.page.valoraciones" /><b class="caret"></b></a>
              <ul class="dropdown-menu" >
					<li><a href="valoracionPlaya/usuario/list.do"><spring:message code="master.page.valoracionPlaya.listUsuarioValoraciones" /></a></li>
					<li><a href="valoracionNegocio/usuario/list.do"><spring:message code="master.page.valoracionNegocio.listUsuarioValoraciones" /></a></li>
			</ul>
			</li>	
			<li><a href="negocio/list.do"><spring:message code="master.page.negocios" /></a></li>
			<li><a href="reserva/usuario/lista.do"><spring:message code="master.page.reservas.my"/></a></li>	
			
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" ><spring:message code="master.page.eventos" /><b class="caret"></b></a>
              <ul class="dropdown-menu" >
					<li><a href="evento/usuario/list.do"><spring:message code="master.page.eventos.list" /></a></li>
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
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.negocios" /><span class="caret"></span></a>
              <ul class="dropdown-menu" >
              		<li><a href="negocio/list.do"><spring:message code="master.page.negocio.list" /></a></li>
					<li><a href="peticionNegocio/administrador/listapendiente.do"><spring:message code="master.page.peticionnegocio.listAll" /></a></li>
			  </ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">			
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-envelope"></span></a>
              <ul class="dropdown-menu" >
					<li><a href="carpeta/actor/lista.do"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;<spring:message code="master.page.folders.list" /></a></li>
					<li><a href="mensaje/actor/enviar.do"><span class="glyphicon glyphicon-send"></span>&nbsp;<spring:message code="master.page.messages.send" /></a></li>
			  </ul>
			</li>
			
			<li class="dropdown">
	              <a class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.profile" /><span class="caret"></span></a>
	              
	              <ul class="dropdown-menu" >
					<security:authorize access="hasRole('USUARIO')">
					<li><a href="perfil/usuario.do"><spring:message code="master.page.profile.usuario" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('EMPRESARIO')">
						<li><a href="perfil/empresario.do"><spring:message code="master.page.profile.empresario" /></a></li>
					</security:authorize>
					<li><a href="perfil/actor/editPassword.do"><spring:message code="master.page.profile.editPassword" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
		
	</div><!--/.nav-collapse -->
  </div><!--/.container-fluid -->
</nav>

	<div class="pull-right" style="margin-right: 5px;">
		<a href="?language=en">en</a> | <a href="?language=es">es</a>
	</div>

</div> <!-- /container -->