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
	<script type="text/javascript" language="JavaScript" src="AjaxClient.js"></script>
	<script type="text/javascript" language="JavaScript" src="AjaxUpload.js"></script>
	<style type="text/css">
		#progbox {margin:10px;border:1px solid blue;min-width:200px;width:auto !important;min-height:15px;height:auto !important;text-align:center;}
		#msgbox {margin:10px;border:1px solid blue;min-width:200px;width:auto !important;min-height:15px;height:auto !important;}
		progress {background-color:white;width:99%;margin:10px;border:1px solid blue;}
	</style>
</head>
<body onload="Init()" style="margin:10px;">
	<xsl:apply-templates />
</body>
<!--
<body style="margin:10px;">
	<xsl:apply-templates />
</body>
-->
</xsl:template>

<xsl:template match="msg">
	<div style="color:red;font-size:110%;">
		<xsl:value-of select="text()"/>
	</div>
</xsl:template>

<xsl:template match="dirs">
	<div style="font-size:130%;margin:10px 5px;">
		<span>Collections</span>
	</div>
	<xsl:if test="@count &lt; @maxcount">
		<a style="margin:0px 10px;color:blue;" href="FileManager.html?dir=new">Create New Collection</a>
	</xsl:if>
	<xsl:if test="@count &gt;= @maxcount">
		<span>You have reached the maximum number of directories.  Please remove an old directory to create a new one.</span>
	</xsl:if>
	<xsl:apply-templates />
</xsl:template>

<xsl:template match="dir">
	<xsl:if test="@name != 'null'">
		<div>
			<a style="margin:0px 10px;color:blue;" href="FileManager.html?dir={@name}">
				<xsl:value-of select="@name"/>
				<xsl:text> (</xsl:text>
				<xsl:value-of select="@count"/>
				<xsl:text>)</xsl:text>
			</a>
		</div>
	</xsl:if>
	<xsl:apply-templates select="inputfiles" />
	<xsl:apply-templates select="outputfiles" />
</xsl:template>


<xsl:template match="inputfiles">
	<div style="font-size:130%;margin:10px 5px;">
		<a style="color:blue;text-decoration:none;" href="FileManager.html">
			<span>Collections &gt; </span>
		</a>
		<span>
			<xsl:value-of select="@dirname"/>
		</span>
		<xsl:if test="@new=0">
			<a style="border:1px solid red;margin:0px 10px;color:red;text-decoration:none;font-size:80%;" href="FileManager.html?dir={@dirname}&amp;act=del" title="Delete this collection">X</a>
		</xsl:if>
	</div>
	<xsl:if test="@new=1">
		<form style="padding:10px;margin:10px;" action="FileManager.html?dir={@dirname}" method="post" enctype="multipart/form-data">
			<div>
				<span>Enter a new name for this collection: </span>
				<input type="text" name="ren" />
				<input type="submit" name="perform" value="Save" />
				<input type="submit" name="perform" value="Cancel" />
			</div>
		</form>
	</xsl:if>
	<xsl:if test="@new=0">
		<noscript>
			<div style="color:red;margin:10px 0px;">If javascript were enabled you would be able to upload multiple files at once by selecting them or by dragging and dropping them into the drop box.</div>
		</noscript>
		<div id="progbox">
		</div>
		<div id="msgbox">
			<b>Drag and drop files here</b>
			<noscript>
				<b style="color:red;"> once you enable javascript!</b>
			</noscript>
		</div>
		<form style="padding:10px;margin:10px;" action="FileManager.html?dir={@dirname}" method="post" enctype="multipart/form-data">
			<div>
				<span>Add files to this collection</span>
				<input type="hidden" id="dirname" name="dirname" value="{@dirname}" />
				<input type="file" id="file_upload" name="file_upload" multiple="multiple" />
				<noscript>
					<input type="submit" id="perform" name="perform" value="Upload" />
				</noscript>
			</div>
		</form>
	</xsl:if>
	<div>
		<xsl:if test="@count = 0">
			<span>This directory has no files.</span>
		</xsl:if>
		<xsl:if test="@count = 1">
			<span>This directory has one file.</span>
		</xsl:if>
		<xsl:if test="@count &gt; 1">
			<span>This directory has </span>
			<xsl:value-of select = "@count" />
			<span> files.</span>
		</xsl:if>
	</div>
	<xsl:apply-templates />

	<div style="margin:1em;padding:1em;">
		<xsl:if test="@count &gt; 0">
		<xsl:if test="@new=0">
			<a style="border:0px;margin:0px 10px;color:green;text-decoration:none;font-size:120%;" href="FileManager.html?dir={@dirname}&amp;act=conv" title="Convert the files in this collection">&gt;&gt;&gt;</a>
		</xsl:if>
		</xsl:if>
	</div>
</xsl:template>

<xsl:template match="outputfiles">
	<xsl:if test="count(file) &gt; 0">
		<div>Output</div>
		<div>
			<a href="FileManager.html?dir={@dirname}&amp;act=zip">Package these into a zip file</a>
		</div>
		<div>
			<a href="FileManager.html?dir={@dirname}&amp;act=targz">Package these into a tar.gz file</a>
		</div>
	</xsl:if>
	<xsl:apply-templates />
</xsl:template>

<xsl:template match="file">
	<div style="margin:5px 20px;">
		<xsl:if test="@op = 0">
			<span style="margin:5px 10px;">
				<xsl:value-of select="@name"/>
			</span>
		</xsl:if>
		<xsl:if test="@op = 1">
			<!--DOWNLOADABLE-->
			<a style="color:blue;text-decoration:none;" href="{@name}?dir={../@dirname}&amp;fn={@name}">
				<xsl:value-of select="@name" />
			</a>
		</xsl:if>
		<xsl:if test="@zip = 0">
			<span style="margin:0px 10px;color:red;">xml?</span>
		</xsl:if>
		<xsl:if test="@zip = 2">
			<a style="color:blue;text-decoration:none;margin:5px 10px;" href="FileManager.html?act=unzip&amp;dir={../@dirname}&amp;fn={@name}">Unzip this file</a>
		</xsl:if>
		<xsl:if test="@zip = 3">
			<a style="color:blue;text-decoration:none;margin:5px 10px;" href="FileManager.html?act=untar&amp;dir={../@dirname}&amp;fn={@name}">Untar this file</a>
		</xsl:if>
		<xsl:if test="@zip = 4">
			<a style="color:blue;text-decoration:none;margin:5px 10px;" href="FileManager.html?act=untargz&amp;dir={../@dirname}&amp;fn={@name}">Ungzip and Untar this file</a>
		</xsl:if>
	</div>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

