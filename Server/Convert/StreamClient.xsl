<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />
<!--
<xsl:output method="xml"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />
-->

<xsl:param name="conv" />
<xsl:param name="dir" />

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

	<script>
		$(function() {
			$( "#progressbar" ).progressbar({value:0});
		});
		$(function() {
			$( "#progressbar1" ).progressbar({value:0});
		});
	</script>
</head>

<body onload="ClientInit('{$dir}','{$conv}')">
	<div style="text-align:center">
		<div id="msgline" style="height:20px;"></div>
		<div id="progressbar" style="margin:auto;width:95%;height:15px;background:white;margin:4px 0px;"></div>
		<div id="msgline1" style="height:20px;"></div>
		<div id="progressbar1" style="margin:auto;width:95%;height:15px;background:white;margin:4px 0px;"></div>
		<input type="submit" style="margin:4px 0px 0px 0px;" value="Cancel" id="cancelbutton" onclick="window.parent.location.href='../Core/FileManager.html?dir={$dir}'" />
		<input type="submit" style="margin:4px 0px 0px 0px;" value="Done" id="donebutton" onclick="window.parent.location.href='../Core/FileManager.html?dir={$dir}'" />
           
	</div>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

