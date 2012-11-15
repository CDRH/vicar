<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<xsl:apply-templates/>
</html>
</xsl:template>

<xsl:template match="signin">
<head>
	<title>Signin</title>
	<meta name="robots" content="noindex,nofollow" />
	<meta name="pragma" content="no-cache" />
	<meta http-equiv="REFRESH" content="0;../Signin.html" />
</head>
<body>
</body>
</xsl:template>

<xsl:template match="password">
<head>
	<title>Change Password</title>
	<meta name="robots" content="noindex,nofollow" />
	<meta name="pragma" content="no-cache" />
	<link rel="stylesheet" type="text/css" href="Signin.css" />
	<xsl:if test="@dispose = 1">
		<meta http-equiv="REFRESH" content="0;url=Account.html" />
	</xsl:if>
</head>
<body>
	<div class="banner">Change Password</div>
	<div class="instruct">Please enter your existing password and your new password twice.</div>
	<xsl:if test="msg/@code = 0">
		<div class="infomsg">
			<span> </span>
		</div>
	</xsl:if>
	<xsl:if test="msg/@code &gt; 0">
		<div class="infomsg">
			<xsl:value-of select="msg/text()"/>
		</div>
	</xsl:if>
	<xsl:if test="msg/@code &lt; 0">
		<div class="warnmsg">
			<xsl:value-of select="msg/text()"/>
		</div>
	</xsl:if>

	<form action="Password.html" method="post">
		<table>
			<tr>
				<td align="right">
					<span>Existing Password</span>
				</td>
				<td>
					<input type="password" name="pwd_curr" />
				</td>
			</tr>
			<tr>
				<td align="right">
					<span>New Password</span>
				</td>
				<td><input type="password" name="pwd_new" /></td>
			</tr>
			<tr>
				<td align="right">
					<span>New Password Again</span>
				</td>
				<td><input type="password" name="pwd_newagain" /></td>
			</tr>
			<tr colspan="2">
				<td><input type="submit" name="perform" value="Change Password"/></td>
				<td><input type="submit" name="perform" value="Cancel/Done"/></td>
			</tr>
		</table>
	</form>
</body>
</xsl:template>

</xsl:stylesheet>

