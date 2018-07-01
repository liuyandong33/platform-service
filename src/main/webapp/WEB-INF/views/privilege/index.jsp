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
    <link rel="stylesheet" type="text/css" href="../libraries/zTree_v3-v3.5.16/css/metroStyle/metroStyle.css">
    <script type="text/javascript" src="../libraries/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="../libraries/zTree_v3-v3.5.16/js/jquery.ztree.all-3.5.js"></script>
    <script type="text/javascript">
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pidKey: "pId",
                    rootId: "0"
                }
            },
            view: {
                selectedMulti: false
            },
            check: {
                enable: true,
                chkStyle: "checkbox"
            }
        };
        $(function () {
            $.get("../privilege/listBackgroundPrivileges", {}, function (result) {
                if (result["successful"]) {
                    var zTreeNodes = result["data"];
                    var zTreeObj = $.fn.zTree.init($("#zTree"), setting, zTreeNodes);
                } else {
                    alert(result["error"]);
                }
            }, "json");
        });
    </script>
</head>
<body>
<div id="zTree" class="ztree"></div>
</body>
</html>
