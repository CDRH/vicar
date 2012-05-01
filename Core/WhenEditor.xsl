<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html lang="en" class="no-js">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="WhenEdit">
<head>
	<title>Editor</title>
</head>
<body>
	<form action="WhenEditor.html" method="post" enctype="multipart/form-data">
		<input type="hidden" name="UID">
			<xsl:attribute name="value">
				<xsl:value-of select="@UID"/>
			</xsl:attribute>
		</input>
		<xsl:apply-templates select="When" />
		<div>
			<input type="submit" name="perform" value="Create" />
			<xsl:if test="@UID != 'new'">
				<input type="submit" name="perform" value="Update" />
				<input type="submit" name="perform" value="Delete" />
			</xsl:if>
			<xsl:if test="@UID = 'new'">
				<input type="submit" name="perform" value="Update" disabled="true"/>
				<input type="submit" name="perform" value="Delete" disabled="true"/>
			</xsl:if>
		</div>
		<input type="submit" name="perform" value="Done" />
	</form>
</body>
</xsl:template>

<xsl:template match="When">
	<div>
		<span>
			<span>Confirm:</span>
			<input type="date" id="confirm_date" name="confirm_date" value="{Confirm/@date}" autocomplete="off" />
			<input type="time" id="confirm_time" name="confirm_time" value="{Confirm/@time}" autocomplete="off" />
		</span>
		<span>
			<span>Start:</span>
			<input type="date" id="start_date" name="start_date" value="{Start/@date}" autocomplete="off" />
			<input type="time" id="start_time" name="start_time" value="{Start/@time}" autocomplete="off" />
		</span>
		<span>
			<span>Finish:</span>
			<input type="date" id="finish_date" name="finish_date" value="{Finish/@date}" autocomplete="off" />
			<input type="time" id="finish_time" name="finish_time" value="{Finish/@time}" autocomplete="off" />
		</span>
	</div>
</xsl:template>

</xsl:stylesheet>

