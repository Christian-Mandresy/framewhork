<%--
  Created by IntelliJ IDEA.
  User: ITU
  Date: 23/02/2022
  Time: 09:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1><%= request.getAttribute("Nom") %></h1>
<h1><%= request.getAttribute("Nomnombre") %></h1>


<% String[] list=(String[]) request.getAttribute("NomFichier") ; %>
<% for(int i=0;i<list.length;i++) {%>
<tr>
    <td> <%= list[i]  %> </td>
</tr>
<% }%>

</body>
</html>
