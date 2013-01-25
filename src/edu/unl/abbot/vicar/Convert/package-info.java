/**
* Contains functionality to invoke Abbot, correct specific errors in the output files, generate validation reports and report on the progress of these actions.
*
* Abbot is called by AbbotConvert via it's clojure interface.  AbbotConvert also corrects specific errors not yet been fixed in Abbot with calls to the Unix system command 'sed.'  Validation reports are created using Jing.  Jing results are converted to html files for display.
*
* StreamServer and StreamClient invokes AbbotConvert and monitors progress.  The details of this invokation depends on whether or not javascript is enabled.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
package edu.unl.abbot.vicar.Convert;


