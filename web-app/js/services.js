var filesutraServices = angular.module("filesutraServices", []);

filesutraServices.factory("fileService", ['$http', function($http) {
  return {
    getItems: function(app, folderId, after, callback) {
      var endpoint = '/api/files/'+app.toLowerCase()
      if (folderId) {
        endpoint += '?folderId='+folderId+'&'+'after='+after;
      }
      $http.get(endpoint).success(function(data) {
        callback(data);
      }).error(function(err){
        console.log('error');
      });
    },
    getListItems: function(app, folderId, callback) {
      var endpoint = '/api/files/'+app.toLowerCase()
      if (folderId) {
        endpoint += '?folderId='+folderId;
      }
      $http.get(endpoint).success(function(data) {
        callback(data);
      });
    },
    import : function(app, item, callback) {
      var endpoint = '/api/import/'+app.toLowerCase();
      $http.post(endpoint, {fileId: item.id, fileName: item.name, size: item.size})
        .success(function(data) {
          callback(data);
      });
    }
  }
}]);

filesutraServices.factory("authService", ['$http', function($http) {
  return {
    logout: function(app, callback) {
      $http.get('/auth/logout/?app='+app).success(function(data) {
        callback(data);
      });
    }
  }
}]);