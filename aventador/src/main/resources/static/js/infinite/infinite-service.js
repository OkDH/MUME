app.service("infiniteService", function(httpService){
	
	// 내 계좌 리스트 조회
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
	
	// 계좌 내 종목 조회
	var promiseStocks = null;
	this.getStocks = function(param){
		if(promiseStocks){
			httpService.stop(promiseStocks);
		}
		promiseStocks = httpService.get({
			url: meta.baseUrl + "api/infinite/stocks",
			data: JSON.stringify(param)
		}).then(function(response){
			return response.data;
		});
		return promiseStocks;
	}
});
