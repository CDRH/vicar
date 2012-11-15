var semaph = false;

//ID IS TO FORCE BROWSERS TO NOT CACHE THE VALUE OF 'src' IN IFRAMES

function show(obj,innerHtml) {
	if(semaph==false){
		setPos(obj);
		var mydivX = document.getElementById('mydiv');
		mydivX.innerHTML = innerHtml;
		mydivX.style.display = 'block';
		semaph = true;
	}
}

function makeSimpleFrame(obj,url){
	clickedobj = obj;
	var schsel = document.getElementById('schemaselect');
	var ssval = schsel.options[schsel.selectedIndex].value;
	//alert('VAL'+ssval);
	var pos = getPos(obj);
        var t = "<div class='innerpopup' style='width:400px;border:3px solid red;'>\n";
        t += "<iframe id='"+((new Date()).getTime())+"' width='99%' height='130px' style='border:0px;' src='"+url+"&conv="+ssval+"'></iframe>";
        t += "</div>";
        show(clickedobj,t);
}

function hideForum(obj,url) {
	if(semaph==true){
		var mydivX = document.getElementById('mydiv');
		mydivX.style.display = 'none';
		semaph = false;
	}
	location.replace(url);
}

function getPos(obj){
	var curleft = curtop = 0;
	if(obj.offsetParent){
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while(obj = obj.offsetParent);
	}
	return [curleft,curtop];
}

function hide(obj) {
	if(semaph==true){
		var mydivX = document.getElementById('mydiv');
		mydivX.style.display = 'none';
		semaph = false;
	}
	location.reload();
}


function makeFramePopup(obj,url,w,h){
	clickedobj = obj;
	var scrolltop = document.documentElement.scrollTop;
	var t = "<table class='innerpopup' style='position:absolute;left:50px;top:"+scrolltop+"px'>\n";
	t += "<tr><td><iframe class='innerpopup' id='"+((new Date()).getTime())+"' width='"+w+"' height='"+h+"' src='"+url+"'></iframe></td></tr>";
	t += "</table>";
	show(clickedobj,t);
}

function makeFramePopupNE(obj,url,w,h){
	var winh = document.documentElement.clientHeight;
	var winw = document.documentElement.clientWidth;
	var scrolltop = document.documentElement.scrollTop;
	clickedobj = obj;
	var t = "<table class='innerpopup' style='position:absolute;left:"+(winw-w-50)+"px;top:"+scrolltop+"px;'>\n";
	t += "<tr><td><iframe class='innerpopup' id='"+((new Date()).getTime())+"' width='"+w+"' height='"+h+"' src='"+url+"'></iframe></td></tr>";
	t += "</table>";
	show(clickedobj,t);
}

function makeFramePopupCenter(obj,url,w,h){
	alert("center");
	var winh = document.documentElement.clientHeight;
	var winw = document.documentElement.clientWidth;
	//var scrolltop = document.documentElement.scrollTop;
	var objpos = getPos(obj);
	//alert(objpos[0]+' '+objpos[1]);
	clickedobj = obj;
	var t = "<table class='innerpopup' style='position:absolute;left:"+((winw-w)/2)+"px;top:"+(objpos[1]-(h/2))+"px;'>\n";
	t += "<tr><td><iframe class='innerpopup' id='"+((new Date()).getTime())+"' width='"+w+"' height='"+h+"' src='"+url+"'></iframe></td></tr>";
	t += "</table>";
	show(clickedobj,t);
}

function makeFramePopupRightOfObj(obj,url,w,h){
	var winh = document.documentElement.clientHeight;
	var winw = document.documentElement.clientWidth;
	//var scrolltop = document.documentElement.scrollTop;
	var objpos = getPos(obj);
	//alert(objpos[0]+' '+objpos[1]);
	clickedobj = obj;
	var t = "<table class='innerpopup' style='position:absolute;left:"+(objpos[0]+20)+"px;top:"+(objpos[1]-(h/2))+"px;'>\n";
	t += "<tr><td><iframe class='innerpopup' id='"+((new Date()).getTime())+"' width='"+w+"' height='"+h+"' src='"+url+"'></iframe></td></tr>";
	t += "</table>";
	show(clickedobj,t);
}


