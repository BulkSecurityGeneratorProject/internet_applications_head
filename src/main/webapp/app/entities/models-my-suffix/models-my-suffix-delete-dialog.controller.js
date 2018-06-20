(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('ModelsMySuffixDeleteController',ModelsMySuffixDeleteController);

    ModelsMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Models'];

    function ModelsMySuffixDeleteController($uibModalInstance, entity, Models) {
        var vm = this;

        vm.models = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Models.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
