/*
 * Debug routine  used to print the frame hierarchy
 */
function printTree()
{
    var date = new Date();
    var dateStr = (date.getMonth()+1) + "/" + date.getDate() + "/" + date.getYear() + "  " +  date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    var printWindow = window.open(null, "_blank");
    printWindow.document.writeln("<html>");
    printWindow.document.writeln("<head>");
    printWindow.document.writeln("</head>");
    printWindow.document.writeln("<body topmargin=0 leftmargin=0 rightmargin=0 marginwidth=0 marginheight=0 style=\"font-weight: bold;font-family: Verdana, sans-serif;font-size: 100%;\">");
    printWindow.document.writeln(dateStr);
    printWindow.document.writeln("<ul>");
    
    printNode(top, printWindow);
    
    printWindow.document.writeln("</ul>");
    printWindow.document.writeln("</body>");
    printWindow.document.writeln("</html>");
    printWindow.document.close();
    return;
}

/*
 * Debug routine  used to print the frame hierarchy
 */
function printNode(node, treeWindow)
{
    treeWindow.document.writeln("<li>");
    
    if( node == top )
    {
        treeWindow.document.writeln("name: top");
    }
    else if( node.name )
    {
        treeWindow.document.writeln("name: " + node.name);
    }
    else
    {
        treeWindow.document.writeln("name: (no name)");
    }
    treeWindow.document.writeln("<br>");
    if( node.parent == top )
    {
        treeWindow.document.writeln("parent: top");
    }
    else
    {
        treeWindow.document.writeln("parent: " + node.parent.name);
    }
    treeWindow.document.writeln("<br>");
    treeWindow.document.writeln("location.href: " + node.location.href);
    treeWindow.document.writeln("<br>");
    // treeWindow.document.writeln("document.URL: " + node.document.URL);
    // treeWindow.document.writeln("<br>");
    treeWindow.document.writeln("frames: " + node.frames.length);
    treeWindow.document.writeln("<br>");
    if( node.frames.length > 0 )
    {
        treeWindow.document.writeln("<ul>");
        for( var i = 0; i < node.frames.length; i++ )
        {
            printNode(node.frames[i], treeWindow);
        }
        treeWindow.document.writeln("</ul>");
    }
    treeWindow.document.writeln("</li>");
    return;
}