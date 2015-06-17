<%--
  Created by IntelliJ IDEA.
  User: vishesh
  Date: 05/05/15
  Time: 7:31 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html ng-app="filesutraApp">
<head>
  <title>File Sutra</title>

  <link rel="stylesheet" href="/css/bootstrap.min.css">

  <script src="/js/lib/jquery.min.js"></script>
  <script src="/js/lib/angular.min.js"></script>
  <script src="/js/lib/angular-route.min.js"></script>
  <script src="/js/app.js"></script>
  <script src="/js/controllers.js"></script>
  <script src="/js/services.js"></script>

  <style>
    li a {
      cursor: pointer;
    }
  </style>
</head>

<body>

<div class="container" style="padding: 10px">
  <div class="row" ng-controller="AppCtrl" ng-init="init(${appSettings})">
    <div class="col-md-3">
      <ul class="list-group">
        <li class="list-group-item">
          <a ng-click="selectApp('Google')">Google Drive</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('Dropbox')">Dropbox</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('Box')">Box</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('OneDrive')">OneDrive</a>
        </li>
      </ul>
    </div>
    <div class="col-md-9">
      <div>
        <div ng-if="app!=undefined">
          <div ng-if="!isConnected(app)">
            <a class="btn btn-primary" href="/login/{{app.toLowerCase()}}">Connect {{app}}</a>
          </div>
          <div ng-if="isConnected(app)">
            Files and folders from {{app}}
            <div ng-repeat="item in items">
              {{item.kind}} - {{item.title}}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>