(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('RolesMySuffixDetailController', RolesMySuffixDetailController);

    RolesMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Roles', 'Employees'];

    function RolesMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Roles, Employees) {
        var vm = this;

        vm.roles = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('headApp:rolesUpdate', function(event, result) {
            vm.roles = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
