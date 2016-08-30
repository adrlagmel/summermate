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


<style> 
 	@import url(http://fonts.googleapis.com/css?family=Roboto:400);
body {
  background-color:#fff;
  -webkit-font-smoothing: antialiased;
  font: normal 14px Roboto,arial,sans-serif;
}

.container3 {
    padding: 25px;
    position: fixed;
}

.form-login {
    background-color: #EDEDED;
    padding-top: 10px;
    padding-bottom: 20px;
    padding-left: 20px;
    padding-right: 20px;
    border-radius: 15px;
    border-color:#d2d2d2;
    border-width: 25px;
    box-shadow:0 1px 0 #cfcfcf;
}

h4 { 
 border:0 solid #fff; 
 border-bottom-width:1px;
 padding-bottom:10px;
 text-align: center;
}

.form-control {
    border-radius: 10px;
}

.wrapper {
    text-align: right;
}
</style>

<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<br><br><br><br><br><br>
<form:form action="j_spring_security_check" modelAttribute="credentials" class="form-horizontal" role="form">
<br>
<br>
<div style="width:150%; margin: center;" >
 <div class="row">
    <div class="col-sm-4">
     <div class="form-login">
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
	    <div class="col-lg-offset-2 col-lg-10">
	      <button type="submit" class="btn btn-default"><spring:message code="security.login" /></button>
	    </div>
	  </div>
	
		<jstl:if test="${showError == true}">
			<div class="error">
				<spring:message code="security.login.failed" />
			</div>
		</jstl:if>
		</div>
		</div>
	</div>
</div>
</form:form>


<style> 
 	body {
 	  background: url(images/summer-814679.png) no-repeat center center fixed; 
  		-webkit-background-size: cover;
	  -moz-background-size: cover;
	  -o-background-size: cover;
	  background-size: cover;
}</style><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
