<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />


<xsl:template match="/">
<html lang="en" class="no-js">
	<xsl:apply-templates />
</html>
</xsl:template>

<!-- Revert user to sign in screen if OwnerID is null (not signed in) -->
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

<!-- Produce the Vicar page -->
<xsl:template match="filemanager">
<head>
	<title>Vicar - access to Abbot</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="description" content="Translation of data files to an Abbot compatible format" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="robots" content="noindex,nofollow" />
	<!--unicode 160 gets past some odd bug that prevents the html5 serializer from working with these script lines-->
	<!--no-js class is found in the modernizr.js file - this is used to detect and work around browsers which have javascript turned off - see http://modernizr.com for more information-->
	<script type="text/javascript" language="JavaScript" src="Modernizr/modernizr.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="Utils/AjaxClient.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="Upload/PopupFrame.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="Upload/AjaxUpload.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="SchemaList.js">&#160;</script>

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">&#160;</script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js">&#160;</script>
	<link rel="stylesheet" type="text/css" href="Vicar.css" />
	<link rel="stylesheet" type="text/css" href="Upload/AjaxUpload.css" />
	<link rel="stylesheet" type="text/css" href="Upload/PopupFrame.css" />

	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/redmond/jquery-ui.css" />

	<script>
		$(function() {
			$( "#progressbar" ).progressbar({value:0});
		});
	</script>
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
	<!-- top banner with account and signout access -->
	<div style="padding:0.5em;margin:0.5em;color:blue;background:yellow;">
		<a href="Account.html">
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
		<a style="position:relative;float:right;color:blue;" href="Signout.html">Sign out</a>
	</div>

	<!-- main page -->
	<xsl:apply-templates />

	<!-- footer which displays detected screen size - will be commented out in released version -->
	<div style="margin:0.6em;font-size:100%;font-weight:bold;color:orange;position:fixed;bottom:20px;right:20px;">
		<div id="smartphonesize">smart phone sized screen</div>
		<div id="tabletsize">tablet sized screen</div>
		<div id="computersize">computer sized screen</div>
	</div>
</body>
</xsl:template>


<!-- display a list of the collections -->
<xsl:template match="dirs">
	<table style="margin:10px 5px;padding:0px;">
		<tr>
			<td style="margin:0px;padding:0px;">
				<span style="font-size:130%;">Collections </span>
			</td>
			<xsl:if test="@count &lt; @maxcount">
				<td>
					<a id="collection_create" style="margin:0em 1em;" href="Vicar.html?dir=new" title="Add New Collection">+</a>
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

<!-- display each collection name in the collection list along with the number of input files it has (subelement to dirs) --> 
<xsl:template match="dir">
	<xsl:if test="@name != 'null'">
		<tr>
			<td></td>
			<td>
				<a style="font-size:130%;color:blue;text-decoration:none;margin:0em 1em;padding:0em;" href="Vicar.html?dir={@name}" title="Created {@date}">
					<xsl:value-of select="@name"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="@count"/>
					<xsl:text>)</xsl:text>
				</a>
			</td>
		</tr>
	</xsl:if>
</xsl:template>

<!-- present any messages returned from the server (subelement to filemanager) -->
<xsl:template match="msg">
	<xsl:if test="@msgcode &gt; 0">
		<div id="errmsg">
			<xsl:value-of select="text()"/>
		</div>
	</xsl:if>
</xsl:template>

