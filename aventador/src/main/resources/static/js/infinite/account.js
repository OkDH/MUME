app.controller("InfiniteAccountController", function($scope, $timeout, $q, httpService, stockService, infiniteService){

	var infiniteAccount = this;
	infiniteAccount.initData = {};
	
	// ETF 기본 정보
	stockService.getInitData().then(function(data){
		infiniteAccount.initData = data;
	});
	
	// 계좌 정보
	infiniteService.getMyAccount().then(function(data){
		console.log("my account ", data);
	});
	
});

