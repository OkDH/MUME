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
	
	stockService.getStocks(symbols.join(",")).then(function(data){
		angular.forEach(data, function(value, key){
			ngStock.marketIndex[key].stock = value;
		});
	});
	
	stockService.getEtfs().then(function(data){
		ngStock.etfs = data;
	});
	
});

app.service("stockService", function(httpService){
	// 단일
	var promiseGetStock = null;
	
	this.getStock = function(symbol){
		
		if(promiseGetStock){
			httpService.stop(promiseGetStock);
		}
		
		promiseGetStock = httpService.get({
			url: meta.baseUrl + "api/stock/" + symbol,
		}).then(function(response){
			return response.data;
		});
		
		return promiseGetStock;
	}
	
	// 복수
	var promiseGetStocks = null;
	
	this.getStocks = function(symbols){
		
		if(promiseGetStocks){
			httpService.stop(promiseGetStocks);
		}
		
		promiseGetStocks = httpService.get({
			url: meta.baseUrl + "api/stocks/" + symbols,
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