function hideNR(obj) { //NO RELOAD
	if(semaph==true){
		var mydivX = document.getElementById('mydiv');
		mydivX.style.display = 'none';
		semaph = false;
	}
}

function setPos(obj){
	//alert(parseInt(obj.offsetLeft)+" "+obj.offsetTop+" "+obj.offsetHeight+" "+obj.offsetWidth);
	var mydivX = document.getElementById('mydiv');
	mydivX.style.left = parseInt(obj.offsetLeft)+'px';
	mydivX.style.top = parseInt(obj.offsetTop)+'px';
}


function makeFrameInset(obj,url,xmargin,ymargin){
	clickedobj = obj;
	var winh = document.documentElement.clientHeight;
	var winw = document.documentElement.clientWidth;
	//alert(xmargin+' '+ymargin);
	if(xmargin<=0){
		xmargin = 0.10*winw;
	}
	if(ymargin<=0){
		ymargin = 0.10*winh;
	}
	//alert(xmargin+' '+ymargin);
	var frh = (winh-(2*ymargin));
	var frw = (winw-(2*xmargin));
	//alert('W: '+winh+' H:'+winw+' FRH:'+frh+' FRW:'+frw);
	var uniqid = (new Date()).getTime();
	var t = "<div style='border:0px;position:fixed;top:"+(ymargin)+"px;left:"+(xmargin)+"px;'>\n";
	t += "<div style='position:absolute;color:red;font-size:30px;top:5px;right:20px;' onclick='hideNR(this)'>&#x2297;</div>";
	t += "<iframe class='innerpopup' style='background-color:lightblue;border:1px solid orange;' id='"+uniqid+"' width='"+frw+"' height='"+frh+"' src='"+url+"'></iframe>";
	t += "</div>";
	show(clickedobj,t);
}

function makeFrameHoriz(obj,url,w,h,color){
	clickedobj = obj;
	var winh = document.documentElement.clientHeight;
	var winw = document.documentElement.clientWidth;
	var objpos = getPos(obj);
	//alert(xmargin+' '+ymargin);
	xmargin = (winw - w)/2;
	if(xmargin<0){
		xmargin = 2;
	}
	ymargin = (winh - h)/2;
	//alert(xmargin+' '+ymargin);
	var frh = h; //(winh-(2*ymargin));
	var frw = w; //(winw-(2*xmargin));
	//alert('W: '+winh+' H:'+winw+' FRH:'+frh+' FRW:'+frw);
	var tp = objpos[1] -(h/2);
	if(tp<0){
		tp = 2;
	}
	var uniqid = (new Date()).getTime();
	//var t = "<div style='padding:2px;margin:2px;border:1px;position:fixed;top:"+(ymargin)+"px;left:"+(xmargin)+"px;'>\n";
	var t = "<div style='padding:2px;margin:2px;border:1px;position:fixed;top:"+tp+"px;left:"+(xmargin)+"px;'>\n";
	t += "<iframe class='innerpopup' style='background-color:lightblue;border:3px solid "+color+";' id='"+((new Date()).getTime())+"' width='"+w+"' height='"+h+"' src='"+url+"'></iframe>";
	t += "</div>";
	show(clickedobj,t);
}

//adapted from makeFrameForum in AjaxFrame.js



function makeFrameFixed(obj,url,w,h,hoffset,color){
	clickedobj = obj;
	var pos = getPos(obj);
        var t = "<table class='innerpopup' style='border:3px solid "+color+";'>\n";
        //BELOW ID IS TO FORCE BROWSERS TO NOT CACHE THE VALUE OF 'src' IN IFRAMES
        t += "<tr><td><iframe id='"+((new Date()).getTime())+"' width='"+w+"' height='"+h+"' src='"+url+"'></iframe></td></tr>";
        t += "</table>";
        show(clickedobj,t);
}

document.write("<iframe id='myframe' src='' frameBorder='0' scrolling='no'></iframe>");
//document.write("<iframe id='myframe' src='javascript:false;' frameBorder='0' scrolling='no'></iframe>");
document.write("<div id='mydiv'></div>");


