(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('EmployeesMySuffixDetailController', EmployeesMySuffixDetailController);

    EmployeesMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employees', 'Roles', 'Magazine'];

    function EmployeesMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Employees, Roles, Magazine) {
        var vm = this;

        vm.employees = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('headApp:employeesUpdate', function(event, result) {
            vm.employees = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
