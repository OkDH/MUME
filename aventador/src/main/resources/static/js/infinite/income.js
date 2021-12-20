app.controller("InfiniteIncomeController", function($scope, httpService, infiniteService){
	var infiniteIncome = this;
	
	var now = new Date();
	infiniteIncome.query = {
		accountId: null,
		doneDateStart: new Date(new Date().setFullYear(now.getFullYear() - 1)).toISOString().split("T")[0], // 1년
		doneDateEnd: now.toISOString().split("T")[0],
		order: 'DESC',
		date: '1년'
	}
	
	infiniteIncome.stats = {
		totalBuy: 0,
		totalSell: 0,
		totalProfit: 0
	}
	
	$scope.$watch("infiniteIncome.query", function(query){
		// 손익현황 가져오기
		infiniteService.promiseGetIncome("profit", query).then(function(data){
			infiniteIncome.profit = data;
			infiniteIncome.stats.totalBuy = 0;
			infiniteIncome.stats.totalSell = 0;
			infiniteIncome.stats.totalProfit = 0;
			
			infiniteIncome.profit.forEach(function(item){
				// 총매수금
				infiniteIncome.stats.totalBuy += item.totalBuyPrice;
				// 총매도금
				infiniteIncome.stats.totalSell += item.totalSellPrice;
				// 총실현손익
				infiniteIncome.stats.totalProfit += item.income;
			});
		});
	}, true);
});