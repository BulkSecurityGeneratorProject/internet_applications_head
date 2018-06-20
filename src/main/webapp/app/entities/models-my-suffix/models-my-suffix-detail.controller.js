(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('ModelsMySuffixDetailController', ModelsMySuffixDetailController);

    ModelsMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Models', 'GenericShoes'];

    function ModelsMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Models, GenericShoes) {
        var vm = this;

        vm.models = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('headApp:modelsUpdate', function(event, result) {
            vm.models = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
