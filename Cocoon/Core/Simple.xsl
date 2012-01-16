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
	<xsl:if test="@mode &lt; 1">
		<!--NOT LOGGED IN-->
		<span style="margin:0.75em;font-size:120%;position:relative;float:right;">
			<span style="color:green;">Sign in using a:</span>
			<a style="color:blue;margin:0 0.5em;" href="../OpenSignin/OpenSignin.html?op=Google">Google ID</a>
			<span style="color:green;"> or </span>
			<a style="color:blue;margin:0 0.5em;" href="../OpenSignin/OpenSignin.html?op=Yahoo">Yahoo! ID</a>
		</span>
		<span style="font-size:140%;margin:0.5em;position:fixed;top:30px;">
			<xsl:if test="@mode &lt; 0">
				<div style="color:red;">You have successfuly logged out of this site, but still likely need to log out of your identity provider (Google or Yahoo) account.</div>
			</xsl:if>
			<div>Welcome to a simple framework for testing components!</div>
			<div>Other features and login options will follow.</div>
		</span>
	</xsl:if>

	<xsl:if test="@mode &gt; 0">
		<!--LOGGED IN-->
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
				<a class="menu_on" href="Simple.html?mode=1">Intro</a>
				<a class="menu_off" href="Simple.html?mode=2">Upload Files via HTML5</a>
				<a class="menu_off" href="Simple.html?mode=3">Specify Conversions</a>
				<a class="menu_off" href="Simple.html?mode=4">Process Files</a>
				<a class="menu_off" href="Simple.html?mode=5">Download Files</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Introduction.</span>
			</div>
		</xsl:if>

		<xsl:if test="@mode = 2">
			<div style="padding:1.0em 0em;">
				<a class="menu_off" href="Simple.html?mode=1">Intro</a>
				<a class="menu_on" href="Simple.html?mode=2">Upload Files via HTML5</a>
				<a class="menu_off" href="Simple.html?mode=3">Specify Conversions</a>
				<a class="menu_off" href="Simple.html?mode=4">Process Files</a>
				<a class="menu_off" href="Simple.html?mode=5">Download Files</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Upload multiple files using HTML5.</span>
<!--
				<iframe src="http://abbot.unl.edu:8080/Html5Upload/upload.html" width="95%" height="500px" />
-->
				<iframe src="http://127.0.0.1:8080/Html5Upload/upload.html?usersession={@SessionID}" width="95%" height="500px" />
			</div>
		</xsl:if>

		<xsl:if test="@mode = 3">
			<div style="padding:1.0em 0em;">
				<a class="menu_off" href="Simple.html?mode=1">Intro</a>
				<a class="menu_off" href="Simple.html?mode=2">Upload Files via HTML5</a>
				<a class="menu_on" href="Simple.html?mode=3">Specify Conversions</a>
				<a class="menu_off" href="Simple.html?mode=4">Process Files</a>
				<a class="menu_off" href="Simple.html?mode=5">Download Files</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Specify Conversions.</span>
			</div>
		</xsl:if>

		<xsl:if test="@mode = 4">
			<div style="padding:1.0em 0em;">
				<a class="menu_off" href="Simple.html?mode=1">Intro</a>
				<a class="menu_off" href="Simple.html?mode=2">Upload Files via HTML5</a>
				<a class="menu_off" href="Simple.html?mode=3">Specify Conversions</a>
				<a class="menu_on" href="Simple.html?mode=4">Process Files</a>
				<a class="menu_off" href="Simple.html?mode=5">Download Files</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Process your files.</span>
			</div>
		</xsl:if>

		<xsl:if test="@mode = 5">
			<div style="padding:1.0em 0em;">
				<a class="menu_off" href="Simple.html?mode=1">Intro</a>
				<a class="menu_off" href="Simple.html?mode=2">Upload Files via HTML5</a>
				<a class="menu_off" href="Simple.html?mode=3">Specify Conversions</a>
				<a class="menu_off" href="Simple.html?mode=4">Process Files</a>
				<a class="menu_on" href="Simple.html?mode=5">Download Files</a>
			</div>
			<div style="padding:1.0em 0em;">
				<span style="font-size:12pt;">Download your processed files.</span>
			</div>
		</xsl:if>
	</xsl:if>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

