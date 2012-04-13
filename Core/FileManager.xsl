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

<xsl:template match="signin">
<head>
	<title>Simple structure for testing components</title>
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
</head>
<body>
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
	<div style="font-size:140%;margin:0.5em;">
		<xsl:if test="@mode &lt; 0">
			<div style="color:red;">You have successfuly logged out of this site, but still likely need to log out of your identity provider (Google or Yahoo) account.</div>
		</xsl:if>
		<div style="color:blue;font-size:120%;font-weight:bold;">Vicar - gateway to Abbot</div>
		<div>Other features and login options will follow.</div>
	</div>
</body>
</xsl:template>

<xsl:template match="filemanager">
<head>
	<title>Simple structure for testing components</title>
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
<!--
	<link rel="stylesheet" type="text/css" href="Simple.css"></link>
-->
	<script type="text/javascript" language="JavaScript" src="AjaxClient.js"></script>
	<script type="text/javascript" language="JavaScript" src="AjaxUpload.js"></script>
	<style type="text/css">
		body { margin:0px;padding:0px;}
		#progbox {margin:10px;border:1px solid blue;min-width:100px;width:auto !important;min-height:15px;height:auto !important;text-align:center;}
		#msgbox {margin:10px;border:1px solid blue;min-width:100px;width:auto !important;min-height:15px;height:auto !important;}
		progress {background-color:white;width:99%;margin:10px;border:1px solid blue;}
		#errmsg {font-size:110%;color:red;}
		#collection_delete {border:1px solid red;margin:0px 10px;color:red;text-decoration:none;font-size:80%;}
		#collection_create {margin:0px 10px;color:blue;border:1px solid blue;background:lightblue;text-decoration:none;}
		#collection_label {font-size:130%;margin:10px 5px;}
		a {color:blue;}

		#addfiles {border:1px solid blue;margin:0px 0px 20px 0px;}

		#inputfiles {margin:0em;padding:0em;border:0px solid blue;float:left;width:35%;}
		#convert {margin:0em;padding:0em 0.5em;border:0px solid orange;float:left;width:22%;}
		#outputfiles {margin:0em;padding:0em;border:0px solid green;float:left;width:35%;}

		@media all and (max-width:320px){
			#smartphonesize{display:block;}
			#tabletsize{display:none;}
			#computersize{display:none;}
			#inputfiles {margin:0em;padding:0em;border:0px solid blue;width:100%;display:block;}
			#convert {margin:0em;padding:1em 0em;border:0px solid orange;width:100%;display:block;}
			#outputfiles {margin:0em;padding:0em;border:0px solid green;width:100%;display:block;}
		}
		@media all and (min-width:321px) and (max-width:768px){
			#smartphonesize{display:none;}
			#tabletsize{display:block;}
			#computersize{display:none;}
			#inputfiles {margin:0em;padding:0em;border:0px solid blue;width:100%;display:block;}
			#convert {margin:0em;padding:1em 0em;border:0px solid orange;width:100%;display:block;}
			#outputfiles {margin:0em;padding:0em;border:0px solid green;width:100%;display:block;}
		}
		@media all and (min-width:769px) {
			#smartphonesize{display:none;}
			#tabletsize{display:none;}
			#computersize{display:block;}
		}
<!--
		#nojsmsg {display:none;}
		no-js #nojsmsg {display:block;}
-->
	</style>
</head>
<body onload="Init()">
	<div style="padding:0.5em;margin:0.5em;color:black;background:yellow;">
		<span>Welcome</span>
		<a style="position:relative;float:right;color:blue;" href="../OpenSignin/OpenSignin.html?op=logout">Sign out</a>
		<div style="font-size:100%;color:navy;">
			<xsl:value-of select="@personname" />
			<xsl:text> (</xsl:text>
			<xsl:value-of select="@personemail" />
			<xsl:text>)</xsl:text>
		</div>
	</div>

	<div style="margin:0.6em;font-size:100%;font-weight:bold;color:orange;">
		<div id="smartphonesize">smart phone sized screen</div>
		<div id="tabletsize">tablet sized screen</div>
		<div id="computersize">computer sized screen</div>
	</div>
	<xsl:apply-templates />
</body>
</xsl:template>

<xsl:template match="msg">
	<div id="errmsg">
		<xsl:value-of select="text()"/>
	</div>
</xsl:template>

