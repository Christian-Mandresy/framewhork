<%-- 
    Document   : Test.jsp
    Created on : 31 août 2021, 13:01:52
    Author     : ITU
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%= request.getAttribute("mety") %></h1>
    </body>
</html>
