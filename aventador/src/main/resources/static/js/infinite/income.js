app.controller("InfiniteIncomeController", function($scope, httpService, infiniteService){
	var infiniteIncome = this;
	
	var now = new Date();
	infiniteIncome.query = {
		accountId: 'ALL',
		sellDateStart: new Date(new Date().setFullYear(now.getFullYear() - 1)).toISOString().split("T")[0], // 1년
		sellDateEnd: now.toISOString().split("T")[0],
		order: 'DESC'
	}
	infiniteIncome.filter = {
		date: '1년'
	}
	
	infiniteIncome.stats = {
		totalBuy: 0,
		totalSell: 0,
		totalIncome: 0
	}
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteIncome.myAccounts = data;
		
		// 계좌 order map
		infiniteIncome.myAccountsOrder = {};
		data.forEach(function(d){
			infiniteIncome.myAccountsOrder[d.accountId] = {};
			infiniteIncome.myAccountsOrder[d.accountId].order = d.accountOrder;
			infiniteIncome.myAccountsOrder[d.accountId].alias = d.accountAlias;
		});
	});
	
	// 필터 모달
	infiniteIncome.openFilterModal = function(){
		$('#filterModal').modal("show");
		// selectpicker
		$("#filterModal #accountSelect").selectpicker("refresh");
	}
	
	$scope.$watch("infiniteIncome.filter", function(filter){
		if(!filter)
			return;
		if(filter.date == "1개월"){
			infiniteIncome.query.sellDateStart = new Date(new Date().setMonth(now.getMonth() - 1)).toISOString().split("T")[0];
			infiniteIncome.query.sellDateEnd = now.toISOString().split("T")[0];
		} else if(filter.date == "3개월"){
			infiniteIncome.query.sellDateStart = new Date(new Date().setMonth(now.getMonth() - 3)).toISOString().split("T")[0];
			infiniteIncome.query.sellDateEnd = now.toISOString().split("T")[0];
		} else if(filter.date == "1년"){
			infiniteIncome.query.sellDateStart = new Date(new Date().setFullYear(now.getFullYear() - 1)).toISOString().split("T")[0];
			infiniteIncome.query.sellDateEnd = now.toISOString().split("T")[0];
		}
	}, true);
	
	$scope.$watch("infiniteIncome.query", function(query){
		
		// 손익현황 가져오기
		infiniteService.promiseGetIncome("income", query).then(function(data){
			infiniteIncome.income = data;
			infiniteIncome.stats.totalBuy = 0;
			infiniteIncome.stats.totalSell = 0;
			infiniteIncome.stats.totalIncome = 0;
			
			infiniteIncome.income.forEach(function(item){
				// 총매수금
				infiniteIncome.stats.totalBuy += item.buyPrice;
				// 총매도금
				infiniteIncome.stats.totalSell += item.sellPrice;
				// 총실현손익
				infiniteIncome.stats.totalIncome += item.income;
			});
		});
		
		// 종목별 손익현황 가져오기
		infiniteService.promiseGetIncome("stock", query).then(function(data){
			infiniteIncome.incomeByStock = data;
		});
		
		// 월별 손익현황 가져오기
		infiniteService.promiseGetIncome("monthly", query).then(function(data){
			infiniteIncome.incomeByMonthly = data;
		});
		
	}, true);
});