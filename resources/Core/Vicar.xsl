<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<xsl:template match="/">
<html lang="en" class="no-js">
	<xsl:apply-templates />
</html>
</xsl:template>

<!-- Revert user to sign in screen if OwnerID is null (not signed in) -->
<xsl:template match="signin">
<head>
	<title>Signin</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="robots" content="noindex,nofollow" />
	<!-- KMD Add google font link -->
	<link href="http://fonts.googleapis.com/css?family=Fjalla+One" rel="stylesheet" type="text/css" />
	<xsl:if test="url/text() !='null'">
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
</xsl:template>

<!-- Produce the Vicar page -->
<xsl:template match="filemanager">
<head>
	<title>Vicar - access to Abbot</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="description" content="Translation of data files to an Abbot compatible format" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="robots" content="noindex,nofollow" />
	<!-- KMD Add google font link -->
	<link href="http://fonts.googleapis.com/css?family=Fjalla+One" rel="stylesheet" type="text/css" />
	<!--unicode 160 gets past some odd bug that prevents the html5 serializer from working with these script lines-->
	<!--no-js class is found in the modernizr.js file - this is used to detect and work around browsers which have javascript turned off - see http://modernizr.com for more information-->
	<script type="text/javascript" language="JavaScript" src="Modernizr/modernizr.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="Utils/AjaxClient.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="Upload/PopupFrame.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="Upload/AjaxUpload.js">&#160;</script>
	<script type="text/javascript" language="JavaScript" src="SchemaList.js">&#160;</script>

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">&#160;</script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js">&#160;</script>

	<script type="text/javascript" language="JavaScript" src="js_karin.js">&#160;</script>

	<link rel="stylesheet" type="text/css" href="Vicar.css" />
	<link rel="stylesheet" type="text/css" href="Upload/AjaxUpload.css" />
	<link rel="stylesheet" type="text/css" href="Upload/PopupFrame.css" />


<!--FSS PER KMD INSTRUCTIONS BELOW-->
<!--
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/redmond/jquery-ui.css" />
-->
	<!-- This needs to link to jquery-ui-1.10.0.custom.css in the KMD resources folder, and will replace the above -->
<!--
	<link rel="stylesheet" href="jquery-ui-1.10.0.custom.css" />
-->
	<link rel="stylesheet" href="jquery-ui-1.css" />


	
	<link rel="stylesheet" type="text/css" href="style.css" />
<!--FSS_NEW-->
<script>
	function toTop() {
		if(top.location != location){
			top.location.href = document.location.href;
		}
	}
</script>
<!--FSS_NEW-->

	<script>
		$(function() {
			$( "#progressbar" ).progressbar({value:0});
		});
	</script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-23044707-2', 'unl.edu');
  ga('send', 'pageview');

</script>
</head>

<!--FSS_NEW-->
<body id="bodyid" onload="toTop();UploadInit()">
<!--FSS_NEW-->
	<!-- Header -->
	<div class="title">
		<h1>Abbot</h1>
		<h2>Text Interoperability Tool</h2>
	</div>
	
	<!-- top banner with account and signout access -->
	
	<div class="identity_box">
		<span>Welcome</span>
		<a class="identity_box_name" href="Account.html">
			<xsl:choose>
				<xsl:when test="@personname != ''">
					<xsl:value-of select="@personname" />
					<xsl:if test="@userid!= ''">
						<xsl:text> (</xsl:text>
						<xsl:value-of select="@userid" />
						<xsl:text>)</xsl:text>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@userid" />
				</xsl:otherwise>
			</xsl:choose>
		</a>
		<xsl:if test="@openid != 'googleplus'">
			<a href="Signout.html" class="identity_box_email">Sign out</a>
		</xsl:if>
		<xsl:if test="@openid = 'googleplus'">
			<a href="oaapp" class="identity_box_email">Sign out</a>
		</xsl:if>
<!--
		<a href="Signout.html" class="identity_box_email">Sign out</a>
		<a href="oaapp" class="identity_box_email">OASign out</a>
		<xsl:if test="@openid != ''">
			<xsl:text> (</xsl:text>
			<xsl:value-of select="@openid" />
			<xsl:text>)</xsl:text>
		</xsl:if>
