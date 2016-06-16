(function() {
'use strict';

angular.module('ng.tablePager', [])
.factory('tablePager', ['$log', '$filter', function($log, $filter) {
  /*
  The rest endpoint must take startRow and endRow request parameters,
  The ngResourceDataFn must be a function of a ngResource (e.g. PortalUsers.query)
  queryParams - other parameters to pass to the rest endpoint
  loadingCb (optional) - a callback called to let the caller know when data is being fetched
  conversionFn (optional) - a function to convert the server's response into {count: number, data: []}

  The endpoint should return {count: number, data: []} or a conversionFn should be
  passed it to convert the data
  */
  return function(ngResourceDataFn, queryParams, loadingCb, conversionFn, ngResourceFailureHandlerFn) {
    loadingCb = loadingCb || angular.noop;
    var buffer, sortDirection, sortField;
    var startRow = 0;
    conversionFn = conversionFn || function(a) { return a; };
    return function($defer, params) {
      if (!queryParams) {
        $defer.resolve([]);
        return;
      }
      _.each(params.sorting(), function(_sortDirection, _sortField) {
        if (_sortDirection != sortDirection || _sortField != sortField) {
          $log.info('Sort change, reload');
          buffer = undefined; //trigger reload
        }
        sortDirection = _sortDirection;
        sortField = _sortField;
      });
      if (!buffer || 
        params.page() * params.count() - startRow > buffer.data.length ||
        params.page() * params.count() - startRow <= 0) {
        startRow = Math.max(0,params.count() * (params.page() - 4));
        var endRow = params.count() * (params.page() + 4);
        $log.info("Getting data from", startRow,"to", endRow);
        
        loadingCb(true);
        ngResourceDataFn(
          _.merge({ 
            startRow: startRow,
            endRow: endRow,
            sortField: sortField,
            sortDirection: sortDirection
          }, queryParams),
          function(_buffer) {
            loadingCb(false);
            buffer = conversionFn(_buffer);
            params.total(buffer.count);
            var sliceFrom = (params.page() - 1) * params.count() - startRow;
            var sliceTo =  params.page() * params.count() - startRow;
            $defer.resolve(buffer.data.slice(sliceFrom, sliceTo));
          }, 
          function() {
            loadingCb(false);
        }).$promise.then(function(response) {
              //$log.info("success! ");
            }, ngResourceFailureHandlerFn);
      } else {
        var sliceFrom = (params.page() - 1) * params.count() - startRow;
        var sliceTo =  params.page() * params.count() - startRow;
        // Applying the filter..
        var filteredBuffer = params.filter() ? $filter("filter")(buffer.data, params.filter()) : buffer.data;
        params.total(filteredBuffer.count);
        $log.info("Using buffer from", sliceFrom,"to",sliceTo);
        $defer.resolve(filteredBuffer.slice(sliceFrom,sliceTo));
      }
    };
  };
}]);
})();