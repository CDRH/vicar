function selectedOption(sel){
	var p = sel.options[sel.selectedIndex].value;
	//alert(p);
	if(p==null){
	}else if(p.charAt(0)=='0'){
		sel.style.color="green";
	}else if(p.charAt(0)=='1'){
		sel.style.color="blue";
	}else if(p.charAt(0)=='2'){
		sel.style.color="black";
	}
}

