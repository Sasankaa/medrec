/*------------------------------------------------------------------- 

This code was originally taken from:

http://www.insidedhtml.com/tips/techniques/ts03/index.htm

Author's Statement:
This script is based on ideas of the author.
You may copy, modify and use it for any purpose. The only condition is that if
you publish web pages that use this script you point to its author at a suitable
place and don't remove this Statement from it.
It's your responsibility to handle possible bugs even if you didn't modify
anything. I cannot promise any support.
Dieter Bungers
GMD (www.gmd.de) and infovation (www.infovation.de)
--------------------------------------------------------------------*/
/*------------------------------------------------------------------- 
Checking the client's browser and setting the text sizes of the headings in
the ToC depending on the global parameter textSizes set in tocParas and the
browser useed:
--------------------------------------------------------------------*/

var isIE = navigator.appName.toLowerCase().indexOf("explorer") > -1;
var mdi = (isIE) ? textSizes[1]:textSizes[3];
var sml = (isIE) ? textSizes[2]:textSizes[4];

// Other global variables:

/*
 * oldCurrentNumber is required to keep a headings ordering string
 * (or number) in case the ToC should change (tocChange > 0) but the
 * contents pane should remain unchanged (i.e. changeContent == 0). In
 * those cases the heading preceeded by oldCurrentNumber has to remain
 * hilited (otherwise the heading preceeded by currentNumber has to be
 * hilited).
 */
var oldCurrentNumber = "", oldLastVisitNumber = "";

/*
 * toDisplay: Array to keep the display status for each heading.
 * It is initialised so only the top level headings are displayed
 * (headings preceeded by a single string without a dot):  
 */
var toDisplay = new Array();
// Changed JW: To store multiple TOC states, toDisplay is now a 2-dimensional array.
// toDisplay[0] is the initial TOC state.
toDisplay[0] = new Array();
for (ir=1; ir<tocTab.length; ir++) {
    if(tocTab[ir][0].split(".").length == 1)
    {
// Changed JW: When displaying the TOC, we'll want to condense the list of visible
// topics so we spend less time looping through toDisplay. Therefore, instead of
// just storing "true" at the index of each visible topic, we store the index
// number.
        toDisplay[0][ir] = ir;
    }
}

// Begin Added JW

// Previously this was in a cookie, but that caused problems when multiple
// browser windows were open to different topics.
var g_currentTopicNumber = "0";

// What TOC state is currently displayed.
var g_currentHistoryID = 0;

// The last-created TOC state.
var g_lastHistoryID = 0;

// Whether the TOC is changing.
var g_boolExpandCollapse = 1;

// End Added JW

// Begin Added JW: Helper function required by javascript array sort.
// The parameters are passed in automatically.
function NumericArraySortHelperFunction(a, b)
{
    return(a - b);
}
// End Added JW

// Begin Added JW: This function will expand or collapse the specified topic.
// Input Parameters:
// topicIndex: The index in tocTab[] of the topic to be expanded/collapsed.
// boolIsTopicExpanded: Whether this topic is expanded.
// boolCollapseExpandedTopic: Whether this topic, if expanded, should be collapsed.
function ExpandCollapseTopic(topicIndex, boolIsTopicExpanded, boolCollapseExpandedTopic)
{
// Determine how many levels deep the topic is.
    var topicNumberArray = tocTab[topicIndex][0].split(".");
    var topicLevel = topicNumberArray.length;

// As we iterate through tocTab[], we'll use these two variables to determine how many
// levels deep each topic is.
    var childTopicNumberArray;
    var childTopicLevel;

// Loop through the topic's children, if any.
    for(var childTopicIndex = topicIndex + 1; childTopicIndex < tocTab.length; childTopicIndex++)
    {
// Determine how many levels deep this candidate child topic is.
        childTopicNumberArray = tocTab[childTopicIndex][0].split(".");
        childTopicLevel = childTopicNumberArray.length;

// If the parent topic is "1.2.3", and the candidate child topic is "1.2.3.4", it is a
// child topic and we should keep iterating.
// Or, if the candidate child topic is "1.2.3.4.5", then it isn't a child topic, but it
// is a descendant, so we should keep iterating, or else we might miss another child
// topic such as "1.2.3.5".
// If the candidate child topic is "1.2.4", however, we should stop iterating.
        if(topicNumberArray[topicLevel - 1] != childTopicNumberArray[topicLevel - 1])
        {
            break;
        }

// If this topic is expanded and we're collapsing it, we want to hide ALL its descendants,
// not just its children, so we hide all topics that are deeper than this topic.
// However, if we're expanding this topic, we only want to show its children,
// so we show only topics that are one level deeper than this topic.
        if(
            (
                (boolIsTopicExpanded && boolCollapseExpandedTopic) &&
                (childTopicLevel > topicLevel)
            ) ||
            (childTopicLevel == (topicLevel + 1))
        )
        {
// We don't always collapse expanded topics, but we always expand collapsed topics.
            if (boolIsTopicExpanded)
            {
                if(boolCollapseExpandedTopic)
                {
                    delete top.toDisplay[top.g_currentHistoryID][childTopicIndex];
                }
            }
            else
            {
                top.toDisplay[top.g_currentHistoryID][childTopicIndex] = childTopicIndex;
            }
        }
    }
}
// End Added JW 10/12/03

