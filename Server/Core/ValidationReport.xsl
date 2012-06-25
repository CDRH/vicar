<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="validationreport">
<head>
	<title>Validation Report</title>
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
<!--
	<link rel="stylesheet" type="text/css" href="Simple.css"></link>
	<link rel="stylesheet" type="text/css" href="Menu.css"></link>
-->
</head>
<body>
	<xsl:apply-templates />
</body>
</xsl:template>

<xsl:template match="msg">
<div>
	<xsl:value-of select="text()" />
</div>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

