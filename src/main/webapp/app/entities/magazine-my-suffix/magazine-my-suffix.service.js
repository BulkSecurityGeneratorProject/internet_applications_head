(function() {
    'use strict';
    angular
        .module('headApp')
        .factory('Magazine', Magazine);

    Magazine.$inject = ['$resource'];

    function Magazine ($resource) {
        var resourceUrl =  'api/magazines/:id';

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
