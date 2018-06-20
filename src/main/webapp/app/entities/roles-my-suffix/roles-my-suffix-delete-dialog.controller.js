(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('RolesMySuffixDeleteController',RolesMySuffixDeleteController);

    RolesMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Roles'];

    function RolesMySuffixDeleteController($uibModalInstance, entity, Roles) {
        var vm = this;

        vm.roles = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Roles.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
