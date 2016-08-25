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
				$('#image').attr('src', e.target.result).width(250);
			};
			reader.readAsDataURL(input.files[0]);
		}
	}
</script>

	<div class="col-md-7  col-md-offset-2" style ="margin: auto;">
	
		<form:form action="usuario/uploadImageUsuario.do?usuarioId=${usuarioId}" enctype="multipart/form-data">
		
		<jstl:if test="${!hasimage}">
		<div style="width:50%; margin: auto;">
			<img class="img-responsive img-circle" id="image" src="images/no-image.png" height="250" width="250" /><br /><br />
		</div>
		</jstl:if>
		<jstl:if test="${hasimage}">
		<div style="width:50%; margin: auto;">
			<img class="img-responsive img-circle" id="image" src="foto/displayImagePerfil.do?usuarioId=${usuarioId}" height="250" width="250"/><br /><br />
 		</div>
 		</jstl:if>
 		
 		<input type="file" name="foto" onchange="readURL(this);" />
		
			<div class="pull-right saveimic">
			<acme:submit code="usuario.save" name="save" />
			</div>
		</form:form>
	
	</div>

</body>
</html>