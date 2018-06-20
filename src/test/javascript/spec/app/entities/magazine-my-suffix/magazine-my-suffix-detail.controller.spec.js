'use strict';

describe('Controller Tests', function() {

    describe('Magazine Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMagazine, MockStorageShoes, MockEmployees;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMagazine = jasmine.createSpy('MockMagazine');
            MockStorageShoes = jasmine.createSpy('MockStorageShoes');
            MockEmployees = jasmine.createSpy('MockEmployees');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Magazine': MockMagazine,
                'StorageShoes': MockStorageShoes,
                'Employees': MockEmployees
            };
            createController = function() {
                $injector.get('$controller')("MagazineMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'headApp:magazineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
