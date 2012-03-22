<!--
//Originally Adapted From:
// JavaScript The Definitive Guide - 5th Edition
// by David Flanagan
// O'Reilly Media Inc. 2006
// but significantly mutated since then

var HTTP = {
};

HTTP._factory = null;

HTTP._factories = [
	function() {return new XMLHttpRequest(); },
	function() {return new ActiveXObject("Msxml2.XMLHTTP"); },
	function() {return new ActiveXObject("Microsoft.XMLHTTP"); }
];

HTTP.newRequest = function() {
	if(HTTP._factory!=null){
		return HTTP._factory();
	}
	for(var i=0;i<HTTP._factories.length;i++){
		try {
			var factory = HTTP._factories[i];
			var request = factory();
			if (request!=null) {
				HTTP._factory=factory;
				return request;
			}
		}catch(e){
			continue;
		}
	}

	HTTP._factory = function() {
		throw new Error("XMLHttpRequest not supported");
	}
	HTTP._factory();
	return null;
};

HTTP.sendXML = function(url,xmltxt){
	var httpRequest = HTTP.newRequest();
	httpRequest.onreadystatechange = function(){getReturnXML(httpRequest);};
	httpRequest.open('POST', url, true);
	httpRequest.send(xmltxt);
	return false;
};

HTTP.sendXMLSingleFile = function(url,xmltxt){
	var httpRequest = HTTP.newRequest();
	httpRequest.onreadystatechange = function(){getReturnXML(httpRequest);};
	httpRequest.open('POST', url, true);
	httpRequest.send(xmltxt);
	return false;
};
-->


