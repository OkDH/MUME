app.controller("StockController", function($scope, $filter, httpService, stockService){
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

	// 종목 차트 모달
	ngStock.openChartModal = function (etf) {
		ngStock.selectedEtf = etf;
		$('#financialChartModal').modal("show");
	}

	// 종목 n개월 데이터 가져와서 차트 그리기
	$scope.$watch("ngStock.selectedEtf", function(etf){
		if(!etf)
			return;

		var n = 3;
		var now = new Date();
		// var startDate = $filter("printDate")(new Date(new Date().setMonth(now.getMonth() - n)));
		// var endDate = $filter("printDate")(now);

		var startDate = "2021-01-01";
		var endDate = "2021-05-01";

		var params = {
			symbol: etf.symbol,
			startDate: startDate,
			endDate: endDate
		}

		stockService.getEtfHistory(params).then(function(data){
			console.log(data);

			// 차트 그리기
			var ctx = document.getElementById('financialChart').getContext('2d');

			var barData = [];
			data.forEach(function(item){
				barData.push({
					x: new Date(item.stockDate).valueOf(),
					o: item.priceOpen,
					h: item.priceHigh,
					l: item.priceLow,
					c: item.priceClose
				});
			});

			var chart = new Chart(ctx, {
				type: 'candlestick',
				data: {
					datasets: [{
						label: 'CHRT - Chart.js Corporation',
						data: barData
					}]
				}
			});
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

	// 특정 Etf 히스토리 가져오기
	var promiseGetEtfHistory = null;
	this.getEtfHistory = function(params) {
		if(promiseGetEtfHistory){
			httpService.stop(promiseGetEtfHistory);
		}

		promiseGetEtfHistory = httpService.post({
			url: meta.baseUrl + "api/stocks/etfs/history",
			data: params
		}).then(function(response){
			return response.data;
		});

		return promiseGetEtfHistory;
	}
	
});