// ***************************************
// The function redisplays the ToC and the content 
// Input parameters:
// currentNumber: Hierarchical ordering string (or number) of the heading the user wants to
//                display.
// Begin Added JW 9/20/03
// >>> This should perhaps be an enumeration.
// bookmarkMode:  Controls whether we display the ToC or bookmarks. 0 = ToC, 1 = Bookmarks.
// End Added JW 9/20/03
// tocChange:     Controls how to change the ToC. 0 = No change, 1 = Change with automatic
//                collapsing of expanded headings that are not on the path to the current heading,
//                2 = Change wthout automatic colapsing (as use for example by Windows Explorer or Mac OS).
// changeContent: Controls whether the content's URL should change to the value given by the 3rd
//                element of an tocTab's entry (= 1) or not (= 0).
// e:             The event that triggered the function call. If it is set it must be the event object.
// ***************************************
function reDisplay(currentNumber,bookmarkMode,tocChange,changeContent,e,contentAnchor)
{

var time1;
var time2;
var titleFindTime;
var expandCollapseTime;
var mainLoopTime;
var tocSynchTime;
var totalTime1;
var totalTime2;
var totalTime;

totalTime1 = new Date();


// debugger;
// printTree();
// Changed JW 9/20/03: Added an extra parameter to let the ToC optionally display the bookmark list
    debug("reDisplay(" + currentNumber + ", " + bookmarkMode + ", " + tocChange + ", " + changeContent + ", " + e + ", " + contentAnchor + ")");

// Begin Changed JW 9/29/03: I'm going back to using global variables to track this information,
// because cookies introduce conflicts between multiple browser windows within the same session.
//    oldCurrentNumber = getCookie("currentTOCTopic");
//    oldLastVisitNumber = getCookie("previousTOCTopic");

    var thisNumber;
    var thisNumArray;
    var thisLevel;
    var theHref;
    var theTarget;

// Added JW 9/22/03: Used for displaying the +/- and book icons in the ToC.
    var strPlusMinusImage;
    var strBookImage;
// >>> JW: Apparently, enumerations don't exist in Javascript.
    var enumNodeExpandCollapseState;
// 0 = Collapsed
// 1 = Expanded
// 2 = Leaf

    /*
     * If there is an event that triggered the function call: Checking
     * the control key depending on the browser used. If it is pressed
     * and tocChange is greater than 0 tocChange is set to 2 so the ToC
     * changes without automatic collapsing: 
     */
// Begin Changed JW 9/18/03: This is unnecessary since we've decided to turn off automatic collapsing.
/*
    if (e) {
        ctrlKeyDown = (isIE) ? e.ctrlKey : (e.modifiers==2);
        if (tocChange && ctrlKeyDown) tocChange = 2;
    }
*/
// End Changed JW 9/18/03

    var tocFrame = top.frames['tocAndContent'].frames['myToc'];
    var contentFrame = top.frames['tocAndContent'].frames['myContent'];

// Begin Added JW 9/23/03: In case the user reaches this page via the back or forward browser button,
// make sure the banner frame is updated accordingly.

/*
    if(bookmarkMode == 0)
    {
        top.frames['banner'].setMode('toc');
    }
    else
*/
    if(bookmarkMode == 1)
    {
        top.frames['banner'].setMode('viewBookmarks');
    }
// End Added JW 9/23/03

time1 = new Date();

// Begin Changed JW 9/11/03: I moved this code up so I could display the topic title
// in the <title> tag.
    /*
     * currentIndex = Current heading's index in the tocTab array:
     */
    var currentIndex = null;
    for (i=0; i<tocTab.length; i++) {
        if (tocTab[i][0] == currentNumber) {
            currentIndex = i;
            break;
        }
    }
// End Changed JW 9/11/03

time2 = new Date();
titleFindTime = time2.valueOf() - time1.valueOf();

    /*
     * Initializing the ToC window's document and displaying the title
     * on it's top. The ToC is performed by a HTML table:
     */
//    tocFrame.document.clear();
//    tocFrame.document.writeln("<html>");
//    tocFrame.document.writeln("<head>");

// Begin Changed JW 9/11/03: Use the topic title, if available, in the <title> tag
    if(tocTab[currentIndex][1] != "")
    {
        tocFrame.document.writeln("<title>" + tocTab[currentIndex][1] + "</title>");
        top.document.title = tocTab[currentIndex][1];
    }
    else
    {
        tocFrame.document.writeln("<title>Site Navigation</title>");
        top.document.title = "WebLogic Server Documentation";
    }
// End Changed JW 9/11/03

    tocFrame.document.writeln("<STYLE type=text/css>");
    tocFrame.document.writeln(".small {font-size: 1pt;}");
    tocFrame.document.writeln("a:link { text-decoration:none; }");
    tocFrame.document.writeln("a:link:hover { text-decoration:underline; }");
    tocFrame.document.writeln("a:noul { text-decoration:none; }");
    tocFrame.document.writeln("</STYLE>");
    tocFrame.document.writeln("</head>");
    tocFrame.document.writeln("<body topmargin=0 leftmargin=0 rightmargin=0 marginwidth=0 marginheight=0 bgcolor=\"" + backColor + "\">");
    tocFrame.document.writeln("<table border=0 cellspacing=0 cellpadding=0>");
    tocFrame.document.writeln("<tr>");
    tocFrame.document.writeln("<td nowrap colspan=" + (nCols+1) + ">");
// Begin Changed JW 9/20/03: Added an extra parameter to let the ToC optionally display the bookmark list
// >>> I don't know what this link was originally intended for. I updated it analogously to the other
// links.
// Changed JW 10/10/2003: >>>
/*
    tocFrame.document.writeln("<a href=\"javaScript:history.go(0)\" onclick=\"top.reDisplay('" +
                          tocTab[0][0] + "',0,0,true)\" style=\"font-family: " + fontTitle + 
                          "; font-weight:bold; font-size:" + textSizes[0] + "em; color: " +
                          titleColor + ";\">");
    tocFrame.document.writeln("" + tocTab[0][1]);
    tocFrame.document.writeln("</a>");
*/
// End Changed JW 9/20/03
    tocFrame.document.writeln("</td>");
    tocFrame.document.writeln("</tr>");
    tocFrame.document.writeln("<tr>");

    /*
     * This is for defining the number of columns of the ToC table and
     * the width of the last one. The first cells of each following row
     * shall be empty or contain the heading symbol, the last ones are
     * reserved for displaying the heding's text:
     */
    for (k=0; k<nCols; k++) {
// Changed JW 10/10/2003
// >>>
//        tocFrame.document.write("<td nowrap><p class=small>&nbsp;</p></td>");
        tocFrame.document.write("<td width=\"16\" nowrap></td>");
    }
    tocFrame.document.write("<td nowrap width=240><p class=small>&nbsp;</p></td></tr>");

    /*
     * currentLevel = the level of the current heading:
     */
    var currentNumArray = currentNumber.split(".");
    var currentLevel = currentNumArray.length-1;

// Changed JW 9/22/03: I added code to close the document elements properly before returning.
    /*
     * If currentNumber was not found in tocTab: No action.
     */
    if (currentIndex == null)
    {
        tocFrame.document.writeln("</table></body></html>");
        tocFrame.document.close();
        return false;
    }

    /*
     * currentIsExpanded = Expand/Collapse-state of the current heading:
     */
    if (currentIndex < tocTab.length-1) {
        nextLevel = tocTab[currentIndex+1][0].split(".").length-1;
        currentIsExpanded = nextLevel > currentLevel &&
        top.toDisplay[top.g_currentHistoryID][currentIndex+1];
    } 
    else currentIsExpanded = false;

    /*
     * Determining the new URL and target (if given) of the current heading
     */
    theHref = (changeContent) ? tocTab[currentIndex][2]: "";
// JW 10/9/03: In practice, the target is always the content window. The other cases
// have not been tested.
    theTarget = tocTab[currentIndex][3];

time1 = new Date();

// Begin Added JW 9/21/03: If we're in bookmark mode, we're not interested in displaying
// the topic hierarchy, just the pages that are bookmarked. The following code is just
// for determining the topic hierarchy and expand/collapse state.
    if(bookmarkMode == 0)
    {
// End Added JW 9/21/03
        /*
         * 1st loop over the tocTab entries: Determining which topic title to display:
         */

// Begin Added JW 10/12/03: I changed the expand/collapse code to improve performance.
// If tocChange is 0, the TOC does not expand/collapse.
        if(tocChange && top.g_boolExpandCollapse)
        {
// Regardless of whether we're expanding or collapsing the topic, we want to be sure
// its ancestors, if any, are expanded. We find the ancestors by splitting the topic
// number and rejoining it one part at a time. For example, if the topic is "1.2.3",
// we know its ancestors are "1" and "1.2".
            var currentNumberArray = tocTab[currentIndex][0].split(".");
            var topicNumberToSearchFor = currentNumberArray[0];
            var indexOfTopicToOpen = 0;

// We loop through the topic's ancestors, then the topic itself.
            for(i = 0; i < currentNumberArray.length; i++)
            {
// If this isn't the root ancestor of the topic, add the next part of the topic number.
                if(i > 0)
                {
                    topicNumberToSearchFor = topicNumberToSearchFor + "." + currentNumberArray[i];
                }

// Find the ancestor's index in tocTab[].
                for( ; indexOfTopicToOpen < tocTab.length; indexOfTopicToOpen++)
                {
                    if(tocTab[indexOfTopicToOpen][0] == topicNumberToSearchFor)
                    {
                        break;
                    }
                }

// If this isn't an ancestor, but the topic itself, then we can either expand or collapse it.
// currentIsExpanded is determined above.
                if(i == (currentNumberArray.length - 1))
                {
// If tocChange is 3, expanded topics are not to be collapsed, so we pass "false" as the
// third parameter (boolCollapseExpandedTopic) to ExpandCollapseTopic().
                    if(tocChange == 3)
                    {
                        ExpandCollapseTopic(indexOfTopicToOpen, currentIsExpanded, false);
                    }
                    else
                    {
// Otherwise, we'll simply collapse expanded topics and expand collapsed topics.
                        ExpandCollapseTopic(indexOfTopicToOpen, currentIsExpanded, true);
                    }
                }
                else
                {
// If this is an ancestor, we want to make sure it's expanded, so we pass "false" as the
// second parameter (boolIsTopicExpanded) to ExpandCollapseTopic(). The third parameter
// (boolCollapseExpandedTopic) is irrelevant.
                    ExpandCollapseTopic(indexOfTopicToOpen, false, false);
                }
            }
        }
// End Added JW 10/12/03

    }

// JW: Originally I was sorting this array with Javascript's sort() function,
// but then I found that sort() works differently in IE and Netscape.
// var toDisplayCondensed.sort(NumericArraySortHelperFunction);

var toDisplayCondensed = top.toDisplay[top.g_currentHistoryID].concat();

var counter = 0;
for(i = 0; i < toDisplayCondensed.length; i++)
{
    if(toDisplayCondensed[i])
    {
        toDisplayCondensed[counter++] = toDisplayCondensed[i];
        if((counter - 1) == i)
        {
            delete toDisplayCondensed[i];
        }
    }
}

time2 = new Date();
expandCollapseTime = time2.valueOf() - time1.valueOf();

// Changed JW 9/21/03
// Variable declaration moved up from inside the new "if" block.
    var scrollY=0, addScroll=tocScroll;

// Begin Added JW 9/21/03: To avoid excessive code duplication, bookmark mode
// is a special case of the ToC.
    if(bookmarkMode == 0)
    {
// End Added JW 9/21/03

time1 = new Date();

var looppart1time1;
var looppart1time2;
var looppart1total = 0;
var looppart2time1;
var looppart2time2;
var looppart2total = 0;
var looppart3time1;
var looppart3time2;
var looppart3total = 0;
var looppart4time1;
var looppart4time2;
var looppart4total = 0;

        /*
         * Loop again over the tocTab entries to display the headings:
         * >>> I don't see why this needs to be done in a separate loop. It might
         * be more efficient to get this code into a single loop. This is how we
         * inherited this code.
         */
         for(var toDisplayTopic = 0; toDisplayTopic < counter; toDisplayTopic++)
         {
looppart1time1 = new Date();

i = toDisplayCondensed[toDisplayTopic];
                thisNumber = tocTab[i][0];
                thisNumArray = thisNumber.split(".");
                thisLevel = thisNumArray.length-1;
                isCurrent = (i == currentIndex);

                /*
                 * Setting the heading's symbol depending on whether this heading is expanded
                 * or not or if it is a leaf. It is expanded if the next heading has a greater
                 * level than this one AND has to be displayed: 
                 */
                if (i < tocTab.length-1) {
                    nextLevel = tocTab[i+1][0].split(".").length-1;
// Begin Changed JW 9/22/03
// 1. Replaced the "img" variable with "strBookImage", declared above.
// 2. Renamed the old "minus" to "open_book", "plus" to "closed_book", and "leaf" to "book_leaf".
// 3. Added a "plus_minus_leaf".
                    if(thisLevel >= nextLevel)
                    {
                        enumNodeExpandCollapseState = 2; // Leaf
                        strBookImage = "book_leaf";
                    }
                    else
                    {
                        if(top.toDisplay[top.g_currentHistoryID][i+1])
                        {
                            enumNodeExpandCollapseState = 1; // Expanded
                            strBookImage = "open_book";
                            strPlusMinusImage = "minus";
                        }
                        else
                        {
                            enumNodeExpandCollapseState = 0; // Collapsed
                            strBookImage = "closed_book";
                            strPlusMinusImage = "plus";
                        }
                    }
                }
                else
                {
                    enumNodeExpandCollapseState = 2; // Leaf
                    strBookImage = "book_leaf"; // The last heading is always a leaf.
                }
// End Changed JW 9/22/03

                /*
                 * If the scoll parameter is set true than increment the scrollY value:
                 */
                if (addScroll) scrollY+=((thisLevel<mLevel)?mdi:sml)*18;
                if (isCurrent) addScroll=false;

                /*
                 * thisTextColor = the text color of this heading
                 */
                /*
                 * thisBgColor = the background color of this heading
                 */
// Changed JW 9/23/03: Originally, text color changes were dependent on tocChange. As I understand it,
// tocChange should only control expand/collapse behavior, so I made text color changes dependent on
// changeContent, not tocChange.
                if (changeContent)
                {
                    if(thisNumber == currentNumber)
                    {
                        thisTextColor = currentColor;
                        thisBgColor = currentBgColor;
                    }
                    else
                    {
                        if(thisNumber == oldCurrentNumber)
                        {
                            thisTextColor = lastVisitColor;
                            thisBgColor = lastVisitBgColor;
                        }
                        else
                        {
                            thisTextColor = normalColor;
                            thisBgColor = normalBgColor;
                        }
                    }
                }
                else
                {
                    if(thisNumber == oldCurrentNumber)
                    {
                        thisTextColor = currentColor;
                        thisBgColor = currentBgColor;
                    }
                    else
                    {
                        if(thisNumber == oldLastVisitNumber)
                        {
                            thisTextColor = lastVisitColor;
                            thisBgColor = lastVisitBgColor;
                        }
                        else
                        {
                            thisTextColor = normalColor;
                            thisBgColor = normalBgColor;
                        }
                    }
                }

looppart1time2 = new Date();
looppart1total += looppart1time2.valueOf() - looppart1time1.valueOf();

looppart2time1 = new Date();
                /*
                 * Now writing this ToC line, i.e. a table row...:            
                 */
                tocFrame.document.writeln("<tr>");

                /*
                 * ...first some empty cells for the line indent depending on the level of
                 * this heading...
                 */
                for (k=1; k<=thisLevel; k++) {
                    tocFrame.document.writeln("<td nowrap><p class=\"small\">&nbsp;</p></td>");
                }

// Begin Added JW 9/22/03: Displaying the +/- icon, with link to update TOC.
                tocFrame.document.writeln("<td nowrap valign=\"center\" align=\"right\">");
// If this node is not a leaf, display the plus or minus image
                if(enumNodeExpandCollapseState != 2)
                {
// Added an extra parameter to let the ToC optionally display the bookmark list.
// Also added code to change the cursor to a pointing hand on mouseover.
                    tocFrame.document.write("<p " +
                        "onMouseOver=\"status='" + tocTab[i][2] + "'; return true;\" " +
                        "onMouseOut=\"status=''; return true;\" " +
                        "title=\"");
 		 tocFrame.document.write(tocTab[i][1] + ", " );
                    if(enumNodeExpandCollapseState == 0)
                    {
                        tocFrame.document.write("Expand ");
                    }
                    else
                    {
                        tocFrame.document.write("Collapse ");
                    }
                   tocFrame.document.write("Node\" "  + 
                        "style=\"cursor: pointer;\"" + ">");

                    tocFrame.document.write("<a name=\"" + thisNumber + "\"></a>");
                    tocFrame.document.write("<a href=\"toc-frame.html" +
                        "?topicNumber=" + escape(thisNumber) +
                        "&bookmarkMode=" + bookmarkMode +
                        "&tocChange=" + tocBehavior[0] +
                        "&changeContent=" + tocLinks[0] +
                        "&currentHistoryID=" + (top.g_lastHistoryID + 1) +
                        "&ignoreCookie=1" +
                        "#" + thisNumber +
                        "\">");

// Added a "strPlusMinusImage" variable, declared above.
                    tocFrame.document.write("<img src=\"images/" + strPlusMinusImage + ".gif\" width=\"9\" height=\"9\" border=\"0\" hspace=\"3\">");
                    tocFrame.document.write("</a></p>");
                }
                tocFrame.document.writeln("</td>");

looppart2time2 = new Date();
looppart2total += looppart2time2.valueOf() - looppart2time1.valueOf();

// End Added JW 9/22/03

looppart3time1 = new Date();

                /*
                 *...then the heading symbol (book icon) with a JavaScript link
                 * calling this function (reDisplay) again: 
                 */
                tocFrame.document.writeln("<td nowrap valign=top>");
// Begin Changed JW 9/20/03
// 1. Added an extra parameter to let the ToC optionally display the bookmark list.
// 2. Changed tocLinks[0] to tocLinks[1], since tocLinks[0] now refers to the +/- icon.
// 3. Changed tocBehavior[0] to tocBehavior[1], since tocBehavior[0] now refers to the +/- icon.
// 4. Added code to change the cursor to a pointing hand on mouseover.
tocFrame.document.write("<p " +
                    "onMouseOver=\"status='" + tocTab[i][2] + "'; return true;\" " +
                    "onMouseOut=\"status=''; return true;\" " +
                    "title=\"");
                    tocFrame.document.write(tocTab[i][1] + ", " );
                  tocFrame.document.write("wls.LEVEL " + thisLevel + ", ");
                    if(enumNodeExpandCollapseState == 0)
                    {
                        tocFrame.document.write("Collapsed ");
                    }
                    else
                    {
                        tocFrame.document.write("Expanded ");
                    }
                    tocFrame.document.write("\" style=\"cursor: pointer;\"" +
                    ">");
// End Changed JW 9/20/03

// Changed JW 9/22/03
                    tocFrame.document.write("<a name=\"" + thisNumber + "\"></a>");
                    tocFrame.document.write("<a href=\"toc-frame.html" +
                        "?topicNumber=" + escape(thisNumber) +
                        "&bookmarkMode=" + bookmarkMode +
                        "&tocChange=" + tocBehavior[1] +
                        "&changeContent=" + tocLinks[1] +
                        "&currentHistoryID=" + (top.g_lastHistoryID + 1) +
                        "&ignoreCookie=1" +
                        "#" + thisNumber +
                        "\">");
// Replaced the "img" variable with "strBookImage", declared above.
                tocFrame.document.write("<img src=\"images/" + strBookImage + ".gif\" width=\"16\" height=\"16\" border=\"0\" hspace=\"3\">");
                tocFrame.document.write("</a></p>");
                tocFrame.document.writeln("</td>");

looppart3time2 = new Date();
looppart3total += looppart3time2.valueOf() - looppart3time1.valueOf();

looppart4time1 = new Date();

                /*
                 *...then the heading text each with a JavaScript link
                 * calling this function (reDisplay) again: 
                 */
                tocFrame.document.writeln("<td nowrap colspan=" + (nCols-thisLevel) + ">");
// Changed 9/10/03 JW
// 1. Moved the background-color attribute from the p tag/style attribute to a new font
// tag (as seen below) so that the background color would not extend beyond
// the length of the text.
// 2. Added an extra parameter to let the ToC optionally display the bookmark list
// 3. Changed tocLinks[1] to tocLinks[2], since tocLinks[0] now refers to the +/- icon.
// 4. Changed tocBehavior[1] to tocBehavior[2], since tocBehavior[0] now refers to the +/- icon.
				tocFrame.document.write("<p title=\"");
                tocFrame.document.write(tocTab[i][1] + ", " );
                tocFrame.document.write("Level " + thisLevel + ", ");
                    if(enumNodeExpandCollapseState == 0)
                    {
                        tocFrame.document.write("Collapsed ");
                    }
                    else
                    {
                        tocFrame.document.write("Expanded ");
                    }

				tocFrame.document.write("\"" +
                    "onclick=\"location='toc-frame.html" +
                    "?topicNumber=" + escape(thisNumber) +
                    "&bookmarkMode=" + bookmarkMode +
                    "&tocChange=" + tocBehavior[2] +
                    "&changeContent=" + tocLinks[2] +
                    "&currentHistoryID=" + (top.g_lastHistoryID + 1) +
                    "&ignoreCookie=1" +
                    "#" + thisNumber +
                    "';\" " +
                    "onMouseOver=\"status='" + tocTab[i][2] + "'; return true;\" " +
                    "onMouseOut=\"status=''; return true;\" " +
                    "title=\"" + tocTab[i][1] + "\" " +
                    "style=\" " +
                        "font-family:" + fontLines + "; " +
                        ((thisLevel<=mLevel)?"font-weight:bold; ":"") + 
                        "font-size:" + ((thisLevel<=mLevel)?mdi:sml) + "em; " +
                        "color:" + thisTextColor + "; " +
                        "cursor: pointer;" +
                    "\">");
// Added 9/10/03 JW
// Moved the background-color attribute from the p tag/style attribute to a new font
// tag so that the background color would not extend beyond the length of the text.
                tocFrame.document.write("<font style=\"background-color:" + thisBgColor + "\">");
                tocFrame.document.write("<a name=\"" + thisNumber + "\"></a>");
// This line is unchanged, but I have enclosed it within a font tag.
                tocFrame.document.write("" + ((showNumbers)?(thisNumber+" "):"") + tocTab[i][1]);
// Added the closing font tag.
                tocFrame.document.write("</font>");
                tocFrame.document.write("</p>");
                tocFrame.document.write("</td>");
                tocFrame.document.writeln("</tr>");

looppart4time2 = new Date();
looppart4total += looppart4time2.valueOf() - looppart4time1.valueOf();
        } // End of loop over the tocTab

time2 = new Date();
mainLoopTime = time2.valueOf() - time1.valueOf();

// Begin Added JW 9/21/03
    }
    else
    {
        var strBookmarksCookieValue = null;

        strBookmarksCookieValue = getCookie("bookmarks");
        if(strBookmarksCookieValue == null || strBookmarksCookieValue == "")
        {
            tocFrame.document.writeln("<td nowrap valign=\"top\">");
            tocFrame.document.writeln("<p " +
                                  "style=\" " +
                                     "font-family:" + fontLines + "; " +
                                     "font-size:" + ((thisLevel<=mLevel)?mdi:sml) + "em; " +
                                     "color:" + normalColor + "; " +
                                  "\">");
            tocFrame.document.write("<font style=\"background-color:" + normalBgColor + "\">");
            tocFrame.document.write("Your bookmark list is empty.");
            tocFrame.document.write("</font>");
            tocFrame.document.write("</p>");
            tocFrame.document.write("</td>");
            tocFrame.document.writeln("</tr>");
            tocFrame.document.writeln("</table></body></html>");
            tocFrame.document.close();

            return;
        }
        var arrstrBookmarkTopicURLs = strBookmarksCookieValue.split(";");

// Display the user's bookmark list.
        var strCurrentBookmarkURL = null;
        var strCurrentBookmarkNumber = null;
        var strCurrentBookmarkTitle = null;
        var intCurrentBookmarkIndexInTocTab = null;

// Loop through the user's bookmark list.
        for (i=0; i<arrstrBookmarkTopicURLs.length; i++)
        {
// Get the topic number of the current bookmark.        
            strCurrentBookmarkURL = arrstrBookmarkTopicURLs[i];
            intCurrentBookmarkIndexInTocTab = null;

// Find this bookmark's index in the tocTab array.
// Remember that the first entry in tocTab is blank.
            for (j=1; j<tocTab.length; j++)
            {
                if (tocTab[j][2] == strCurrentBookmarkURL)
                {
                    strCurrentBookmarkNumber = tocTab[j][0];
                    strCurrentBookmarkTitle = tocTab[j][1];
                    intCurrentBookmarkIndexInTocTab = j;
                    break;
                }
            }

// If currentBookmarkIndexInTocTab was not found in tocTab: No action.
// Unknown topic: should never arise.
            if (intCurrentBookmarkIndexInTocTab == null)
            {
                continue;
            }

            thisLevel = 0;
            isCurrent = (intCurrentBookmarkIndexInTocTab == currentIndex);

// Bookmarks are treated as leaves; there's no topic hierarchy in the bookmark
// list.
// Replaced the "img" variable with "strBookImage", declared above.
            strBookImage = "bookmark";

            /*
             * thisTextColor = the text color of this heading
             */
                thisTextColor = normalColor;
                thisBgColor = normalBgColor;

                thisTextColor = (strCurrentBookmarkNumber==currentNumber)
                                   ? currentColor : normalColor;

	    /*
             * thisBgColor = the background color of this heading
             */
                thisBgColor = (strCurrentBookmarkNumber==currentNumber)
                                   ? currentBgColor : normalBgColor;

            /*
             * Now writing this ToC line, i.e. a table row...:            
             */
            tocFrame.document.writeln("<tr>");

// Display a delete symbol, with link to relevant javascript function calls.
// We use the javascript replace() function to reload bookmark.html to prevent
// old bookmark list states from getting into the history list.
            tocFrame.document.writeln("<td nowrap valign=top>");

            tocFrame.document.writeln("<p " +
                                  "onclick=\"top.deleteBookmark('" + strCurrentBookmarkURL + "'); " +
                                  "document.location.replace('bookmark.html');\" " +
                                  "onMouseOver=\"status='Delete This Bookmark'; return true;\" " +
                                  "onMouseOut=\"status=''; return true;\" " +
                                  "title=\"Delete This Bookmark\" " +
                                  "style=\"cursor: pointer;\"" +
                                  ">");

            tocFrame.document.write("<img src=\"images/delete_bookmark.gif\" width=\"16\" height=\"16\" border=\"0\" hspace=\"3\">");
            tocFrame.document.write("</p>");
            tocFrame.document.writeln("</td>");

            /*
             *...then the heading symbol with a JavaScript link
             * calling this function (reDisplay) again: 
             */
            tocFrame.document.writeln("<td nowrap valign=top>");
// Added an extra parameter to let the ToC optionally display the bookmark list
// Clicking on this symbol is the same as clicking on the bookmark. Symbol is just cosmetic,
// doesn't provide any unique functionality.
// javascript replace() fills in content frame so that every time a user selects a
// different bookmark, it doesn't go in the history list.
// >>> Under the current implementation, when the user opens a content page via a bookmark,
// that page doesn't go in the history list.
// To change this, you could just use a call to reDisplay() instead of using javascript replace()
// function.
            tocFrame.document.writeln("<p " +
                                  "onclick=\"top.g_currentTopicNumber = '" + strCurrentBookmarkNumber + "';" +
                                  "document.location.replace('bookmark.html');\" " +
                                  "onMouseOver=\"status='" + strCurrentBookmarkURL + "'; return true;\" " +
                                  "onMouseOut=\"status=''; return true;\" " +
                                  "title=\"" + strCurrentBookmarkTitle + "\" " +
                                  "style=\" " +
                                     "background-color:" + thisBgColor + "; " + 
                                     "text-decoration:underline; " +
                                     "cursor: pointer; " +
                                  "\">");

            tocFrame.document.write("<img src=\"images/" + strBookImage + ".gif\" width=\"16\" height=\"16\" border=\"0\" hspace=\"3\">");
            tocFrame.document.write("</p>");
            tocFrame.document.writeln("</td>");

            /*
             *...then the heading text each with a JavaScript link
             * calling this function (reDisplay) again: 
             */
            tocFrame.document.writeln("<td nowrap colspan=" + (nCols-thisLevel) + ">");
// 1. Moved the background-color attribute from the p tag/style attribute to a new font
// tag (as seen below) so that the background color would not extend beyond
// the length of the text.
// 2. Added an extra parameter to let the ToC optionally display the bookmark list
            tocFrame.document.writeln("<p " +
                                  "onclick=\"top.g_currentTopicNumber = '" + strCurrentBookmarkNumber + "';" +
                                  "document.location.replace('bookmark.html');\" " +
                                  "onMouseOver=\"status='" + strCurrentBookmarkURL + "'; return true;\" " +
                                  "onMouseOut=\"status=''; return true;\" " +
                                  "title=\"" + strCurrentBookmarkTitle + "\" " +
                                  "style=\" " +
                                     "font-family:" + fontLines + "; " +
                                     ((thisLevel<=mLevel)?"font-weight:bold; ":"") + 
                                     "font-size:" + ((thisLevel<=mLevel)?mdi:sml) + "em; " +
                                     "color:" + thisTextColor + "; " +
                                     "cursor: pointer;" +
                                  "\">");
// Added 9/10/03 JW
// Moved the background-color attribute from the p tag/style attribute to a new font
// tag so that the background color would not extend beyond the length of the text.
            tocFrame.document.write("<font style=\"background-color:" + thisBgColor + "\">");
            tocFrame.document.write("" + ((showNumbers)?(thisNumber+" "):"") + strCurrentBookmarkTitle);
// Added 9/10/03 JW
// Added the closing font tag.
            tocFrame.document.write("</font>");
            tocFrame.document.write("</p>");
            tocFrame.document.write("</td>");
            tocFrame.document.writeln("</tr>");
        }
    } // End of loop over the tocTab
// End Added JW 9/21/03


    /*
     * Closing the ToC document, scrolling its frame window and displaying new content
     * in the content frame or in the top window if required 
     */

    /*
     * Updating the global variables oldCurrentNumber and oldLastVisitNumber. See above
     * for its definition
     */
    if (changeContent) { 
// Begin Changed JW 9/29/03: I'm going back to using global variables to track this information,
// because cookies introduce conflicts between multiple browser windows within the same session.
        oldLastVisitNumber = oldCurrentNumber;
        oldCurrentNumber = currentNumber;
//
// use cookies to store the state instead of variables
//        setCookie("currentTOCTopic", currentNumber);
//        setCookie("previousTOCTopic", oldCurrentNumber);
// End Changed JW 9/29/03
    }

    /*
     * Closing the ToC table and the document
     */
// Changed JW 9/11/03
// Before writing the </body> and </html> tags and closing the document, we'll use a <script>
// tag to populate the content frame.
	tocFrame.document.writeln("</table>\n");

    /*
     * Scrolling the ToC if required
     */
    if (tocScroll)
    {
        tocFrame.scroll(0,scrollY);
    }
    
    /*
     * Setting the top or content window's location if required.
     * theHref is non-null if the user clicked a book or header;
     * null if the user clicked the +/- icon.
     */
    if (theHref) 
    {
// JW 10/9/03: In practice, the first three cases are never called; only the
// "else" case is relevant. The code for the first three cases was inherited
// and has not been tested.
        if (theTarget=="top")
        {
            window.top.location.href = theHref;
        }
        else if (theTarget=="parent") 
        {
            window.parent.location.href = theHref;
        }
        else if (theTarget=="blank")
        {
            window.open(theHref,"");
        }
        else
        {
// Add the skipReload=true so the frameset isn't reloaded, just loads the page into
// the existing frame.
            var newLocation = appendQuery(theHref, '?skipReload=true');

            if(contentAnchor && contentAnchor != "")
            {
// Strip off existing anchor if any
                if(newLocation.indexOf("#") != -1)
                {
                    newLocation = newLocation.substring(0, newLocation.indexOf("#"));
                }
                newLocation += "#" + contentAnchor;
            }

// Begin Changed JW 10/9/03: This code is no longer relevant.
//            // if we're displaying the top topic, use replace so it doesn't
//            // go in the history.  otherwise use location=url so it does
//            // go in the history.
//            if( currentNumber == "0" )
//            {
//              debug('reDisplay: contentFrame.location.replace(' +
//                        newLocation + ')');
//              contentFrame.location.replace(newLocation);
//            }
//            else
//            {
// End Changed JW 10/9/03
//                debug('reDisplay: contentFrame.location=' + newLocation);
//                contentFrame.location = newLocation;
// Begin Changed JW 9/11/03 (original above)
// The function we're in, reDisplay(), is only triggered by explicit user input such as a mouse click.
// We're putting a <script> tag in the page to ensure that the following code executes (to reload the
// content frame) not at the level of the present function, but even when the user hits the page via the
// Back or Forward browser button, or selects it from the browser history list.
                top.g_currentTopicNumber = currentNumber ;
                debug('reDisplay: contentFrame.location.replace2(' + newLocation + "'");
                top.frames['tocAndContent'].frames['myContent'].location.replace(newLocation);
// End Changed JW 9/11/03
//            }
        }
    }

time1 = new Date();

// Begin Changed JW 10/4/03: We're writing javascript code into the page that synchs the shape of the
// TOC even when the user hits the page via the Back or Forward browser button, or selects it from the
// browser history list.
// End Changed JW 10/4/03

time2 = new Date();
tocSynchTime = time2.valueOf() - time1.valueOf();

//    tocFrame.document.writeln("</body></html>");
    tocFrame.document.close();

totalTime2 = new Date();
totalTime = totalTime2.valueOf() - totalTime1.valueOf();
/*
alert("Title Find: " + titleFindTime + "\n" +
"Expand/Collapse: " + expandCollapseTime + "\n" +
"Main Loop: " + mainLoopTime + "\n" +
"Main Loop Part 1: " + looppart1total + "\n" +
"Main Loop Part 2: " + looppart2total + "\n" +
"Main Loop Part 3: " + looppart3total + "\n" +
"Main Loop Part 4: " + looppart4total + "\n" +
"TOC Synch: " + tocSynchTime + "\n" +
"Total: " + totalTime + "\n" +
"Unaccounted for: " + (totalTime - titleFindTime - expandCollapseTime - mainLoopTime - tocSynchTime));
*/
}

