<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />
<!--
<xsl:output method="xml"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />
-->

<xsl:param name="dir" />
<xsl:param name="conv" />
<xsl:param name="abbotns" />
<xsl:param name="abbotcustom" />

<xsl:template match="/">
<html lang="en">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="streamclient">
<head>

	<meta charset="UTF-8" />
	<script type="text/javascript" src="../Utils/AjaxClient.js">&#160;</script>
	<script type="text/javascript" src="StreamClient.js">&#160;</script>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">&#160;</script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js">&#160;</script>

	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/redmond/jquery-ui.css" />

	<link rel="stylesheet" type="text/css" href="StreamClient.css" />

        <link rel="stylesheet" type="text/css" href="../style.css" />
        <link rel="stylesheet" href="../jquery-ui-1.10.0.custom.css" />
    
	<script>
		$(function() {
			$( "#progressbar" ).progressbar({value:0});
		});
		$(function() {
			$( "#progressbar1" ).progressbar({value:0});
		});
	</script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-23044707-2', 'unl.edu');
  ga('send', 'pageview');

</script>
</head>

<body onload="ClientInit('{$dir}','{$conv}','{$abbotns}','{$abbotcustom}')">
	<div style="text-align:center">
		<div id="msgline" class="msgline"></div>
		<div id="progressbar" class="progdiv"></div>
		<div id="msgline1" class="msgline"></div>
		<div id="progressbar1" class="progdiv"></div>
		<input type="submit" class="btn" value="Cancel" id="cancelbutton" onclick="window.parent.location.href='../UploadFiles/Test.html'" />
		<input type="submit" class="btn" value="Done" id="donebutton" onclick="window.parent.location.href='../UploadFiles/Test.html'" />
           
	</div>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

