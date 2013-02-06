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
	<!--KMD-->
	<link rel="stylesheet" type="text/css" href="style.css" />

	<script>
		$(function() {
			$( "#progressbar" ).progressbar({value:0});
		});
	</script>
</head>

<body id="bodyid" onload="UploadInit()">

	<!-- top banner with account and signout access -->
<!--KMD-->
	<div class="identity_box">
         <!-- KMD Changed the order around a bit -->
         <span>Welcome</span>
         <a class="identity_box_name" href="Account.html">
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
	</a>
         <a href="Signout.html" class="identity_box_email">Sign out</a>
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
<!--KMD-->
	<table class="collection_selector">
		<tbody>
		<tr>
			<td>
				<span>Collections </span>
			</td>
<!--ZZZ_A-->
			<xsl:if test="@count &lt; @maxcount">
				<td>
					<a id="collection_create" href="Vicar.html?dir=new" title="Add New Collection">+</a>
				</td>
			</xsl:if>
			<xsl:if test="@count &gt;= @maxcount">
				<td>
					<span>You have reached the maximum number of directories.  Please remove an old directory to create a new one.</span>
				</td>
			</xsl:if>
<!--/ZZZ_A-->
		</tr>
		<xsl:apply-templates select="dir" />
		</tbody>
	</table>
</xsl:template>

<!-- display each collection name in the collection list along with the number of input files it has (subelement to dirs) --> 
<xsl:template match="dir">
	<xsl:if test="@name != 'null'">
		<tr>
			<td></td>
			<td>
<!--ZZZ_B-->
				<a style="font-size:130%;color:blue;text-decoration:none;margin:0em 1em;padding:0em;" href="Vicar.html?dir={@name}" title="Created {@date}">
					<xsl:value-of select="@name"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="@count"/>
					<xsl:text>)</xsl:text>
				</a>
<!--/ZZZ_B-->
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
	<table class="collection_selector">
		<tbody>
		<tr>
			<td>
				<a href="Vicar.html">
					<span>Collections </span>
				</a>
			</td>
			<td>
				<span>&gt;</span>
				<span>
					<xsl:value-of select="@dirname"/>
				</span>
				<xsl:if test="@new=0">
					<xsl:if test="../msg/@msgcode = 0">
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=del" title="Delete this collection">x</a>
					</xsl:if>
					<xsl:if test="../msg/@msgcode &lt; 0">
<!--ZZZ_C-->
						<span style="color:red;font-size:130%;">Delete this Collection? </span>
<!--/ZZZ_C-->
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=delconfirm" title="Delete this collection">Yes</a>
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=delcancel" title="Keep this collection">Cancel</a>
					</xsl:if>
				</xsl:if>
			</td>
		</tr>
		</tbody>
	</table>

	<!-- if this collection is new then request that the user give it a name -->
	<xsl:if test="@new=1">
		<form action="Vicar.html?dir={@dirname}" method="post" accept-charset="utf-8" enctype="multipart/form-data">
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
<!--ZZZ_CC-->
		<div id="nojsmsg">
			<div style="color:red;margin:10px 0px;">If javascript were enabled you would be able to upload multiple files at once by selecting them or by dragging and dropping them into the drop box.</div>
		</div>
<!--/ZZZ_CC-->

<!--KMD-->
	    <div class="uploads">
            <h4>Add files </h4>
            <div id="upload_msgbox">
               <form enctype="multipart/form-data" method="post" action="Vicar.html?dir={@dirname}" id="upload_form">
                  <div class="select_files">
                     <span>Drag and drop input and conversion (.rng) files here or</span>
                     <input value="{@dirname}" name="dirname" id="dirname" type="hidden" />
                     <input title="Navigate or drag/drop files here." multiple="multiple" name="file_upload" id="file_upload" type="file" />
                  </div><!-- /select_files -->
                  <input value="Upload" name="perform" id="perform" type="submit" />
               </form>

               <div id="progressbar"
                  class="ui-progressbar ui-widget ui-widget-content ui-corner-all"
                  role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
                  <div class="ui-progressbar-value ui-widget-header ui-corner-left"> </div>
               </div><!-- /progressbar -->

            </div><!-- /upload_msgbox -->
         </div><!-- /uploads -->
	</xsl:if>

	<!-- build rest of page -->
<!--KMD-->
	<div class="files">
		<xsl:if test="@new=0">
			<xsl:apply-templates select="inputfiles" />
		</xsl:if>
		<xsl:if test="( inputfiles/@count &gt; 0 ) and ( @new=0 )">
			<xsl:apply-templates select="schemalist" />
		</xsl:if>

		<xsl:if test="outputfiles/@count &gt; 0">
			<xsl:apply-templates select="outputfiles" />
		</xsl:if>
