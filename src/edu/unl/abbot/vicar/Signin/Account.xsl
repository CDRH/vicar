<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<xsl:apply-templates/>
</html>
</xsl:template>

<xsl:template match="account">
<head>
	<title>Account Information</title>
	<meta name="robots" content="noindex,nofollow" />
	<meta name="pragma" content="no-cache" />
	<link rel="stylesheet" type="text/css" href="Signin.css" />
</head>
<body>
	<div class="banner">Account Information</div>
	<xsl:if test="msg/@code = 0">
		<div class="infomsg"/>
	</xsl:if>
	<xsl:if test="msg/@code &gt; 0">
		<div class="infomsg">
			<xsl:value-of select="msg/text()"/>
		</div>
	</xsl:if>
	<xsl:if test="msg/@code &lt; 0">
		<div class="warnmsg">
			<xsl:value-of select="msg/text()"/>
		</div>
	</xsl:if>

	<xsl:if test="@openid = 'none'">
		<div>You have signed in locally.</div>
		<a href="Password.html">Change Password</a>
	</xsl:if>
	<xsl:if test="@openid = 'google'">
		<div>You have signed in using your Google Account.</div>
	</xsl:if>
	<xsl:if test="@openid = 'yahoo'">
		<div>You have signed in using your Yahoo Account.</div>
	</xsl:if>

	<div>
		<a href="{@mainurl}" class="button">Back to Files</a>
	</div>
</body>
</xsl:template>

</xsl:stylesheet>

