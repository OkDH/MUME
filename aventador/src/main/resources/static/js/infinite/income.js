app.controller("InfiniteIncomeController", function($scope, httpService, infiniteService){
	var infiniteIncome = this;
	infiniteIncome.query = {
		accountId: null,
		doneDateStart: null,
		doneDateEnd: null
	}
	
	$scope.$watch("infiniteIncome.query", function(query){
		// 손익현황 가져오기
		infiniteService.promiseGetIncome("profit", query).then(function(data){
			infiniteIncome.profit = data;
		});
	});
});