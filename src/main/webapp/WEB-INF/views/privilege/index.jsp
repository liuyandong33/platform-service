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
        var interval = undefined;
        var zTreeObj = undefined;
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

            $.get("../privilege/listBackgroundPrivileges", {}, function (result) {
                if (result["successful"]) {
                    var treeNodes = result["data"];
                    zTreeObj = $.fn.zTree.init($("#tree"), setting, treeNodes);
                    zTreeObj.expandAll(true);
                    var selectedNode = zTreeObj.getNodeByParam("id", "17", null);
                    if (selectedNode && !selectedNode["isParent"]) {
                        zTreeObj.checkNode(selectedNode, true, true);
                    }
                }
            }, "json");

            startPolling();

            window.setTimeout(function () {
                stopPolling();
            }, 5000);
        });

        function startPolling() {
            interval = window.setInterval(function () {
                console.log(123);
            }, 500);
        }

        function stopPolling() {
            if (interval) {
                window.clearInterval(interval);
            }
        }
        
        function saveRole() {
            var checkedNodes = zTreeObj.getCheckedNodes(true);
            var checkedNodeIds = [];
            var length = checkedNodes.length;
            for (var index = 0; index < length; index++) {
                checkedNodeIds.push(checkedNodes[index]["id"]);
            }
            alert(checkedNodeIds.join(","));
        }
    </script>
</head>
<body>
<div id="tree" class="ztree"></div>
<button onclick="saveRole();">保存</button>
</body>
</html>
