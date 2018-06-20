(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('StorageShoesMySuffixController', StorageShoesMySuffixController);

    StorageShoesMySuffixController.$inject = ['StorageShoes'];

    function StorageShoesMySuffixController(StorageShoes) {

        var vm = this;

        vm.storageShoes = [];

        loadAll();

        function loadAll() {
            StorageShoes.query(function(result) {
                vm.storageShoes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
