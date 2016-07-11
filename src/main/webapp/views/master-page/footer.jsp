<%--
 * footer.jsp
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:useBean id="date" class="java.util.Date" />

<footer class="page-footer">

     
          <div class="footer-copyright">
            <div class="container">

<b>Copyright &copy; <fmt:formatDate value="${date}" pattern="yyyy" /> Summermate, Inc.</b> |
		<a onclick="mostrarcookies()"><spring:message code="cookies.link" /></a> |
		<a href="javascript: void(0);" id="termsLink" data-toggle="modal" data-target="#terms"><spring:message code="legal.link" /></a>
    </div>
          </div>
		</footer>