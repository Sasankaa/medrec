<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@	page import="com.bea.help.search.SearchHit" %>

<head>
	<link rel="StyleSheet" href="../docs/css/wls.css" type="text/css"/>
</head>

<% 
SearchHit[] searchHits = (SearchHit[])request.getAttribute("searchHits");
int numResults = 0;
if (searchHits != null) numResults= searchHits.length;

%>

<body topmargin=0 leftmargin=0 rightmargin=0 marginwidth=0 marginheight=0 bgcolor="#FFFFFF">
	<p style="font-size:12px;color:000000;font-family: Tahoma;text-decoration:none">
    <%=numResults%> documents found:</p>
	<table border=0 cellspacing=0 cellpadding=0>
		<%
			for (int i=0; i<numResults; i++) {
				// All URLs are relative to core direcotry.
                String aUrl = null;                
                if(searchHits[i].getUrl().startsWith("/"))
                    aUrl = "core/.." + searchHits[i].getUrl() + "?skipReload=true";
                else
				    aUrl = "core/" + searchHits[i].getUrl() + "?skipReload=true";
		%>
			<tr>
				<td nowrap>
					<a style="font-size:12px;color:000000;font-family: Tahoma;text-decoration:none" href="<%=aUrl%>" 
						target="myContent" title="<%=searchHits[i].getTitle()%>"
						onMouseOver="status='<%=aUrl%>'; return true;"><%=searchHits[i].getTitle()%></a>
				</td>
			</tr>
		<%
			}
		%>
		</table>
</body>

