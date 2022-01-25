app.controller("StockController", function($scope, $timeout, $q, httpService, stockService){
	var ngStock = this;
	
	// 시장지수
	ngStock.marketIndex = {
		DJI : { symbol : "^DJI"	},
		IXIC : { symbol : "^IXIC" },
		GSPC : { symbol : "^GSPC" },
		SOX : { symbol : "^SOX" },
	};
	
	var symbols = [ngStock.marketIndex.DJI.symbol, ngStock.marketIndex.IXIC.symbol, ngStock.marketIndex.GSPC.symbol, ngStock.marketIndex.SOX.symbol];
	
	// 미국 시장지수
	stockService.getStocks(symbols.join(",")).then(function(data){
		angular.forEach(data, function(value, key){
			ngStock.marketIndex[key].stock = value;
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

	// 단일 종목 조회
	var promiseGetStock = null;
	
	this.getStock = function(symbol){
		
		if(promiseGetStock){
			httpService.stop(promiseGetStock);
		}
		
		promiseGetStock = httpService.get({
			url: meta.baseUrl + "api/stock/" + encodeURIComponent(symbol),
		}).then(function(response){
			return response.data;
		});
		
		return promiseGetStock;
	}
	
	// 복수 종목 조회
	var promiseGetStocks = null;
	
	this.getStocks = function(symbols){
		
		if(promiseGetStocks){
			httpService.stop(promiseGetStocks);
		}
		
		promiseGetStocks = httpService.get({
			url: meta.baseUrl + "api/stocks/" + encodeURIComponent(symbols),
		}).then(function(response){
			return response.data;
		});
		
		return promiseGetStocks;
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