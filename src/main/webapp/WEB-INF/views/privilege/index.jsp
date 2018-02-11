<%--
  Created by IntelliJ IDEA.
  User: liuyandong
  Date: 2017-12-29
  Time: 10:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../libraries/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        function submit() {
            var form = $('<form method="post" id="form" action="../privilege/index"><input type="text" name="name" value="name"><input type="password" name="password" value="password"></form>');
            form.css({"display": "none"});
            form.appendTo("body");
            form.submit();
        }
    </script>
</head>
<body>
<button onclick="submit();">aa</button>
</body>
</html>