-->
	</div>

	<!-- main page -->
	<xsl:apply-templates />

	<!-- footer which displays detected screen size - will be commented out in released version -->
	<!--
	<div style="margin:0.6em;font-size:100%;font-weight:bold;color:orange;position:fixed;bottom:20px;right:20px;">
		<div id="smartphonesize">smart phone sized screen</div>
		<div id="tabletsize">tablet sized screen</div>
		<div id="computersize">computer sized screen</div>
	</div>
	-->
</body>
</xsl:template>


<!-- display a list of the collections -->
<xsl:template match="dirs">
	<table class="collection_selector">
		<tbody>
			<tr>
				<td>
					<span>Collections </span>
				</td>
				<!--ZZZ_A KMD added class directory_warning -->
				<xsl:if test="@count &lt; @maxcount">
					<td>
						<a id="collection_create" href="Vicar.html?dir=new" title="Add New Collection">+</a>
					</td>
				</xsl:if>
				<xsl:if test="@count &gt;= @maxcount">
					<td>
						<span class="directory_warning">You have reached the maximum number of directories.  Please remove an old directory to create a new one.</span>
					</td>
				</xsl:if>
<!--/ZZZ_A-->
			</tr>
			<xsl:apply-templates select="dir" />
		</tbody>
	</table>
</xsl:template>

<!-- display each collection name in the collection list along with the number of input files it has (subelement to dirs) --> 
<xsl:template match="dir">
	<xsl:if test="@name != 'null'">
		<tr>
			<td></td>
			<td>
<!--ZZZ_B KMD added class collection_list -->
				<a id="coll_{@name}" class="collection_list" href="Vicar.html?dir={@name}" title="Created {@date}">
					<xsl:value-of select="@name"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="@count"/>
					<xsl:text>)</xsl:text>
				</a>
<!--/ZZZ_B-->
			</td>
		</tr>
	</xsl:if>
</xsl:template>

<!-- present any messages returned from the server (subelement to filemanager) -->
<xsl:template match="msg">
	<xsl:if test="@msgcode &gt; 0">
		<div id="errmsg">
			<xsl:value-of select="text()"/>
		</div>
	</xsl:if>
</xsl:template>

<!-- display the details of a specific selected selection (subelement to filemanager) -->
<xsl:template match="collection">
	<!-- show a link back to the listing, show the collection name, offer a chance to delete it -->
	<table class="collection_selector">
		<tbody>
		<tr>
			<td>
				<a href="Vicar.html" id="collection_list">
					<span>Collections </span>
				</a>
			</td>
			<td>
				<span>&gt;</span>
				<span>
					<xsl:value-of select="@dirname"/>
				</span>
				<xsl:if test="@new=0">
					<xsl:if test="../msg/@msgcode = 0">
						<a id="collection_delete" href="Vicar.html?dir={@dirname}&amp;act=del" title="Delete this collection">x</a>
					</xsl:if>
					<xsl:if test="../msg/@msgcode &lt; 0">
<!--ZZZ_C KMD added class error-->
						<span class="error">Delete this Collection? </span>
<!--/ZZZ_C-->
						<a id="collection_deleteyes" href="Vicar.html?dir={@dirname}&amp;act=delconfirm" title="Delete this collection">Yes</a>
						<a id="collection_deleteno" href="Vicar.html?dir={@dirname}&amp;act=delcancel" title="Keep this collection">Cancel</a>
					</xsl:if>
				</xsl:if>
			</td>
		</tr>
		</tbody>
	</table>

	<!-- if this collection is new then request that the user give it a name -->
	<xsl:if test="@new=1">
		<form action="Vicar.html?dir={@dirname}" method="post" accept-charset="utf-8" enctype="multipart/form-data" class="new_collection">
			<div class="new_collection">
				<span class="new_collection_instructions">Enter a new name for this collection: </span>
				<input type="text" name="ren" autocomplete="off" />
				<input type="submit" name="perform" value="Save" />
				<input type="submit" name="perform" value="Cancel" />
			</div>
		</form>
	</xsl:if>

	<!-- allow upload of files to named (non new) collections -->
	<xsl:if test="@new=0">
