<%--
  Created by IntelliJ IDEA.
  User: ITU
  Date: 23/02/2022
  Time: 09:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="http://localhost:8080/Framewhorek_war_exploded/TestFile.do" method="post" enctype="multipart/form-data" >
    <input type="text" name="izay1">
    <input type="datetime-local" name="izay2">
    <input type="text" name="izay3">
    <input type="file" name="file">
    <button type="submit">Valider</button>
</form>
</body>
</html>
