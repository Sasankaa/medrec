/* These are the parameters to define the appearance of the ToC. */

showNumbers = false;          // display the ordering strings: yes=true | no=false
backColor = "#FFFFFF";        // background color of the ToC 
normalColor = "#000000";      // text color of the ToC headlines
lastVisitColor = "#000000";   // text color of the line last visited
currentColor = "#000099";     // text color of the actual line just clicked on
titleColor = "#000000";       // text color of the title "Table of Contents"
normalBgColor = "#FFFFFF";    // background color of the ToC headlines
lastVisitBgColor = "#FFFFFF"; // background color of the line last visited
currentBgColor = "#cccccc";   // background color of the actual line just clicked on
titleColor = "#000000";       // text color of the title "Table of Contents"
mLevel = -1;                   // number of levels minus 1 the headlines of which are presentet with large
                              // and bold fonts   
textSizes = new Array(1, 0.8, 0.8, 0.8, 0.8);  // font-size factors for: [0] the title "Table of Contents",
                                               // [1] larger and bold fonts
                                               // [2] smaller fonts if MS Internet Explorer
                                               // [3] larger and bold fonts
                                               // [4] smaller fonts if Netscape Navigator.
fontTitle = "Helvetica,Arial"; // font-family of the title "Table of Contents"
fontLines = "Verdana,Arial,sans-serif"; // font-family of the headlines
tocScroll=false;                // Automatic scrolling of the ToC frame (true) or not(false)
// Changed JW 9/22/03:
// 1. Added a parameter to control +/- icon behavior.
// 2. Added a value to specify that a closed topic should open when it is clicked on,
// but an open topic should not close. This is to distinguish the behavior of the
// +/- symbol from the heading symbol.

// >>> These should not be arrays. Each element should be its own variable of an enumerated type
// defining the different behaviors.
tocBehavior = new Array(2,3,3); // Indicates how the ToC shall change when clicking in the +/- symbol
                                 // (1st arg.) resp. in the heading symbol (2nd arg) resp. the heading
                                 // text (3rd arg). Arg's meaning:
                                 //    0 = No change,
                                 //    1 = ToC changes with automatic collapsing,
                                 //    2 = ToC changes with no automatic collapsing.
                                 //    3 = ToC changes with no automatic collapsing. Closed topics are
                                 //        opened, but open topics are not closed.
// Changed JW 9/22/03: Added a parameter to control +/- icon behavior.
tocLinks = new Array(0,1,1);     // Indicates whether the content's location shall be changed when clicking in
                                 // the +/- symbol (1st arg.) resp. the heading symbol (2nd arg.)
                                 // resp. in the heading text (3rd arg).
                                 // Arg's meaning: 0 = No, 1 = Yes.
