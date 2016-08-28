<%--
 * action-2.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
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

<form action="playa/nearToMe.do" method="get" name="nearToMe">
						<input type="text" name="lat" id="lat" class="hidden"/>
						<input type="text" name="lon" id="lon" class="hidden"/>
						
						<a href="javascript: nearToMe.submit()" class="btn btn-success"> <spring:message code="master.page.playa.nearToMe"/></a>
						<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&key=AIzaSyBBrx34wk6LRo3M4xsVJEe3U9umurfaMik" ></script>
									<script>
										var x = document.getElementById("lat");
										var y = document.getElementById("lon");
							    		if (navigator.geolocation) {
							       			navigator.geolocation.getCurrentPosition(initialize);
							    		}
										function initialize(position) {
											x.value = position.coords.latitude;
											y.value = position.coords.longitude;
											
										}
</script></form>

<div class="table-responsive">
<display:table pagesize="5" class="table table-condensed" keepStatus="true"
	name="playas" requestURI="${requestURI}" id="row">
	<!-- Attributes -->

	<security:authentication var="user" property="principal.id" />
	<display:column>
		<jstl:if test="${row.administrador.userAccount.id == user}">
		<a href="playa/admin/edit.do?playaId=${row.id}" class="btn btn-warning">
			<spring:message code="playa.editar"/>
		</a>
	</jstl:if>
	</display:column>
		
	<display:column>
		<jstl:if test="${row.imagen != null}">
			<img class="img-responsive img-rounded" id="image" src="foto/displayImage.do?playaId=${row.id}" height="75" width="75"/><br />
		</jstl:if>
	</display:column>
	
	<spring:message code="playa.nombre" var="nombre" />
	<display:column property="nombre" title="${nombre}"
		sortable="true" />

	<spring:message code="playa.localizacion.provincia" var="provincia" />
	<display:column property="localizacion.provincia" title="${provincia}"
		sortable="true" />
		
	<spring:message code="playa.localizacion.ciudad" var="ciudad" />
	<display:column property="localizacion.ciudad" title="${ciudad}"
		sortable="true" />
		
		<spring:message code="playa.valoracionMedia" var="valoracionMedia" />
	<display:column property="valoracionMedia" title="${valoracionMedia}"
		sortable="true" />
		
	<spring:message code="playa.detallePlaya" var="detallePlaya" />
		<display:column sortable="false">
			<a href="playa/display.do?playaId=${row.id}" class="btn btn-info"><spring:message code="playa.detallePlaya" /></a>	
			
	</display:column>
	
	<display:column>
				<a href="negocio/listNegocios.do?playaId=${row.id}" class="btn btn-success"><spring:message code="playa.negocios" /></a>
		</display:column>
	
	<security:authorize access="hasRole('ADMINISTRADOR')">
		<display:column>
			<jstl:if test="${row.imagen==null}">
				<a href="playa/admin/uploadImage.do?playaId=${row.id}" class="btn btn-primary"><spring:message code="playa.uploadImage" /></a>
			</jstl:if>
		</display:column>
		
		<jstl:if test="${row.administrador.userAccount.id == user && row.negocios.size() < 1}">
			<display:column>
					<a href="playa/admin/uploadCoordenates.do?playaId=${row.id}" class="btn btn-success"><spring:message code="playa.uploadCoordenates" /></a>
			</display:column>
		</jstl:if>
		
		<spring:message code="playa.delete" var="borrar" />
		<display:column>
		<jstl:if test="${row.administrador.userAccount.id == user && row.negocios.size() < 1}">
			<a href="playa/admin/delete.do?playaId=${row.id}" class="btn btn-danger" onclick="return confirm('<spring:message code="playa.confirmDelete" />')"><spring:message code="playa.delete" /></a>
		</jstl:if>
		</display:column>
	</security:authorize>
		
	<security:authorize access="hasRole('USUARIO')">
		<spring:message code="valoracionPlaya.valoracionPlaya" var="valorarPlaya" />
		<display:column sortable="false">
			<a href="valoracionPlaya/usuario/create.do?playaId=${row.id}" class="btn btn-success"><spring:message code="valoracionPlaya.valoracionPlaya" /></a>	
		</display:column>
	</security:authorize>
	
</display:table>
</div>


<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&key=AIzaSyBBrx34wk6LRo3M4xsVJEe3U9umurfaMik" ></script>
<script>
// In the following example, markers appear when the user clicks on the map.
// The markers are stored in an array.
// The user can then click an option to hide, show or delete the markers.
var map;
var markers = [];

function initialize() {
	<jstl:forEach var="playa" items="${playas}">
	var id= "${playa.id}";
	var name="${playa.nombre}";
	var lat2="${playa.localizacion.latitud}";
	var lon2="${playa.localizacion.longitud}";
	var pos = new google.maps.LatLng("${playa.localizacion.latitud}", "${playa.localizacion.longitud}");
	addMarker(pos, id, name, lat2, lon2);
	</jstl:forEach>
	
	var playas = document.getElementById("playas");
  	var myPosition = new google.maps.LatLng(lat2, lon2);
 	var mapOptions = {
    zoom: 7,
    center: myPosition,
    zoomControl: true,
    streetViewControl: true,
    scrollwheel: true,
    panControl: false,
    mapTypeControl: false,
   
    scaleControl: false 
    
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);


  // Adds a marker at the center of the map.
 
  	
	console.log(playas);
	

	showMarkers();
}

// Add a marker to the map and push to the array.
function addMarker(location, id, name, lat2, lon2) {
	var contentString ="<br><h3>"+name+"</h3>"+ "<button class='btn btn-primary' ><span class='el el-map-marker'></span><a style='color:white' href='playa/display.do?playaId="+id+"'><spring:message code="playa.display"/></a></button>";
		
	var infowindow = new google.maps.InfoWindow({
	      content: contentString
	  });
	
	var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';
  var marker = new google.maps.Marker({
    position: location,
    map: map,
    icon: iconBase + 'sunny.png'
  });
  
  google.maps.event.addListener(marker, 'click', function() {
	    infowindow.open(map,marker);
	  });
  
  markers.push(marker);
}

// Sets the map on all markers in the array.
function setAllMap(map) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
  setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
  setAllMap(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
  clearMarkers();
  markers = [];
}

google.maps.event.addDomListener(window, 'load', initialize);
</script>

<div class="container" style="margin-bottom:50px;">
		<div id="map-canvas" class="Flexible-container" style="border-radius:4px; height:200px;"></div>
	</div>