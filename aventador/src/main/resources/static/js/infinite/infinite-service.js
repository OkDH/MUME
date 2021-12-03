app.service("infiniteService", function(httpService){
	
	// 내 계좌 리스트 조회
	var promiseMyAcounts = null;
	this.getMyAccounts = function(){
		if(promiseMyAcounts){
			httpService.stop(promiseMyAcounts);
		}
		promiseMyAcounts = httpService.get({
			url: meta.baseUrl + "api/infinite/my-account",
		}).then(function(response){
			return response.data;
		});
		return promiseMyAcounts;
	}
	
	// 계좌 내 종목 조회
	var promiseStocks = null;
	this.getStocks = function(params){
		if(promiseStocks){
			httpService.stop(promiseStocks);
		}
		promiseStocks = httpService.post({
			url: meta.baseUrl + "api/infinite/stocks",
			data: params
		}).then(function(response){
			return response.data;
		});
		return promiseStocks;
	}
	
	// 계좌 내 종목 추가
	var promiseAddStock = null;
	this.addStock = function(params){
		if(promiseAddStock){
			httpService.stop(promiseAddStock);
		}
		promiseAddStock = httpService.post({
			url: meta.baseUrl + "api/infinite/stock",
			data: params
		}).then(function(response){
			return response.data;
		});
		return promiseAddStock;
	}
});
