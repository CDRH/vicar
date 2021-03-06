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
	<!-- KMD Add google font link -->
	<link href="http://fonts.googleapis.com/css?family=Fjalla+One" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="Signin.css" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<xsl:if test="url/text() !='NONE'">
		<meta http-equiv="REFRESH" content="{url/@delay};url={url/text()}" />
	</xsl:if>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-23044707-2', 'unl.edu');
  ga('send', 'pageview');

</script>
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
					<form action="Signin.html?act=signin" method="post" class="login_form">
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
				<!-- Not adding logos because of diverging branding issues, they are not required. -->
				<div class="alternate_login">
					<a href="OpenSignin.html?op=Yahoo" name="signin">Sign in through Yahoo</a>
				</div>
<!--FSS_NEW-->
				<div>This action requires the use of a popup for Google+</div>
<!--
				<div class="alternate_login">
					<iframe style="border:0px;min-height:20px;" src="/cocoon/vicar/oauth">
					</iframe>
				</div>
-->
				<div class="alternate_login">
					<a href="/cocoon/vicar/oauth" name="signin">Sign in through Google+</a>
				</div>
<!--FSS_NEW-->

				<a href="Signin.html?act=resetpwd" name="origsignin">
					<span class="highlightlink reset">Reset Password</span>
				</a>
				<a href="Signin.html?act=register" name="origsignin">
					<span class="highlightlink register">Register</span>
				</a>
<!--
				<div style="margin:0.5em 0em;font-size:75%;">Google sign-in to Abbot has been removed.</div>
-->
			</div><!-- /login -->
         
			<!-- KMD New div, this text will change, here as placeholder -->
			<div class="description">
<!--
			  	<p style="font-size:24px;color:red;">This site is currently down for maintenance.</p>
--> 
			   <p>The <a href="http://cdrh.unl.edu">Center for Digital Research in the Humanities</a> at
			      the University of Nebraska and Northwestern University's <a
			         href="http://www.it.northwestern.edu/about/departments/at/">Academic and Academic
			         Research Technologies</a> are pleased to announce the first fruits of a collaboration
			      between the Abbot and EEBO-MorphAdorner projects: the
			      release of some 2,000 18th century texts from the TCP-ECCO collections in a TEI-P5
			      format and with linguistic annotation. More texts will follow shortly, subject to the
			      access restrictions that will govern the use of TCP texts for the remainder of this
			      decade. Read the full announcement or download the 2,000 ECCO  texts <a href="http://abbot.unl.edu/abbot-morphadorner/index.html">here</a>.</p>

				<p>Abbot is a tool designed to convert dissimilar collections of
 XML texts into a common interoperable form. Abbot's key feature is the 
ability to read an XML schema file and output procedures to convert 
source files into a valid form that is consistent with the target 
schema. Abbot's schema-harvesting procedures focus on TEI, but are 
extremely flexible and format agnostic. Abbot makes no particular 
judgment or demand concerning the type of interoperability sought. It 
can transform texts into a variety of TEI schema, and accommodates user 
customization.</p>
        
				<p>Abbot is more likely than conventional file-conversion 
methods to spot and deal with problems because it operates consistently 
and algorithmically across large numbers of texts. Abbot sets its course
 on an ambitious but sensible path&#8212;moving toward total 
interoperability, while at the same time accepting the uniqueness of 
individual text collections. Abbot's method allows for different forms 
of interoperability, from small, one-off instances to the creation of 
large, permanent digital libraries.</p>

<!--
				<p>Abbot was developed at the <a href="http://cdrh.unl.edu/">Center for Digital Research in the Humanities</a> by Brian L. Pytlik Zillig, Stephen Ramsay, Martin Mueller, and <a href="oauth" name="signin" style="color:black;text-decoration:none;">Frank Smutniak</a>. Support for Abbot was provided by the <a href="http://www.mellon.org/">Andrew W. Mellon Foundation</a>.</p>
