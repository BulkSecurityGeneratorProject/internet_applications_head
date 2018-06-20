(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('StorageShoesMySuffixDeleteController',StorageShoesMySuffixDeleteController);

    StorageShoesMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'StorageShoes'];

    function StorageShoesMySuffixDeleteController($uibModalInstance, entity, StorageShoes) {
        var vm = this;

        vm.storageShoes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StorageShoes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
