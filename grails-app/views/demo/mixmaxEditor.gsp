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
  <script defer src="https://d1xa36cy0xt122.cloudfront.net/v1/Mixmax.js"></script>
  <style>
    html, body {
      height: 100%;
      overflow: auto;
    }
    #filesutraFrame {
      border: 0; position:fixed; top:0; left:0; right:0; bottom:0; width:100%; height:100%
    }
  </style>
</head>

<body>
<iframe id="filesutraFrame"></iframe>
<script>
  filesutra.importFiles(function(data) {
    Mixmax.done(data);
  }, {
    dialogType: 'iframe',
    parentId: 'filesutraFrame'
  });
</script>
</body>
</html>