//Add show more link
$(document).ready(function(){
	$(".abbotresults > .paddingdiv").append("\<a href=\"#\" id=\"more_button\">Show More\</a>")
});


//Hide all but first three xml file results initially
$(document).ready(function(){
	$(".abbotresults .result:gt(2)").hide()
});

//Toggle showing all abbot results, and change text of link
$(document).ready(function(){
	$( ".abbotresults .paddingdiv a#more_button" ).toggle(function() {
		$(this).html('Show Fewer');
		$(".abbotresults .result").show()
	}, function() {
		$(this).html('Show More');
		$(".abbotresults .result:gt(2)").hide()
	});
});



 $(function() {
$( ".accordion" ).accordion();
});
