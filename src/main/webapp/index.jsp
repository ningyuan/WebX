<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<html>
<body>
<h2>Hello World!</h2>
<h5><%= request.getSession().getAttribute("index") %></h5>
</body>
</html>
