 <%--
 * login.jsp
 *
 * Copyright (C) 2014 Universidad de Sevilla
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


<form:form action="j_spring_security_check" modelAttribute="credentials" class="form-horizontal" role="form">

  <div class="form-group">
    <label for="ejemplo_email_3" class="col-lg-2 control-label"><spring:message code="security.username" /></label>
    <div class="col-lg-10">
   	 	<form:input path="username" class="form-control" id="ejemplo_password_3" style="width:300px;"/>	
		<form:errors class="error" path="username" />
    </div>
  </div>
  <div class="form-group">
    <label for="ejemplo_password_3" class="col-lg-2 control-label"><spring:message code="security.password" /></label>
    <div class="col-lg-10">
     	<form:password path="password" class="form-control" id="ejemplo_email_3" style="width:300px;"/>	
		<form:errors class="error" path="password" />
    </div>
  </div>
  
 <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <div class="checkbox">
        <label>
          <input type="checkbox"> <spring:message code="security.rememberme" />
        </label>
      </div>
    </div>
  </div>
  
  <div class="form-group">
    <div class="col-lg-offset-2 col-lg-10">
      <button type="submit" class="btn btn-default"><spring:message code="security.login" /></button>
    </div>
  </div>

	<jstl:if test="${showError == true}">
		<div class="error">
			<spring:message code="security.login.failed" />
		</div>
	</jstl:if>
	
	
</form:form>