//
// strip off the leading part of the URL and leave only the last
//  couple of directory names and filename. 
//
function trimTopicURL(url)
{
    if (url != null)
    {
        topic = url;

        // find the / before the filename, if any
        var index = topic.lastIndexOf('/');
        
        // get the directory name before the filename, if any.
        // toc entries should always have at least one directory name
        // before the filename, representing the help component
        if (index > 0)
        {
            index = topic.lastIndexOf('/', index-1);
        }

        // grab the substring starting at the earliest slash found
        if (index > 0)
        {
            topic = topic.substring(index);
        }

        // strip off the fragment, if any
        index = topic.lastIndexOf('#');
        if (index > 0)
        {
            topic = topic.substring(0, index);
        }
        return topic;
    }
    else
    {
        return url;
    }
}
 
function findTopicNumber(targetUrl)
{ 
    // initialize to top-level topic be default
    var topicNumber = tocTab[0][0];
    var found = false;

    //
    // The loop below will search the TOC for "url" attributes that contain
    // the target URL as a substring. We want the search string to be short
    // but still probably unique in the TOC.
    //
//
// JM - reactivated this simple technique 11/4/03
//
    if (targetUrl != null)
    {
        var topic = trimTopicURL(targetUrl);
        
        for (i=0; i<tocTab.length; i++)
        {
            if (tocTab[i][2].indexOf(topic) >0)
            {
                topicNumber = tocTab[i][0];
                found = true;
                break;
            }
        }
    }

    //
    // NOTE: the technique below is pretty expensive compared to the simple
    // indexOf compare above (old technique). it may need to be removed for
    // that reason if perf is bad.
    //
    // we want to find the targetUrl in the TOC. The targetUrl is likely to be an
    // absolute URL like
    //
    //   C:/somedir/help/doc/en/portal/buildportals/createCampaigns.html
    //
    // and the candidate URLs in the TOC are likely to be relative URLs like
    //
    //   ../portal/buildportals/createCampaigns.html
    //
    // In the case above, the algorithm below would find "portal" as the first
    // "real" segment in the candidate URL and then look for that segment in the
    // target URL (looping in case the segment occurs more than
    // once in the targetUrl). If the segment occurs in the targetUrl, then
    // look for the substring of the targetUrl beginning at the segment as a
    // substring of the candidate URL.  I.e., see if
    //
    //   portal/buildportals/createCampaigns.html
    //
    // occurs as a substring of
    //
    //   ../portal/buildportals/createCampaigns.html
    //
    // if it does, we've found a match.
    //
// JM - commented out "new" technique 11/4/03.
//      it doesn't need to be this complicated.
//      reactivated simple teichnique above.
//
//    if( targetUrl != null )
//    {
//        var found = false;
//        var candidateUrl;
//        var candidateSegments;
//        var firstSegments;
//        var numSegments;
//        var segment;
//        var index;
//        var start;
//        var i;
//        var trimmedTargetUrl;
//
//        // strip off the fragment, if any
//        index = targetUrl.lastIndexOf('#');
//        if (index != -1)
//        {
//            trimmedTargetUrl = targetUrl.substring(0, index);
//        }
//        else
//        {
//            trimmedTargetUrl = targetUrl;
//        }
//
//        // loop over all TOC nodes
//        for(i=0; i<tocTab.length; i++)
//        {
//
//            // candidateUrl is the value of the url attribute for this TOC node
//            candidateUrl = tocTab[i][2];
//            candidateSegments = candidateUrl.split("/");
//            numSegments = candidateSegments.length;
//
//            // find the first real segment, ignoring empty segments and . and .. segments
//            segment = 0;
//            while( (segment < numSegments) &&
//                   ((candidateSegments[segment] == "") ||
//                     (candidateSegments[segment] == ".") ||
//                     (candidateSegments[segment] == "..")) )
//            {
//                segment++;
//            }
//            if( segment < numSegments )
//            {
//                firstSegment = candidateSegments[segment];
//
//                // see if the targetUrl contains the first segment of the candidateUrl
//                start = 0;
//                while( !found && ((index = targetUrl.indexOf(firstSegment, start)) != -1) )
//                {
//                    // if the targetUrl contains the first segment
//                    // of the candidateUrl, then see if the candidateUrl contains as a
//                    // substring the entire remaining targetUrl (trimmed of any #fragment)
//                    if( candidateUrl.indexOf(trimmedTargetUrl.substring(index)) != -1)
//                    {
//                        found = true;
//                        break;
//                    }
//                    start = index + 1;
//                }
//                if( found )
//                {
//                    break;
//                }
//            }
//        }
//        if( found )
//        {
//            topicNumber = tocTab[i][0];
//        }
//    }
    
    return topicNumber;
}

