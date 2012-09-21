//SAVE BUTTON
var baseurl = '../Convert/StreamServer.xml';
var msgbox;
var msgline;
var msgline1;
var contentlen = 0;
var segment;
var reload = 0;

var cancelbutton;
var donebutton;

	function ClientInit(dir,conv){
		//msgbox = document.getElementById("msgbox");
		msgline = document.getElementById("msgline");
		msgline1 = document.getElementById("msgline1");
		cancelbutton = document.getElementById("cancelbutton");
		donebutton = document.getElementById("donebutton");

		cancelbutton.disabled=false;
		donebutton.disabled=true;

		contentlen = 0;
		var url = baseurl+'?dir='+dir+'&conv='+conv;
		//var url = baseurl;
		//alert(url);
		HTTP.doGet(url);
	}

	function getReturnText(httpRequest){
		if(httpRequest.readyState==3){
			if(httpRequest.status==200){
				var result = httpRequest.responseText;
 				segment = result.substring(contentlen);
				//alert(segment);
				//if(segment.indexOf("<datastream")>=0){
				if(segment.indexOf("<value")>=0){
					reload = 0;
					var segmenttxt= segment.split("'");
					var titles = segmenttxt[1].split("|");
					msgline.innerHTML = "<span>"+titles[0]+"</span>";
					if(titles.length>1){
						msgline1.innerHTML = "<span>"+titles[1]+"</span>";
						$("#progressbar1").css({'display':'block;'});
					}else if(titles.length==1){
						$("#progressbar1").css({'display':'none;'});
					}
				}else if(segment.indexOf("<next")>=0){//first segment is the title
					var instruction = segment.split("'");
					if(instruction[1].indexOf('reload')>0){
						reload = 1;
					}
				//}else if(segment.indexOf("</datastream")>=0){
				//}else if(segment.indexOf("</value")>=0){
				}else{//value
					//var p = parseInt(segment);
					//$("#progressbar").progressbar('value',p);

					//msgbox.value = segment;
					var segmentline = segment.split(" ");
					var segmentval = segmentline[segmentline.length-1].split("|");

					var p = parseInt(segmentval[0]);
					$("#progressbar").progressbar('value',p);
					var p1 = parseInt(segmentval[1]);
					$("#progressbar1").progressbar('value',p1);
					if(p>99){
						cancelbutton.disabled=true;
						donebutton.disabled=false;
					}
				}
				contentlen = result.length;
			}
		}else if(httpRequest.readyState==4){
			if(httpRequest.status==200){
				//msgbox.value = httpRequest.responseText;
				$("#progressbar").progressbar('value',100);
				$("#progressbar1").progressbar('value',100);
				contentlen = 0;
			}else{
				//alert("httpRequest status error:"+httpRequest.status);
			}
		}
	}