<!--ZZZ_CC KMD added class nojs_warning-->
		<div id="nojsmsg">
			<div class="nojs_warning">If javascript were enabled you would be able to upload multiple files at once by selecting them or by dragging and dropping them into the drop box.</div>
		</div>
<!--/ZZZ_CC-->

		<div class="uploads">
			<h4>Add files </h4>
			<div id="upload_msgbox">
				<form enctype="multipart/form-data" method="post" action="Vicar.html?dir={@dirname}" id="upload_form">
					<div class="select_files">
						<span>Drag and drop input (.xml) and, optionally, schema (.rng) files here or</span>
						<input value="{@dirname}" name="dirname" id="dirname" type="hidden" />
						<input title="Navigate or drag/drop files here." multiple="multiple" name="file_upload" id="file_upload" type="file" />
					</div><!-- /select_files -->
					<input value="Upload" name="perform" id="perform" type="submit" />
				</form>

<!--FSS-->
				<div id="progressbar" style="height:15px;background:white;"></div>
<!--
				<div id="progressbar"
						class="ui-progressbar ui-widget ui-widget-content ui-corner-all"
						role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="30">
					<div class="ui-progressbar-value ui-widget-header ui-corner-left"> </div>
				</div>
-->
<!-- /progressbar -->

			</div><!-- /upload_msgbox -->
		</div><!-- /uploads -->
	</xsl:if>

	<!-- build rest of page -->

	<div class="files">
		<xsl:if test="@new=0">
			<xsl:apply-templates select="inputfiles" />
		</xsl:if>
		<xsl:if test="( inputfiles/@count &gt; 0 ) and ( @new=0 )">
			<xsl:apply-templates select="schemalist" />
		</xsl:if>

		<xsl:if test="outputfiles/@count &gt; 0">
			<xsl:apply-templates select="outputfiles" />
		</xsl:if>
	</div><!--/filesdiv-->
<!--
	<div class="files">
		<div class="paddingdiv">
			<h4>Adorn:</h4>
			<div id="nojsmsg">
				<form id="convert" action="Adorn/MAStreamServer.html?dir={@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
					<div>
						<span>Corpus:</span>
						<select id="macorpusselectnojs" name="macorpus" class="selected">
							<option value="eme" title="Early Modern English" selected="selected">Early Modern English</option>
							<option value="ece" title="Eighteenth Century English">Eighteenth Century English</option>
							<option value="ncf" title="Nineteenth Century Fiction">Nineteenth Century Fiction</option>
						</select>
						<div>
							<input type="checkbox" name="mausechoice" id="mausechoice" checked="checked"> Use choice structure to emit standard spelling</input>
						</div>
					</div>
					<input class="process_files_submit" type="submit" name="perform" value="Adorn Files &gt;&gt;&gt;" title="Adorn output files using Northwestern University's MorphAdorner - No progress reporting is available since Javascript is disabled" >
						<xsl:if test="count(schema) &lt; 1">
							<xsl:attribute name="disabled">
								<xsl:text>disabled</xsl:text>
							</xsl:attribute>
						</xsl:if>
					</input>
				</form>
			</div>

			<div id="nojshide">
				<form id="convert" action="Adorn/MAStreamServer.html?dir={@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
					<div>
						<span>Corpus:</span>
						<select id="macorpusselect" name="macorpus" class="selected">
							<option value="eme" title="Early Modern English" selected="selected">Early Modern English</option>
							<option value="ece" title="Eighteenth Century English">Eighteenth Century English</option>
							<option value="ncf" title="Nineteenth Century Fiction">Nineteenth Century Fiction</option>
						</select>
					</div>
					<div>
						<input type="checkbox" name="mausechoicejs" id="mausechoicejs" checked="checked"> Use choice structure to emit standard spelling</input>
					</div>
				</form>
				<input class="process_files_submit" type="submit" name="perform" value="Adorn Files &gt;&gt;&gt;" onclick="MAmakeSimpleFrame(this,'Adorn/MAStreamClient.html?dir={@dirname}');" title="Adorn output files using Northwestern University's MorphAdorner" >
				</input>
			</div>
		</div>
		<xsl:if test="adornfiles/@count &gt; 0">
			<xsl:apply-templates select="adornfiles" />
		</xsl:if>
	</div>
