app.controller("InfiniteAccountController", function($scope, $timeout, $q, httpService, stockService, infiniteAccountService){

	var infiniteAccount = this;
	infiniteAccount.initData = {};
	
	// ETF 기본 정보
	stockService.getInitData().then(function(data){
		infiniteAccount.initData = data;
	});
	
	// 계좌 정보
	infiniteAccountService.getMyAccount().then(function(data){
		console.log("my account ", data);
	});
	
});

app.service("infiniteAccountService", function(httpService){
	// 기본값 세팅
	var promiseMyAcount = null;
	
	this.getMyAccount = function(){
		
		if(promiseMyAcount){
			httpService.stop(promiseMyAcount);
		}
		
		promiseMyAcount = httpService.get({
			url: meta.baseUrl + "api/infinite/my-account",
		}).then(function(response){
			return response.data;
		});
		
		return promiseMyAcount;
	}
});