<!-- display the details of a specific selected selection (subelement to filemanager) -->
<xsl:template match="collection">

	<!-- show a link back to the listing, show the collection name, offer a chance to delete it -->
	<table style="margin:10px 5px;padding:0px;">
		<tr>
			<td style="margin:0px;padding:0px;">
				<a style="margin:0px;padding:0px;color:blue;text-decoration:none;" href="Vicar.html">
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
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=del" title="Delete this collection">x</a>
					</xsl:if>
					<xsl:if test="../msg/@msgcode &lt; 0">
						<span style="color:red;font-size:130%;">Delete this Collection? </span>
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=delconfirm" title="Delete this collection">Yes</a>
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=delcancel" title="Keep this collection">Cancel</a>
					</xsl:if>
				</xsl:if>
			</td>
		</tr>
	</table>

	<!-- if this collection is new then request that the user give it a name -->
	<xsl:if test="@new=1">
		<form style="padding:10px;margin:10px;" action="Vicar.html?dir={@dirname}" method="post" accept-charset="utf-8" enctype="multipart/form-data">
			<div>
				<span>Enter a new name for this collection: </span>
				<input type="text" name="ren" autocomplete="off" />
				<input type="submit" name="perform" value="Save" />
				<input type="submit" name="perform" value="Cancel" />
			</div>
		</form>
	</xsl:if>

	<!-- allow upload of files to named (non new) collections -->
	<xsl:if test="@new=0">
		<div id="nojsmsg">
			<div style="color:red;margin:10px 0px;">If javascript were enabled you would be able to upload multiple files at once by selecting them or by dragging and dropping them into the drop box.</div>
		</div>

		<div style="margin:0.5em 0em 1em 0em;padding:0em;">
			<div id="upload_msgbox">
				<span>Drag and drop input and conversion (.rng) files here</span>
			</div>

			<form id="upload_form" style="padding:0.5em;margin:0.5em;" action="Vicar.html?dir={@dirname}" method="post" enctype="multipart/form-data">
				<span>Add files </span>
				<input type="hidden" id="dirname" name="dirname" value="{@dirname}" />
				<input type="file" id="file_upload" name="file_upload" multiple="multiple" title="Navigate or drag/drop files here."></input>
				<input type="submit" id="perform" name="perform" value="Upload" />
			</form>
			<div id="progressbar" style="height:15px;background:white;"></div>
		</div>

	</xsl:if>

	<!-- build rest of page -->
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

<!-- show uploaded input files - presentation of 'outercolumn' media selected via CSS -->
<xsl:template match="inputfiles">
	<div class="outercolumn">
		<div class="columnheader">
			<span class="columntitle">Input Files</span>
		</div>
		<xsl:apply-templates select="file"/>
	</div>
</xsl:template>


<!-- show schema files and present conversion button - presentation of 'innercolumn' media selected via CSS -->
<xsl:template match="schemalist">
	<div class="innercolumn">
		<div class="columnheader">
			<span class="columntitle">Convert With:</span>
		</div>
		<!--IF JAVASCRIPT IS NOT ENABLED THEN PRESENT THIS AS AN OPTION TO START A CONVERSION-->
		<div id="nojsmsg">
		<form id="convert" action="Convert/StreamServer.html?dir={../@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
			<div style="margin:5px 20px;">
				<span>Namespace:</span>
				<input type="text" size="40" name="abbotns" id="abbotns" autocomplete="on" value="http://www.tei-c.org/ns/1.0" />
			</div>
			<div style="margin:5px 20px;">
				<span>Custom:</span>
				<input type="text" size="40" name="abbotcustom" id="abbotcustom" autocomplete="on" value="http://abbot.unl.edu/abbot_config.xml" />
			</div>
			<div style="margin:5px 20px;">
				<select id="schemaselectnojs" name="conv" style="color:blue;">
					<xsl:apply-templates />
				</select>
			</div>
			<input type="submit" style="color:red;font-weight:bold;" name="perform" value="&gt;&gt;&gt;" title="Generate output files using Abbot - No progress reporting is available since Javascript is disabled" />
		</form>
		</div>

		<!--HIDE THE FOLLOWING IF THERE IS NO JAVASCRIPT ENABLED ELSE SHOW IT AS IT IS THE NORMAL WAY TO START A CONVERSION-->
		<!--NOTE THAT UNDER NORMAL OPERATION THE onclick EVENT ON THE SUBMIT BUTTON IS USED RATHER THAN THE action ON THE form ELEMENT-->
		<div id="nojshide">
		<form id="convert" action="Convert/StreamServer.html?dir={../@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
			<div style="margin:5px 20px;">
				<span>Namespace:</span>
				<input type="text" size="40" name="abbotnsjs" id="abbotnsjs" autocomplete="on" value="http://www.tei-c.org/ns/1.0" />
			</div>
			<div style="margin:5px 20px;">
				<span>Custom:</span>
				<input type="text" size="40" name="abbotcustomjs" id="abbotcustomjs" autocomplete="on" value="http://abbot.unl.edu/abbot_config.xml" />
			</div>
			<div style="margin:5px 20px;">
				<select id="schemaselect" name="conv" style="color:blue;">
					<xsl:apply-templates />
				</select>
			</div>
		</form>
		<input type="submit" style="color:green;font-weight:bold;" name="perform" value="&gt;&gt;&gt;" onclick="makeSimpleFrame(this,'Convert/StreamClient.html?dir={../@dirname}');" title="Generate output files using Abbot with progress reporting" />
		</div>
	</div>
