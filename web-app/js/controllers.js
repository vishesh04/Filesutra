var filesutraControllers = angular.module("filesutraControllers", ["filesutraServices"]);

filesutraControllers.controller("AppCtrl", ['$scope', '$http', "fileService", function($scope, $http, fileService) {
    $scope.selectApp = function(app) {
      $scope.app = app;
      $scope.items = fileService.getItems(app, null, function(items) {
        $scope.items = items;
      });
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
      $scope.items = [];
    }
}]);