<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

</head>

<body>

<script type="text/javascript">
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#image').attr('src', e.target.result).width(595);
			};
			reader.readAsDataURL(input.files[0]);
		}
	}
</script>

	<div class="col-md-7  col-md-offset-2">
	
		<form:form action="playa/admin/uploadImage.do?playaId=${playaId}" modelAttribute="file" enctype="multipart/form-data">
		
		<jstl:if test="${!hasimage}">
		<img class="img-responsive img-rounded" id="image" src="images/no-image.png" height="350" width="590" /><br /><br />
		</jstl:if>
		<jstl:if test="${hasimage}">
		<img class="img-responsive img-rounded" id="image" src="foto/displayImage.do?playaId=${playaId}" height="350" width="590"/><br /><br />
 		</jstl:if>
 		
 		<input type="file" name="image" onchange="readURL(this);" />
		
			<div class="pull-right saveimic">
			<acme:submit code="playa.save" name="saveFoto" />
			<acme:cancel code="playa.atras" url="/playa/list.do" />
			</div>
		</form:form>
	
	</div>

</body>
</html>