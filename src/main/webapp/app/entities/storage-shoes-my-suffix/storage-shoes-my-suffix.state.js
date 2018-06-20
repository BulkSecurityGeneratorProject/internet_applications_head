(function() {
    'use strict';

    angular
        .module('headApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('storage-shoes-my-suffix', {
            parent: 'entity',
            url: '/storage-shoes-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StorageShoes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-shoes-my-suffix/storage-shoesmySuffix.html',
                    controller: 'StorageShoesMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('storage-shoes-my-suffix-detail', {
            parent: 'storage-shoes-my-suffix',
            url: '/storage-shoes-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StorageShoes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-shoes-my-suffix/storage-shoes-my-suffix-detail.html',
                    controller: 'StorageShoesMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'StorageShoes', function($stateParams, StorageShoes) {
                    return StorageShoes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'storage-shoes-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('storage-shoes-my-suffix-detail.edit', {
            parent: 'storage-shoes-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-shoes-my-suffix/storage-shoes-my-suffix-dialog.html',
                    controller: 'StorageShoesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StorageShoes', function(StorageShoes) {
                            return StorageShoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-shoes-my-suffix.new', {
            parent: 'storage-shoes-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-shoes-my-suffix/storage-shoes-my-suffix-dialog.html',
                    controller: 'StorageShoesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('storage-shoes-my-suffix', null, { reload: 'storage-shoes-my-suffix' });
                }, function() {
                    $state.go('storage-shoes-my-suffix');
                });
            }]
        })
        .state('storage-shoes-my-suffix.edit', {
            parent: 'storage-shoes-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-shoes-my-suffix/storage-shoes-my-suffix-dialog.html',
                    controller: 'StorageShoesMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StorageShoes', function(StorageShoes) {
                            return StorageShoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-shoes-my-suffix', null, { reload: 'storage-shoes-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-shoes-my-suffix.delete', {
            parent: 'storage-shoes-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-shoes-my-suffix/storage-shoes-my-suffix-delete-dialog.html',
                    controller: 'StorageShoesMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StorageShoes', function(StorageShoes) {
                            return StorageShoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-shoes-my-suffix', null, { reload: 'storage-shoes-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
