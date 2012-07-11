<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html lang="en" class="no-js">
	<xsl:apply-templates />
</html>
</xsl:template>


<xsl:template match="filemanager">
<head>
	<title>Vicar - gateway to Abbot</title>
	<meta name="description" content="Collection of files to be processed by Abbot" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="robots" content="noindex,nofollow" />
	<!--unicode 160 gets past some odd bug that prevents the html5 serializer from working with these script lines-->
	<script type="text/javascript" language="JavaScript" src="../Modernizr/modernizr.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="../Progress/Monitor.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="../Upload/AjaxUpload.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="SchemaList.js">&#160;</script>
	<link rel="stylesheet" type="text/css" href="FileManager.css" />
	<link rel="stylesheet" type="text/css" href="../Progress/Monitor.css" />
	<link rel="stylesheet" type="text/css" href="../Upload/AjaxUpload.css" />
<!--
	<script type="text/javascript">
		if("files" in DataTransfer.prototype){
			alert('files');
		}else{
			alert('no files');
		}
	</script>
-->
</head>

<body id="bodyid" onload="UploadInit()">
<!--BANNER-->
	<div style="padding:0.5em;margin:0.5em;color:blue;background:yellow;">
		<a style="position:relative;float:right;color:blue;" href="../OpenSignin/OpenSignin.html?op=logout">Sign out</a>
		<span>Welcome</span>
		<span style="font-size:100%;color:blue;">
			<xsl:value-of select="@personname" />
			<xsl:text> (</xsl:text>
			<xsl:value-of select="@personemail" />
			<xsl:text>)</xsl:text>
		</span>
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

<!--DIRECTORY-->
<xsl:template match="dirs">
	<table style="margin:10px 5px;padding:0px;">
		<tr>
			<td style="margin:0px;padding:0px;">
				<span style="font-size:130%;">Collections </span>
			</td>
			<xsl:if test="@count &lt; @maxcount">
				<td>
					<a id="collection_create" style="margin:0em 1em;" href="FileManager.html?dir=new" title="Add New Collection">+</a>
				</td>
			</xsl:if>
			<xsl:if test="@count &gt;= @maxcount">
				<td>
					<span style="font-size:130%;color:red;margin:0em 1em;padding:0em;">You have reached the maximum number of directories.  Please remove an old directory to create a new one.</span>
				</td>
			</xsl:if>
		</tr>
		<xsl:apply-templates select="dir" />
	</table>
</xsl:template>

<xsl:template match="dir">
	<xsl:if test="@name != 'null'">
		<tr>
			<td></td>
			<td>
				<a style="font-size:130%;color:blue;text-decoration:none;margin:0em 1em;padding:0em;" href="FileManager.html?dir={@name}" title="Created {@date}">
					<xsl:value-of select="@name"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="@count"/>
					<xsl:text>)</xsl:text>
				</a>
			</td>
		</tr>
	</xsl:if>
</xsl:template>

<xsl:template match="msg">
	<xsl:if test="@msgcode &gt; 0">
		<div id="errmsg">
			<xsl:value-of select="text()"/>
		</div>
	</xsl:if>
</xsl:template>


