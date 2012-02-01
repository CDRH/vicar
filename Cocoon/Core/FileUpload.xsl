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

<xsl:template match="fileupload">
<head>
	<title>File Upload</title>
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
	<xsl:if test="@dir != 'null'">
		<meta http-equiv="REFRESH" content="0;url=FileManager.html?dir={@dir}" />
	</xsl:if>
<!--
	<link rel="stylesheet" type="text/css" href="Simple.css"></link>
-->
</head>
<body style="margin:10px;">
	<xsl:if test="@dir = 'null'">
		<div style="color:red;font-size:130%;">
			<span>This file upload only handles one file at a time.  This is for development purposes only.  Once the details of this section are worked out the multiple upload components will be inserted here.  It's just easier to debug a pure cocoon site than one which is integrated with a servlet.</span>
		</div>
		<div>
			<xsl:value-of select="@filename" />
		</div>
		<form action="FileUpload.html" method="post" enctype="multipart/form-data">
			<input type="file" name="uploaded_file" />
			<input type="submit" name="upload" value="upload" />
		</form>
	</xsl:if>
</body>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

