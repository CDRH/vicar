//SAVE BUTTON
var url = 'UploadServer.xml';
var dirname;
var fileupload;
var ddupload;
var progressbarjq;
var msgbox;
var pbdiv;

var sentbytes;
var recvdbytes;

	function UploadInit(){
		//REMOVES NEED FOR UPLOAD BUTTON
		if(document.getElementById("dirname")!=null){
			dirname = document.getElementById("dirname").value;
		}
		fileupload = document.getElementById("file_upload");
		if(fileupload!=null){
			fileupload.addEventListener("change",FileSelectHandler,false);
		}

		progressbarjq = document.getElementById("myprogressbar");
		msgbox = document.getElementById("upload_text");

		ddupload = document.body;
		if(ddupload!=null){
			ddupload.addEventListener("dragenter",function(e){e.preventDefault();},false);
			ddupload.addEventListener("dragover",function(e){e.preventDefault();},false);
			ddupload.addEventListener("drop",FileSelectHandler,false);
		}

		pbdiv = document.getElementById("upload_progressbox");
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
			//alert(sendurl);
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
				var xmldoc = httpRequest.responseXML;
				var response = xmldoc.getElementsByTagName('Response')[0];
				var complete = response.getAttribute("complete");
				var msg = xmldoc.getElementsByTagName('Msg')[0];
				var msgtxt = msg.firstChild.nodeValue;
				var filename = msg.getAttribute("filename");
				var filesize = msg.getAttribute("filesize");
				recvdbytes += parseInt(filesize);
				var p = parseInt(((recvdbytes)/sentbytes)*100);
				//alert("RB<"+recvdbytes+"> SB<"+sentbytes+">FN<"+filename+"> FS<"+filesize+"> P<"+p+"> COMP<"+complete+"> MSG<"+msg+">");
				if(p<99){
					$("#myprogressbar").progressbar('value',p);
					msgbox.innerHTML = msgbox.innerHTML+"<span>"+msgtxt+"</span><br />";
				}else{
					$("#myprogressbar").progressbar('value',100);
					msgbox.innerHTML = msgbox.innerHTML+"<span>"+msgtxt+"</span><br />";
					msgbox.innerHTML = msgbox.innerHTML+" "+"TOTAL "+sentbytes;
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

function pausecomp(millis) {
var date = new Date();
var curDate = null;

do { curDate = new Date(); } 
	while(curDate-date < millis);
} 


