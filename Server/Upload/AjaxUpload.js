//SAVE BUTTON
var url = '../Upload/AjaxServer.xml';
var msgbox;
var progressbox;
var sentbytes;
var recvdbytes;
var dirname;

	function UploadInit(){
		//REMOVES NEED FOR UPLOAD BUTTON
		var fu = document.getElementById("file_upload");
		if(fu!=null){
			fu.addEventListener("change",FileSelectHandler,false);
		}

		msgbox = document.getElementById("upload_msgbox");
		if(msgbox!=null){
			msgbox.addEventListener("dragenter",function(e){e.preventDefault();},false);
			msgbox.addEventListener("dragover",function(e){e.preventDefault();},false);
			msgbox.addEventListener("drop",FileSelectHandler,false);
		}

		progressbox = document.getElementById("upload_progressbox");
		if(progressbox!=null){
			progressbox.addEventListener("dragenter",function(e){e.preventDefault();},false);
			progressbox.addEventListener("dragover",function(e){e.preventDefault();},false);
			progressbox.addEventListener("drop",FileSelectHandler,false);
		}

		if(document.getElementById("dirname")!=null){
			dirname = document.getElementById("dirname").value;
		}
	}

	function FileSelectHandler(e){
		e.preventDefault();
		var files = e.target.files || e.dataTransfer.files;
		sentbytes = 0;
		recvdbytes = 0;
		for(var i = 0,f;f=files[i];i++){
			uploadFile(f);
			makeSimpleFrame(this,'../Progress/MonitorServer.html');
			sentbytes += f.size;
		}
	}

	function uploadFile(file){
		//alert('filetype '+file.type+' of size '+file.size);
		var reader = new FileReader();
		reader.onload = function(e){
			var sendurl = url+'?fn='+file.name+'&mt='+file.type+'&sz='+file.size+'&dir='+dirname;
			HTTP.sendXML(sendurl,e.target.result);
		};
		if(file.type.indexOf("text") == 0){
			reader.readAsText(file);
		}else if(file.type.indexOf("image") == 0){
			reader.readAsDataURL(file);
		}else if(file.type.indexOf("application") == 0){
			reader.readAsDataURL(file);
		}else if(file.name.indexOf(".rng")){
			reader.readAsText(file);
		}
	}

	function getReturnXML(httpRequest){
		if(httpRequest.readyState==4){
			if(httpRequest.status==200){
				//alert(httpRequest.responseText);
				var xmldoc = httpRequest.responseXML;
				var response = xmldoc.getElementsByTagName('Response')[0];
				var msg = xmldoc.getElementsByTagName('Msg')[0];
				var filename = msg.getAttribute("filename");
				var filesize = msg.getAttribute("filesize");
				recvdbytes += parseInt(filesize);
				var p = parseInt(recvdbytes/sentbytes*100);
				if(p<100){
					progressbox.innerHTML = '<progress value="'+p+'" max="100">'+p+'%</progress>';
				}else{
					//alert('reload');
					location.replace('FileManager.html?dir='+dirname);
				}
				var msgtxt = "";
				if(msg.firstChild!=null){
					msgtxt = msg.firstChild.nodeValue;
				}
				msgbox.innerHTML = "<div>"+msgtxt+"</div>";
			}else{
				alert("httpRequest status error:"+httpRequest.status);
			}
		}
	}


//CLIENT FUNCTIONS

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


