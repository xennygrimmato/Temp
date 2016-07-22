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

        .when('/contact', {
            templateUrl: 'contact.html',
            controller: 'contactController'
        })

        .when('/checkout', {
            templateUrl: 'checkout.html',
            controller: 'checkoutController'
        })

        .when('/summary', {
            templateUrl: 'final_summary.html',
            controller: 'summaryController'
        })

});

my_app.controller('mainController', function($scope, $http, $cookies) {

    if($cookies.get("items") == null) {
        $cookies.put("items", 0);
    }

    if($cookies.getObject("prices") != null) {
        $cookies.putObject("prices");
    }

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

    $scope.count_items = function() {
        console.log("inside count()");
        var val = $cookies.get("items");
        console.log(val);
        return $cookies.get("items");
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
                $cookies.put("order_id", order_id);
                //alert("New order created! Order ID: " + order_id);
                var cart = $cookies.getObject("cart");
                $cookies.putObject("cart", cart);

                if(quantity != undefined) {
                    if (quantity > 0)
                        var counter = parseInt($cookies.get("items"));
                    counter += 1;
                    $cookies.put("items", counter);
                }
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
            if(parseInt(qty) != undefined) {
                if (parseInt(qty) > 0) {
                    var tmp = parseInt($cookies.getObject("items"));
                    tmp += 1;
                    $cookies.putObject("items", tmp);
                }
            }
        }

        //if(qty == undefined || parseInt(qty) <= 0) {
        //    alert("Please enter a valid quantity!");
        //} else
        if(parseInt(qty) > 1) {
            alert(qty + " items of Product " + product["code"] + " added to cart!");
        }
        else {
            alert(qty + " item of Product " + product["code"] + " added to cart!");
        }
        $cookies.putObject("cart", cart);
        console.log($cookies.getObject("cart"));


        var cached_prices = $cookies.getObject("price");
        if(cached_prices == null) {
            $cookies.putObject("price", {});
            cached_prices = {};
        }
        cached_prices[product["id"]] = parseFloat(parseFloat(product["price"]));
        $cookies.putObject("price", cached_prices);
    }

});

my_app.controller('viewCartController', function($scope, $http, $cookies) {
    $scope.cart_items = $cookies.getObject("cart");
    var cart = $scope.cart_items;
    console.log(cart);

    var items = [];

    for(var key in cart) {
        if (!cart.hasOwnProperty(key)) continue;
        if(key == "order_id") continue;
        if(!(key == "order_id")) {
            console.log("key = " + key);
            console.log("qty = " + cart[key]);
            console.log("Initiated fetching products/" + key);
            $http.get("http://127.0.0.1:8000/api/products/" + key).then(function(response) {
                var prod = response.data;
                prod["qty"] = parseInt(cart[key]);
                prod["total"] = parseFloat(cart[key]) * parseFloat(prod.price);
                console.log(cart[key] + ", " + prod["total"]);
                items.push(prod);
                console.log(prod);
            });
        }
    }
    console.log($scope.cart_items);
    $scope.cart_items = items;

});

my_app.controller('contactController', function($scope, $http, $cookies, $window) {
    $scope.submit_contact_form = function() {
        var uname = $scope.username;
        var msg = $scope.message;
        var url = "http://127.0.0.1:8000/api/contact/";
        var parameters = {"uname": uname, "msg": msg};
        $http.post(url, parameters).
        success(function(data, status, headers, config) {
            console.log(data);
            alert("Thank you! We appreciate your feedback.");
        }).
        error(function(data, status, headers, config) {
            alert("Thank you! We appreciate your feedback.");
        });
        $window.location="/untitled/index.html";
    }
});

my_app.controller('checkoutController', function($scope, $http, $cookies, $window) {
    $scope.perform_checkout = function() {
        var uname = $scope.username;
        var addr = $scope.address;
        var order_id = $cookies.get("order_id");

        $scope.cart_items = $cookies.getObject("cart");
        var cart = $scope.cart_items;
        console.log("cart = " + cart);
        var order_id = $cookies.get("order_id");
        var price_cookie = $cookies.getObject("price");
        console.log("price_cookie = " + price_cookie);
        console.log(price_cookie);

        for(var key in cart) {
            price_ = price_cookie[parseInt(key)];
            //alert(price_);
            console.log(price_);
            url = "http://127.0.0.1:8000/api/orders/" + order_id + "/orderlineitem/";
            parameters = {"product_id": key, "price": price_};
            $http.post(url, JSON.stringify(parameters)).then(function (response) {
                console.log(response);
                // delete cookie
                //$cookies.remove("cart");
            });
        }

    }
});

my_app.controller('summaryController', function($scope, $http, $cookies, $window) {

});