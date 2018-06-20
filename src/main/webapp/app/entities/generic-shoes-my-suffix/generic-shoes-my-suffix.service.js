(function() {
    'use strict';
    angular
        .module('headApp')
        .factory('GenericShoes', GenericShoes);

    GenericShoes.$inject = ['$resource'];

    function GenericShoes ($resource) {
        var resourceUrl =  'api/generic-shoes/:id';

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
