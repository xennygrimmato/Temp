function Hello($scope, $http) {
    $http.get('http://127.0.0.1/api/products/').
        success(function(data) {
            console.log(data);
            $scope.greeting = data[0];
        });
}
