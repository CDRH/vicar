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

<xsl:template match="simple">
<head>
	<title>Simple structure for testing components</title>
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
	<link rel="stylesheet" type="text/css" href="Simple.css"></link>
</head>
<body>
<!--NOT LOGGED IN-->
	<xsl:if test="@mode &lt; 1">
		<div align="right" style="border:0px;margin:8px;padding:0px 25px 0px 0px;">
			<div style="padding:25px 0px 25px 0px;">
				<a name="signin" href="../OpenSignin/OpenSignin.html?op=Yahoo" style="color:blue;text-decoration:none;margin:0px;padding:0px;">
					<img src="../OpenSignin/YahooOpenID_13.png" alt="Sign In With Yahoo" />
				</a>
			</div>
			<div style="padding:0px 0px 8px 0px;">
				<a name="signin" href="../OpenSignin/OpenSignin.html?op=Google" valign="bottom" style="font-size:82%;font-weight:bold;background:lightgrey;color:#222;border:1px solid #7D7D7D;text-decoration:none;margin:0px;padding:3px 1px 4px 2px;">
					<img src="../OpenSignin/GoogleGImage.png" height="20px;" style="margin:0px;padding:0px;vertical-align:middle;" />
					<span style="margin:4px;padding:0px;">Sign In through Google</span>
				</a>
			</div>
		</div>
<!--
		<span style="font-size:140%;margin:0.5em;position:fixed;top:30px;">
			<xsl:if test="@mode &lt; 0">
				<div style="color:red;">You have successfuly logged out of this site, but still likely need to log out of your identity provider (Google or Yahoo) account.</div>
			</xsl:if>
			<div>Welcome to a simple framework for testing components!</div>
			<div>Other features and login options will follow.</div>
		</span>
-->
		<div style="font-size:140%;margin:0.5em;">
			<xsl:if test="@mode &lt; 0">
				<div style="color:red;">You have successfuly logged out of this site, but still likely need to log out of your identity provider (Google or Yahoo) account.</div>
			</xsl:if>
			<div>Welcome to a simple framework for testing components!</div>
			<div>Other features and login options will follow.</div>
		</div>
	</xsl:if>

<!--LOGGED IN-->
	<xsl:if test="@mode &gt; 0">
		<div style="font-size:90%;color:black;border:1px solid red;padding:0.5em; margin:0.5em;" >
			<span>This is not a system design but merely a structure on which I can test and demo various components.</span>
		</div>
		<div style="padding:0.5em;margin:0.5em;color:black;background:yellow;">
			<span>Welcome </span>
			<span style="color:navy;">
				<xsl:value-of select="@personname" />
				<xsl:text> (</xsl:text>
				<xsl:value-of select="@personemail" />
				<xsl:text>)</xsl:text>
			</span>
			<a style="position:relative;float:right;color:blue;" href="../OpenSignin/OpenSignin.html?op=logout"> Sign out</a>
		</div>

		<xsl:if test="@mode = 1">
			<div style="padding:1.0em 0em;">
				<a class="menu_on" href="Simple.html?mode=1">Welcome</a>
				<a class="menu_off" href="Simple.html?mode=2">Use Abbot</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Welcome to Vicar! - gateway to Abbot.</span>
			</div>
		</xsl:if>

		<xsl:if test="@mode = 2">
			<div style="padding:1.0em 0em;">
				<a class="menu_off" href="Simple.html?mode=1">Welcome</a>
				<a class="menu_on" href="Simple.html?mode=2">Use Abbot</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Use Abbot</span>

				<iframe src="FileManager.html" width="95%" height="500px" />
			</div>
		</xsl:if>
	</xsl:if>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>


