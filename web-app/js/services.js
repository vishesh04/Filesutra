var filesutraServices = angular.module("filesutraServices", []);

filesutraServices.factory("fileService", ['$http', function($http) {
  return {
    getItems: function(app, folderId, callback) {
      $http.get('/api/files/'+app.toLowerCase()).success(function(data) {
        callback(data);
      });
    }
  }
}]);