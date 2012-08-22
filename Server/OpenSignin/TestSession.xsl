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

<xsl:template match="test">
<head>
	<title>Simple structure for testing components</title>
</head>
<body>
	<h2>
		<xsl:text>IP:</xsl:text>
		<xsl:value-of select="@IP" />
	</h2>
	<h2>
		<xsl:text>SESSION:</xsl:text>
		<xsl:value-of select="@SessionID" />
	</h2>
<!--
	<a href="TestSession.html">Again</a>
-->
	<div>
		<a href="http://abbot.unl.edu/cocoon/vicar/OpenSignin/TestSession.html">abbot.unl.edu/cocoon/vicar/OpenSignin/TestSession.html</a>
	</div>
	<div>
		<a href="http://abbot.unl.edu/vicar/OpenSignin/TestSession.html">abbot.unl.edu/vicar/OpenSignin/TestSession.html</a>
	</div>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>


