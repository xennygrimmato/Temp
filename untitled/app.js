var my_app = angular.module('my_app', ['ngRoute', 'ngCookies', 'ngStorage']);

my_app.config(function($routeProvider) {

    $routeProvider

        // home page
        .when('/', {
            templateUrl: 'home.html',
            controller: 'mainController'
        })

        // product details page
        .when('/product:product_id', {
            templateUrl: 'product_details.html',
            controller: 'productController'
        })

        .when('/view-cart', {
            templateUrl: 'view_cart.html',
            controller: 'viewCartController'
        })

});

my_app.controller('mainController', function($scope, $http, $cookies) {
    $http.get("http://127.0.0.1:8000/api/products/").then(function(response) {
        $scope.product_data = response.data;
        console.log(response.data);
    });

    $http.get("http://127.0.0.1:8000/api/categories/").then(function(response) {
        $scope.categories = response.data;
        console.log(response.data);
    });

    // Function to check the selected category id for filtering
    $scope.filter_by_category = function (category_id) {
        $scope.filter_category = category_id;
    }
});

my_app.controller('productController', function($scope, $routeParams, $http, $cookies) {

    $scope.product_id = $routeParams.product_id;
    $http.get("http://127.0.0.1:8000/api/products/" + $scope.product_id).then(function(response) {
        $scope.product = response.data;
        console.log(response.data);
    });

    // bind add to cart button with cookies
    $scope.add_product_to_cart = function(quantity) {
        var product = $scope.product;
        var qty = quantity;
        console.log(qty);

        // check if product with code exists in map or not
        // check for key through console
        if($cookies.getObject("cart") == null) {
            $cookies.putObject("cart", {});
            // add order via ajax
            var url = "http://127.0.0.1:8000/api/orders/";
            var parameters = {username: "____", address:"____", status: "Created"};
            $http.post(url, parameters).
            success(function(data, status, headers, config) {
                // this callback will be called asynchronously
                // when the response is available
                console.log(data);
                // add order_id to cart as it will be used during checkout
                var order_id = data["id"];
                alert("New order created! Order ID: " + order_id);
                var cart = $cookies.getObject("cart");
                $cookies.putObject("cart", cart);
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                console.log("Error log: " + data);
                console.log("Error status: " + status);
            });
        }

        var cart = $cookies.getObject("cart");

        console.log(cart);

        if(cart[product["id"]] != null) {
            // increment quantity
            cart[product["id"]] += parseInt(qty);
        }
        else {
            // set quantity
            cart[product["id"]] = parseInt(qty);
        }
        if(parseInt(qty) > 1)
            alert(qty + " items of Product " + product["id"] + " added to cart!");
        else
            alert(qty + " item of Product " + product["id"] + " added to cart!");
        $cookies.putObject("cart", cart);
        console.log($cookies.getObject("cart"));
    }

});

my_app.controller('viewCartController', function($scope, $http, $cookies, $localStorage) {
    $scope.cart_items = $cookies.getObject("cart");
    var cart = $scope.cart_items;
    console.log(cart);

    var items = [];

    for(var key in cart) {
        if(!(key == "order_id")) {
            console.log("Initiated fetching products/" + key);
            $http.get("http://127.0.0.1:8000/api/products/" + key).then(function(response) {
                var prod = response.data;
                prod["quantity"] = parseInt(cart[key]);
                prod["total"] = parseInt(cart[key]) * parseFloat(prod.price);
                items.push(prod);
                console.log(prod);
            });
        }
    }

    $scope.cart_items = items;

});