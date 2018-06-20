(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('GenericShoesMySuffixDeleteController',GenericShoesMySuffixDeleteController);

    GenericShoesMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'GenericShoes'];

    function GenericShoesMySuffixDeleteController($uibModalInstance, entity, GenericShoes) {
        var vm = this;

        vm.genericShoes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GenericShoes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
