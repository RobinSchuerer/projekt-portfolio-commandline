(function() {
	angular.module('project-portfolio')
	.controller('portfolioController',
			['$scope', '$upload', '$log', '$timeout','$filter','$http',  
	  function($scope, $upload,  $log, $timeout, $filter,$http) {
				console.log("Enter the controller...");
				$scope.showProfile = false;
				$scope.onFileSelect = function ($files) {
					console.log("file is selected...");
					$scope.loadingProducts = true;
					for (var i = 0; i < $files.length; i++) {
			            var file = $files[i];
			            $scope.upload = $upload.upload({
			              url: 'portfolio/input',
			              method: 'POST',
			              file: file
			            }).progress(function(evt) {
			            }).success(function(data, status, headers, config) {
			            	$scope.showProfile = true;
			            	$scope.showSucess = false;
			            	$scope.portfolio = data;
			            	_.forEach($scope.portfolio.teams, function(team) {
//			            		team.teamBuckets = $filter('filledBuckets')(team.teamBuckets);
			            	});
			            	// Sort out the Team and their work on each project.
			            	sortOutPortfolio($scope.portfolio);
			            	$scope.getHighestBucketsTeam($scope.portfolio.teams);
			            	$timeout(function() {
			                    $scope.loadingProducts = false;
			                  },500);
			            	console.log(data);
			            });
			          }
				};
				$scope.onExportFile = function () {
					console.log("file export is selected...");
					$scope.loadingProducts = true;
					for (var i = 0; i < document.getElementById("file").files.length; i++) {
			            var file = document.getElementById("file").files[i];
			            $scope.upload = $upload.upload({
			            		url : 'portfolio/inputexport',
			            		 method: 'POST',
					              file: file
					            }).success(function(data, status, headers, config) {
			            	$scope.showSucess = true;
//			            	$scope.portfolio = data;
//			            	_.forEach($scope.portfolio.teams, function(team) {
////			            		team.teamBuckets = $filter('filledBuckets')(team.teamBuckets);
//			            	});
//			            	// Sort out the Team and their work on each project.
//			            	sortOutPortfolio($scope.portfolio);
//			            	$scope.getHighestBucketsTeam($scope.portfolio.teams);
			            	$timeout(function() {
			                    $scope.loadingProducts = false;
			                  },500);
			            	console.log(data);
			            });
			          }
				};
				
				$scope.$watch('showProfile', function(val) {
					$scope.showProfile = val;
			      });				
//				$scope.projectTeamWork = function (tb, project) {
//					var ptw =  _.find(tb.projectTeamWorks, function(ptw) {
//						return ptw.projectName === project.name;
//					});
//						if(ptw) {
//							if(ptw.effort !== 0) {
//								$scope.manipulatedProjectTeamWork = Math.round((ptw.effort * 100) / tb.teamCapacity.manDays) + "%";
//							} else {
//								$scope.manipulatedProjectTeamWork = 0;
//							}
//							return true;
//						} else {
//							$scope.manipulatedProjectTeamWork = 0;
//							return false;
//						}
//					
//				};
				
				$scope.projectTeamWork = function (tb, project,team) {
					console.log(project);
					var teamProject = {};
					var ptw =  _.find(tb.projectTeamWorks, function(ptw) {
						return ptw.projectName === project.name;
					});
					var fullprecentage = 0;
					var countproject = 0;
					projectmusthave = 0;
					projectproduct = 0;
//					_.forEach(tb.projectTeamWorks,function(pteamwork)
//					{
//						//console.log(pteamwork);
//						//console.log(pteamwork.projectName +"<<<>>>"+pteamwork.effort+">>"+pteamwork.bucketMonth);
//						if(pteamwork.effort !=0 )
//						{
//							fullprecentage += Math.round((pteamwork.effort * 100) / tb.teamCapacity.manDays);
//							
//							_.forEach($scope.portfolio.projects,function(project1)
//									{
//									   if(project1.name==pteamwork.projectName)
//										   {
//										    if(project1.projectType.projectTypeCode == "MHP")
//										    	projectmusthave++;
//										    else
//										    	projectproduct++;
//										   }
//									});
//							
//							
//							countproject ++;
//						}	
//					});
					
						if(ptw) {
							if(ptw.effort !== 0) {
								fullprecentage = Math.round((ptw.effort * 100) / tb.teamCapacity.manDays);
//								if(fullprecentage != 100 && fullprecentage > 0)
//								{
//									if(!(project.projectType.projectTypeCode == "MHP"))
//											teamProject.effort = Math.round((100/countproject)) +"%";
//									else{
//										var prestriction = _.find($scope.portfolio.restrictions,function(prestriction){
//											return prestriction.projectType.projectTypeCode === "MHP"
//										});
//											console.log(prestriction);
//										if(fullprecentage != prestriction.teamCapacityAllocation)
//										{
////											teamProject.effort = Math.round(( prestriction.teamCapacityAllocation /countproject)) +"%";
//											if(countproject != projectmusthave)
//											{
//												restrictionval = 100 - (100 / countproject);
//											
//												teamProject.effort = Math.round(( restrictionval /projectmusthave)) +"%";
//											}else
//											{
//												teamProject.effort = Math.round(( prestriction.teamCapacityAllocation /countproject)) +"%";
//											}
//
//										}else{
//											teamProject.effort = Math.round((ptw.effort * 100) / tb.teamCapacity.manDays) + "%";
//										}
//									}
//								}else{
//									teamProject.effort = Math.round((ptw.effort * 100) / tb.teamCapacity.manDays) + "%";
//								}
								teamProject.effort = fullprecentage + "%";
								teamProject.projectName = project.name;
								teamProject.teamName = tb.teamName;
								teamProject.color = project.color;
								teamProject.teamColor = team.color;
							} else {
								teamProject.effort = 0 + "%";
								teamProject.projectName = project.name;
								teamProject.teamName = tb.teamName;
								//teamProject.color = project.color;
								teamProject.teamColor = team.color;
							}
							
						} else {
							teamProject.effort = 0+ "%";
						}
						return teamProject; 
				};
				
				$scope.getProjectTeamWork = function (teamBucket, project){
					if(teamBucket.projectTeamWorks.length > 0) {
						
						/*_.forEach(teamBucket.projectTeamWorks,function(pteamwork)
								{
									console.log(pteamwork.projectName);
								});*/
						
						var ptWorks =  _.remove(teamBucket.projectTeamWorks , function(ptw) {
			    			 return ptw.projectName == project.name;
							});	 
			    			 if(ptWorks.length) {
			    				 $scope.manipulatedProjectTeamWork = ptWorks[0].effort;
			    				 return true;
			    			 } else {
			    				 return false;
			    			 }
			    	}
				};
				
				$scope.getHighestBucketsTeam = function(teams) {
					var count = 0;
					$scope.teamName = "";
					_.forEach(teams, function(team) {
						if(team.teamBuckets.length > count) {
							count = team.teamBuckets.length;
							$scope.teamName = team.name;
						} 
					});
					
					var higestBucketsTeam = _.find(teams, function(team) {
						return team.name === $scope.teamName; 
					});
					
					$scope.buckets = higestBucketsTeam.teamBuckets;
				};
				
				$scope.projectColors = {};
				_.forEach(portfolio.projects, function(project) {
					projectColors.project.color = true;
				});
				
				var sortOutPortfolio = function(portfolio) {
					
					$scope.projectTeamWorks = [];
					var teamProjectOverflow = 0;
					$scope.teamColors = {};
					_.forEach(portfolio.teams, function(team) {
						
						_.forEach(portfolio.projects, function(project) {
							var teamProjectWorks = {};
							var teamsProjectsWorks = [];
							
							_.forEach(team.teamBuckets, function(bucket){
								teamsProjectsWorks.push($scope.projectTeamWork(bucket, project,team));
							});
//							if(team.missingTeamCapacity.projectTeamWorks.length > 0) {
//								var projectTWork = _.find(team.missingTeamCapacity.projectTeamWorks, function(ptw) {
//									return ptw.projectName === project.name;
//								});
//								if(projectTWork) {
//									teamProjectOverflow = projectTWork.effort;
//								} else {
//									teamProjectOverflow = 0;
//								}
//								 		
//							}
//							var teamProject = team.name+'-'+project.name+'-'+teamProjectOverflow;
							teamProjectWorks.name = team.name+'-'+project.name+'-'+team.overflowWork;
							teamProjectWorks.value = teamsProjectsWorks;
//							teamProjectWorks[team.name+'-'+project.name+'-'+teamProjectOverflow] = teamsProjectsWorks;
							$scope.teamColors[team.name+'-'+project.name+'-'+team.overflowWork] = team.color;
							$scope.projectTeamWorks.push(teamProjectWorks);
							console.log($scope.projectTeamWorks);
//							teamProjectWorks.push(teamsProjectsWorks);
						});
					});
					
					// convert object to array...
//					$scope.projectTeamWorks = $filter('toArray')(teamProjectWorks, true);
//					$scope.projectTeamWorks = teamProjectWorks;
					
				};
				
		  }]
	);
})();