-->
</xsl:template>

<!-- show uploaded input files - presentation of 'outercolumn' media selected via CSS -->
<xsl:template match="inputfiles">

	<div class="outercolumn">
		<!-- KMD Added padding div because of stupid CSS box model -->
		<div class="paddingdiv">
			<!-- KMD Changed this to h4 instead of div for clarity -->
			<h4>Input Files</h4>
			<xsl:apply-templates select="file"/>
		</div><!--/paddindiv-->
	</div><!--/outercolumn-->
</xsl:template>


<!-- show schema files and present conversion button - presentation of 'innercolumn' media selected via CSS -->
<xsl:template match="schemalist">
	<div class="innercolumn">
		<div class="paddingdiv">
			<h4>Convert With:</h4>
			<!--IF JAVASCRIPT IS NOT ENABLED THEN PRESENT THIS AS AN OPTION TO START A CONVERSION-->
			<div id="nojsmsg">
				<form id="convert" action="Convert/StreamServer.html?dir={../@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
					<div>
						<span>Namespace:</span>
						<input type="text" size="40" name="abbotns" id="abbotns" autocomplete="on" value="http://www.tei-c.org/ns/1.0" />
					</div>
					<div>
						<span>Custom:</span>
						<input type="text" size="40" name="abbotcustom" id="abbotcustom" autocomplete="on" value="http://abbot.unl.edu/abbot_config.xml" />
					</div>
					<div>
						<select id="schemaselectnojs" name="conv" class="selected">
							<!--
							<xsl:if test="count(schema) &lt; 1">
								<option value="No Available Schemas" title="No Available Schemas" selected="selected" style="color:red;">No Available Schemas</option>
							</xsl:if>
							-->
							<xsl:apply-templates />
						</select>
					</div>
					<input class="process_files_submit" type="submit" name="perform" value="Process Files &gt;&gt;&gt;" title="Generate output files using Abbot - No progress reporting is available since Javascript is disabled" >
						<xsl:if test="count(schema) &lt; 1">
							<xsl:attribute name="disabled">
								<xsl:text>disabled</xsl:text>
							</xsl:attribute>
						</xsl:if>
					</input>
				</form>
			</div>

			<!--HIDE THE FOLLOWING IF THERE IS NO JAVASCRIPT ENABLED ELSE SHOW IT AS IT IS THE NORMAL WAY TO START A CONVERSION-->
			<!--NOTE THAT UNDER NORMAL OPERATION THE onclick EVENT ON THE SUBMIT BUTTON IS USED RATHER THAN THE action ON THE form ELEMENT-->
			<div id="nojshide">
				<form id="convert" action="Convert/StreamServer.html?dir={../@dirname}&amp;act=noblock" method="post" enctype="multipart/form-data">
					<div>
						<span>Namespace:</span>
						<input type="text" size="40" name="abbotnsjs" id="abbotnsjs" autocomplete="on" value="http://www.tei-c.org/ns/1.0" />
					</div>
					<div>
						<span>Custom:</span>
						<input type="text" size="40" name="abbotcustomjs" id="abbotcustomjs" autocomplete="on" value="http://abbot.unl.edu/abbot_config.xml" />
					</div>
					<div>
						<select id="schemaselect" name="conv" class="selected">
							<!--
							<xsl:if test="count(schema) &lt; 1">
								<option value="No Available Schemas" title="No Available Schemas" selected="selected" style="color:red;">No Available Schemas</option>
							</xsl:if>
							-->
							<xsl:apply-templates />
						</select>
					</div>
				</form>
<!--makeSimpleFrame is responsible for pulling in parameters for the form-->
				<input class="process_files_submit" type="submit" name="perform" value="Process Files &gt;&gt;&gt;" onclick="makeSimpleFrame(this,'Convert/StreamClient.html?dir={../@dirname}');" title="Generate output files using Abbot" >
					<!--
					<xsl:if test="count(schema) &lt; 1">
						<xsl:attribute name="disabled">
							<xsl:text>disabled</xsl:text>
						</xsl:attribute>
					</xsl:if>
					-->
				</input>
			</div>
		</div><!--/paddingdiv-->
	</div><!--/innercolumn-->
