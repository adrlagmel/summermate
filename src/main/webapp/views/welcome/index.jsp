<%--
 * index.jsp
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
<style> 
 	body {
 	  background: url(images/summer-814679.png) no-repeat center center fixed; 
  		-webkit-background-size: cover;
	  -moz-background-size: cover;
	  -o-background-size: cover;
	  background-size: cover;
}</style><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>


<div class="container" style="margin-bottom:50px;">
		<div id="map-canvas" class="Flexible-container" style="border-radius:4px; height:200px;"></div>
	</div>