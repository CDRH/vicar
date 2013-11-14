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

function setPos(obj){
	var mydivX = document.getElementById('mydiv');
	mydivX.style.left = parseInt(obj.offsetLeft)+'px';
	mydivX.style.top = parseInt(obj.offsetTop)+'px';
}

function makeUploadFrame(obj,url){
	clickedobj = obj;
	var pos = getPos(obj);
	var srcurl = url;
        var t = "<div class='innerpopup' style='width:800px;border:3px solid red;'>\n";
        t += "<iframe id='"+((new Date()).getTime())+"' width='99%' height='300px' style='border:0px;' src='"+srcurl+"'></iframe>";
        t += "</div>";
        show(clickedobj,t);
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

//function hideForum(obj,url) {
//	if(semaph==true){
//		var mydivX = document.getElementById('mydiv');
//		mydivX.style.display = 'none';
//		semaph = false;
//	}
//	location.replace(url);
//}

//function hide(obj) {
//	if(semaph==true){
//		var mydivX = document.getElementById('mydiv');
//		mydivX.style.display = 'none';
//		semaph = false;
//	}
//	location.reload();
//}

//function hideNR(obj) { //NO RELOAD
//	if(semaph==true){
//		var mydivX = document.getElementById('mydiv');
//		mydivX.style.display = 'none';
//		semaph = false;
//	}
//}


document.write("<iframe id='myframe' src='' frameBorder='0' scrolling='no'></iframe>");
document.write("<div id='mydiv'></div>");


