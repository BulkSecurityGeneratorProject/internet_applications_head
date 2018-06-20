(function() {
    'use strict';

    angular
        .module('headApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('models-my-suffix', {
            parent: 'entity',
            url: '/models-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Models'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/models-my-suffix/modelsmySuffix.html',
                    controller: 'ModelsMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('models-my-suffix-detail', {
            parent: 'models-my-suffix',
            url: '/models-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Models'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/models-my-suffix/models-my-suffix-detail.html',
                    controller: 'ModelsMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Models', function($stateParams, Models) {
                    return Models.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'models-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('models-my-suffix-detail.edit', {
            parent: 'models-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/models-my-suffix/models-my-suffix-dialog.html',
                    controller: 'ModelsMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Models', function(Models) {
                            return Models.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('models-my-suffix.new', {
            parent: 'models-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/models-my-suffix/models-my-suffix-dialog.html',
                    controller: 'ModelsMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                modelName: null,
                                modelType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('models-my-suffix', null, { reload: 'models-my-suffix' });
                }, function() {
                    $state.go('models-my-suffix');
                });
            }]
        })
        .state('models-my-suffix.edit', {
            parent: 'models-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/models-my-suffix/models-my-suffix-dialog.html',
                    controller: 'ModelsMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Models', function(Models) {
                            return Models.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('models-my-suffix', null, { reload: 'models-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('models-my-suffix.delete', {
            parent: 'models-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/models-my-suffix/models-my-suffix-delete-dialog.html',
                    controller: 'ModelsMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Models', function(Models) {
                            return Models.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('models-my-suffix', null, { reload: 'models-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
