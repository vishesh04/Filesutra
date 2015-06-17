<%--
  Created by IntelliJ IDEA.
  User: vishesh
  Date: 15/06/15
  Time: 12:31 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>File Sutra Demo</title>
  <script src="/js/lib/jquery.min.js"></script>
  <script src="/js/filesutra.js"></script>
</head>

<body>
<div style="text-align: center; margin-top: 150px">
  <button id="importBtn">Import Files</button>
</div>
<script>
  $("#importBtn").click(function() {
    window.open("/picker", "Filesutra", "width=800, height=600, top=100, left=300")
  });
</script>
</body>
</html>