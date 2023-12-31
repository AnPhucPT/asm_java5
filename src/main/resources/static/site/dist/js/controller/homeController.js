var app = angular.module('app', []);
app.controller('homeController', function ($scope) {
    $scope.products = getCartFromLS();
    $scope.getTotalQuantity = () => {
        return $scope.products.reduce((acc, curr) => acc + curr.quantity, 0);
    };
    $scope.user = JSON.parse(localStorage.getItem('user')) || null;
    $scope.signOut = () => {
        localStorage.removeItem('access_token');
        localStorage.removeItem('user');
        $scope.user = null;
        window.open('http://localhost:8080/', '_self');
    };
});
