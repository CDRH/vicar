<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml"
	doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />


<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" class="home">
	<xsl:apply-templates/>
</html>
</xsl:template>

<!-- passes user on to specified page upon successful sign in, or offers a one of several forms -->
<xsl:template match="signin">
<head>
	<title>
		<xsl:value-of select="title/text()"/>
	</title>
	<meta name="description" content="Sign In" />
	<meta name="robots" content="noindex,nofollow" />
	<meta name="pragma" content="no-cache" />
	<link rel="stylesheet" type="text/css" href="Signin.css" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<xsl:if test="url/text() !='NONE'">
		<meta http-equiv="REFRESH" content="{url/@delay};url={url/text()}" />
	</xsl:if>
</head>
<body>
<xsl:if test="url/text() ='NONE'">
	<div class="title">
		<h1>Abbot</h1>
		<h2>Text Interoperability Tool</h2>
	</div>

	<!-- sign in for either first time or in response to a sign out -->
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

      <!-- KMD added content div for positioning -->
      <div class="content">
         <div class="login">
            <div class="login_container">
               <form action="Signin.html?act=signin"
                  method="post" class="login_form">
                  <input name="jsenabled" value="false" type="hidden" />
                  <span>
                     <label for="signinid">ID: </label>
                     <input value="" autocomplete="off" name="signinid" type="text" />
                  </span>
                  <span>
                     <label for="pwd"> Password: </label>
                     <input name="pwd" type="password" />
                  </span>
                  <input value="Sign In" name="perform" type="submit" class="button" />
               </form>
            </div><!-- /login_container -->

            <div class="login_or">OR</div>
            
            <!-- KMD Will add images to yahoo and google signons via CSS -->
            <div class="alternate_login">
               <a href="OpenSignin.html?op=Yahoo"
                  name="signin">Sign in through Yahoo</a>
            </div>

            <div class="alternate_login">
               <a href="OpenSignin.html?op=Google"
                  name="signin">Sign In through Google</a>
            </div>

            <a href="Signin.html?act=resetpwd"
               name="origsignin">
               <span class="highlightlink reset">Reset Password</span>
            </a>
            <a href="Signin.html?act=register"
               name="origsignin">
               <span class="highlightlink register">Register</span>
            </a>

         </div><!-- /login -->
         
         <!-- KMD New div, this text will change, here as placeholder -->
         <div class="description">

            <p>Vicar is an Andrew Mellon Foundation funded web application which provides
               researchers with an easy and fast way to convert their data files into a MONK
               compatible format using Abbot. Vicar was developed by Frank Smutniak at the Center
               for Digital Research in the Humanities at the University of Nebraska - Lincoln.</p>
            <p>MONK (Metadata Offer New Knowledge) is a web based system for undertaking text
               analysis and visualization with large full-text literary archives. These archives are
               represented in various dialects of the TEI XML schema. Disparate uses and
               interpretations for the TEI element set has led to complications in the use of MONK
               for any TEI file.</p>
            <p>Abbot, a standalone XSLT program written by Brian Pytlik-Zillig, was created to read
               different files and convert them to a core set of TEI elements named TEI-A such that
               MONK could use them consistently. Abbot harvests the schema of a collection of
               submitted files and creates another XSLT program to convert those files to TEI-A for
               use in MONK or elsewhere. Further work by Stephen Ramsey gave Abbot a clojure wrapper
               which allows its invocation by Vicar and other software components.</p>
            <p>
               <!-- KMD Link to More info page, will style that after -->
               <a href="AboutVicar.html" class="more_info" >more info</a>
            </p>

         </div>
         <!-- /description -->
      </div>
      <!-- /content -->
      <div> </div>
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
		</div>
	</xsl:if>

	<!-- implements register form -->
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



