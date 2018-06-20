(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('StorageShoesMySuffixDetailController', StorageShoesMySuffixDetailController);

    StorageShoesMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StorageShoes', 'GenericShoes', 'Magazine'];

    function StorageShoesMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, StorageShoes, GenericShoes, Magazine) {
        var vm = this;

        vm.storageShoes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('headApp:storageShoesUpdate', function(event, result) {
            vm.storageShoes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
