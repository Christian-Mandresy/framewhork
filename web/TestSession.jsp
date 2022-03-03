<%--
  Created by IntelliJ IDEA.
  User: ITU
  Date: 02/03/2022
  Time: 11:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <p> <% HttpSession session1 =request.getSession() ;%> </p>
    <p><%= session1.getAttribute("password")%> </p>
</body>
</html>
