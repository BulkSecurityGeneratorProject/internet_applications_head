(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('EmployeesMySuffixDeleteController',EmployeesMySuffixDeleteController);

    EmployeesMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Employees'];

    function EmployeesMySuffixDeleteController($uibModalInstance, entity, Employees) {
        var vm = this;

        vm.employees = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Employees.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