function displayTOC()
{
    var topicNumber = "0";
    var url;

    // First, try to read data stored in the cookie. If the cookie is not
    // defined display the top level help topic.
    var destTOC = new Cookie(document, "destTOC");
    if (destTOC.load())
    {
        url = unescape(destTOC.url);
        debug("displayTOC: destTOC cookie: " + url);
        if (url != null)
        {        
            topicNumber = findTopicNumber(url);
        }
        destTOC.remove();
    }
    else
    {
        debug("displayTOC: destTOC cookie not present");
    }

    top.reDisplay(topicNumber, 0, tocBehavior[2], tocLinks[2], null, null);
}

// Begin Added JW 9/22/03
function deleteBookmark(strBookmarkTopicURLToDelete)
{
    var strBookmarksCookieValue = null;
    var arrstrBookmarkTopicURLs = null;

    strBookmarksCookieValue = getCookie("bookmarks");

    arrstrBookmarkTopicURLs = strBookmarksCookieValue.split(";");

    strBookmarksCookieValue = "";
    for(var i = 0; i < arrstrBookmarkTopicURLs.length; i++)
    {
        if(arrstrBookmarkTopicURLs[i] != strBookmarkTopicURLToDelete)
        {
            strBookmarksCookieValue = strBookmarksCookieValue + arrstrBookmarkTopicURLs[i] + ";";
        }
    }

// Trim the last ";"
    if(strBookmarksCookieValue != "")
    {
        strBookmarksCookieValue = strBookmarksCookieValue.substring(0, strBookmarksCookieValue.length - 1);
    }

// Set the cookie for roughly ten years.
    setCookie("bookmarks", strBookmarksCookieValue, 87600);
}
// End Added JW 9/22/03