</xsl:template>


<!-- create options for schema select list -->
<!--ZZZ_D KMD added classes type_1 and type_2-->
<xsl:template match="schema">
	<option value="{@type}{@name}" title="{text()}">
		<xsl:if test="@current = 1">
			<xsl:attribute name="selected">
				<xsl:text>selected</xsl:text>
			</xsl:attribute>
		</xsl:if>
		<xsl:if test="@type &lt;= 1">
			<xsl:attribute name="class">
				<xsl:text>type_1</xsl:text>
			</xsl:attribute>
		</xsl:if>
		<xsl:if test="@type &gt;= 2">
			<xsl:attribute name="class">
				<xsl:text>type_2</xsl:text>
			</xsl:attribute>
		</xsl:if>
		<xsl:value-of select="@name" />
	</option>
</xsl:template>
<!--/ZZZ_D-->


<!-- show output files - presentation of 'outercolumn abbotresults' media selected via CSS -->
<xsl:template match="outputfiles">
	<div class="outercolumn abbotresults">
		<div class="paddingdiv">
			<h4>Abbot Generated Results</h4>
			<div class="download">
				<p>Download all files as:</p>
				<a href="Download/{@dirname}/output/{@dirname}.zip" id="download_zip">zip file</a>
				<a href="Download/{@dirname}/output/{@dirname}.tar.gz" id="download_targz">tar.gz file</a>
			</div>
			<xsl:apply-templates select="file"/>
		</div>

<!--YYY-->
            <!-- Create new div with padding div inside outercolumn -KMD -->
            <div class="othertransformations"> <!-- I am not actually using "othertransformations" yet, but might want to in the future -KMD  -->
               <div class="paddingdiv">
                  <h3>Other transformations</h3>
                  
                  <!-- Start accordion - javascript grabs the "accordion" class -KMD -->
                  <div class="morphadorner accordion">
<!--
                     <h3>Convert with MorphAdorner:</h3>
-->
                     <h3 class="ui-accordion-header">Convert with MorphAdorner:</h3>
                     <div>


                     <div id="nojsmsg">
                        <form enctype="multipart/form-data" method="post" action="Adorn/MAStreamServer.html?dir={@dirname}&amp;act=noblock" id="convert">
                           <div>
                              <span>Corpus:</span>
                              <select class="selected" name="macorpus" id="macorpusselectnojs">
                                 <option selected="selected" title="Early Modern English" value="eme">Early Modern English</option>
                                 <option title="Eighteenth Century English" value="ece">Eighteenth Century English</option>
                                 <option title="Nineteenth Century Fiction" value="ncf">Nineteenth Century Fiction</option>
                              </select>
                              <div>
                                 <input checked="checked" id="mausechoice" name="mausechoice" type="checkbox"> Use choice structure to emit standard spelling</input>
                              </div>
                           </div>
                           <input title="Adorn output files using Northwestern University's MorphAdorner - No progress reporting is available since Javascript is disabled" value="Adorn Files &gt;&gt;&gt;" name="perform" class="process_files_submit" type="submit" />
                        </form>
                     </div>

                     <div id="nojshide">
                        <form enctype="multipart/form-data" method="post" action="Adorn/MAStreamServer.html?dir={@dirname}&amp;act=noblock" id="convert">
                           <div>
                              <span>Corpus:</span>
                              <select class="selected" name="macorpus" id="macorpusselect">
                                 <option selected="selected" title="Early Modern English" value="eme">Early Modern English</option>
                                 <option title="Eighteenth Century English" value="ece">Eighteenth Century English</option>
                                 <option title="Nineteenth Century Fiction" value="ncf">Nineteenth Century Fiction</option>
                              </select>
                           </div>
                           <div>
                              <input checked="checked" id="mausechoicejs" name="mausechoicejs" type="checkbox"> Use choice structure to emit standard spelling</input>
                           </div>
                        </form>
                        <input title="Adorn output files using Northwestern University's MorphAdorner" onclick="MAmakeSimpleFrame(this,'Adorn/MAStreamClient.html?dir={@dirname}');" value="Adorn Files &gt;&gt;&gt;" name="perform" class="process_files_submit" type="submit" />
                     </div>


                     <p><a href="http://morphadorner.northwestern.edu/">MorphAdorner</a> created by Northwestern University.</p>
			<xsl:if test="../adornfiles/@count &gt; 0">
				<xsl:apply-templates select="../adornfiles" />
			</xsl:if>
                     </div>
