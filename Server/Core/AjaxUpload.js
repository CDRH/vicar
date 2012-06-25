//SAVE BUTTON
var url = 'AjaxServer.xml';
var progressbox;
var msgbox;
var sentbytes;
var recvdbytes;
var dirname;

	function Init(){
		//REMOVES NEED FOR UPLOAD BUTTON
		var fu = document.getElementById("file_upload");
		if(fu!=null){
			fu.addEventListener("change",FileSelectHandler,false);
		}

		msgbox = document.getElementById("msgbox");
		if(msgbox!=null){
			msgbox.addEventListener("dragenter",function(e){e.preventDefault();},false);
			msgbox.addEventListener("dragover",function(e){e.preventDefault();},false);
			msgbox.addEventListener("drop",FileSelectHandler,false);
		}

		progressbox = document.getElementById("progressbox");
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
//		msgbox.innerHTML = "<div><b>Drop files here</b></div>";
		e.preventDefault();
		var files = e.target.files || e.dataTransfer.files;
		sentbytes = 0;
		recvdbytes = 0;
		for(var i = 0,f;f=files[i];i++){
			uploadFile(f);
			makeSimpleFrame(this,'../Prog/MonitorServer.html');
			sentbytes += f.size;
			//alert(sentbytes);
		}
		
//		msgbox.innerHTML += "<div>SENT "+sentbytes+"</div>";
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
				//msgbox.innerHTML += "<div>RECVD "+recvdbytes+" PCT "+p+"</div>";
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
				//msgbox.innerHTML += "<div>"+msgtxt+"</div>";
				//alert(msgbox.innerHTML);
			}else{
				alert("httpRequest status error:"+httpRequest.status);
			}
		}
	}

//window.addEventListener("load",Init,false);
