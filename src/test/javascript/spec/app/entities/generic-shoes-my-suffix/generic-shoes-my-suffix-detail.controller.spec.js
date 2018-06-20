'use strict';

describe('Controller Tests', function() {

    describe('GenericShoes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGenericShoes, MockModels, MockStorageShoes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGenericShoes = jasmine.createSpy('MockGenericShoes');
            MockModels = jasmine.createSpy('MockModels');
            MockStorageShoes = jasmine.createSpy('MockStorageShoes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GenericShoes': MockGenericShoes,
                'Models': MockModels,
                'StorageShoes': MockStorageShoes
            };
            createController = function() {
                $injector.get('$controller')("GenericShoesMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'headApp:genericShoesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
