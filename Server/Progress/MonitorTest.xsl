<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="MonitorTest">
<head>
	<title>monitor test</title>
</head>
<body>
	<form action="MonitorTest.html" method="POST" enctype="multipart/form-data">
		<input type="text" name="name" />
		<input type="text" name="value" />
		<input type="submit" name="perform" value="Update"/>
	</form>
</body>
</xsl:template>

</xsl:stylesheet>

