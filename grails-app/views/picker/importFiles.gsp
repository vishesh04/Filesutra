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
  <link rel="shortcut icon" href="/images/favicon.ico" />

  <link rel="stylesheet" href="/css/bootstrap.min.css">

  <script src="/js/lib/jquery.min.js"></script>
  <script src="/js/lib/angular.min.js"></script>
  <script src="/js/lib/angular-route.min.js"></script>
  <script src="/js/app.js"></script>
  <script src="/js/controllers.js"></script>
  <script src="/js/services.js"></script>
  <script src="http://filesutra.com/js/filesutra.js"></script>

  <style>
    li a {
      cursor: pointer;
    }
    .filesPane {
      min-height: 400px;
      max-height: 400px;
      overflow: auto;
    }
    .list-group-item1 {
    height: 150px;
    width: 150px;
    margin: 0.5cm;
    }
    .selectedItem {
      background-color: #46b8da;
    }
    .filesutraItem {
      cursor: pointer;
    }
    .action-group{
      width: 100%;
      position: absolute;
      bottom: 90px;
      margin-left: 30px;
    }
    .list-group-item1{
        position: relative;
    display: block;
    padding: 10px 15px;
    border: 1px solid #ddd;

    }
    .imgContainer{
    float:left;
}
    .import-btn {
      margin-right: 20px;
    }
  </style>
</head>

<body>

<div class="container" style="padding: 10px">
  <div class="row" ng-controller="AppCtrl" ng-init="init(${appSettings})">
    <div class="col-md-3 col-sm-3">
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
        <li class="list-group-item">
          <a ng-click="selectApp('AmazonCloudDrive')">Amazon Cloud Drive</a>
          <a ng-if="isConnected('AmazonCloudDrive')" ng-click="logout('AmazonCloudDrive')" class="pull-right">logout</a>
        </li>
        <li class="list-group-item">
          <a ng-click="selectApp('Facebook')">Facebook</a>
          <a ng-if="isConnected('Facebook')" ng-click="logout('Facebook')" class="pull-right">logout</a>
        </li>
      </ul>
    </div>
    <div class="col-md-6 col-sm-6">
    <div class="row filesPane">
        <div>
          <div ng-if="app!=undefined">
            <div ng-if="!isConnected(app)" style="text-align: center; margin-top: 40px">
              <a class="btn btn-primary" ng-click="login(app)">
                Connect {{app=='AmazonCloudDrive'? 'Amazon Cloud Drive' : app}}</a>
            </div>
            <div ng-if="isConnected(app)">
              <div ng-if="!items" style="text-align: center;">
                Loading...
              </div>
              <div ng-if="items.length == 0" style="text-align: center;">
                No Files or Folders
              </div>
              
              <div ng-if="runningApp !='Facebook'" ng-repeat="item in items" >
                <div class="filesutraItem" ng-click="selectItem(item)"
                     ng-class="item.id == selectedItem.id && item.type != 'folder' ? 'selectedItem' : ''">
                  <i ng-class="item.type == 'file' ? 'glyphicon glyphicon-file' : 'glyphicon glyphicon-folder-open'"></i> {{item.name}}
                </div>
              </div>

              <div ng-if="isConnected('Facebook') && runningApp =='Facebook'"  >
                <div ng-repeat="item in items">
                  <div class="filesutraItem" ng-if="item.type != 'file'" ng-click="selectItem(item)"
                       ng-class="item.id == selectedItem.id && item.type != 'folder' ? 'selectedItem' : ''">
                    <i ng-class="item.type == 'file' ? 'glyphicon glyphicon-file' : 'glyphicon glyphicon-folder-open'"></i> {{item.name}}
                  </div>
                  <div class="imgContainer" ng-if="item.type == 'file'">
                    
                      <ul class="list-group" ng-click="selectItem(item)">
                        <li class="list-group-item1" ng-class="item.id == selectedItem.id && item.type != 'folder' ? 'selectedItem' : ''">

                          <img  src="{{item.iconurl}}" height=70px width:130px style="margin-top: 23px;">
                       </li>
                      </ul>

                  </div>

                  <a class="btn btn-primary pull-right" ng-show="$last && showButton" ng-if="app && isConnected(app) && item.type == 'file'"  ng-click="gettingList(1)">Load More</a>
                </div>
                
              </div>

            </div>
          </div>
        </div>
        
      </div>
      <div class="row">

        <a class="btn btn-primary pull-right import-btn" ng-if="app && isConnected(app)"
           ng-disabled="!selectedItem || selectedItem.type == 'folder'" ng-click="import()">Import</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>