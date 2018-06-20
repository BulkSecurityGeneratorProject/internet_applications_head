(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('RolesMySuffixDialogController', RolesMySuffixDialogController);

    RolesMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Roles', 'Employees'];

    function RolesMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Roles, Employees) {
        var vm = this;

        vm.roles = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employees.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.roles.id !== null) {
                Roles.update(vm.roles, onSaveSuccess, onSaveError);
            } else {
                Roles.save(vm.roles, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('headApp:rolesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