<xsl:template match="dirs">
	<div>
		<span id="collection_label">Collections</span>
		<xsl:if test="@count &lt; @maxcount">
			<a id="collection_create" href="FileManager.html?dir=new">Create New Collection</a>
		</xsl:if>
	</div>
	<xsl:if test="@count &gt;= @maxcount">
		<span>You have reached the maximum number of directories.  Please remove an old directory to create a new one.</span>
	</xsl:if>
	<xsl:apply-templates select="dir" />
</xsl:template>

<xsl:template match="dir">
	<xsl:if test="@name != 'null'">
		<div style="margin:0.5em 0.8em;">
			<a style="color:blue;text-decoration:none;" href="FileManager.html?dir={@name}">
				<xsl:value-of select="@name"/>
				<xsl:text> (</xsl:text>
				<xsl:value-of select="@count"/>
				<xsl:text>)</xsl:text>
			</a>
		</div>
	</xsl:if>
</xsl:template>


<xsl:template match="inputfiles">
<div id="addfiles">
	<div style="font-size:130%;margin:10px 5px;">
		<a style="color:blue;text-decoration:none;" href="FileManager.html">
			<span>Collections </span>
		</a>
		<span style="color:black;"> &gt; </span>
		<span style="color:black;">
			<xsl:value-of select="@dirname"/>
		</span>
		<xsl:if test="@new=0">
			<a id="collection_delete" href="FileManager.html?dir={@dirname}&amp;act=del" title="Delete this collection">X</a>
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
			<b>Drag and drop data and rng files here</b>
			<noscript>
				<b style="color:red;"> once you enable javascript!</b>
			</noscript>
<!--
			<div id="nojsmsg">
				<b style="color:red;"> once you enable javascript!</b>
			</div>
-->
		</div>
		<form style="padding:10px;margin:10px;" action="FileManager.html?dir={@dirname}" method="post" enctype="multipart/form-data">
			<div>
				<span style="margin:5px;">Add files </span>
				<input type="hidden" id="dirname" name="dirname" value="{@dirname}" />
				<input type="file" id="file_upload" name="file_upload" multiple="multiple" />
				<noscript>
					<input type="submit" id="perform" name="perform" value="Upload" />
				</noscript>
			</div>
		</form>
	</xsl:if>
</div>

<div id="inputfiles">
	<xsl:if test="@new=0">
		<div style="font-weight:bold;font-size:110%;">
			<xsl:if test="@count = 0">
				<span>This collection has no input files.</span>
			</xsl:if>
			<xsl:if test="@count = 1">
				<span>This collection has one input file.</span>
			</xsl:if>
			<xsl:if test="@count &gt; 1">
				<span>This collection has </span>
				<xsl:value-of select = "@count" />
				<span> input files.</span>
			</xsl:if>
		</div>
		<xsl:apply-templates />
	</xsl:if>
</div>

</xsl:template>

<xsl:template match="convertfiles">
	<xsl:if test="( ../inputfiles/@count &gt; 0 ) and ( ../inputfiles/@new=0 )">
	<div id="convert">
		<div style="font-weight:bold;font-size:110%;">Convert with:</div>
		<form action="FileManager.html?dir={@dirname}&amp;act=conv" method="post" enctype="multipart/form-data">
			<div style="margin:5px 20px;">
				<xsl:for-each select="file">
					<div>
						<input type="radio" name="conv" value="{@name}">
							<xsl:if test="../@last = @name">
								<xsl:attribute name="checked">
									<xsl:text>checked</xsl:text>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="@name" />
						</input>
					</div>
				</xsl:for-each>
			</div>
			<input type="submit" name="perform" value="&gt;&gt;&gt;" title="Generate output files using Abbot" />
		</form>
	</div>
	</xsl:if>
</xsl:template>

<xsl:template match="outputfiles">
<xsl:if test="count(file) &gt; 0">
	<div id="outputfiles">
		<div style="font-weight:bold;font-size:110%;">Abbot Generated Output</div>

		<xsl:apply-templates />
		<div>
			<a href="{@dirname}.zip?dir={@dirname}&amp;fn={@dirname}.zip">Download all in a single zip</a>
		</div>
		<div>
			<a href="{@dirname}.tar.gz?dir={@dirname}&amp;fn={@dirname}.tar.gz">Download all in a single tar.gz</a>
		</div>
	</div>
</xsl:if>
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

