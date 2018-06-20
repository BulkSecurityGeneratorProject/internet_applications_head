(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('ModelsMySuffixDialogController', ModelsMySuffixDialogController);

    ModelsMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Models', 'GenericShoes'];

    function ModelsMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Models, GenericShoes) {
        var vm = this;

        vm.models = entity;
        vm.clear = clear;
        vm.save = save;
        vm.genericshoes = GenericShoes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.models.id !== null) {
                Models.update(vm.models, onSaveSuccess, onSaveError);
            } else {
                Models.save(vm.models, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('headApp:modelsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
