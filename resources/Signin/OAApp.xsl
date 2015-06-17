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

<!-- JavaScript specific to this application that is not related to API calls -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" >&#160;</script>
	<link rel="stylesheet" type="text/css" href="style.css" />

<!--
	<style>
		#customBtn {
			width: 155px;
 			border:1px solid green;
			border-radius:2px;
			background:lightgreen;
		}
		#customBtn:hover {
			box-shadow: 2px 2px 3px #888888;
			border-radius: 5px;
		}
	</style>
-->
</head>

<body>
	<input type="hidden" id="clientid" value="{clientid/text()}"> </input>
	<input type="hidden" id="state" value="{state/text()}"> </input>
	<xsl:apply-templates />
</body>
<!--
<script src="oauth_helper.js" >&#160;</script>
-->
<script>

function disconnectServer() {
      // Revoke the server tokens
      $.ajax({
        type: 'POST',
        url: $(location).attr('origin') + '/cocoon/vicar/disconnect',
        async: false,
        success: function(result) {
		//alert("DISC "+result);
		location.replace(result);
        },
        error: function(xhr,ajaxObjectio,thrownError) {
		location.replace(xhr.responseText);
		//alert("CONNECT ERROR STATUS "+xhr.status+" TEXT "+xhr.responseText+" THROWN "+thrownError);
        }
      });
};
</script>

</xsl:template>

<xsl:template match="signout">
	<div>Signing out of abbot does not sign you out of your Google Plus account!  That must be done separately.</div>
	<div class="alternate_login">
		<a onClick="disconnectServer()" href="#" value="Sign Out">Sign Out</a>
	</div>
</xsl:template>


<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

