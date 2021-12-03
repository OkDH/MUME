app.controller("InfiniteAccountController", function($scope, $timeout, $q, httpService, stockService, infiniteService){

	var infiniteAccount = this;
	
	// ETF 기본 정보
	infiniteAccount.initData = {};
	stockService.getInitData().then(function(data){
		infiniteAccount.initData = data;
		infiniteAccount.addStock.init();
	});
	
	infiniteAccount.account = {};
	infiniteAccount.account.query = {
		accountId: null,
		infiniteType: null,
		infiniteState: "진행중",
		offset: 0,
		limit: 30
	};
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteAccount.account.myAccounts = data;
		infiniteAccount.addStock.init();
	});
	
	// 계좌 선택 필터 변경 시 종목 조회
	$scope.$watch("infiniteAccount.account.query", function(query){
		if(!query){
			return;
		}
		infiniteService.getStocks(query).then(function(data){
			infiniteAccount.stocks = data;
		});
	}, true);
	
	infiniteAccount.addStock = {};
	infiniteAccount.addStock.init = function(){
		infiniteAccount.addStock.data = {
			symbol: null,
			infiniteType: "v2.1",
			startedDate: null,
			seed: null,
			unitPrice: null,
			quantity: null,
			accountId: null
		}
		if(infiniteAccount.initData.symbols)
			infiniteAccount.addStock.data.symbol = infiniteAccount.initData.symbols[0];
		if(infiniteAccount.account.myAccounts)
			infiniteAccount.addStock.data.accountId = infiniteAccount.account.myAccounts[0].accountId;
	}
	
	// 종목 추가
	infiniteAccount.addStock.add = function(){
		infiniteService.addStock(infiniteAccount.addStock.data).then(function(data){
			if(data == true){
				// TODO : 알림창 
				
				infiniteService.getStocks(infiniteAccount.account.query).then(function(data){
					infiniteAccount.stocks = data;
				});
				infiniteAccount.addStock.init();
			}
		})
	}
});