<!--
                     <h3 class="ui-accordion-header">two</h3>
                     <div class="ui-accordion-content">asfdjhsadkjf</div>
                     <h3 class="ui-accordion-header">three</h3>
                     <div class="ui-accordion-content">sflasf skjdlfhlk asd klasdhjflk asdhjf</div>
-->
                  </div>
               </div>
            </div>
<!--YYY-->

	</div>
</xsl:template>

<xsl:template match="adornfiles">
	<div class="adornresults">
		<div class="paddingdiv">
			<h4>Adorned files</h4>
			<div class="download">
				<p>Download all files as:</p>
				<a href="Download/{@dirname}/adorn/{@dirname}_adorn.zip" id="download_adorn_zip">zip file</a>
				<a href="Download/{@dirname}/adorn/{@dirname}_adorn.tar.gz" id="download_adorn_targz">tar.gz file</a>
			</div>
			<xsl:apply-templates select="file"/>
		</div>
	</div>
</xsl:template>

<!-- create display of individual files for use by inputfiles and outputfiles elements-->
<xsl:template match="file">
	<div>
		<!-- display files for 'inputfiles' -->
		<xsl:if test="@op = 0">
			<span>
				<xsl:value-of select="@name"/>
			</span>
		</xsl:if>
		<!-- display files for 'outputfiles' -->
		<xsl:if test="@op = 1">
			<xsl:attribute name="class">
				<xsl:text>result</xsl:text>
			</xsl:attribute>
			<a href="Download/{../@dirname}/{@name}" id="download_{@name}">
				<xsl:value-of select="@name" />
			</a>
			<!-- 0 validation errors -->
			<xsl:if test="@errors &lt;= 0">
				<a target="_validation" class="error_report_green" href="valid/{../@dirname}/{@vname}" id="errors_{@name}" title="This conversion contains no errors.">
					<xsl:text>OK</xsl:text>
				</a>
			</xsl:if>
			<!-- 1 or more validation errors -->
			<xsl:if test="@errors &gt; 0">
				<a target="_validation" class="error_report_red" href="valid/{../@dirname}/{@vname}" id="errors_{@name}" title="This conversion encountered {@errors} errors!">
					<xsl:value-of select="@errors" />
				</a>
			</xsl:if>
		</xsl:if>
		<xsl:if test="@op = 2">
			<xsl:attribute name="class">
				<xsl:text>af_result</xsl:text>
			</xsl:attribute>
			<a href="Download/{../@dirname}/adorn/{@name}" id="download_adorn_{@name}">
				<xsl:value-of select="@name" />
			</a>
		</xsl:if>
		<!-- below used by 'inputfiles' element to ask if an XML file (or not) should be unzipped/untarred/ungzipuntarred -->
<!--ZZZ_E KMD added class error -->
		<xsl:if test="@zip = 0">
			<span class="error">xml?</span>
		</xsl:if>
		<xsl:if test="@zip = 2">
			<a class="unzip_or_tar" href="Vicar.html?act=unzip&amp;dir={../@dirname}&amp;fn={@name}"> Unzip this file</a>
		</xsl:if>
		<xsl:if test="@zip = 3">
			<a class="unzip_or_tar" href="Vicar.html?act=untar&amp;dir={../@dirname}&amp;fn={@name}"> Untar this file</a>
		</xsl:if>
		<xsl:if test="@zip = 4">
			<a class="unzip_or_tar" href="Vicar.html?act=untargz&amp;dir={../@dirname}&amp;fn={@name}"> Ungzip and Untar this file</a>
		</xsl:if>
<!--/ZZZ_E-->
	</div>
</xsl:template>

<xsl:template match="*">
</xsl:template>

</xsl:stylesheet>

