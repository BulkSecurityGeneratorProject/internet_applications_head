(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('GenericShoesMySuffixDetailController', GenericShoesMySuffixDetailController);

    GenericShoesMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GenericShoes', 'Models', 'StorageShoes'];

    function GenericShoesMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, GenericShoes, Models, StorageShoes) {
        var vm = this;

        vm.genericShoes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('headApp:genericShoesUpdate', function(event, result) {
            vm.genericShoes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
