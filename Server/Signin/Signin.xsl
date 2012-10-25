<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />


<xsl:template match="/">
<html lang="en">
	<xsl:apply-templates/>
</html>
</xsl:template>

<!--SI_SIGNIN-->
<xsl:template match="signin">
<head>
	<title>Sign In</title>
	<meta name="description" content="Sign In" />
	<meta name="robots" content="noindex,nofollow" />
	<meta name="pragma" content="no-cache" />
	<style type="text/css">
		form {
			margin:0.25em;
		}
		body {
			margin:0em;
			border:0em;
			padding:0em;
			outline:none;
			font-family:arial;
			font-size:120%;
		}
		body *{
			outline:none;
		}
		a {
			text-decoration:none;
			margin:0.25em;
			color:blue;
		}
		.infomsg {
			min-height:18px;
			margin:0.25em;
			color:green;
		}
		.warnmsg {
			min-height:18px;
			margin:0.25em;
			color:red;
		}
		.banner {
			font-size:110%;
			color:green;
		}
		.instruct {
			font-size:90%;
			color:black;
		}
		.instructsmall {
			font-size:70%;
			color:black;
		}
		.button {
			font-size:85%;
			border-radius:3px;
			-moz-border-radius:3px;
			-webkit-border-radius:3px;
			-khtml-border-radius:3px;
			font-family:arial;
		}
	</style>
        <xsl:if test="url/text() !='null'">
                <meta http-equiv="REFRESH" content="{url/@delay};url={url/text()}" />
        </xsl:if>
</head>
<body>
<!--
<h2>
	<span>ACTION:</span>
	<xsl:value-of select="@act"/>
</h2>
-->

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

	<xsl:if test="@act = 'signin' or @act = 'null'">
		<div class="banner">Sign In </div>
		<form method="post" action="Signin.html?act=signin">
			<span>ID: </span>
			<input type="text" name="signinid" autocomplete="off" value="" />
			<span>  Password: </span>
			<input type="password" name="pwd" />
			<input class="button" type="submit" name="perform" value="Sign In" />
			<input class="button" type="submit" name="perform" value="Cancel" onclick="checkJS()"/>
		</form>
		<a href="Signin.html?act=resetpwd">Reset Password</a>
		<a href="Signin.html?act=register">Register</a>
	</xsl:if>

	<xsl:if test="@act = 'register'">
		<div class="banner">Register</div>
		<div class="instruct">Enter your email address for ID and then enter a new password twice.</div>
		<div class="instructsmall">Passwords must contain a more than 5 characters but fewer than 20.</div>
		<form method="post" action="Signin.html?act=register">
			<span>ID: </span>
			<input type="text" name="signinid" autocomplete="off" value="" />
			<span>  Password: </span>
			<input type="password" name="pwd" />
			<span>  Password Again: </span>
			<input type="password" name="pwdalt" />
<!--
			<span>I have read and agree with the Terms Of Service and Privacy Policy.</span>
			<input type="checkbox" name="consent" />
-->
			<input class="button" type="submit" name="perform" value="Register" />
			<input class="button" type="submit" name="perform" value="Cancel" />
		</form>
	</xsl:if>

	<xsl:if test="@act = 'confirm'">
		<a href="Signin.html?act=signin">Sign In</a>
	</xsl:if>

	<xsl:if test="@act = 'resetpwd'">
		<div class="banner">Reset Password </div>
		<div class="instruct">Enter your email address and a password reset link will be sent to you via email.</div>
		<form method="post" action="Signin.html?act=resetpwd">
			<span>ID: </span>
			<input type="text" name="signinid" autocomplete="off" value="" />
			<input class="button" type="submit" name="perform" value="Reset" />
			<input class="button" type="submit" name="perform" value="Cancel" />
		</form>
	</xsl:if>

	<xsl:if test="@act = 'resetpwddone'">
		<a href="Signin.html?act=signin">Sign In</a>
	</xsl:if>

	<xsl:if test="@act = 'resetpwdlink'">
		<div class="banner">Password Reset </div>
		<div class="instruct">Enter your new password twice.</div>
		<div class="instructsmall">Passwords must contain a more than 5 characters but fewer than 20.</div>
		<form method="post" action="Signin.html?act=resetpwdlink">
			<input type="hidden" name="signinid" value="{@ID}" />
			<span>New Password: </span>
			<input type="password" name="pwd" />
			<span>  New Password Again: </span>
			<input type="password" name="pwdalt" />
			<input class="button" type="submit" name="perform" value="Reset" />
			<input class="button" type="submit" name="perform" value="Cancel" />
		</form>
	</xsl:if>

	<xsl:if test="@act = 'resetpwdlinkdone'">
		<a href="Signin.html?act=signin">Sign In</a>
	</xsl:if>
</body>

</xsl:template>

</xsl:stylesheet>


