<%--
  Created by IntelliJ IDEA.
  User: liuyandong
  Date: 2018-06-05
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../libraries/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        function upload() {
            $.get("../oss/obtainPolicy", {}, function (result) {
                if (result["successful"]) {
                    var data = result["data"];
                    var formData = new FormData();
                    formData.append("key", "user-dir/fhc03.jpg");
                    formData.append("policy", data["policy"]);
                    formData.append("OSSAccessKeyId", data["accessid"]);
                    formData.append("success_action_status", 200);
                    formData.append("callback", data["callback"]);
                    formData.append("signature", data["signature"]);
                    formData.append("file", $("#file")[0]["files"][0]);
                    $.ajax({
                        type : "post",
                        url : data["host"],
                        data : formData,
                        cache : false,
                        processData : false,
                        contentType : false,
                        success: function (result) {
                            alert(JSON.stringify(result))
                        }
                    });
                } else {
                    alert(result["error"]);
                }
            }, "json");
        }
    </script>
</head>
<body>
    <input type="file" name="file" id="file" style="display: none;">
    <button onclick="$('#file').click()">选择文件</button>
    <button onclick="upload()">上传</button>
</body>
</html>
