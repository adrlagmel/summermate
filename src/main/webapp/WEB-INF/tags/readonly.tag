<%--
 * readonly.tag
 *
 * Muestra un dato de solo lectura con el mismo aspecto que acme:textbox, pero
 * sin enlazarlo al formulario: el valor no viaja en la petición, así que no se
 * puede modificar desde el navegador.
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Attributes --%>

<%@ attribute name="code" required="true" %>
<%@ attribute name="value" required="true" type="java.lang.Object" %>

<%-- Definition --%>

<div style="width:50%; margin: auto;">
	<label><spring:message code="${code}" /></label>
	<input type="text" class="form-control" readonly="readonly" value="<jstl:out value="${value}" />" />
</div>
