(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('StorageShoesMySuffixDialogController', StorageShoesMySuffixDialogController);

    StorageShoesMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StorageShoes', 'GenericShoes', 'Magazine'];

    function StorageShoesMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StorageShoes, GenericShoes, Magazine) {
        var vm = this;

        vm.storageShoes = entity;
        vm.clear = clear;
        vm.save = save;
        vm.genericshoes = GenericShoes.query();
        vm.magazines = Magazine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.storageShoes.id !== null) {
                StorageShoes.update(vm.storageShoes, onSaveSuccess, onSaveError);
            } else {
                StorageShoes.save(vm.storageShoes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('headApp:storageShoesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
