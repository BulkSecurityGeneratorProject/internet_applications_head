(function() {
    'use strict';

    angular
        .module('headApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('generic-shoes-my-suffix', {
            parent: 'entity',
            url: '/generic-shoes-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GenericShoes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/generic-shoes-my-suffix/generic-shoesmySuffix.html',
                    controller: 'GenericShoesMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('generic-shoes-my-suffix-detail', {
            parent: 'generic-shoes-my-suffix',
            url: '/generic-shoes-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GenericShoes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/generic-shoes-my-suffix/generic-shoes-my-suffix-detail.html',
                    controller: 'GenericShoesMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'GenericShoes', function($stateParams, GenericShoes) {
                    return GenericShoes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'generic-shoes-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('generic-shoes-my-suffix-detail.edit', {
            parent: 'generic-shoes-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/generic-shoes-my-suffix/generic-shoes-my-suffix-dialog.html',
                    controller: 'GenericShoesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenericShoes', function(GenericShoes) {
                            return GenericShoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('generic-shoes-my-suffix.new', {
            parent: 'generic-shoes-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/generic-shoes-my-suffix/generic-shoes-my-suffix-dialog.html',
                    controller: 'GenericShoesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                shoeType: null,
                                brand: null,
                                color: null,
                                size: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('generic-shoes-my-suffix', null, { reload: 'generic-shoes-my-suffix' });
                }, function() {
                    $state.go('generic-shoes-my-suffix');
                });
            }]
        })
        .state('generic-shoes-my-suffix.edit', {
            parent: 'generic-shoes-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/generic-shoes-my-suffix/generic-shoes-my-suffix-dialog.html',
                    controller: 'GenericShoesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GenericShoes', function(GenericShoes) {
                            return GenericShoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('generic-shoes-my-suffix', null, { reload: 'generic-shoes-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('generic-shoes-my-suffix.delete', {
            parent: 'generic-shoes-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/generic-shoes-my-suffix/generic-shoes-my-suffix-delete-dialog.html',
                    controller: 'GenericShoesMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GenericShoes', function(GenericShoes) {
                            return GenericShoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('generic-shoes-my-suffix', null, { reload: 'generic-shoes-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
