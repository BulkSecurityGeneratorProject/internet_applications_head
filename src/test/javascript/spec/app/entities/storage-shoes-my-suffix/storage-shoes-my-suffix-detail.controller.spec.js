'use strict';

describe('Controller Tests', function() {

    describe('StorageShoes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStorageShoes, MockGenericShoes, MockMagazine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStorageShoes = jasmine.createSpy('MockStorageShoes');
            MockGenericShoes = jasmine.createSpy('MockGenericShoes');
            MockMagazine = jasmine.createSpy('MockMagazine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StorageShoes': MockStorageShoes,
                'GenericShoes': MockGenericShoes,
                'Magazine': MockMagazine
            };
            createController = function() {
                $injector.get('$controller')("StorageShoesMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'headApp:storageShoesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
