//SAVE BUTTON
var url = '../Upload/AjaxServer.xml';
var watchurl = '../Upload/MonitorServer.xml';
var msgbox;
//var progressbox;
var progressbarjq;
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

		//progressbox = document.getElementById("upload_progressbox");
		//if(progressbox!=null){
		//	progressbox.addEventListener("dragenter",function(e){e.preventDefault();},false);
		//	progressbox.addEventListener("dragover",function(e){e.preventDefault();},false);
		//	progressbox.addEventListener("drop",FileSelectHandler,false);
		//}

		if(document.getElementById("dirname")!=null){
			dirname = document.getElementById("dirname").value;
		}

		progressbarjq = document.getElementById("progressbar");
	}

	function FileSelectHandler(e){
		e.preventDefault();
		var files = e.target.files || e.dataTransfer.files;
		sentbytes = 0;
		recvdbytes = 0;
		//makeSimpleFrame(this,'../Stream/StreamClient.html');
		for(var i = 0,f;f=files[i];i++){
			sentbytes += f.size;
		}
		//HTTP.watchFiles(watchurl+'?dir='+dirname+'&totsz='+sentbytes);
		for(var i = 0,f;f=files[i];i++){
			//alert('IU'+i+' '+files[i].size);
			uploadFile(f,sentbytes);
		}
		//alert(sentbytes);
	}

	function uploadFile(file,sentbytes){
		//alert('filetype '+file.type+' of size '+file.size+' of total '+sentbytes);
		var reader = new FileReader();
		reader.onload = function(e){
			var sendurl = url+'?fn='+file.name+'&mt='+file.type+'&sz='+file.size+'&dir='+dirname+'&totsz='+sentbytes;
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
				var complete = response.getAttribute("complete");
				var msg = xmldoc.getElementsByTagName('Msg')[0];
				var filename = msg.getAttribute("filename");
				var filesize = msg.getAttribute("filesize");
				recvdbytes += parseInt(filesize);
				var p = parseInt(((recvdbytes)/sentbytes)*100);
				//alert("RB<"+recvdbytes+"> SB<"+sentbytes+">FN<"+filename+"> FS<"+filesize+"> P<"+p+"> COMP<"+complete+">");
				if(p<99){
					//progressbox.innerHTML = '<progress value="'+p+'" max="100">'+p+'%</progress>';
					$("#progressbar").progressbar('value',p);
				}else{
					//location.replace('FileManager.html?dir='+dirname);
					window.location.href='FileManager.html?dir='+dirname;
				}
			}else{
				//alert("httpRequest status error:"+httpRequest.status);
			}
		}
	}

	function getReturnWatch(httpRequest){
		if(httpRequest.readyState==4){
			if(httpRequest.status==200){
				alert(httpRequest.responseText);
			}else{
				//alert("httpRequest status error:"+httpRequest.status);
			}
		}
	}


