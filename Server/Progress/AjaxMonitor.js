//SAVE BUTTON
var url = 'MonitorServer.xml';
var msgbox;
var progressbox;
var botmsgbox;
var botprogressbox;
var sentbytes;
var recvdbytes;
var dirname;
var p = 0;
var disp = 0;

	function MonitorInit(){
		HTTP.doGet(url+'?new=true');
		msgbox = document.getElementById("top_msgbox");
		progressbox = document.getElementById("top_progressbox");
		botmsgbox = document.getElementById("bot_msgbox");
		botprogressbox = document.getElementById("bot_progressbox");
	}

	HTTP.doGet = function(url){
		var httpRequest = HTTP.newRequest();
		httpRequest.onreadystatechange = function(){getMonitorReturn(httpRequest);};
		httpRequest.open('GET',url,true);
		httpRequest.send();
		return false;
	}

	function getMonitorReturn(httpRequest){
		if(httpRequest.readyState==4){
			if(httpRequest.status==200){
				//alert(httpRequest.responseXML);
				var xmldoc = httpRequest.responseXML;
				var mon = xmldoc.getElementsByTagName("Monitor")[0];
				disp = mon.getAttribute("dispose");
				var prog = xmldoc.getElementsByTagName("Prog");
				for(var i=0;i<prog.length;i++){
					//alert(prog[i]);
					var topval = prog[i].getAttribute("value");
					if(prog[i].firstChild!=null){//ONLY USE 2 FOR NOW
						var montxt = prog[i].firstChild.nodeValue;
						if(i==0){
							msgbox.innerHTML = "<div style='color:red;'>"+montxt+"</div>";
							progressbox.innerHTML = "<progress value='"+topval+"' max='100'>"+topval+"%</progress>";
						}else if(i==1){
							botmsgbox.innerHTML = "<div style='color:red;'>"+montxt+"</div>";
							botprogressbox.innerHTML = "<progress value='"+topval+"' max='100'>"+topval+"%</progress>";
						}
					}
				}


				if(disp<1){
					HTTP.doGet(url);
				}
			}else{
				alert("httpRequest status error:"+httpRequest.status);
			}
		}
	}

	function FileSelectHandler(e){
		e.preventDefault();
		var files = e.target.files || e.dataTransfer.files;
		sentbytes = 0;
		recvdbytes = 0;
		for(var i = 0,f;f=files[i];i++){
			uploadFile(f);
			//makeSimpleFrame(this,'../Progress/MonitorServer.html');
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


