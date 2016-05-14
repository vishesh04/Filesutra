var filesutraControllers = angular.module("filesutraControllers", ["filesutraServices"]);

filesutraControllers.controller("AppCtrl", ['$scope', '$http', '$location', "fileService", "authService",
  function($scope, $http, $location, fileService, authService) {
    $scope.selectApp = function(app) {
      $scope.runningApp = app;
      $location.path(app);
    }

    $scope.login = function(app) {
      var redirectUrl = '/auth/' + (app == 'AmazonCloudDrive'? 'amazon' : app.toLowerCase());
      if (window.opener) {
        window.location = redirectUrl;
      } else {
        var oAuthWndow = window.open(redirectUrl, "Filesutra", "width=800, height=600, top=100, left=300");
        var interval = window.setInterval(function() {
          if (oAuthWndow.location.href.indexOf('picker') != -1) {
            oAuthWndow.close();
            location.reload();
          }
        }, 1000);
      }
    }

    $scope.logout = function(app) {
      var connectedAppPos = $scope.appSettings.connectedApps.indexOf(app)
      if (connectedAppPos != -1) {
        authService.logout(app, function(data) {
          if (data.success) {
            $scope.appSettings.connectedApps.splice(connectedAppPos, 1);
            $location.path(app);
          }
        });
      }
    }

    $scope.isConnected = function(app) {
     // console.log($scope.appSettings.connectedApps);
      if ($scope.appSettings.connectedApps.indexOf(app) != -1) {
        return true;
      } else {
        return false;
      }
    }

    $scope.selectItem = function (item) {
      if (item.type == "folder") {
        $location.path($location.path()+'/'+item.id);
      }
      $scope.selectedItem = item;
    }

    $scope.import = function() {
      fileService.import($scope.app, $scope.selectedItem, function(data) {

        var message = {
          type  : 'filesutra',
          data   :  data
        }
        if (window.opener) {
          window.opener.postMessage(message, '*');
          window.close();
        } else {
          // iframe
          parent.postMessage(message, '*');
        }
      });
    }
    $scope.zippy = function(){
        filesutra.importFiles(function(data) {
});
    }

    $scope.init = function(appSettings){
      $scope.appSettings = appSettings;
    }

    $scope.$on("$locationChangeSuccess", function (event, newUrl) {
      $scope.gettingList(0);
    });

    $scope.gettingList = function(code){
      $scope.showButton = false;
            var path = $location.path();
      var chunks = path.split("/");
      var app, folderId;

      if (chunks.length < 2) {
        $scope.selectApp("Google");
        return;
      } else {
        app = chunks[1];
        $scope.app = app;
              $scope.runningApp = app;

      }
      if (chunks.length > 2) {
        folderId = chunks[chunks.length - 1];
      }
      
      if($scope.app == "Facebook"){
        
      if(code==0){
        delete $scope.items;
      $scope.afterTokenVal = '';

        if ($scope.isConnected(app)) {
           fileService.getItems(app, folderId, $scope.afterTokenVal, function (items) {
            //delete $scope.items;
             $scope.items = [];
             $scope.afterTokenVal = items.afterval;
             if(items.listresponse.length < 25){
                    $scope.showButton = false;

             }else{
              $scope.showButton = true;
             }
             for(var i=0; i< items.listresponse.length;i++){
               $scope.items.push(items.listresponse[i]);
             }
            //$scope.items.push(items.listresponse);
          });
        }
       }else{
        fileService.getItems(app, folderId, $scope.afterTokenVal, function (items) {
                        $scope.afterTokenVal = items.afterval;
                        if(items.listresponse.length < 25){
                    $scope.showButton = false;

             }else{
              $scope.showButton = true;
             }
                       for(var i=0; i< items.listresponse.length;i++){
               $scope.items.push(items.listresponse[i]);
             }
          });

       }
     } else {
      if ($scope.isConnected(app)) {
        delete $scope.items;
        $scope.items = null
           fileService.getListItems(app, folderId, function (items) {
            $scope.items = items;
            });
         }
     }
      
    }
}]);