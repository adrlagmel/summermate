
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<jstl:if test="${mostrarMapa!=null}">

<div>
	
	
</div>
</jstl:if>
<form>
<input type="hidden" id="lat1" value="${lat1}"/>
<input type="hidden" id="lon1" value="${lon1 }"/>
<input type="hidden" id="sportCenters" value="${sportCenters}"/>
</form>



<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>
<script>
// In the following example, markers appear when the user clicks on the map.
// The markers are stored in an array.
// The user can then click an option to hide, show or delete the markers.
var map;
var markers = [];

function initialize() {
	var lat1 = document.getElementById("lat1");
	var lon1 = document.getElementById("lon1");
	var sportCenters = document.getElementById("sportCenters");
  var myPosition = new google.maps.LatLng(lat1.value, lon1.value);
  var mapOptions = {
    zoom: 15,
    center: myPosition,
    zoomControl: true,
    zoomControlOptions: {
    	style: google.maps.ZoomControlStyle.LARGE,
        position: google.maps.ControlPosition.LEFT_CENTER
      },
      streetViewControl: true,
      streetViewControlOptions: {
          
      },
     scrollwheel: false,
    panControl: true,
    mapTypeControl: true,
    mapTypeControlOptions: {
        style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
        position: google.maps.ControlPosition.BOTTOM_CENTER
    },
   
    scaleControl: true 
    
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);


  // Adds a marker at the center of the map.
  var marker = new google.maps.Marker({
		position: myPosition,
		draggable: true,
		map: map,
		title: '${location.name}'
		});
			google.maps.event.addListener(marker, 'click', function() {
			map.setCenter(marker.getPosition());
			x.value = marker.getPosition().lat();
			y.value = marker.getPosition().lng();
			marker.setMap(null);
			window.location.href = "sportCenter/nearToMe.do?lat="+marker.getPosition().lat()+"&lon="+marker.getPosition().lng();
		});
	console.log(sportCenters);
	<jstl:forEach var="sportCenter" items="${sportCenters}">
		var id= "${sportCenter.id}";
		var name="${sportCenter.name}";
		var priceHour="${sportCenter.priceHour}";
		var publicCapacity="${sportCenter.publicCapacity}";
		var lat2="${sportCenter.location.latitude}";
		var lon2="${sportCenter.location.longitude}";
		var pos = new google.maps.LatLng("${sportCenter.location.latitude}", "${sportCenter.location.longitude}");
		addMarker(pos,id,name,priceHour,publicCapacity,lat1,lon1,lat2,lon2)
	</jstl:forEach>

	showMarkers();
}

// Add a marker to the map and push to the array.
function addMarker(location,id,name,priceHour,publicCapacity,myLat,myLon,lat2,lon2) {
	var contentString ="<br><h3>"+name+"</h3>"+
		"<h4>"+priceHour+"<span class='el el-eur'/></h4>"+
		"<h4>"+publicCapacity+"<span class='el el-person'/></h4>"+
		"<button class='btn btn-primary' ><span class='el el-map-marker'></span><a style='color:white' href='sportCenter/display.do?sportCenterId="+id+"'><spring:message code="sportCenter.display"/></a></button>"+
		"    <button class='btn btn-primary' ><span class='el el-map-marker'></span><a style='color:white' target=\"_blank\" href='https://www.google.es/maps/dir/"+myLat.value+","+myLon.value+"/"+location.lat()+","+location.lng()+"'><spring:message code="sportCenter.route"/></a></button>";
		
	var infowindow = new google.maps.InfoWindow({
	      content: contentString
	  });
	
	var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';
  var marker = new google.maps.Marker({
    position: location,
    map: map,
    icon: iconBase + 'schools_maps.png'
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
<div id="map-canvas" class="Flexible-container" style="border-radius:4px;"></div>
</div>