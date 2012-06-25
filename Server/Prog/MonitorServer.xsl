<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="Monitor">
<head>
	<title>Monitor Server</title>
	<meta http-equiv="Refresh" content="0;URL=MonitorServer.html" />
	<link rel="stylesheet" href="Monitor.css" />
	<script src="Monitor.js"></script>

	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/redmond/jquery-ui.css" />
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js"></script>
	<style>
		#progressbar.ui-progressbar { height:10px;}
	</style>
	<script>
		$(function(){
			$("#progressbar").progressbar({value:
				<xsl:value-of select="@value"/>
			});
		});
	</script>
</head>
<body>
	<xsl:if test="@dispose=1">
		<xsl:attribute name="onload">
			<xsl:text>parent.hideForum(this,'Monitor.html');</xsl:text>
		</xsl:attribute>
	</xsl:if>

<!--
	<div style="width:100%;">
		<progress style="width:100%;" max="100" value="{@value}"></progress>
	</div>
-->
	<div id="progressbar"></div>
	<div id="progressmsg" style="text-align:center;">
		<xsl:value-of select="msg/text()"/>
	</div>
</body>
</xsl:template>

</xsl:stylesheet>

