(function() {
    'use strict';
    angular
        .module('headApp')
        .factory('Models', Models);

    Models.$inject = ['$resource'];

    function Models ($resource) {
        var resourceUrl =  'api/models/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
