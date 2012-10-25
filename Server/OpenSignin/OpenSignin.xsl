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

<xsl:template match="openid">
<head>
	<title>Open Signin</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
	<xsl:if test="url/text() !='null'">
		<meta http-equiv="REFRESH" content="{url/@delay};url={url/text()}" />
	</xsl:if>
	<link rel="stylesheet" type="text/css" href="OpenSignin.css"></link>
</head>
<body>
	<xsl:if test="msg/text()!='null'">
		<div style="color:red;font-size:120%;">
			<xsl:value-of select="msg/text()" />
		</div>
		<div style="color:red;">
			<a href="{url/text()}">Continue</a>
		</div>
	</xsl:if>
	<xsl:if test="url/text()='null'">
		<xsl:if test="@loginstatus &lt; 1">
			<xsl:if test="@loginstatus &lt; 0">
				<div style="color:red;">You have logged out of this site, but may also need to log out of your provider account (Google or Yahoo.).</div>
			</xsl:if>
			<div style="font-size:120%;margin:0.5em 0em;">Please sign in using a Google or Yahoo account.</div>
			<div>
				<span style="margin:0em 0.25em">
					<a href="OpenSignin.html?op=Google">Google Login</a>
				</span>
				<span style="margin:0em 0.25em">
					<a href="OpenSignin.html?op=Yahoo">Yahoo Login</a>
				</span>
			</div>
		</xsl:if>
		<xsl:if test="@loginstatus &gt; 0">
			<div>
				<a href="OpenSignin.html?op=logout">Logout</a>
			</div>
		</xsl:if>
	</xsl:if>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

