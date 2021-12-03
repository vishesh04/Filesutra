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
  <script src="/js/jquery.form.js"></script>
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
    .selectedItem {
      background-color: #46b8da;
    }
    .color1{
      background-color: #edf3fe;
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
      height: 150px;
      width: 150px;
      margin: 0.5cm;
      display: block;
      padding: 10px 15px;
      text-align: center;
      /*border: 1px solid #ddd;*/
    }
    .bottom2 { 
      width: 250px;   
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

<div class="container submitObs" style="padding: 10px">
  <div class="row" ng-controller="AppCtrl" ng-init="init(${appSettings})">
  <br/>
    <div class="col-md-3 col-sm-3">
    <ul class="list-group">
     <li class="list-group-item" ng-click="selectApp('Local')">
          <a >My Computer</a>
        </li>
        <li class="list-group-item" ng-click="selectApp('Google')">
          <a >Google Drive</a>
          <a ng-if="isConnected('Google')" ng-click="logout('Google')" class="pull-right">logout</a>
        </li>
        <!--li class="list-group-item">
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
        </li-->
        <li class="list-group-item"  ng-click="selectApp('Facebook')">
          <a>Facebook</a>
          <a ng-if="isConnected('Facebook')" ng-click="logout('Facebook')" class="pull-right">logout</a>
        </li>
         <li class="list-group-item" ng-click="selectApp('Flickr')">
          <a >Flickr</a>
          <a ng-if="isConnected('Flickr')" ng-click="logout('Flickr')" class="pull-right">logout</a>
        </li>
        <li class="list-group-item" ng-click="selectApp('Picasa')">
          <a>Picasa</a>
          <a ng-if="isConnected('Picasa')" ng-click="logout('Picasa')" class="pull-right">logout</a>
        </li>
        <!--li class="list-group-item" ng-click="selectApp('Dropbox')">
          <a>Dropbox</a>
          <a ng-if="isConnected('Dropbox')" ng-click="logout('Dropbox')" class="pull-right">logout</a>
        </li-->
      </ul>
    </div>
    <div class="col-md-9 col-sm-9">
    <a class="btn btn-primary pull-left glyphicon glyphicon-chevron-left" ng-if="showBackButton" ng-click="backButton()"></a>
    <div class="row filesPane">
        <div>
          <div ng-if="app!=undefined ">
            <div ng-if="!isConnected(app) && runningApp !='Local'" style="text-align: center; margin-top: 40px">
              <a class="btn btn-primary" ng-click="login(app)">
                Connect {{app=='AmazonCloudDrive'? 'Amazon Cloud Drive' : app}}</a>
            </div>
             <div ng-if="!isConnected(app) && runningApp =='Local'" style="text-align: center; margin-top: 40px">
              <form id="submitIt" class="upload_resource1" method="post"  enctype="multipart/form-data" style="text-align: -moz-center;">                
                <input type="file" class="fileUploadInput btn btn-primary" style="display: -webkit-inline-box;" name="resources" id="fileUploadInput" custom-on-change="uploadFile" accept="image/*" title="Choose File" multiple/> 
              </form>                
            </div>

            <div ng-if="isConnected(app)">
              <div ng-if="!items" style="text-align: center;">
                Loading...
              </div>
              <div ng-if="items.length == 0" style="text-align: center;">
                No Files or Folders
              </div>
                <div class="list-group filesutraItem"  >
                    <a class="list-group-item" ng-class-odd="{'color1':toggleObject}" ng-repeat="item in items"  ng-if="item.type != 'file'" ng-click="selectItem(item); toggleObject = !toggleObject"
                       ng-class="itemId.indexOf(item.id) > -1 && item.type != 'folder' ? 'selectedItem' : ''">
                     <i ng-class="item.type == 'file' ? 'glyphicon glyphicon-file' : 'glyphicon glyphicon-folder-open'"></i>
                      {{item.name}}
                     <i ng-class="item.type == 'file' ? '' : 'glyphicon glyphicon-chevron-right float'" style="float:right"></i>

                      </a>
                  </div>
                  <div class="imgContainer" ng-if="item.type == 'file'"  ng-repeat="item in items">
                    
                      <ul class="list-group" ng-click="selectItem(item); toggleObject = !toggleObject">
                        <li class="list-group-item1" ng-class-odd="{'color1':toggleObject}" ng-class="itemId.indexOf(item.id) > -1 && item.type != 'folder' ? 'selectedItem' : ''" >

                          <img  ng-if="runningApp != 'Google'" src="{{item.iconurl}}" class="img-responsive img-thumbnail" style="max-height:100%">
                          <img  ng-if="runningApp == 'Google'" src="{{item.thumbnail}}" class="img-responsive img-thumbnail" style="max-height:100%">

                       <p style="overflow: hidden;white-space: nowrap;text-align: center;">{{item.name}}</p>
                       </li>
                      </ul>
                        <button id="singlebutton" name="singlebutton" ng-disabled="isDisabled" style="margin-left: 50%;" class="btn btn-primary bottom2" ng-show="$last && showButton" ng-click="gettingList(1)">{{loadMoreText}}</button> 
                  </div>
            </div>
          </div>
          
        </div>
        
      </div>
      <div class="row">

        <a class="btn btn-primary pull-right import-btn" ng-if="app && isConnected(app)"
           ng-disabled="itemId.length == 0 || selectedItem.type == 'folder'" ng-click="import()">Import</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>