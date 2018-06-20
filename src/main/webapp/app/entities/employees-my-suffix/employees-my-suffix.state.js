(function() {
    'use strict';

    angular
        .module('headApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employees-my-suffix', {
            parent: 'entity',
            url: '/employees-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Employees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employees-my-suffix/employeesmySuffix.html',
                    controller: 'EmployeesMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('employees-my-suffix-detail', {
            parent: 'employees-my-suffix',
            url: '/employees-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Employees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employees-my-suffix/employees-my-suffix-detail.html',
                    controller: 'EmployeesMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Employees', function($stateParams, Employees) {
                    return Employees.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'employees-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('employees-my-suffix-detail.edit', {
            parent: 'employees-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-my-suffix/employees-my-suffix-dialog.html',
                    controller: 'EmployeesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employees', function(Employees) {
                            return Employees.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employees-my-suffix.new', {
            parent: 'employees-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-my-suffix/employees-my-suffix-dialog.html',
                    controller: 'EmployeesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                login: null,
                                password: null,
                                firstName: null,
                                lastName: null,
                                position: null,
                                email: null,
                                active: null,
                                createDate: null,
                                salary: null,
                                branch: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employees-my-suffix', null, { reload: 'employees-my-suffix' });
                }, function() {
                    $state.go('employees-my-suffix');
                });
            }]
        })
        .state('employees-my-suffix.edit', {
            parent: 'employees-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-my-suffix/employees-my-suffix-dialog.html',
                    controller: 'EmployeesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employees', function(Employees) {
                            return Employees.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employees-my-suffix', null, { reload: 'employees-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employees-my-suffix.delete', {
            parent: 'employees-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees-my-suffix/employees-my-suffix-delete-dialog.html',
                    controller: 'EmployeesMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Employees', function(Employees) {
                            return Employees.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employees-my-suffix', null, { reload: 'employees-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
