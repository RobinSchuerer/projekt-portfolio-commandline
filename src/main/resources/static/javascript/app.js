(function() {
	'use strict';
	angular.module('project-portfolio', [ 'ngResource', 'angularFileUpload','ngSanitize','angular-toArrayFilter','freezepaneModule'])
	.filter('projectTeamWork', function() {
	      return function(projectTeamWorks, project) {
	    	  var ptWorks =  _.remove(projectTeamWorks , function(ptw) {
	    			 return ptw.projectName == project.name; 
	    		});
	    	  return projectTeamWorks = ptWorks;
	      }; 
      })
      .filter('filledBuckets', function() {
    	  return function(buckets){
    		var filledBuckets = _.remove(buckets, function(bucket) {
    			return bucket.projectTeamWorks.length !== 0;
    		});
    		console.log("filled buckets length: "+buckets.length);
    		return  filledBuckets;
    		
    	  };
      }).
      filter('formatDate', function() {
    	  return function(bucketMonth) {
    		  var formattedMonth="";
				switch(bucketMonth[1]) {
				case 1:
					formattedMonth = 'JAN'+" "+ bucketMonth[0].toString().substr(2,3);
				 break;
				case 2:
					formattedMonth = 'FEB'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 3:
					formattedMonth = 'MAR'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 4:
					formattedMonth = 'APR'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 5:
					formattedMonth = 'MAY'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 6:
					formattedMonth = 'JUN'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 7:
					formattedMonth = 'JUL'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 8:
					formattedMonth = 'AUG'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 9:
					formattedMonth = 'SEP'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 10:
					formattedMonth = 'OCT'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 11:
					formattedMonth = 'NOV'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				case 12:
					formattedMonth = 'DEC'+" "+ bucketMonth[0].toString().substr(2,3);;
				 break;
				}
				return formattedMonth;
    	  };
      })
      .filter('split', function() {
          return function(input, splitChar, splitIndex) {
              // do some bounds checking here to ensure it has that index
              return input.split(splitChar)[splitIndex];
          }
      });;
})();