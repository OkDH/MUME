app.controller("InfiniteAccountController", function($scope, $timeout, $q, httpService, stockService){

	var infiniteAccount = this;
	infiniteAccount.initData = {};
	
	stockService.getInitData().then(function(data){
		infiniteAccount.initData = data;
		console.log(infiniteAccount)
	});
	
});
