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


<link rel=StyleSheet href="http://mondeca.com/mdc_css/A.weather.css.pagespeed.cf.bm2KxYrJN6.css" type="text/css"/>

 <base href="http://mondeca.com/index.php/en/any-place-en" />
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <meta name="keywords" content="geolocation" />
  <meta name="description" content="Find location coordinates with Google Maps" />
  <meta name="generator" content="Joomla! - Open Source Content Management  - Version 3.4.1" />
  <title>Any Place En</title>
  <link href="http://mondeca.com/index.php/en/any-place-en" rel="canonical" />
  <link href="/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
  <link rel="stylesheet" href="/t3-assets/css/css-7a2c4.css?t=792" type="text/css" />
  <link rel="stylesheet" href="/t3-assets/css/css-66b8a.css?t=792" type="text/css" />
  <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:500,400italic,100,300,500italic,300italic,400,200" type="text/css" />
  <link rel="stylesheet" href="/t3-assets/css/css-a062a.css?t=346" type="text/css" />
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  
  <script src="/t3-assets/js/js-5c76c.js?t=297" type="text/javascript"></script>
  <script src="http://ajax.googleapis.com/ajax/libs/webfont/1/webfont.js" type="text/javascript"></script>
  <script src="/t3-assets/js/js-e03bf.js?t=345" type="text/javascript"></script>
  <script type="text/javascript">
  
	jQuery(window).on('load',  function() {
	                                new JCaption('img.caption');
	                        });
	jQuery(document).ready(function(){
	        jQuery('.hasTooltip').tooltip({"html": true,"container": "body"});
	});
	;jQuery(document).ready(function(){
	        jQuery('.hasTooltip').tooltip({"html": true,"container": "body"});
	});
	  </script>
	  <script type="text/javascript">
	    (function() {
	      Joomla.JText.load({"JLIB_FORM_FIELD_INVALID":"Invalid field:&#160"});
	    })();
  </script>
 

<!-- //META FOR IOS & HANDHELD -->
 
 
<!-- You can add Google Analytics here or use T3 Injection feature -->
<meta name="google-site-verification" content="iD_X10CcCcgJfT4IZJ8Un-orddTOeuISR8CZ9R8VXFw" />
 
 
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments);},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m);
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
 
  ga('create', 'UA-17663461-1', 'auto');
  ga('send', 'pageview');
 
</script>
<!-- Universal Google Analytics Plugin by PB Web Development -->

</head>

<body>

<jstl:if test="${mostrarMapa!=null}">
<div>
		
</div>
</jstl:if>

<form>
	<input type="text" name="playaId" id="playaId" class="hidden"/>
</form>

<div class="custom"  >
 
<script type="text/javascript"
        src="https://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
<script type="text/javascript"
        src="https://code.jquery.com/jquery-2.1.3.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery.simpleWeather/3.0.2/jquery.simpleWeather.min.js"></script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=AIzaSyBVTKLztwVOGDuo1qGsjHzdY7wXRcKbAVI"> </script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script>
// Docs at http://simpleweatherjs.com
 
