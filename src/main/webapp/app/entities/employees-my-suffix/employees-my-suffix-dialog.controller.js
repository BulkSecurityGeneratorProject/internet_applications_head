(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('EmployeesMySuffixDialogController', EmployeesMySuffixDialogController);

    EmployeesMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Employees', 'Roles', 'Magazine'];

    function EmployeesMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Employees, Roles, Magazine) {
        var vm = this;

        vm.employees = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.roles = Roles.query();
        vm.magazines = Magazine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employees.id !== null) {
                Employees.update(vm.employees, onSaveSuccess, onSaveError);
            } else {
                Employees.save(vm.employees, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('headApp:employeesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
