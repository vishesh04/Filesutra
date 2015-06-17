var filesutraControllers = angular.module("filesutraControllers", ["filesutraServices"]);

filesutraControllers.controller("AppCtrl", ['$scope', '$http', "fileService", function($scope, $http, fileService) {
    $scope.selectApp = function(app) {
      $scope.app = app;
      if ($scope.isConnected($scope.app)) {
        $scope.items = fileService.getItems(app, null, function(items) {
          $scope.items = items;
        });
      }
    }

    $scope.logout = function(app) {
      var connectedAppPos = $scope.appSettings.connectedApps.indexOf(app)
      if (connectedAppPos != -1) {
        $scope.appSettings.connectedApps.splice(connectedAppPos, 1)
      }
    }

    $scope.isConnected = function(app) {
      if ($scope.appSettings.connectedApps.indexOf(app) != -1) {
        return true;
      } else {
        return false;
      }
    }

    $scope.init = function(appSettings){
      $scope.appSettings = appSettings;
      $scope.app = "Google";
      if ($scope.isConnected($scope.app)) {
          $scope.items = fileService.getItems($scope.app, null, function(items) {
          $scope.items = items;
        });
      }
    }
}]);