<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />
<!--
<xsl:output method="xml"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />
-->

<xsl:template match="/">
<html lang="en">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="test">
<head>
	<title>Upload Test</title>
	<link rel="stylesheet" type="text/css" href="PopupFrame.css" />
	<script type="text/javascript" language="JavaScript" src="PopupFrame.js">&#160;</script>
</head>

<body>
	<div style="padding:1em;margin:1em;">
		<a style="text-decoration:none;color:blue;border:1px solid blue;margin:3px;padding:3px;border-radius:3px;-moz-border-radius:3px;-webkit-border-radius:3px;-khtml-border-radius:3px;font-family:arial;" href="UploadFiles.html?dir={@dirname}" onclick="makeUploadFrame(this,'UploadFiles.html?dir={@dirname}');return false;">Upload Files</a>
		<a style="color:blue;" href="UploadFiles.html">Upload Direct Link</a>
	</div>
	<div style="padding:1em;margin:1em;">
		<a style="text-decoration:none;color:blue;border:1px solid blue;margin:3px;padding:3px;border-radius:3px;-moz-border-radius:3px;-webkit-border-radius:3px;-khtml-border-radius:3px;font-family:arial;" href="../Progress/StreamClient.html?act=test" onclick="makeUploadFrame(this,'../Progress/StreamClient.html?act=test');return false;">Process Files</a>
		<a style="color:blue;" href="../Progress/StreamClient.html">Progress Direct Link</a>
	</div>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>


