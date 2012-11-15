<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html lang="en" class="no-js">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="signin">
<head>
	<title>Signin</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="robots" content="noindex,nofollow" />
	<xsl:if test="url/text() !='null'">
		<meta http-equiv="REFRESH" content="{url/@delay};url={url/text()}" />
	</xsl:if>
</head>
</xsl:template>

<xsl:template match="sample">
<head>
	<title>Vicar - gateway to Abbot</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="description" content="Collection of files to be processed by Abbot" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="robots" content="noindex,nofollow" />
	<!--unicode 160 gets past some odd bug that prevents the html5 serializer from working with these script lines-->
	<script type="text/javascript" language="JavaScript" src="../Modernizr/modernizr.js">&#160;</script>
<!--
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">&#160;</script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js">&#260;</script>

	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/redmond/jquery-ui.css" />
-->
	<link rel="stylesheet" type="text/css" href="Sample.css" />
</head>

<body id="bodyid" onload="UploadInit()">
<!--BANNER-->
<!--
<h2>
	<span>
	<xsl:value-of select="@personname" />
	</span>
</h2>
<h2>
	<span>
	<xsl:value-of select="@userid" />
	</span>
</h2>
-->
	
	<div style="padding:0.5em;margin:0.5em;color:blue;background:yellow;">
		<a href="../Signin/Account.html">
			<span>Welcome</span>
			<span style="font-size:100%;color:blue;">
				<xsl:choose>
					<xsl:when test="@personname != ''">
						<xsl:value-of select="@personname" />
						<xsl:if test="@userid!= ''">
							<xsl:text> (</xsl:text>
							<xsl:value-of select="@userid" />
							<xsl:text>)</xsl:text>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@userid" />
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</a>
		<a style="position:relative;float:right;color:blue;" href="../Signin/Signout.html">Sign out</a>
	</div>

	<xsl:apply-templates />

<!--FOOTER-->
	<div style="margin:0.6em;font-size:100%;font-weight:bold;color:orange;position:fixed;bottom:20px;right:20px;">
		<div id="smartphonesize">smart phone sized screen</div>
		<div id="tabletsize">tablet sized screen</div>
		<div id="computersize">computer sized screen</div>
	</div>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>


