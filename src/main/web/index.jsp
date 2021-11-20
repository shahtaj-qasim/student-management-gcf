<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 11/19/2021
  Time: 3:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="functions.HelloWorld" %>
<% HelloWorld msg = new HelloWorld(); %>
<html>
  <head>
    <title>Student Management Application</title>
  </head>
  <body>
  <%= msg.getMsg() %>
  </body>
</html>
