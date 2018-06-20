(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('ModelsMySuffixController', ModelsMySuffixController);

    ModelsMySuffixController.$inject = ['Models'];

    function ModelsMySuffixController(Models) {

        var vm = this;

        vm.models = [];

        loadAll();

        function loadAll() {
            Models.query(function(result) {
                vm.models = result;
                vm.searchQuery = null;
            });
        }
    }
})();
