<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html lang="en">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="uploadfiles">
<head>
	<title>Upload Frame Page</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<script type="text/javascript" language="JavaScript" src="../Utils/AjaxClient.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="UploadFiles.js">&#160;</script>

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">&#160;</script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js">&#160;</script>
	<link rel="stylesheet" type="text/css" href="UploadFiles.css" />

	<link rel="stylesheet" type="text/css" href="../style.css" />
<!--
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/redmond/jquery-ui.css" />
-->
	<link rel="stylesheet" href="../jquery-ui-1.10.0.custom.css" />
	<script>
		$(function() {
			$( "#myprogressbar" ).progressbar({value:0});
		});
	</script>
</head>

<body id="bodyid" onload="UploadInit()">
	<div class="uploads">
		<div id="upload_msgbox">
			<form enctype="multipart/form-data" method="post" action="UploadFiles.html" id="upload_form">
				<div class="select_files">
					<span>Drag and drop input and conversion (.rng) files here or</span>
					<input value="{@dirname}" name="dirname" id="dirname" type="hidden" />
					<input title="Navigate or drag/drop files here." multiple="multiple" name="file_upload" id="file_upload" type="file" />
				</div>
				<input value="Upload" name="perform" id="perform" type="submit" />
			</form>
		</div>
	</div>

	<div id="myprogressbar" style="height:15px;background:white;border:1px solid brown;"></div>
	<div id="upload_text" name="upload_text" style="position:static!important;overflow:auto;height:60px;"></div>
	<input type="submit" style="margin:4px 0px 0px 0px;" value="Done" id="donebutton" onclick="window.parent.location.href='Test.html?dir={@dirname}'" />
<!--
	<form enctype="multipart/form-data" method="post" action="UploadFiles.html" id="upload_form">
		<span>Drag and drop input and conversion (.rng) files here or</span>
		<input value="{@dirname}" name="dirname" id="dirname" type="hidden" />
		<input title="Navigate or drag/drop files here." multiple="multiple" name="file_upload" id="file_upload" type="file" />
		<input value="Upload" name="perform" id="perform" type="submit" />
	</form>

	<div id="progressbar" style="height:15px;background:white;border:1px solid brown;"></div>
	<div id="upload_msgbox" name="upload_msgbox" style="position:static!important;overflow:auto;height:60px;"></div>
	<input type="submit" style="margin:4px 0px 0px 0px;" value="Done" id="donebutton" onclick="window.parent.location.href='Test.html?dir={@dirname}'" />
-->
</body>
</xsl:template>


<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

