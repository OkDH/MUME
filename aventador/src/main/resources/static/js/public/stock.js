app.controller("StockController", function($scope, $filter, httpService, stockService){
	var ngStock = this;
	var financialChart;
	var rsiChart;

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

		// 차트 그리기
		stockService.getEtfHistory(params).then(function(data){

			var financialData = []; // 주가 차트 데이터
			var ma = 20; // 20일 평균
			var sum = 0;
			var avgData = []; // 이동평균 데이터
			var rsiLabels = [];
			var rsiData = []; // rsi 데이터


			for (var i = 0; i < data.length; i++) {
				var item = data[i];
				financialData.push({
					x: new Date(item.stockDate).valueOf(),
					o: item.priceOpen.toFixed(2),
					h: item.priceHigh.toFixed(2),
					l: item.priceLow.toFixed(2),
					c: item.priceClose.toFixed(2),
					rsi: item.rsi.toFixed(2)
				});

				rsiLabels.push(new Date(item.stockDate).valueOf());
				rsiData.push(item.rsi.toFixed(2));

				// 이동평균 구하기
				sum += item.priceClose;
				if(i < ma-1)
					avgData.push({x: new Date(item.stockDate).valueOf()});
				else if(i == ma-1){
					avgData.push({x: new Date(item.stockDate).valueOf(), y: (sum/ma).toFixed(2)});
				} else {
					sum -= data[i-(ma-1)].priceClose;
					avgData.push({x: new Date(item.stockDate).valueOf(), y: (sum/ma).toFixed(2)});
				}

			}

			var ctx = document.getElementById('financialChart').getContext('2d');
			if(financialChart)
				financialChart.destroy();
			financialChart = new Chart(ctx, {
				type: 'candlestick',
				data: {
					datasets: [
						{
							label: 'OHLC',
							data: financialData
						},
						{
							type: 'line',
							label: 'MA(20)',
							data: avgData,
							pointRadius: 0
						}
					]
				},
				options: {
					layout: {
						padding: {
							left: 10
						}
					},
					plugins: {
						legend: { display: false },
						tooltip: {
							callbacks: {
								footer: (tooltipItems) => {
									return 'RsI: ' + tooltipItems[0].parsed.rsi;
								},
							}
						}
					},
				}
			});

			// rsi chart
			var rsiCtx = document.getElementById('rsiChart').getContext('2d');
			if(rsiChart)
				rsiChart.destroy();
			setTimeout(function(){ // 모달의 차트 element가 다 뜨기 전에 그리기 시작하면 오류가 발생함.
				rsiChart = new Chart(rsiCtx, {
					type: 'line',
					data: {
						labels: rsiLabels,
						datasets: [
							{
								label: 'RSI',
								data: rsiData,
								pointRadius: 0,
								fill: {
									target:{
										value: etf.baseRsi
									},
									below: (context) => {
										const chart = context.chart;
										const { ctx, chartArea, data, scales } = chart;
										if(!chartArea) {
											return null;
										}
										return belowGradient(ctx, chartArea, data, scales);
									},
									above: (context) => {
										const chart = context.chart;
										const { ctx, chartArea, data, scales } = chart;
										if(!chartArea) {
											return null;
										}
										return aboveGradient(ctx, chartArea, data, scales);
									},
								},
								borderColor: (context) => {
									const chart = context.chart;
									const { ctx, chartArea, data, scales } = chart;
									if(!chartArea) {
										return null;
									}
									return getGradient(ctx, chartArea, data, scales);
								},
								tension: 0.4,
							}
						]
					},
					options: {
						aspectRatio: 5,
						layout: {
							padding: {
								left: 10
							}
						},
						scales: {
							x: {
								type: 'time',
								time: {
									unit: 'day'
								},
								grid: {
									display: false
								},
								ticks: {
									display: false
								}
							},
							y: {
								grid: {
									display: false
								},
							}
						},
						plugins: {
							legend: { display: false },
						}
					},
					plugins: [
						{
							id: 'dottedLine',
							beforeDatasetsDraw(chart, args, pluginOptions) {
								const { ctx, data, chartArea: {left, right, width}, scales: {x,y} } = chart;

								const startingPoint = etf.baseRsi

								ctx.save();
								ctx.beginPath();
								ctx.lineWidth = 1;
								ctx.setLineDash([1, 5]);
								ctx.strokeStyle = 'rgba(102,102,102,1)';
								ctx.moveTo(left, y.getPixelForValue(etf.baseRsi));
								ctx.lineTo(right, y.getPixelForValue(etf.baseRsi));
								ctx.stroke();
								ctx.closePath();
								ctx.setLineDash([]);

								ctx.beginPath();
								ctx.fillStyle = 'rgba(102,102,102,1)';
								ctx.fillRect(0, y.getPixelForValue(startingPoint) - 10, left, 20);
								ctx.closePath();

								ctx.font = '12px sans-serif';
								ctx.fillStyle = 'white';
								ctx.textBaseline = 'middle';
								ctx.textAlign = 'center';
								ctx.fillText(etf.baseRsi, left / 2, y.getPixelForValue(startingPoint));
							}
						}
					]
				});
			}, 200);

			// https://www.youtube.com/watch?v=Q8np-7coqho&list=PLc1g3vwxhg1UlBvTOSZ3VJUjC_s6S2cMD&index=1
			function getGradient(ctx, chartArea, data, scales) {
				const { left, right, top, bottom, width, height } = chartArea;
				const { x, y } = scales;
				const gradientBorder = ctx.createLinearGradient(0, 0, 0, bottom);
				const shift = y.getPixelForValue(etf.baseRsi) / bottom;

				gradientBorder.addColorStop(0, 'rgba(75,192,192,1)');
				gradientBorder.addColorStop(shift, 'rgba(75,192,192,1)');
				gradientBorder.addColorStop(shift, 'rgba(255,26,104,1)');
				gradientBorder.addColorStop(1, 'rgba(255,26,104,1)');
				return gradientBorder;
			}

			function belowGradient(ctx, chartArea, data, scales) {
				const { left, right, top, bottom, width, height } = chartArea;
				const { x, y } = scales;
				const gradientBackground = ctx.createLinearGradient(0, y.getPixelForValue(etf.baseRsi), 0, bottom);
				gradientBackground.addColorStop(0, 'rgba(255,26,104,0)');
				gradientBackground.addColorStop(1, 'rgba(255,26,104,0.5)');
				return gradientBackground;
			}

			function aboveGradient(ctx, chartArea, data, scales) {
				const { left, right, top, bottom, width, height } = chartArea;
				const { x, y } = scales;
				const gradientBackground = ctx.createLinearGradient(0, y.getPixelForValue(etf.baseRsi), 0, top);
				gradientBackground.addColorStop(0, 'rgba(75,192,192,0)');
				gradientBackground.addColorStop(1, 'rgba(75,192,192,0.5)');
				return gradientBackground;
			}
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