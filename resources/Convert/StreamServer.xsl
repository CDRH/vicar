<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<!--USED ONLY BY Vicar.html FOR WHEN JAVASCRIPT IS DISABLED, OTHERWISE Vicar.html WILL CALL StreamClient.html WHICH WILL CALL StreamServer.xml-->

<xsl:template match="/">
<html lang="en">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="datastream">
<head>
	<title>Abbot Conversion</title>
	<meta charset="UTF-8" />
	<!--FIRST CALL RETURNS MODE 0 WHICH RESULTS IN A CALL WITH act=join WHICH BLOCKS AND WAITS FOR AbbotConvert TO COMPLETE-->
	<xsl:if test="@mode = 0">
		<meta http-equiv="refresh" content="1;url=StreamServer.html?dir={@dirname}&amp;act=join" />
	</xsl:if>
	<!--RETURN FROM act=join CALL RESULTS IN A RETURN TO Vicar.html-->
	<xsl:if test="@mode = 1">
		<meta http-equiv="refresh" content="1;url=../Vicar.html?dir={@dirname}" />
	</xsl:if>
</head>
<body>
	<xsl:if test="@mode = 0">
		<div style="font-size:120%;color:green;">Abbot conversion in progress.</div>
	</xsl:if>
	<xsl:if test="@mode = 1">
		<div style="font-size:120%;color:green;">Abbot conversion completed.</div>
	</xsl:if>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

