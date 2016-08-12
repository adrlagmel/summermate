<%--
 * planner.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<html>
    <body>
        <div class="content" id="content">
           <div id="scheduler">${body}</div>
        </div>
        
        <acme:cancel url="/negocio/list.do" code="button.return"/>
    </body>
</html>