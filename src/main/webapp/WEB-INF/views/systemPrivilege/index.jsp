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
    <link type="text/css" rel="stylesheet" href="../libraries/zTree_v3-v3.5.16/css/metroStyle/metroStyle.css">
    <script type="text/javascript" src="../libraries/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="../libraries/zTree_v3-v3.5.16/js/jquery.ztree.all-3.5.js"></script>
    <script type="text/javascript">
        $(function () {
            var setting = {
                view: {
                    selectedMulti: false,
                    expandSpeed: "fast"
                },
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "pId",
                        rootId: ""
                    },
                    key: {
                        name: 'name'
                    }
                },
                check: {
                    enable: true,
                    chkStyle: "checkbox"
                }
            };

            $.get("../systemPrivilege/findAll", {}, function (result) {
                if (result["successful"]) {
                    var treeNodes = result["data"];
                    var zTree = $.fn.zTree.init($("#tree"), setting, treeNodes);
                }
            }, "json");
        });
    </script>
</head>
<body>
<div id="tree" class="ztree"></div>
</body>
</html>
