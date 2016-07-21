var app = angular.module('my_app', []);
app.controller('products_controller', function($scope, $http) {


  $scope.status_show_all_products = true;
  $http.get("http://127.0.0.1:8000/api/products/").then(function(response) {
    $scope.names = response.data;
    console.log(response.data);
  });

  $scope.toggle = function() {
    $scope.other_status = true;
    $scope.status_show_all_products = false;
    $http.get("http://127.0.0.1:8000/api/products/1").then(function(response) {
      $scope.specific_data = response.data;
      console.log(response.data);
    });
  }



});
