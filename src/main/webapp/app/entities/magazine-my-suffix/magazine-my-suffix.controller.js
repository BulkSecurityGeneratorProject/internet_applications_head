(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('MagazineMySuffixController', MagazineMySuffixController);

    MagazineMySuffixController.$inject = ['Magazine'];

    function MagazineMySuffixController(Magazine) {

        var vm = this;

        vm.magazines = [];

        loadAll();

        function loadAll() {
            Magazine.query(function(result) {
                vm.magazines = result;
                vm.searchQuery = null;
            });
        }
    }
})();