</xsl:template>


<!-- create options for schema select list -->
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


<!-- show output files - presentation of 'outercolumn' media selected via CSS -->
<xsl:template match="outputfiles">
	<div class="outercolumn">
		<div class="columnheader">
			<span class="columntitle">Abbot Generated Results</span>
		</div>
		<xsl:apply-templates select="file"/>
		<div style="padding:0em 0em 0em 0.3em">
			<a href="Download/{@dirname}/{@dirname}.zip">Download all in a single zip</a>
		</div>
		<div style="padding:0em 0em 0em 0.3em">
			<a href="Download/{@dirname}/{@dirname}.tar.gz">Download all in a single tar.gz</a>
		</div>
	</div>
</xsl:template>

<!-- create display of individual files for use by inputfiles and outputfiles elements-->
<xsl:template match="file">
	<div style="margin:5px 20px;">
		<!-- display files for 'inputfiles' -->
		<xsl:if test="@op = 0">
			<span>
				<xsl:value-of select="@name"/>
			</span>
		</xsl:if>
		<!-- display files for 'outputfiles' -->
		<xsl:if test="@op = 1">
			<a style="color:blue;text-decoration:none;" href="Download/{../@dirname}/{@name}">
				<xsl:value-of select="@name" />
			</a>
			<!-- 0 validation errors -->
			<xsl:if test="@errors &lt;= 0">
				<a target="_validation" class="error_report_green" href="valid/{../@dirname}/{@vname}" title="This conversion contains no errors.">
					<xsl:text>OK</xsl:text>
				</a>
			</xsl:if>
			<!-- 1 or more validation errors -->
			<xsl:if test="@errors &gt; 0">
				<a target="_validation" class="error_report_red" href="valid/{../@dirname}/{@vname}" title="This conversion encountered {@errors} errors!">
					<xsl:value-of select="@errors" />
				</a>
			</xsl:if>
		</xsl:if>
		<!-- below used by 'inputfiles' element to ask if an XML file (or not) should be unzipped/untarred/ungzipuntarred -->
		<xsl:if test="@zip = 0">
			<span style="margin:0px 10px;color:red;">xml?</span>
		</xsl:if>
		<xsl:if test="@zip = 2">
			<a style="color:blue;text-decoration:none;" href="Vicar.html?act=unzip&amp;dir={../@dirname}&amp;fn={@name}"> Unzip this file</a>
		</xsl:if>
		<xsl:if test="@zip = 3">
			<a style="color:blue;text-decoration:none;" href="Vicar.html?act=untar&amp;dir={../@dirname}&amp;fn={@name}"> Untar this file</a>
		</xsl:if>
		<xsl:if test="@zip = 4">
			<a style="color:blue;text-decoration:none;" href="Vicar.html?act=untargz&amp;dir={../@dirname}&amp;fn={@name}"> Ungzip and Untar this file</a>
		</xsl:if>
	</div>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

