
var version = "@VERSION@";

var helpURL = null;
var feedbackAddress = null;

var defaultHelpURL = "http://docs.oracle.com/en/";
var defaultFeedbackAddress = "http://www.oracle.com/corporate/contact";

var new_window=null;
function makeWindow()
{
  if((new_window != null) &&(!new_window.closed))
  {
    new_window.close();
  }
  new_window = open('','feedback_window','resizable=1,scrollbars=yes,menubar=0,toolbar=0,status=0,outerHeight='+screen.availHeight+',outerWidth=720');
}

function getFeedbackURL()
{
  // load the tocAndContent.html page, which will look up the cookie and do the
  // right thing in the TOC and content panes.
  indexLocation = document.links[0].href;
  indexLocation = indexLocation.substring(0,indexLocation.lastIndexOf('/')) + '/feedback.html';
  return(indexLocation);
}

//
// writeCustomTopicInfo allows a partner to supply a custom base help URL and a custom feedback
// email address.  These will be used to display the portable topic URL at the bottom of each
// topic and the address to which the feedback form is sent.
//
// For example, at the bottom of a topic you might have:
//
// <script language="Javascript">
// writeCustomTopicInfo("http://my.server.com/workshop/doc/", "control-doc-feedback@mycompany.com");
// </script>
//
function writeCustomTopicInfo(customHelpURL, customFeedbackAddress)
{
  if (customHelpURL != null)
  {
    helpURL = customHelpURL;
  }
  if (customFeedbackAddress != null)
  {
    feedbackAddress = customFeedbackAddress;
  }
  writeTopicInfo();
}

//
// write topic information. this function is called at the bottom of each content page
// to display the topic's real URL, version and feedback link
//
function writeTopicInfo()
{
  document.writeln('<hr size="1">');

  // write a portable URL for the topic.  For oracle docs, this is usually on docs.oracle.com.
  // Extension help authors should use a suitable root URL.
  var finalPath;
  var rawPath = window.location.pathname;
  var topicPath = rawPath.replace(/\\/g, "/");
  var pathSplitIndex = topicPath.search("\/doc\/??\/") + 5;
  var localHelpURL = topicPath.substring(1, pathSplitIndex);
  var topicRelPath = topicPath.substring(pathSplitIndex);

  if (topicRelPath.indexOf("/partners/") < 0)
  {
    // if we're not looking at a partners topic, use the BEA doc base URL
    helpURL = defaultHelpURL;
    feedbackAddress = defaultFeedbackAddress;
  }
  else
  {
    if (helpURL == null)
    {
      // if no helpURL was supplied, use real file location on local disk (not portable)
      helpURL = localHelpURL;
    }
  }
  finalPath = helpURL + topicRelPath;

  // write the feedback link
  if (feedbackAddress != null)
  {
    document.writeln('<p class="fileurl">');
    document.writeln('<a href="'+getFeedbackURL()+'?address='+escape(feedbackAddress)+'&title='+escape(document.title)+'&URL='+escape(window.location.pathname.substring(1))+'" target="feedback_window" onclick="javascript:makeWindow()">{wls.FEEDBACK}</a>');
    document.writeln(' | ');
  }

  // figure out a relative path that can be found in the TOC, so that the TOC
  // will synchronize
  var relpath;
  var slashIndex = topicRelPath.lastIndexOf("/");
  if (slashIndex > 0)
  {
    var slash2Index = topicRelPath.lastIndexOf("/", slashIndex-1);
    if (slash2Index > 0)
    {
      relpath = ".." + topicRelPath.substring(slash2Index);
    }
    else
    {
      relpath = "./" + topicRelPath.substring(slashIndex);
    }
  }
  else
  {
    relpath = topicRelPath;
  }

  document.writeln('<a href="javascript:reloadTOC(\'' + relpath + '\')">Show in Table of Contents</a>');
  document.writeln('</p>');

  // write a path for the topic.  portable if not "partners" doc or if partner supplied customHelpURL
  document.writeln('<p class="fileurl">' + finalPath + '</p>');

  // write the topic's  version
  document.write('<p class="modifieddate">Version: ');
  document.write(version);
  document.writeln('</p>');
}
