/**
 * @license FreezePane v1.0
 * @author Jatin Marwah(https://github.com/jatinmarwah)
 * License: MIT
 */
angular.module('freezepaneModule', []).directive('tablefreeze', function () {
  return {
    restrict: 'E',
    templateUrl: "directive/freezepane.html", //Modify this to suit your directory requirements
    transclude: true,
    scope: true,
    link: function (scope, elem, attrs, ctrl) {

      scope.getPartial = function () {
        return attrs.tabletemplateurl;
      };

      var
        tableID = attrs.tableid;
        scope.random = tableID + "_" + Math.floor((Math.random() * 999999) + 1),
        targetLeft = $(elem).find(".div_hscroll .leftS"),
        targetRight = $(elem).find(".div_hscroll .rightS"),
        targetUpwards = $(elem).find(".div_vscroll .upS"),
        targetDown = $(elem).find(".div_vscroll .downS");

      if (attrs.freezerows == undefined) {
        attrs.freezerows = 1;
      }
      if (attrs.freezecols == undefined) {
        attrs.freezecols = 1;
      }
      //generate table instance
      scope[scope.random] = new scope.freezePane(tableID, attrs.freezerows, attrs.freezecols);


      targetLeft.on("mousedown", function () {
        scope[scope.random].left(0);
      });

      targetLeft.on("mouseup", function () {
        scope[scope.random].left(1);
      });

      targetRight.on("mousedown", function () {
        scope[scope.random].right(0);
      });

      targetRight.on("mouseup", function () {
        scope[scope.random].right(1);
      });

      targetUpwards.on("mousedown", function () {
        scope[scope.random].upwards(0);
      });

      targetUpwards.on("mouseup", function () {
        scope[scope.random].upwards(1);
      });

      targetDown.on("mousedown", function () {
        scope[scope.random].down(0);
      });

      targetDown.on("mouseup", function () {
        scope[scope.random].down(1);
      });


    },
    controller: function ($scope) {
      $scope.freezePane = function (id, rowsToFreeze, colsToFreeze) {
        var targetRow = rowsToFreeze,
          targetCol = colsToFreeze,
          scrollSpeed = 100, //in milliseconds
          targetTable,
          totalRows,
          totalColumns,
          timeoutID;

        this.initTable = function () {

          if (typeof id === undefined) {
            console.log("Please define table ID");
            return;
          } else {
            this.id = id;
          }


          if (!targetTable) {
            targetTable = document.getElementById(id);
          }
          totalColumns = targetTable.rows[0].cells.length;
          totalRows = targetTable.rows.length;

          for (var x = 0; x < targetTable.rows[0].cells.length; x++) {
            var columnWidth = targetTable.rows[0].cells[x].offsetWidth;
            targetTable.rows[0].cells[x].setAttribute("width", columnWidth - 4);

          }
        };

        this.left = function (up) {
          if (up) {
            window.clearTimeout(timeoutID);
            return;
          }
          if (!targetTable) {
            this.initTable();
          }

          if (targetCol < (totalColumns - 1)) {
            for (var x = 0; x < totalRows; x++) {
              targetTable.rows[x].cells[targetCol].style.display = "none";
            }
            targetCol++;
            timeoutID = setTimeout((this.left).bind(this), scrollSpeed);

          }
        };

        this.right = function (up) {
          if (up) {
            window.clearTimeout(timeoutID);
            return;
          }

          if (!targetTable) {
            this.initTable();
          }

          if (targetCol < (totalColumns)) {
            for (var x = 0; x < totalRows; x++) {
              targetTable.rows[x].cells[targetCol].style.display = "";
            }
            if (targetCol > colsToFreeze) {
              targetCol--;
            }
            timeoutID = setTimeout((this.right).bind(this), scrollSpeed);
          }
        };

        this.down = function (up) {
          if (up) {
            window.clearTimeout(timeoutID);
            return;
          }
          if (!targetTable) {
            this.initTable(id);
          }

          if (targetRow < (totalRows - 1)) {
            targetTable.rows[targetRow].style.display = "none";
            targetRow++;
            timeoutID = setTimeout((this.down).bind(this), scrollSpeed);
          }
        };

        this.upwards = function (up) {
          if (up) {
            window.clearTimeout(timeoutID);
            return;
          }
          if (!targetTable) {
            this.initTable(id);
          }
          if (targetRow <= totalRows) {
            targetTable.rows[targetRow].style.display = "";
            if (targetRow > rowsToFreeze) {
              targetRow--;
            }
            timeoutID = setTimeout((this.upwards).bind(this), scrollSpeed);
          }
        };

      }
    }
  };
});
