app.controller("MarketIndexController", function($scope, $timeout, $q, stockService){
	
	var marketIndex = this;
	
	stockService.getMarketIndex().then(function(data){
		angular.forEach(data, function(value, key){
			marketIndex[key] = value;
		});
	});
	
});