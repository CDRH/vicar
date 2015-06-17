<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<xsl:apply-templates />
</html>
</xsl:template>

<xsl:template match="oauth">
<head>
<title>
	<xsl:value-of select="appname/text()" />
</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="description" content="Collection" />
	<meta name="robots" content="noindex,nofollow" />
<script type="text/javascript">
	var auth2 = auth2 || {};
	(function() {
		var po = document.createElement('script');
		po.type = 'text/javascript'; po.async = true;
		po.src = 'https://plus.google.com/js/client:plusone.js?onload=startApp';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(po, s);
	})();
</script>
<!-- JavaScript specific to this application that is not related to API calls -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" >&#160;</script>

        <style>
                #customBtn {
<!--
                        width: 155px;
                        border:1px solid green;
                        border-radius:2px;
                        background:lightgreen;
-->
                        border: 1px solid #000;
                        background-color: #fbcc2e;
                        margin: 10px 0;
                        text-align: center;
                        color: #000;
                        padding: 3px;
                        display: block;
                }
                #customBtn:hover {
                        box-shadow: 2px 2px 3px #888888;
                        border-radius: 5px;
                }
        </style>
</head>

<body style="border:1px solid green;padding:0px;margin:0px;">
	<input type="hidden" id="clientid" value="{clientid/text()}"> </input>
	<input type="hidden" id="state" value="{state/text()}"> </input>
<!--
	<input type="text" id="clientid" value="{clientid/text()}"> </input>
	<input type="text" id="state" value="{state/text()}"> </input>
-->
	<xsl:apply-templates />
</body>

<script src="oauth_helper.js" >&#160;</script>

</xsl:template>

<xsl:template match="signin">
<!--
	<div>This action requires the use of a popup for Google+</div>
-->
        <input type="button" id="customBtn" onClick="signInClick()" alt="Sign in with Google+" value="Sign in with Google+"></input>
<!--
	<img id="customBtn" src="signin_button.png" onClick="signInClick()" alt="Sign in with Google+" />
-->
</xsl:template>

<xsl:template match="signout">
	<img id="customBtn" src="signin_button.png" onClick="disconnect()" alt="Sign Out+" />
</xsl:template>


<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>


