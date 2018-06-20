(function() {
    'use strict';

    angular
        .module('headApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('roles-my-suffix', {
            parent: 'entity',
            url: '/roles-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Roles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/roles-my-suffix/rolesmySuffix.html',
                    controller: 'RolesMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('roles-my-suffix-detail', {
            parent: 'roles-my-suffix',
            url: '/roles-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Roles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/roles-my-suffix/roles-my-suffix-detail.html',
                    controller: 'RolesMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Roles', function($stateParams, Roles) {
                    return Roles.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'roles-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('roles-my-suffix-detail.edit', {
            parent: 'roles-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles-my-suffix/roles-my-suffix-dialog.html',
                    controller: 'RolesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Roles', function(Roles) {
                            return Roles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('roles-my-suffix.new', {
            parent: 'roles-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles-my-suffix/roles-my-suffix-dialog.html',
                    controller: 'RolesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                roleName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('roles-my-suffix', null, { reload: 'roles-my-suffix' });
                }, function() {
                    $state.go('roles-my-suffix');
                });
            }]
        })
        .state('roles-my-suffix.edit', {
            parent: 'roles-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles-my-suffix/roles-my-suffix-dialog.html',
                    controller: 'RolesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Roles', function(Roles) {
                            return Roles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('roles-my-suffix', null, { reload: 'roles-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('roles-my-suffix.delete', {
            parent: 'roles-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles-my-suffix/roles-my-suffix-delete-dialog.html',
                    controller: 'RolesMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Roles', function(Roles) {
                            return Roles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('roles-my-suffix', null, { reload: 'roles-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
