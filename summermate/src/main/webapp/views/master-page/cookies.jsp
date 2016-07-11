<%--
 * cookies.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<div class="cookies" id="cookie1">
	<spring:message code="cookies.policy" /> <br />
	<spring:message code="cookies.policy.confirm" /> &nbsp;
	<a href="http://politicadecookies.com" target="_blank"><spring:message code="cookies.information" /></a>
<button onclick="controlcookies()" class="buttoncookie"><spring:message code="cookies.policy.close" /></button>

</div>

<script type="text/javascript">
	if (localStorage.controlcookie>0){ 
		document.getElementById('cookie1').style.display = 'none';

	}
</script>