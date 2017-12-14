<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<h2>spring websocket demo 依赖JDK7和Tomcat8</h2>
<input type="button" id="connect" value="connect" onclick="connect();">
<input type="button" id="disconnect" value="disconnect" onclick="disconnect();">
<p id="console"></p>
<div>
	<textarea id="message" rows="10" cols="50" placeholder="请输入消息."></textarea>
</div>
<div>
	<button id="echo" onclick="echo();" disabled="disabled">Send Message</button>
</div>
</body>
<script type="text/javascript" src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
<script type="text/javascript">
	var ws = null;
	var url = null;
	var transports = [];
	function setConnected(connected) {
		document.getElementById("connect").disabled = connected;
		document.getElementById("disconnect").disabled = !connected;
		document.getElementById("echo").disabled = !connected;
	}
	
	function connect() {
		var protocol = location.protocol, host = location.host, wsProtocol = "ws:";
		if(protocol === 'http') {
			wsProtocol = "ws:";
		} else if(protocol === 'https') {
			wsProtocol = "wss:";
		}
		var transports = ['xdr-streaming', 'xhr-streaming', 'eventsource', 'iframe-eventsource', 
		                  'htmlfile', 'iframe-htmlfile', 'xdr-polling', 'xhr-polling', 
		                  'iframe-xhr-polling', 'jsonp-polling'];
		if('WebSocket' in window) {
			ws = new WebSocket(wsProtocol + "//" + host + "/sys/info");
		} else if('MozWebSocket' in window) {
			// 火狐
			ws = new MozWebSocket(wsProtocol + "//" + host + "/sys/info");
		} else {
			// 没有WebSocket情况下使用SockJS
			ws = new SockJS(protocol + "//" + host + "/sockjs/sys/info", null, {transports:transports});
		}
		ws.onopen = function() {
			setConnected(true);
			log('Info: connection opened.');
		}
		ws.onmessage = function(event) {
			console.log(event);
			log('Received：' + event.data);
		}
		ws.onclose = function(event) {
			setConnected(false);
			log('Info：connection closed.');
			// log(event);
		}
	}
	
	function echo() {
		if(ws != null) {
			var message = document.getElementById("message").value;
			log('Sent：' + message);
			ws.send(message);
		} else {
			alert('connection not established, please connect.');
		}
	}
	
	function disconnect() {
		if(ws != null) {
			ws.close();
			ws = null;
		}
		setConnected(false);
	}
	
	function log(message) {
		console.log("message：" + message);
		var cons = document.getElementById('console');
		var p = document.createElement('p');
		p.style.wordWrap = "break-word";
		p.appendChild(document.createTextNode(message));
		cons.appendChild(p);
		while(cons.childNodes.length > 25) {
			cons.removeChild(cons.firstChild);
		}
		cons.scrollTop = cons.scrollHeight;
	}
</script>
</html>
