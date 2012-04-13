
Current operation as of 4/13/2012

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
Current operation as of 3/22/2012

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

