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
        function doPay() {
            var paidScene = 7;
            $.post("../order/doPay", {orderInfoId: 1, userId: 1, paidScene: paidScene}, function (result) {
                if (result["successful"]) {
                    var data = result["data"];
                    if (paidScene == 1) {

                    } else if (paidScene == 2) {

                    } else if (paidScene == 7 || paidScene == 8) {
                        var index = data.lastIndexOf("<script>");
                        var form = $(data.substring(0, index));
                        form.css({"display": "none"});
                        form.appendTo("body");
                        form.submit();
                    }
                } else {
                    alert(result["error"]);
                }
            }, "json");
        }
    </script>
</head>
<body>
<button onclick="doPay();">aa</button>
</body>
</html>