<!--KMD-->
	</div><!--/filesdiv-->
</xsl:template>

<!-- show uploaded input files - presentation of 'outercolumn' media selected via CSS -->
<xsl:template match="inputfiles">
<!--KMD-->
            <div class="outercolumn">
               <!-- KMD Added padding div because of stupid CSS box model -->
		<div class="paddingdiv">
                  <!-- KMD Changed this to h4 instead of div for clarity -->
                  <h4>Input Files</h4>
		  <xsl:apply-templates select="file"/>
		</div><!--/paddindiv-->
            </div><!--/outercolumn-->
	
</xsl:template>


<!-- show schema files and present conversion button - presentation of 'innercolumn' media selected via CSS -->
<xsl:template match="schemalist">
	<div class="innercolumn">
	<!--KMD-->
	<div class="paddingdiv">
		<h4>Convert With:</h4>
		<!--IF JAVASCRIPT IS NOT ENABLED THEN PRESENT THIS AS AN OPTION TO START A CONVERSION-->
		<div id="nojsmsg">
		<form id="convert" action="Convert/StreamServer.html?dir={../@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
			<div>
				<span>Namespace:</span>
				<input type="text" size="40" name="abbotns" id="abbotns" autocomplete="on" value="http://www.tei-c.org/ns/1.0" />
			</div>
			<div>
				<span>Custom:</span>
				<input type="text" size="40" name="abbotcustom" id="abbotcustom" autocomplete="on" value="http://abbot.unl.edu/abbot_config.xml" />
			</div>
			<div>
				<select id="schemaselectnojs" name="conv" class="selected">
					<xsl:apply-templates />
				</select>
			</div>
			<input class="process_files_submit" type="submit" style="color:red;font-weight:bold;" name="perform" value="&gt;&gt;&gt;" title="Generate output files using Abbot - No progress reporting is available since Javascript is disabled" />
		</form>
		</div>

		<!--HIDE THE FOLLOWING IF THERE IS NO JAVASCRIPT ENABLED ELSE SHOW IT AS IT IS THE NORMAL WAY TO START A CONVERSION-->
		<!--NOTE THAT UNDER NORMAL OPERATION THE onclick EVENT ON THE SUBMIT BUTTON IS USED RATHER THAN THE action ON THE form ELEMENT-->
		<div id="nojshide">
		<form id="convert" action="Convert/StreamServer.html?dir={../@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
			<div>
				<span>Namespace:</span>
				<input type="text" size="40" name="abbotnsjs" id="abbotnsjs" autocomplete="on" value="http://www.tei-c.org/ns/1.0" />
			</div>
			<div>
				<span>Custom:</span>
				<input type="text" size="40" name="abbotcustomjs" id="abbotcustomjs" autocomplete="on" value="http://abbot.unl.edu/abbot_config.xml" />
			</div>
			<div>
				<select id="schemaselect" name="conv" class="selected">
					<xsl:apply-templates />
				</select>
			</div>
		</form>
		<input class="process_files_submit" type="submit" name="perform" value="&gt;&gt;&gt;" onclick="makeSimpleFrame(this,'Convert/StreamClient.html?dir={../@dirname}');" title="Generate output files using Abbot with progress reporting" />
		</div>
	</div><!--/paddingdiv-->
	</div><!--/innercolumn-->
</xsl:template>


<!-- create options for schema select list -->
<!--ZZZ_D-->
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
<!--/ZZZ_D-->


<!-- show output files - presentation of 'outercolumn' media selected via CSS -->
<xsl:template match="outputfiles">
	<div class="outercolumn">
	<div class="paddingdiv">
<!--
		<div class="columnheader">
			<span class="columntitle">Abbot Generated Results</span>
		</div>
-->
		<h4>Abbot Generated Results</h4>
		<div class="download">
			<p>Download all files as:</p>
			<a href="Download/{@dirname}/{@dirname}.zip">zip file</a>
			<a href="Download/{@dirname}/{@dirname}.tar.gz">tar.gz file</a>
		</div>
<!--
		<div style="padding:0em 0em 0em 0.3em">
		</div>
-->
		<xsl:apply-templates select="file"/>
	</div>
	</div>
</xsl:template>

<!-- create display of individual files for use by inputfiles and outputfiles elements-->
<xsl:template match="file">
	<div>
		<!-- display files for 'inputfiles' -->
		<xsl:if test="@op = 0">
			<span>
				<xsl:value-of select="@name"/>
			</span>
		</xsl:if>
		<!-- display files for 'outputfiles' -->
		<xsl:if test="@op = 1">
			<a href="Download/{../@dirname}/{@name}">
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
<!--ZZZ_E-->
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
<!--/ZZZ_E-->
	</div>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

