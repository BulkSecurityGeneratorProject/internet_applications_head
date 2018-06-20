(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('MagazineMySuffixDialogController', MagazineMySuffixDialogController);

    MagazineMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Magazine', 'StorageShoes', 'Employees'];

    function MagazineMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Magazine, StorageShoes, Employees) {
        var vm = this;

        vm.magazine = entity;
        vm.clear = clear;
        vm.save = save;
        vm.storageshoes = StorageShoes.query();
        vm.employees = Employees.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.magazine.id !== null) {
                Magazine.update(vm.magazine, onSaveSuccess, onSaveError);
            } else {
                Magazine.save(vm.magazine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('headApp:magazineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
