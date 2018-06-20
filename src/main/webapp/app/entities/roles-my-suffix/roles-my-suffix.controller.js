(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('RolesMySuffixController', RolesMySuffixController);

    RolesMySuffixController.$inject = ['Roles'];

    function RolesMySuffixController(Roles) {

        var vm = this;

        vm.roles = [];

        loadAll();

        function loadAll() {
            Roles.query(function(result) {
                vm.roles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
