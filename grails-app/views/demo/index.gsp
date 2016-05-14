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
  <link rel="stylesheet" href="/css/bootstrap.min.css">
  <style>
    body {
      margin-top: 100px;
    }
    #filesutraFrame {
      margin: 20px 0px 0px 20px;
      height: 500px;
      border: none;
    }
  </style>
</head>

<body>
<div class="container">
  <div class="row">
    <a id="importBtnPopup" class="btn btn-default col-md-offset-4">Import Files (Popup)</a>
    <a id="importBtnIframe" class="btn btn-default">Import Files (Iframe)</a>
  </div>
  <div class="row frame-container">
    <iframe id="filesutraFrame" class="col-md-12"></iframe>
  </div>
</div>
<script>
  $("#importBtnPopup").click(function() {
    filesutra.importFiles(function(data) {
      console.log(data);
    });
  });

  $("#importBtnIframe").click(function() {
    filesutra.importFiles(function(data) {
      console.log(data);
    }, {
      dialogType: 'iframe',
      parentId: 'filesutraFrame'
    });
  });
</script>
</body>
</html>