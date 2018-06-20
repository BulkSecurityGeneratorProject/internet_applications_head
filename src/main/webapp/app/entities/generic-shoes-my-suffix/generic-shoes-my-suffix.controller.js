(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('GenericShoesMySuffixController', GenericShoesMySuffixController);

    GenericShoesMySuffixController.$inject = ['GenericShoes'];

    function GenericShoesMySuffixController(GenericShoes) {

        var vm = this;

        vm.genericShoes = [];

        loadAll();

        function loadAll() {
            GenericShoes.query(function(result) {
                vm.genericShoes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