<xsl:template match="collection">
	<table style="margin:10px 5px;padding:0px;">
		<tr>
			<td style="margin:0px;padding:0px;">
				<a style="margin:0px;padding:0px;color:blue;text-decoration:none;" href="FileManager.html">
					<span style="font-size:130%;">Collections </span>
				</a>
			</td>
			<td style="margin:0em 1em;padding:0px;">
				<span style="color:black;margin:0em 0.25em;font-size:130%;">&gt;</span>
				<span style="color:black;font-size:130%;margin:0px;padding:0px;">
					<xsl:value-of select="@dirname"/>
				</span>
				<xsl:if test="@new=0">
					<xsl:if test="../msg/@msgcode = 0">
						<a id="collection_delete" href="FileManager.html?dir={@dirname}&amp;act=del" title="Delete this collection">x</a>
					</xsl:if>
					<xsl:if test="../msg/@msgcode &lt; 0">
						<span style="color:red;font-size:130%;">Delete this Collection? </span>
						<a id="collection_delete" href="FileManager.html?dir={@dirname}&amp;act=delconfirm" title="Delete this collection">Yes</a>
						<a id="collection_delete" href="FileManager.html?dir={@dirname}&amp;act=delcancel" title="Keep this collection">Cancel</a>
					</xsl:if>
				</xsl:if>
			</td>
		</tr>
	</table>

	<xsl:if test="@new=1">
		<form style="padding:10px;margin:10px;" action="FileManager.html?dir={@dirname}" method="post" accept-charset="utf-8" enctype="multipart/form-data">
			<div>
				<span>Enter a new name for this collection: </span>
				<input type="text" name="ren" autocomplete="off" />
				<input type="submit" name="perform" value="Save" />
				<input type="submit" name="perform" value="Cancel" />
			</div>
		</form>
	</xsl:if>

	<xsl:if test="@new=0">
		<div id="nojsmsg">
			<div style="color:red;margin:10px 0px;">If javascript were enabled you would be able to upload multiple files at once by selecting them or by dragging and dropping them into the drop box.</div>
		</div>

		<div style="margin:0.5em 0em 1em 0em;padding:0em;">
			<div id="upload_msgbox">
				<span>Drag and drop input and conversion (.rng) files here</span>
			</div>
			<div id="upload_progressbox"></div>
			<form id="upload_form" style="padding:0.5em;margin:0.5em;" action="FileManager.html?dir={@dirname}" method="post" enctype="multipart/form-data">
				<span>Add files </span>
				<input type="hidden" id="dirname" name="dirname" value="{@dirname}" />
				<input type="file" id="file_upload" name="file_upload" multiple="multiple" title="Navigate or drag/drop files here."></input>
				<input type="submit" id="perform" name="perform" value="Upload" />
			</form>
		</div>

	</xsl:if>

	<hr style="color:blue;width:99%;height:2px;"/>
	<xsl:if test="@new=0">
		<xsl:apply-templates select="inputfiles" />
	</xsl:if>
	<xsl:if test="( inputfiles/@count &gt; 0 ) and ( @new=0 )">
		<xsl:apply-templates select="schemalist" />
	</xsl:if>

	<xsl:if test="outputfiles/@count &gt; 0">
		<xsl:apply-templates select="outputfiles" />
	</xsl:if>
</xsl:template>


<xsl:template match="inputfiles">
	<div class="outercolumn">
		<div class="columnheader">
			<span class="columntitle">Input Files</span>
		</div>
		<xsl:apply-templates select="file"/>
	</div>
</xsl:template>

<xsl:template match="schemalist">
	<div class="innercolumn">
		<div class="columnheader">
			<span class="columntitle">Convert With:</span>
		</div>
		<form action="FileManager.html?dir={../@dirname}&amp;act=conv" method="post" enctype="multipart/form-data">
			<div style="margin:5px 20px;">
				<select name="conv" style="color:blue;">
					<xsl:apply-templates />
				</select>
			</div>
			<input type="submit" style="color:blue;font-weight:bold;" name="perform" value="&gt;&gt;&gt;" title="Generate output files using Abbot" />
		</form>
	</div>
</xsl:template>

<xsl:template match="schema">
	<option value="{@type}{@name}" title="{text()}">
		<xsl:if test="@current = 1">
			<xsl:attribute name="selected">
				<xsl:text>selected</xsl:text>
			</xsl:attribute>
		</xsl:if>
		<xsl:if test="@type &lt;= 1">
			<xsl:attribute name="style">
				<xsl:text>color:blue;</xsl:text>
			</xsl:attribute>
		</xsl:if>
		<xsl:if test="@type &gt;= 2">
			<xsl:attribute name="style">
				<xsl:text>color:black;</xsl:text>
			</xsl:attribute>
		</xsl:if>
		<xsl:value-of select="@name" />
	</option>
</xsl:template>

