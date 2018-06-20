'use strict';

describe('Controller Tests', function() {

    describe('Employees Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEmployees, MockRoles, MockMagazine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEmployees = jasmine.createSpy('MockEmployees');
            MockRoles = jasmine.createSpy('MockRoles');
            MockMagazine = jasmine.createSpy('MockMagazine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Employees': MockEmployees,
                'Roles': MockRoles,
                'Magazine': MockMagazine
            };
            createController = function() {
                $injector.get('$controller')("EmployeesMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'headApp:employeesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
