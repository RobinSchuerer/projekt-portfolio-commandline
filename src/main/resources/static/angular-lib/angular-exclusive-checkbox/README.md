# Angular Exclusive Checkbox

A pair of simple directives to create _checkbox groups_ that behave similar to radio buttons but unlike radio buttons, they can be unselected.

## Usage
    
Add the module dependency:
    
    angular.module('app', ['ngExclusiveCheckbox']);

Adding to the view:

    <div class='checkbox' checkbox-group>
      <label><input type='checkbox' ng-model='dogPerson' exclusive-checkbox />Dogs</label><br />
      <label><input type='checkbox' ng-model='catPerson' exclusive-checkbox />Cats</label><br />
    </div>

