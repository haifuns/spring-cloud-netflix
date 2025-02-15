<#import "/spring.ftl" as spring />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="${basePath}">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hystrix Dashboard</title>

	<!-- Javascript to monitor and display -->
	<script src="<@spring.url '/webjars/jquery/2.1.1/jquery.min.js'/>" type="text/javascript"></script>
	
	<script>
		function sendToMonitor() {
			
			if($('#stream').val().length > 0) {
				var url = "<@spring.url '/hystrix'/>/monitor?stream=" + encodeURIComponent($('#stream').val()) + "";
				if($('#delay').val().length > 0) {	
					url += "&delay=" + $('#delay').val();
				}
				if($('#title').val().length > 0) {	
					url += "&title=" + encodeURIComponent($('#title').val());
				}
				location.href= url;
			} else {
				$('#message').html("The 'stream' value is required.");
			}
		}
	</script>
</head>
<body>
<div style="width:800px;margin:0 auto;">
	
	<center>
	<img width="264" height="233" src="<@spring.url '/hystrix'/>/images/hystrix-logo.png">
	<br>
	<br>
	
	<h2>Hystrix Dashboard</h2>
	<input id="stream" type="textfield" size="120" placeholder="https://hostname:port/turbine/turbine.stream"></input>
	<br><br>
	<i>Cluster via Turbine (default cluster):</i> https://turbine-hostname:port/turbine.stream
	<br>
	<i>Cluster via Turbine (custom cluster):</i> https://turbine-hostname:port/turbine.stream?cluster=[clusterName]
	<br>
	<i>Single Hystrix App:</i> https://hystrix-app:port/hystrix.stream
	<br><br>
	Delay: <input id="delay" type="textfield" size="10" placeholder="2000"></input>ms 
	&nbsp;&nbsp;&nbsp;&nbsp; 
	Title: <input id="title" type="textfield" size="60" placeholder="Example Hystrix App"></input><br>
	<br>
	<button onclick="sendToMonitor()">Monitor Stream</button>
	<br><br>
	<div id="message" style="color:red"></div>
	
	</center>
</div>
</body>
</html>