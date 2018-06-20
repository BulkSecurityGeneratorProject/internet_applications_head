(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('GenericShoesMySuffixDialogController', GenericShoesMySuffixDialogController);

    GenericShoesMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GenericShoes', 'Models', 'StorageShoes'];

    function GenericShoesMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GenericShoes, Models, StorageShoes) {
        var vm = this;

        vm.genericShoes = entity;
        vm.clear = clear;
        vm.save = save;
        vm.models = Models.query();
        vm.storageshoes = StorageShoes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.genericShoes.id !== null) {
                GenericShoes.update(vm.genericShoes, onSaveSuccess, onSaveError);
            } else {
                GenericShoes.save(vm.genericShoes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('headApp:genericShoesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
