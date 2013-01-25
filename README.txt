Updated 01/24/2013
The java source code had been moved into a more proper packaging in the 'src' subdirectory.
All other files (html,xsl,css,js,png etc) are now in the 'resources' subdirectory.
build.xml now uses a build.local.properties file for server specific information.
Updated AbbotConvert to use abbot-0.7.1.jar.
 
Updated 12/20/2012
Significant documentation upgrade  - added AboutVicar.html and VicarSoftware.html as well as javadoc and xsl comments.


Updated on 11/15/2012 
Fixed 'no javascript' operation.  Built out Register/PasswordReset/PasswordChange functionality for local signin.  Improved integration between local and openid signin.  Changed local storage from '/tmp' to '/var' directory. Shortened main URL paths.


Updated on 10/25/2012 
Added a local register/signin/password_reset functionality.  Currently this is file based but can easily be made to use mysql or similar database.
Vicar was made to be functional even when the user turns off javascript.




################################################
Updated on 8/22/2012

Vicar now monitors the progress of Abbot.convert and validation and displays a two tiered progress window.  The top progress bar shows total progress while the bottom shows the progress of the current task. (Though the actual means of determining progress is not currently accurate!)

Vicar now has an Anonymous login which is useful for quick testing.  The files are saved in a directory/account named the same as the IP of the user.  Currently the only IP addresses allowed are 127.0.0.1 (local computer) and those of people associated with the project.

Chrome now refreshes but might not be a good fix - revisit - problem may lie with the recursive way that the getReturnXML function works.
	see Upload/AjaxUpload.js around where percentage and 'complete' is measured and the function recurses or exits and reloads the main page
Opera now handles drag/drop and multiple file upload (version 12)
IE 9 is not expected to be able to use FileReader either per http://caniuse.com/#feat=filereader (html5 javascript function to read files on the browser's local system)

Current efforts focus on utilizing java 7 features where applicable (i.e. java.nio.file.* and 'try with resources').

################################################
Updated on 6/25/2012
Added validation using jing.
Changed presentation of public and user schema files.
Vicar now uses sed to change the output files to reflect the person initiating the conversion and the name of the schema file.
Progress bars for slow single uploads and for schema conversion are partially implemented - in this version you may see a few things flash on your screen during uploads or conversions.
Changed directory structure.
################################################
Updated on 5/22/2012

Cleaned directory of some extraneous files used for testing.
Cleaned up interface.  
Added confirm cycle to the deletion of a collection.
Fixed screen arrangement for computer and tablet sizes.

################################################
Updated on 5/01/2012

Added Modernizr javascript detection.

Confirmed vicar use of different .rng translation files.

Minor fixes.

################################################
Updated on 4/13/2012

Removed 'Simple' shell around FileManager as all currently available functionality fits togther well in FileManager.
	(It may be resurrected if needed for buildout of other features.)
	New URL is http://abbot.unl.edu:8080/cocoon/vicar/Core/FileManager.html

Added ability to accept .rng files for control of translation.

Added 'media queries' to adjust layout based on screen size.

Changed zip and tar.gz create functions to 'download' functions which
	create an up to date zip or tar.gz only if requested for download.

Figured out how to get cocoon to serialize an HTML5 document.

Have not yet tested on Internet Explorer

################################################
Updated on 3/22/2012

Basic testing of major browsers as listed in http://www.w3schools.com/browsers/browsers_stats.asp
 
	Internet Explorer
			Not tested yet pending upload of test data files to a windows computer

	Firefox 10.0.2
			OpenID google signin works
			drag and drop works for multiple files
			input box of type 'file' works for multiple files and refreshes automatically

	Chrome 17.0.963.83
			OpenID google signin works
			drag and drop works for multiple files but collections listing does not refresh
			input box of type 'file' works for multiple files but collections listing does not refresh

	Safari 5.1.4
			OpenID google signin works
			drag and drop does not work
			input box of type 'file' works for single files
			(will list multiple files but only uploads last one)

	Opera 11.61
			OpenID google signin works
			drag and drop does not work
			input box of type 'file' works for multiple files and refreshes automatically

Other problems;
When uploading many files you will see the 'Drag and drop files here' box expand to list each file for which
the browser has received a report back from the server.  You will simultaneously see the box above it show a
progress bar.  Once the files are uploaded the box collapses and the progress bar disappears (this may change).  
There is no evidence that any action is happening until the first file is uploaded.

If there is a latency in the uploads (as there is between Lawrence, KS to Lincoln, NE) and the file is large
it will appear that nothing is happening for a while until that first file is done.  If you are close to abbot
then the uploads will presumably happen very fast and you may even miss the text and progress bar.
I'll be looking into ways to present this better.