jQuery(document).ready(function($) {
		
        var weatheron=0;
        $('#weatherbutton').on('click', function() {
    weatheron=1-weatheron;
         ga('send', 'event', 'button', 'click', 'weather');
     
     
    if (weatheron)
    {
                lng=$('#lng')[0].innerHTML;
                lat=$('#lat')[0].innerHTML;
        loadWeather(lat+','+lng); //load weather using your lat/lng coordinates
        $('#weatherbutton').html('Hide weather');
        $('#weatherbutton').css('color', 'white');
    }
      else
      {
       
        $('#weather').html('');
        $('#weatherbutton').html('Show weather');
        $('#weatherbutton').css('color', 'white');
         
     }

});
 
});
</script>
<script>
  function load() {
 
      if (GBrowserIsCompatible()) {
        var map = new GMap2(document.getElementById("map"));
        map.addControl(new GSmallMapControl());
        map.addControl(new GMapTypeControl());
        var center = new GLatLng(36.65909, -4.50927);
        map.setCenter(center, 7);
        geocoder = new GClientGeocoder();
        var marker = new GMarker(center, {draggable: true});  
        map.addOverlay(marker);
        document.getElementById("lat").innerHTML = center.lat().toFixed(5);
        document.getElementById("lng").innerHTML = center.lng().toFixed(5);
 
       
        GEvent.addListener(map, "dragstart", function() {
                document.getElementById("weather").innerHTML = "";
          document.getElementById("weatherbutton").innerHTML = "Show weather";
       
         });
             
       
          GEvent.addListener(marker, "dragend", function() {
        ga('send', 'event', 'map', 'drag/move', 'map');
       var point = marker.getPoint();
              map.panTo(point);
       document.getElementById("lat").innerHTML = point.lat().toFixed(5);
       document.getElementById("lng").innerHTML = point.lng().toFixed(5);
 
        });
       GEvent.addListener(marker, "dragstart", function() {
                document.getElementById("weather").innerHTML = "";
            document.getElementById("weatherbutton").innerHTML = "Show weather";
       
         });
       
 
         GEvent.addListener(map, "moveend", function() {
       ga('send', 'event', 'map', 'drag/move', 'map');
                  map.clearOverlays();
    var center = map.getCenter();
                  var marker = new GMarker(center, {draggable: true});
                  map.addOverlay(marker);
                  document.getElementById("lat").innerHTML = center.lat().toFixed(5);
           document.getElementById("lng").innerHTML = center.lng().toFixed(5);
 
 
         GEvent.addListener(marker, "dragend", function() {
       
       ga('send', 'event', 'map', 'drag/move', 'map');
      var point =marker.getPoint();
             map.panTo(point);
      			document.getElementById("lat").innerHTML = point.lat().toFixed(5);
             	document.getElementById("lng").innerHTML = point.lng().toFixed(5);
             	
        });
 
        });
 
      }
    }
 
           function showAddress(address) {
           var map = new GMap2(document.getElementById("map"));
       map.addControl(new GSmallMapControl());
       map.addControl(new GMapTypeControl());
       if (geocoder) {
        geocoder.getLatLng(
          address,
          function(point) {
            if (!point) {
              alert(address + " not found");
            } else {
                  document.getElementById("lat").innerHTML = point.lat().toFixed(5);
           document.getElementById("lng").innerHTML = point.lng().toFixed(5);
                 map.clearOverlays();
                        map.setCenter(point, 14);
   var marker = new GMarker(point, {draggable: true});  
                 map.addOverlay(marker);
 
                GEvent.addListener(marker, "dragend", function() {
      var pt = marker.getPoint();
             map.panTo(pt);
      document.getElementById("lat").innerHTML = pt.lat().toFixed(5);
             document.getElementById("lng").innerHTML = pt.lng().toFixed(5);
        });
 
 
         GEvent.addListener(map, "moveend", function() {
                  map.clearOverlays();
    var center = map.getCenter();
                  var marker = new GMarker(center, {draggable: true});
                  map.addOverlay(marker);
                  document.getElementById("lat").innerHTML = center.lat().toFixed(5);
           document.getElementById("lng").innerHTML = center.lng().toFixed(5);
 
         GEvent.addListener(marker, "dragend", function() {
     var pt = marker.getPoint();
            map.panTo(pt);
    		document.getElementById("lat").innerHTML = pt.lat().toFixed(5);
           	document.getElementById("lng").innerHTML = pt.lng().toFixed(5);
           	
 			
        });
 
         
     	
        GEvent.addListener(marker, "dragstart", function() {
                console.log('dragstart');
                document.getElementById("weather").innerHTML = "";
                document.getElementById("weatherbutton").innerHTML = "Show weather";
        });
       
       
        });
 
            }
          }
        );
      }
    }
    	
       function extraerParametroUrl(parametro){
           var regexS = "[\\?&]"+parametro+"=([^&#]*)";
           var regex = new RegExp ( regexS );
           var tmpURL = window.location.href;
           var results = regex.exec( tmpURL );
           	if(results == null){ 
           		return "";
           	}else{
           		
           	return results[1];
           	}
  		};
          	
           
     function newDoc() {
    	 
    	 playaId=$('#playaId')[0].innerHTML;
    	 
    	 lng=$('#lng')[0].innerHTML;
         lat=$('#lat')[0].innerHTML;
   	 	
   	 	window.location.assign("playa/coordenates/save.do?lat=" + lat + "&lon=" + lng + "&playaId=" + parseInt(extraerParametroUrl('playaId')));
	};
	
 
  if(window.attachEvent) {
    window.attachEvent('onload', load);
} else {
    if(window.onload) {
        var curronload = window.onload;
        var newonload = function() {
            curronload();
            load();
        };
        window.onload = newonload;
    } else {
        window.onload = load;
    }
}

</script>´



<div style="display:flex;flex-wrap:wrap; margin-top: 2%; margin-bottom: 2%;">
		
        <div align="center" id="map" style="min-width:300px; min-height:300px;max-width:600px;max-height:600px; width:48%;"></div>
        <div style="display: flex; flex-wrap:wrap; flex-direction:column;
               
              max-width: 600px; justify-content:flex-start; min-width:300px; width:48%; ">
      
      <table class="table" style="margin-left:5%; margin-top:5%; width:100%;">
            <tbody>
              <tr><th>Latitude</th><th>Longitude</th><th></th></tr>
              <tr>
                  <td style="font-size: 48px; color: green;" id="lat">&nbsp;</td>
                  <td style="font-size: 48px; color: green;" id="lng">&nbsp;</td>
                   <td style="vertical-align:middle;">
                  <button class="weather btn btn-mondeca text-center" onclick="newDoc()" style="float:right;" >Guardar</button></td>
             </tr>
      </tbody>
        </table>
        <div id="weather"></div>
    </div>
  	
  </div>
 
<script>
var formaddress = document.getElementById("address");
 
formaddress.addEventListener("focusin", myFocusFunction);
formaddress.addEventListener("focusout", myBlurFunction);
 
function myFocusFunction() {
    document.getElementById("weather").innerHTML = "";
 
}
 
function myBlurFunction() {
    document.getElementById("weather").innerHTML = "";
}
 
</script>
 
 

</div>
 
                                       
                       
<div id="None" class=" col-lg-12 col-md-12 col-sm-12 col-xs-12">
     &nbsp;
</div>
                       	
 
 
</body>
</html>
 