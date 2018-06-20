(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('MagazineMySuffixDetailController', MagazineMySuffixDetailController);

    MagazineMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Magazine', 'StorageShoes', 'Employees'];

    function MagazineMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Magazine, StorageShoes, Employees) {
        var vm = this;

        vm.magazine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('headApp:magazineUpdate', function(event, result) {
            vm.magazine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
