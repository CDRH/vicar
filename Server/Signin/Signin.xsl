<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />


<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<xsl:apply-templates/>
</html>
</xsl:template>

<xsl:template match="signin">
<head>
	<title>
		<xsl:value-of select="title/text()"/>
	</title>
	<meta name="description" content="Sign In" />
	<meta name="robots" content="noindex,nofollow" />
	<meta name="pragma" content="no-cache" />
	<link rel="stylesheet" type="text/css" href="Signin.css" />
	<xsl:if test="url/text() !='NONE'">
		<meta http-equiv="REFRESH" content="{url/@delay};url={url/text()}" />
	</xsl:if>
</head>
<body>
<xsl:if test="url/text() ='NONE'">
	<div style="color:blue;font-size:120%;font-weight:bold;margin:1em;position:absolute;top:45%;">
		<xsl:value-of select="title/text()"/>
	</div>

	<xsl:if test="@act = 'signin' or @act = 'null'">
		<div align="right" style="border:0px;margin:8px;padding:0px 25px 0px 0px;">
			<xsl:if test="msg/@code = 0">
				<div class="infomsg">
					<span> </span>
				</div>
			</xsl:if>
			<xsl:if test="msg/@code &gt; 0">
				<div class="infomsg">
					<span>
						<xsl:value-of select="msg/text()"/>
					</span>
				</div>
			</xsl:if>
			<xsl:if test="msg/@code &lt; 0">
				<div class="warnmsg">
					<span>
						<xsl:value-of select="msg/text()"/>
					</span>
				</div>
			</xsl:if>
		</div>
		<div align="right" style="border:0px;margin:8px;padding:0px 25px 0px 0px;">
			<div style="padding:25px 0px 25px 0px;">
				<form style="color:blue;text-decoration:none;border:0px;margin:0px;padding:0px;" method="post" action="Signin.html?act=signin">
					<input type="hidden" value="false" name="jsenabled"/>
					<span>ID: </span>
					<input type="text" name="signinid" autocomplete="off" value="" />
					<span>  Password: </span>
					<input type="password" name="pwd" />
					<input class="button" type="submit" name="perform" value="Sign In" />
				</form>
			</div>
			<a name="origsignin" href="Signin.html?act=register" style="text-decoration:none;">
				<span class="highlightlink">Register</span>
			</a>
			<a name="origsignin" href="Signin.html?act=resetpwd" style="text-decoration:none;">
				<span class="highlightlink">Reset Password</span>
			</a>

			<div style="padding:35px 0px 5px 0px;">OR</div>
			<div style="padding:25px 0px 25px 0px;">
				<a name="signin" href="OpenSignin.html?op=Yahoo" style="color:blue;text-decoration:none;margin:0px;padding:0px;">
					<img src="YahooOpenID_13.png" height="22px;" alt="Sign In With Yahoo" />
				</a>
			</div>
			<div style="padding:15px 0px 5px 0px;">OR</div>
			<div style="padding:25px 0px 25px 0px;">
				<a name="signin" href="OpenSignin.html?op=Google" valign="bottom" style="font-size:60%;font-weight:bold;background:lightgrey;color:#222;border:1px solid #7D7D7D;text-decoration:none;margin:0px;padding:3px 1px 4px 2px;">
					<img src="GoogleGImage.png" height="17px;" style="margin:0px;padding:0px;vertical-align:middle;" />
					<span style="margin:4px;padding:0px;">Sign In through Google</span>
				</a>
			</div>
		</div>
		<div style="font-size:140%;margin:0.5em;">
			<xsl:if test="@mode = 0">
				<div style="color:red;">You have successfuly logged out of your anonymous account.</div>
			</xsl:if>
			<xsl:if test="@mode = -1">
				<div style="color:red;">You have successfuly logged out of this site but this does not log you out of your Google account.</div>
			</xsl:if>
			<xsl:if test="@mode = -2">
				<div style="color:red;">You have successfuly logged out of this site but this does not log you out of your Yahoo account.</div>
			</xsl:if>
<!--
			<div style="color:blue;font-size:120%;font-weight:bold;position:absolute;top:10px;">
				<xsl:value-of select="title/text()"/>
			</div>
-->
		</div>
	</xsl:if>

	<xsl:if test="@act = 'register'">
		<div class="banner">Register</div>
		<div class="instruct">Enter your email address for ID and then enter a new password twice.</div>
		<div class="instructsmall">Passwords must contain a more than 5 characters but fewer than 20.</div>
		<form method="post" action="Signin.html?act=register">
			<span>ID: </span>
			<input type="text" name="signinid" autocomplete="off" value="" />
			<span class="label">  Password: </span>
			<input type="password" name="pwd" />
			<span>  Password Again: </span>
			<input type="password" name="pwdalt" />
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
			<span class="label">ID: </span>
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
			<span class="label">New Password: </span>
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
</xsl:if>
</body>
</xsl:template>

</xsl:stylesheet>

<!--
			<span>I have read and agree with the Terms Of Service and Privacy Policy.</span>
			<input type="checkbox" name="consent" />
-->
<!--
		<div style="padding:0px 0px 8px 0px;margin:2em 0em;">
			<a name="signin" href="../Signin/OpenSignin.html?op=Test" style="outline:none;color:blue;text-decoration:none;" title="Anonymous login currently only for testing purposes and is limited to requests from unl.edu IP addresses.">
				<span style="margin:4px;padding:0px;">Anonymous Sign In</span>
			</a>
		</div>
-->


