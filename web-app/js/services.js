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
         callback("error");
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
      /*var endpoint = '/api/import/'+app.toLowerCase();
      $http.post(endpoint, {fileId: item.id, fileName: item.name, size: item.size, mimetype: item.mimetype, fileurl: item.iconurl})
        .success(function(data) {
          callback(data);
      });*/
      var importedFile = {"filename": item.name,"url": item.iconurl,"size": item.size,"mimetype": item.mimetype}
      callback(importedFile);
    }
  }
}]);

filesutraServices.factory("authService", ['$http', function($http) {
  return {
    logout: function(app, callback) {
      $http.get('/auth/logout/?app='+app).success(function(data) {
        console.log(data);
        callback(data);
      });
    }
  }
}]);

filesutraServices.directive('customOnChange', function() {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var onChangeHandler = scope.$eval(attrs.customOnChange);
      element.bind('change', onChangeHandler);
    }
  };
});
