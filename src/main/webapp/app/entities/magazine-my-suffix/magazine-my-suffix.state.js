(function() {
    'use strict';

    angular
        .module('headApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('magazine-my-suffix', {
            parent: 'entity',
            url: '/magazine-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Magazines'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/magazine-my-suffix/magazinesmySuffix.html',
                    controller: 'MagazineMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('magazine-my-suffix-detail', {
            parent: 'magazine-my-suffix',
            url: '/magazine-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Magazine'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/magazine-my-suffix/magazine-my-suffix-detail.html',
                    controller: 'MagazineMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Magazine', function($stateParams, Magazine) {
                    return Magazine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'magazine-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('magazine-my-suffix-detail.edit', {
            parent: 'magazine-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magazine-my-suffix/magazine-my-suffix-dialog.html',
                    controller: 'MagazineMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Magazine', function(Magazine) {
                            return Magazine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('magazine-my-suffix.new', {
            parent: 'magazine-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magazine-my-suffix/magazine-my-suffix-dialog.html',
                    controller: 'MagazineMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                location: null,
                                capacity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('magazine-my-suffix', null, { reload: 'magazine-my-suffix' });
                }, function() {
                    $state.go('magazine-my-suffix');
                });
            }]
        })
        .state('magazine-my-suffix.edit', {
            parent: 'magazine-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magazine-my-suffix/magazine-my-suffix-dialog.html',
                    controller: 'MagazineMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Magazine', function(Magazine) {
                            return Magazine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('magazine-my-suffix', null, { reload: 'magazine-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('magazine-my-suffix.delete', {
            parent: 'magazine-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magazine-my-suffix/magazine-my-suffix-delete-dialog.html',
                    controller: 'MagazineMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Magazine', function(Magazine) {
                            return Magazine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('magazine-my-suffix', null, { reload: 'magazine-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
