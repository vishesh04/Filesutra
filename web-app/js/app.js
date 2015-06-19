var filesutraApp = angular.module("filesutraApp", [
  'filesutraControllers'
]);

filesutraApp.config(function($locationProvider) {
  $locationProvider.html5Mode({
    enabled: false,
    requireBase: true
  });
});