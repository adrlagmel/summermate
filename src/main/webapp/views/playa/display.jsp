<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="playa" >	

	<input type="hidden" id="playa" value="${playa}"/><br/>
	
	<div style="width:50%; margin: auto;" >	
	<jstl:if test="${playa.imagen!=null}">
		<img class="img-responsive img-rounded" src="foto/displayImage.do?playaId=${playa.id}" height="350" width="590" />
	</jstl:if><br/>
	</div>
	
	
	<div style="width:50%; margin: auto;" >
		<acme:textarea code="playa.nombre" path="nombre" readonly="true"/><br/>
		<acme:textarea code="playa.descripcion" path="descripcion" readonly="true"/><br/>
		<acme:textarea code="playa.servicios" path="servicios" readonly="true"/>	<br/>
	</div>
	<acme:textbox code="playa.composicion" path="composicion" readonly="true"/>	<br/>
	<acme:textbox code="playa.extension" path="extension" readonly="true"/>	<br/>
	<acme:textbox code="playa.valoracionMedia" path="valoracionMedia" readonly="true"/>	<br/>
	
	<br/>
</form:form>
	
	
<form>
<input type="hidden" id="playa" value="${playas}"/>
</form>
	
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
	var contentString ="<br><h3>"+name+"</h3>";
		
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
	
<div style="width:50%; margin: auto;" >
 	<acme:cancel code="playa.atras" url="/playa/list.do" />
	</div>	