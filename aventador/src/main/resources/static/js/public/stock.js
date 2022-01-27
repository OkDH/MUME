app.controller("StockController", function($scope, $timeout, $q, httpService, stockService){
	var ngStock = this;
	
	
	// 시장지수
	stockService.getMarketIndex().then(function(data){
		ngStock.marketIndex = {};
		
		angular.forEach(data, function(value, key){
			ngStock.marketIndex[key] = value;
		});
	});
	
	// 무매 종목
	stockService.getEtfs().then(function(data){
		ngStock.etfs = data;
		ngStock.arrayEtfs = Object.keys(data).map(function(key) {
			return data[key];
		});
	});
	
});

app.service("stockService", function(httpService){
	
	// 기본값 세팅
	var promiseInit = null;
	
	this.getInitData = function(){
		
		if(promiseInit){
			httpService.stop(promiseInit);
		}
		
		promiseInit = httpService.get({
			url: meta.baseUrl + "api/stock/init",
		}).then(function(response){
			return response.data;
		});
		
		return promiseInit;
	}

	// 시장지수조회
	var promiseGetMarketIndex = null;
	
	this.getMarketIndex = function(){
		
		if(promiseGetMarketIndex){
			httpService.stop(promiseGetMarketIndex);
		}
		
		promiseGetMarketIndex = httpService.get({
			url: meta.baseUrl + "api/stocks/market-index",
		}).then(function(response){
			return response.data;
		});
		
		return promiseGetMarketIndex;
	}
	
	// 3X ETFs
	var promiseGetEtfs = null;
	
	this.getEtfs = function(){
		
		if(promiseGetEtfs){
			httpService.stop(promiseGetEtfs);
		}
		
		promiseGetEtfs = httpService.get({
			url: meta.baseUrl + "api/stocks/etfs",
		}).then(function(response){
			return response.data;
		});
		
		return promiseGetEtfs;
	}
	
});