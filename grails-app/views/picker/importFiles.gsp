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
    .filesPane {
      max-height: 400px;
      overflow: auto;
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
          <a ng-if="isConnected('Google')" ng-click="logout('Google')" class="pull-right">logout</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('Dropbox')">Dropbox</a>
          <a ng-if="isConnected('Dropbox')" ng-click="logout('Dropbox')" class="pull-right">logout</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('Box')">Box</a>
          <a ng-if="isConnected('Box')" ng-click="logout('Box')" class="pull-right">logout</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('OneDrive')">OneDrive</a>
          <a ng-if="isConnected('OneDrive')" ng-click="logout('OneDrive')" class="pull-right">logout</a>
        </li>
      </ul>
    </div>
    <div class="col-md-9 filesPane">
      <div>
        <div ng-if="app!=undefined">
          <div ng-if="!isConnected(app)">
            <a class="btn btn-primary" href="/login/{{app.toLowerCase()}}">Connect {{app}}</a>
          </div>
          <div ng-if="isConnected(app)">
            <div ng-if="!items || items.length == 0" style="text-align: center;">
              Loading...
            </div>
            <div ng-repeat="item in items">
              <i ng-class="item.type == 'file' ? 'glyphicon glyphicon-file' : 'glyphicon glyphicon-folder-open'"></i> {{item.name}}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>