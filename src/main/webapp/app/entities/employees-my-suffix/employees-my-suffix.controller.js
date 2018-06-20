(function() {
    'use strict';

    angular
        .module('headApp')
        .controller('EmployeesMySuffixController', EmployeesMySuffixController);

    EmployeesMySuffixController.$inject = ['Employees'];

    function EmployeesMySuffixController(Employees) {

        var vm = this;

        vm.employees = [];

        loadAll();

        function loadAll() {
            Employees.query(function(result) {
                vm.employees = result;
                vm.searchQuery = null;
            });
        }
    }
})();