<xsl:template match="outputfiles">
	<div class="outercolumn">
		<div class="columnheader">
			<span class="columntitle">Abbot Generated Results</span>
		</div>
		<xsl:apply-templates select="file"/>
		<div style="padding:0em 0em 0em 0.3em">
			<a href="../Download/{@dirname}/{@dirname}.zip">Download all in a single zip</a>
		</div>
		<div style="padding:0em 0em 0em 0.3em">
			<a href="../Download/{@dirname}/{@dirname}.tar.gz">Download all in a single tar.gz</a>
		</div>
	</div>
</xsl:template>

<xsl:template match="file">
	<div style="margin:5px 20px;">
		<xsl:if test="@op = 0">
			<span>
				<xsl:value-of select="@name"/>
			</span>
		</xsl:if>
		<xsl:if test="@op = 1">
			<a style="color:blue;text-decoration:none;" href="../Download/{../@dirname}/{@name}">
				<xsl:value-of select="@name" />
			</a>
			<xsl:if test="@errors &lt;= 0">
				<a target="_validation" class="error_report_green" href="../valid/{../@dirname}/{@vname}">
					<xsl:text>OK</xsl:text>
				</a>
			</xsl:if>
			<xsl:if test="@errors &gt; 0">
				<a target="_validation" class="error_report_red" href="../valid/{../@dirname}/{@vname}">
					<xsl:value-of select="@errors" />
				</a>
			</xsl:if>
		</xsl:if>
		<xsl:if test="@zip = 0">
			<span style="margin:0px 10px;color:red;">xml?</span>
		</xsl:if>
		<xsl:if test="@zip = 2">
			<a style="color:blue;text-decoration:none;" href="FileManager.html?act=unzip&amp;dir={../@dirname}&amp;fn={@name}"> Unzip this file</a>
		</xsl:if>
		<xsl:if test="@zip = 3">
			<a style="color:blue;text-decoration:none;" href="FileManager.html?act=untar&amp;dir={../@dirname}&amp;fn={@name}"> Untar this file</a>
		</xsl:if>
		<xsl:if test="@zip = 4">
			<a style="color:blue;text-decoration:none;" href="FileManager.html?act=untargz&amp;dir={../@dirname}&amp;fn={@name}"> Ungzip and Untar this file</a>
		</xsl:if>
	</div>
</xsl:template>


<!--SIGNIN-->
<xsl:template match="signin">
<head>
	<title>Simple structure for testing components</title>
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
	<link rel="stylesheet" type="text/css" href="FileManager.css" />
</head>
<body>
	<div align="right" style="border:0px;margin:8px;padding:0px 25px 0px 0px;">
		<div style="padding:25px 0px 25px 0px;">
			<a name="signin" href="../OpenSignin/OpenSignin.html?op=Yahoo" style="color:blue;text-decoration:none;margin:0px;padding:0px;">
				<img src="../OpenSignin/YahooOpenID_13.png" height="22px;" alt="Sign In With Yahoo" />
			</a>
		</div>
		<div style="padding:0px 0px 8px 0px;">
			<a name="signin" href="../OpenSignin/OpenSignin.html?op=Google" valign="bottom" style="font-size:72%;font-weight:bold;background:lightgrey;color:#222;border:1px solid #7D7D7D;text-decoration:none;margin:0px;padding:3px 1px 4px 2px;">
				<img src="../OpenSignin/GoogleGImage.png" height="17px;" style="margin:0px;padding:0px;vertical-align:middle;" />
				<span style="margin:4px;padding:0px;">Sign In through Google</span>
			</a>
		</div>
		<div style="padding:0px 0px 8px 0px;margin:2em 0em;">
			<a name="signin" href="../OpenSignin/OpenSignin.html?op=Test" style="outline:none;color:blue;text-decoration:none;">
				<span style="margin:4px;padding:0px;">Anonymous Sign In</span>
			</a>
		</div>
	</div>
	<div style="font-size:140%;margin:0.5em;">
		<xsl:if test="@mode &lt; 0">
			<div style="color:red;">You have successfuly logged out of this site but this does not log you out of your identity provider (Google or Yahoo) account.</div>
		</xsl:if>
		<div style="color:blue;font-size:120%;font-weight:bold;">Vicar - gateway to Abbot</div>
		<div>Other features and login options will follow.</div>
	</div>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>


