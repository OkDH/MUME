app.controller("MarketIndexController", function($scope, $timeout, $q, stockService){
	
	var marketIndex = this;
	
//	marketIndex = {
//			DJI : { symbol : "^DJI"	},
//			IXIC : { symbol : "^IXIC" },
//			GSPC : { symbol : "^GSPC" },
//			SOX : { symbol : "^SOX" },
//			ESF : { symbol : "ES=F" },
//			NQF : { symbol : "NQ=F" },
//			YMF : { symbol : "YM=F" },
//		};
	
	// 시장지수
	var symbols = ["^DJI", "^IXIC", "^GSPC", "^SOX",
					"ES=F", "NQ=F", "YM=F", "RTY=F", 
					"KRW=X", "CL=F", "GC=F", "SI=F", 
					"^TNX", "^VIX", "^KS11", "^KQ11",
					"000001.SS", "^N225", "^GDAXI", "BTC-KRW"];
	
	stockService.getStocks(symbols.join(",")).then(function(data){
		angular.forEach(data, function(value, key){
			marketIndex[key] = {}
			marketIndex[key].stock = value;
		});
	});
	
});