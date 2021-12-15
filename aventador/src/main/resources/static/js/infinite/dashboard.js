app.controller("InfiniteDashboardController", function($scope, httpService, infiniteService){
	var infiniteDashboard = this;
	infiniteDashboard.query = {
		accountId: null,
		infiniteState: ["진행중","원금소진","매수중지"]
	}
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteDashboard.myAccounts = data;
	});
	
	// 통계 자료 가져오기
	$scope.$watch("infiniteDashboard.query.accountId", function(query){
		// 월별 손익현황
		infiniteService.getStatistics("profit-monthly", infiniteDashboard.query).then(function(data){
			infiniteDashboard.profitMonthly = data;
		});
		
		// 종목별 손익현황
		infiniteService.getStatistics("profit-stock", infiniteDashboard.query).then(function(data){
			infiniteDashboard.profitStock = data;
		});
		
		// 최근 2개월 매수금액
		infiniteService.getStatistics("buy-daily", infiniteDashboard.query).then(function(data){
			infiniteDashboard.buyDaily = data;
		});
	});
	
	$scope.$watch("infiniteDashboard.query.infiniteState", function(query){
		// 종목 진행률
		infiniteService.getStocks(infiniteDashboard.query).then(function(data){
			infiniteDashboard.stocks = data;
			
			console.log("stocks: " , data);
		});
	}, true);
	
	// 차트 그리기
	// 월별 손익현황 차트
	$scope.$watch("infiniteDashboard.profitMonthly", function(profitMonthly){
		if(!profitMonthly)
			return;
		
		console.log("profitMonthly :" , profitMonthly);
	});
	
	// 종목별 손익현황 차트
	$scope.$watch("infiniteDashboard.profitStock", function(profitStock){
		if(!profitStock)
			return;
		
		console.log("profitStock :" , profitStock);
	});
	
	// 최근 2개월 매수 손익현황 차트
	$scope.$watch("infiniteDashboard.buyDaily", function(buyDaily){
		if(!buyDaily)
			return;
		console.log("buyDaily :" , buyDaily);
	});
	
});