<%--
 * postbutton.tag
 *
 * Botón que lanza una acción que cambia datos (borrar, dar de alta, apuntarse...)
 * mediante un formulario POST con el token CSRF. Sustituye a los enlaces GET
 * que hacían esas acciones: un GET no debe modificar nada y, además, lo puede
 * disparar cualquier página externa (o un simple <img>).
 *
 * Uso:
 *   <acme:postbutton url="reserva/usuario/delete.do?reservaId=${row.id}"
 *                    code="booking.delete" confirmCode="booking.confirmDelete" />
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Attributes --%>

<%@ attribute name="url" required="true" %>
<%@ attribute name="code" required="true" %>
<%@ attribute name="confirmCode" required="false" %>
<%@ attribute name="cssClass" required="false" %>

<jstl:if test="${cssClass == null}">
	<jstl:set var="cssClass" value="btn btn-danger" />
</jstl:if>

<%-- Definition --%>

<form action="${url}" method="post" style="display: inline;"
	<jstl:if test="${confirmCode != null}">
		onsubmit="return confirm('<spring:message code="${confirmCode}" javaScriptEscape="true" />')"
	</jstl:if>
	>
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	<button type="submit" class="${cssClass}">
		<spring:message code="${code}" />
	</button>
</form>