-->
				<p>Abbot was developed at the <a href="http://cdrh.unl.edu/">Center for Digital Research in the Humanities</a> by Brian L. Pytlik Zillig, Stephen Ramsay, Martin Mueller, and <a href="oauth" name="signin" style="color:black;text-decoration:none;">Frank Smutniak</a>. Support for Abbot was provided by the <a href="http://www.mellon.org/">Andrew W. Mellon Foundation</a>.</p>

				<p>
					<p>Power users may want to try the command-line version of Abbot, <a href="http://abbot.unl.edu/downloads/abbot/">available here</a>.</p>
					<p>For further information please contact us at <a href="mailto:cdrh@unl.edu">cdrh@unl.edu</a>.</p>

				   <p>Please see the Abbot software <a href="http://abbot.unl.edu/downloads/abbot/license.txt">license</a>.</p>
               
					<a href="AboutVicar.html" class="more_info" >more info</a>
				</p>

			</div> <!-- /description -->
		</div> <!-- /content -->
		<div> </div>
		<div class="login_box">
			<xsl:if test="@mode = 0">
				<div class="login_warning">You have successfuly logged out of your anonymous account.</div>
			</xsl:if>
			<xsl:if test="@mode = -1">
				<div class="login_warning">You have successfuly logged out of this site but this does not log you out of your Google account.</div>
			</xsl:if>
			<xsl:if test="@mode = -2">
				<div class="login_warning">You have successfuly logged out of this site but this does not log you out of your Yahoo account.</div>
			</xsl:if>
		</div>
	</xsl:if>
	<!-- implements register form -->
	<xsl:if test="@act = 'register'">
		<div class="content registration">
		<h2>Register</h2>
<!--FSS_start-->
		<xsl:if test="msg/@code = 0">
			<div class="infomsg">
				<span> </span>
			</div>
		</xsl:if>
		<xsl:if test="msg/@code &lt; 0">
			<div class="warnmsg">
				<span>
					<xsl:value-of select="msg/text()"/>
				</span>
			</div>
		</xsl:if>
<!--FSS_end-->
		<p class="instruct">Enter your email address for ID and then enter a new password twice.</p>
		<p class="instructsmall">Passwords must contain a more than 5 characters but fewer than 20.</p>
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
		</div>
	</xsl:if>

	<xsl:if test="@act = 'confirm'">
<!--FSS_start-->
		<p class="instruct">Your registration is almost complete!</p>
		<p class="instructsmall">Please check your email for a confirmation link.  Once that has been clicked then you may sign in.</p>
<!--FSS_end-->
		<a href="Signin.html?act=signin">Sign In</a>
	</xsl:if>

	<xsl:if test="@act = 'resetpwd'">
		<div class="content registration">
		<h2>Reset Password </h2>
		<p class="instruct">Enter your email address and a password reset link will be sent to you via email.</p>
		<form method="post" action="Signin.html?act=resetpwd">
			<span class="label">ID: </span>
			<input type="text" name="signinid" autocomplete="off" value="" />
			<input class="button" type="submit" name="perform" value="Reset" />
			<input class="button" type="submit" name="perform" value="Cancel" />
		</form>
		</div>
	</xsl:if>

	<xsl:if test="@act = 'resetpwddone'">
		<a href="Signin.html?act=signin">Sign In</a>
	</xsl:if>

	<xsl:if test="@act = 'resetpwdlink'">
		<div class="content registration">
		<h2>Password Reset </h2>
		<p class="instruct">Enter your new password twice.</p>
		<p class="instructsmall">Passwords must contain a more than 5 characters but fewer than 20.</p>
		<form method="post" action="Signin.html?act=resetpwdlink">
			<input type="hidden" name="signinid" value="{@ID}" />
			<span class="label">New Password: </span>
			<input type="password" name="pwd" />
			<span>  New Password Again: </span>
			<input type="password" name="pwdalt" />
			<input class="button" type="submit" name="perform" value="Reset" />
			<input class="button" type="submit" name="perform" value="Cancel" />
		</form>
		</div>
	</xsl:if>

	<xsl:if test="@act = 'resetpwdlinkdone'">
		<a href="Signin.html?act=signin">Sign In</a>
	</xsl:if>
</xsl:if>
</body>
</xsl:template>

</xsl:stylesheet>



