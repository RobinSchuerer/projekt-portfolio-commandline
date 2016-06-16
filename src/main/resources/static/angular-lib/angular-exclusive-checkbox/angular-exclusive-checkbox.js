(function() {
  'use strict';
  angular.module('ngExclusiveCheckbox',[]).directive('checkboxGroup', [function() {
    return {
      restrict: 'AE',
      controller: [function() {
        this.checkboxes = [];
        this.register = function(checkbox) {
          this.checkboxes.push(checkbox);
        };
        this.others = function(checkbox) {
          return _.filter(this.checkboxes, function(cb) {
            return cb !== checkbox;
          });
        };
      }]
    };
  }])
  .directive('exclusiveCheckbox', [function() {
    return {
      restrict: 'A',
      require: ['ngModel', '^checkboxGroup'],
      link: function($scope, element, attrs, controllers) {
        var ngModelController = controllers[0];
        var checkboxGroupController = controllers[1];
        var me = {
          uncheck: function() {
            ngModelController.$setViewValue(false);
            ngModelController.$render();
          }
        };
        checkboxGroupController.register(me);
        ngModelController.$viewChangeListeners.push(function() {
          if (ngModelController.$viewValue) {
            _.each(checkboxGroupController.others(me), function(other) {
              other.uncheck();
            });
          }
        });
      }
    };
  }]);
})();