app.controller("InfiniteDashboardController", function($scope, $filter, httpService, infiniteService){
	var infiniteDashboard = this;
	
	// chartjs object
	infiniteDashboard.chart = {}
	
	infiniteDashboard.query = {
		accountId: 'ALL',
		infiniteState: ["진행중","원금소진","매수중지"]
	}
	
	// 색상 배열
	infiniteDashboard.colors = [
		'#5cb85c',
		'#5bc0de',
		'#f0ad4e',
		'#d9534f',
		'#4e73df',
		'#6f42c1',
		'#20c9a6',
		'#f6c23e',
		'#e83e8c',
		'#5a5c69'
	]
	
	// 색상 반환
	infiniteDashboard.getColor = function(index){
		return infiniteDashboard.colors[index % infiniteDashboard.colors.length];
	}
	
	// 기본 상태 정보
	infiniteDashboard.state = {};
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteDashboard.myAccounts = data;
		
		// 계좌 order map
		infiniteDashboard.myAccountsOrder = {};
		data.forEach(function(d){
			infiniteDashboard.myAccountsOrder[d.accountId] = {};
			infiniteDashboard.myAccountsOrder[d.accountId].order = d.accountOrder;
			infiniteDashboard.myAccountsOrder[d.accountId].alias = d.accountAlias;
		});
	});
	
	// 필터 모달
	infiniteDashboard.openFilterModal = function(){
		$('#filterModal').modal("show");
		// selectpicker
		$("#filterModal #accountSelect").selectpicker("refresh");
	}
	
	// 통계 자료 가져오기
	$scope.$watch("infiniteDashboard.query.accountId", function(query){
		
		// 계좌 내 종목 현황 조회
		infiniteService.getAccountState({accountId:query}).then(function(data){
			infiniteDashboard.state = data;
			
			// 월별 손익현황
			infiniteService.getStatistics("profit-monthly", infiniteDashboard.query).then(function(data){
				infiniteDashboard.profitMonthly = data;
			});
			
			// 종목별 손익현황
			infiniteService.getStatistics("profit-stock", infiniteDashboard.query).then(function(data){
				infiniteDashboard.profitStock = data;
			});
			
			// 최근 2개월 매수금액
			infiniteService.getStatistics("buy-daily", infiniteDashboard.query).then(function(data){
				infiniteDashboard.buyDaily = data;
			});
			
			// 종목 일별 매매금액
			infiniteService.getStatistics("runout-rate", infiniteDashboard.query).then(function(data){
				infiniteDashboard.runoutRate = data;
			});
			
			// 종목 진행률
			infiniteService.getStocks(infiniteDashboard.query).then(function(data){
				infiniteDashboard.stocks = data;
			});
		});
	});
	
	// 차트 그리기
	// 월별 손익현황 차트
	$scope.$watch("infiniteDashboard.profitMonthly", function(profitMonthly){
		if(!profitMonthly)
			return;
		
		var labels = [];
		var data = [];
		var backgroundColors = [];
		
		profitMonthly.forEach(function(item){
			labels.push(item.monthly);
			data.push(item.income);
			backgroundColors.push(item.income > 0 ? 'rgb(255, 99, 132)': 'rgb(54, 162, 235)');
		});
		
		// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
		if(infiniteDashboard.chart.profitMonthlyChart)
			infiniteDashboard.chart.profitMonthlyChart.destroy();
		
		// 차트 그리기
		var ctx = document.getElementById("profitMonthlyChart");
		infiniteDashboard.chart.profitMonthlyChart = new Chart(ctx, {
		  type: 'bar',
		  data: {
		  	labels: labels,
		    datasets: [{
		      label: "손익금",
		      backgroundColor: backgroundColors,
		      borderWidth: 1,
		      data: data
		    }],
		  },
		  options: {
			  maintainAspectRatio: false,
			  layout: {
			      padding: {
			        left: 0,
			        right: 0,
			        top: 0,
			        bottom: 0
			      }
			  },
			  scales: {
				yAxes: [{
					ticks: {
						maxTicksLimit: 5,
						padding: 5,
						// Include a dollar sign in the ticks
						callback: function(value, index, values) {
							return $filter('number')(value, 0) + '$';
						}
					},
				}],
			  },
			  legend: {
				  display: false
			  },
			  tooltips: {
			      titleMarginBottom: 10,
			      titleFontColor: '#6e707e',
			      titleFontSize: 14,
			      backgroundColor: "rgb(255,255,255)",
			      bodyFontColor: "#858796",
			      borderColor: '#dddfeb',
			      borderWidth: 1,
			      xPadding: 12,
			      yPadding: 12,
			      displayColors: false,
			      caretPadding: 10,
			      callbacks: {
			        label: function(tooltipItem, chart) {
			          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
			          return datasetLabel + ': ' + $filter('number')(tooltipItem.yLabel, 2) + '$';
			        }
			  	 }
			  },
			  plugins: {
				  labels: {
					  render: function (args) {
					      return $filter('number')(args.value, 2) + '$';
					  },
					  fontColor: function (args) {
						  return args.value > 0 ? '#6f6f6f' : '#ffffff';
					  }
				  }
			  }
		  }
		});
	}, true);
	
	// 종목별 누적 손익 차트
	$scope.$watch("infiniteDashboard.profitStock", function(profitStock){
		if(!profitStock)
			return;
		
		var labels = [];
		var data = [];
		var backgroundColors = [];

		profitStock.forEach(function(item){
			labels.push(item.symbol);
			data.push(item.income);
			backgroundColors.push(item.income > 0 ? 'rgb(255, 99, 132)': 'rgb(54, 162, 235)');
		});
		
		// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
		if(infiniteDashboard.chart.profitStockChart)
			infiniteDashboard.chart.profitStockChart.destroy();
		
		// 차트 그리기
		var ctx = document.getElementById("profitStockChart");
		infiniteDashboard.chart.profitStockChart = new Chart(ctx, {
		  type: 'bar',
		  data: {
		  	labels: labels,
		    datasets: [{
		      label: "손익금",
		      backgroundColor: backgroundColors,
		      borderWidth: 1,
		      data: data
		    }],
		  },
		  options: {
			  maintainAspectRatio: false,
			  layout: {
			      padding: {
			        left: 0,
			        right: 0,
			        top: 0,
			        bottom: 0
			      }
			  },
			  scales: {
				yAxes: [{
					ticks: {
						maxTicksLimit: 5,
						padding: 5,
						// Include a dollar sign in the ticks
						callback: function(value, index, values) {
							return $filter('number')(value, 0) + '$';
						}
					}
				}],
			  },
			  legend: {
				  display: false
			  },
			  tooltips: {
			      titleMarginBottom: 10,
			      titleFontColor: '#6e707e',
			      titleFontSize: 14,
			      backgroundColor: "rgb(255,255,255)",
			      bodyFontColor: "#858796",
			      borderColor: '#dddfeb',
			      borderWidth: 1,
			      xPadding: 12,
			      yPadding: 12,
			      displayColors: false,
			      caretPadding: 10,
			      callbacks: {
			        label: function(tooltipItem, chart) {
			          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
			          return datasetLabel + ': ' + $filter('number')(tooltipItem.yLabel, 2) + '$';
			        }
			  	 }
			  },
			  plugins: {
				  labels: {
					  render: function (args) {
					      return $filter('number')(args.value, 2) + '$';
					  },
					  fontColor: function (args) {
						  return args.value > 0 ? '#6f6f6f' : '#ffffff';
					  }
				  }
			  }
		  }
		});
	}, true);
	
	// 비중 관련 차트, 일별 시드 사용률 차트
	$scope.$watch("infiniteDashboard.stocks", function(stocks){
		if(!stocks)
			return;
		
		// 현금비중 파이 차트
		{
			// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
			if(infiniteDashboard.chart.dallerGravityChart)
				infiniteDashboard.chart.dallerGravityChart.destroy();
			
			// 차트 그리기
			var ctx = document.getElementById("dallerGravityChart");
			infiniteDashboard.chart.dallerGravityChart = new Chart(ctx, {
			  type: 'doughnut',
			  data: {
			    labels: ["주식", "현금"],
			    datasets: [{
			      data: [infiniteDashboard.state.sumInfiniteBuyPrice, infiniteDashboard.state.sumAccountSeed-infiniteDashboard.state.sumInfiniteBuyPrice],
			      backgroundColor: ['#f6c23e', '#36b9cc'],
			      hoverBackgroundColor: ['#f5ba25', '#2c9faf'],
			      hoverBorderColor: "rgba(234, 236, 244, 1)",
			    }],
			  },
			  options: {
				  maintainAspectRatio: false,
				  legend: {
					  display: false
				  },
				  tooltips: {
				      titleMarginBottom: 10,
				      titleFontColor: '#6e707e',
				      titleFontSize: 14,
				      backgroundColor: "rgb(255,255,255)",
				      bodyFontColor: "#858796",
				      borderColor: '#dddfeb',
				      borderWidth: 1,
				      xPadding: 12,
				      yPadding: 12,
				      displayColors: false,
				      caretPadding: 10,
				      callbacks: {
				        label: function(tooltipItem, chart) {
				        	var datasetLabel = chart.labels[tooltipItem.index] || '';
				        	return datasetLabel + ': ' + $filter('number')(chart.datasets[0].data[tooltipItem.index], 2) + '$';
				        }
				  	 }
				  },
				  plugins: {
					  labels: {
						  render: 'percent',
						  fontColor: '#ffffff'
					  }
				  }
			  },
			});
		}
		
		// 종목비중 정렬
		{
			infiniteDashboard.gravityStocks = [];
			
			// 같은 종목은 매입금을 합쳐줌
			var datas = {};
			stocks.forEach(function(stock){
				if(datas[stock.symbol] == undefined)
					datas[stock.symbol] = { buyPrice : 0 };
				datas[stock.symbol].buyPrice = datas[stock.symbol].buyPrice + stock.buyPrice;
			});
			
			Object.keys(datas).forEach(function(symbol){
				infiniteDashboard.gravityStocks.push({symbol: symbol, buyPrice: datas[symbol].buyPrice});
			});
			
			// 매입금 순으로 정렬
			infiniteDashboard.gravityStocks.sort(function(o1, o2){
				return o2.buyPrice - o1.buyPrice; // 내림차순
			});
		}
		
	}, true);
	
	// 최근 2개월 매수 손익현황 차트
	$scope.$watch("infiniteDashboard.buyDaily", function(buyDaily){
		if(!buyDaily)
			return;
		
		var labels = [];
		
		var thisDataMap = {};
		var beforeDataMap = {};
		
		{
			// 이번달 데이터 세팅
			buyDaily.thisMonth.forEach(function(item){
				thisDataMap[item.tradeDate] = item.buyPrice;
			});
			
			// 전월 데이터 세팅
			buyDaily.beforeMonth.forEach(function(item){
				beforeDataMap[item.tradeDate] = item.buyPrice;
			});
		}
		
		// 최근 2개월 날짜 리스트 만들기
		var date = new Date();
		var y = date.getFullYear();
		var m = date.getMonth();
		
		// 이번달 1일과 마지막일 변수
		var curDate = new Date(y, m, 1);
		var lastDate = new Date(y, m+1, 1);
		var today = new Date(y, m, date.getDate());
		
		var thisMonthData = [];
		var lastPointRadius = [];
		
		while(curDate <= lastDate){
			var textDate = $filter("printDate")(curDate);
			labels.push(textDate.substring(5).replace("-", "."));
			
			if(curDate <= today){ // 오늘일자까지만 데이터 만들기
				if(thisDataMap[textDate] != undefined){
					if(thisMonthData.length == 0){
						thisMonthData.push(thisDataMap[textDate]);
					} else {
						// 누적
						thisMonthData.push(thisDataMap[textDate] + thisMonthData[thisMonthData.length - 1]);
					}
				} else {
					if(thisMonthData.length == 0){
						thisMonthData.push(0);
					} else {
						// 누적
						thisMonthData.push(thisMonthData[thisMonthData.length - 1]);
					}
				}
				// 마지막 포인트만 5
				if(curDate.getMonth() == today.getMonth() && curDate.getDate() == today.getDate())
					lastPointRadius.push(5);
				else 
					lastPointRadius.push(0);
			}
			// 다음일
			curDate.setDate(curDate.getDate() + 1);
		}
		
		// 이번달 총 매입금
		infiniteDashboard.state.thisMonthSumBuyPrice = thisMonthData[thisMonthData.length - 1];
		
		// 전월 데이터 만들기
		var curDate = new Date(y, m-1, 1);
		var lastDate = new Date(y, m, 1);
		
		var beforeMonthData = [];
		
		while(curDate <= lastDate){
			var textDate = $filter("printDate")(curDate);
			
			if(beforeDataMap[textDate] != undefined){
				if(beforeMonthData.length == 0){
					beforeMonthData.push(beforeDataMap[textDate]);
				} else {
					// 누적
					beforeMonthData.push(beforeDataMap[textDate] + beforeMonthData[beforeMonthData.length - 1]);
				}
			} else {
				if(beforeMonthData.length == 0){
					beforeMonthData.push(0);
				} else {
					// 누적
					beforeMonthData.push(beforeMonthData[beforeMonthData.length - 1]);
				}
			}
			
			// 전월 이맘 때 매입금
			if(date.getDate() >= curDate.getDate()){
				infiniteDashboard.state.beforeMonthSumBuyPrice = beforeMonthData[beforeMonthData.length - 1];
				infiniteDashboard.state.sumBuyPriceGap = infiniteDashboard.state.thisMonthSumBuyPrice - infiniteDashboard.state.beforeMonthSumBuyPrice;
			}
			
			// 다음일
			curDate.setDate(curDate.getDate() + 1);
		}
		
		if(beforeMonthData < thisMonthData) // 이번월이 전월보다 일수가 많을경우 전월 하루 추가
			beforeMonthData.push(beforeMonthData[beforeMonthData.length - 1]);
		
		// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
		if(infiniteDashboard.chart.buyDailyChart)
			infiniteDashboard.chart.buyDailyChart.destroy();
		
		// 차트 그리기
		var ctx = document.getElementById("buyDailyChart");
		infiniteDashboard.chart.buyDailyChart = new Chart(ctx, {
		  type: 'line',
		  data: {
		    labels: labels,
		    datasets: [
		     {
			      label: (m+1)+"월",
			      lineTension: 0.3,
			      fill: false,
			      pointRadius: lastPointRadius,
			      backgroundColor: "rgba(78, 115, 223, 1)",
			      borderColor: "rgba(78, 115, 223, 1)",
			      pointBackgroundColor: "rgba(78, 115, 223, 1)",
			      pointBorderColor: "rgba(78, 115, 223, 1)",
			      pointHoverRadius: 7,
			      pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
			      pointHoverBorderColor: "rgba(78, 115, 223, 1)",
			      data: thisMonthData,
			 },
		     {
			      label: (m != 0 ? m : 12)+"월",
			      lineTension: 0.1,
			      backgroundColor: "rgba(204, 204, 204, 0.3)",
			      borderColor: "#CCCCCC",
			      pointRadius: 0,
			      pointBackgroundColor: "rgba(78, 115, 223, 1)",
			      pointBorderColor: "rgba(78, 115, 223, 1)",
			      pointHoverRadius: 0,
			      pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
			      pointHoverBorderColor: "rgba(78, 115, 223, 1)",
			      data: beforeMonthData,
		     }
		    ],
		  },
		  options: {
		    maintainAspectRatio: false,
		    layout: {
		      padding: {
		        left: 0,
		        right: 0,
		        top: 10,
		        bottom: 0
		      }
		    },
		    scales: {
		      xAxes: [{
		        time: {
		          unit: 'date'
		        },
		        gridLines: {
		          display: false,
		          drawBorder: false
		        },
		        ticks: {
		          maxTicksLimit: 5
		        }
		      }],
		      yAxes: [{
		        ticks: {
		          maxTicksLimit: 5,
		          padding: 10,
		          // Include a dollar sign in the ticks
		          callback: function(value, index, values) {
		            return value + '$';
		          }
		        },
		        gridLines: {
		          color: "rgb(234, 236, 244)",
		          zeroLineColor: "rgb(234, 236, 244)",
		          drawBorder: false,
		          borderDash: [2],
		          zeroLineBorderDash: [2]
		        }
		      }],
		    },
		    legend: {
		      display: true,
		      position: 'bottom'
		    },
		    tooltips: {
		      titleMarginBottom: 10,
			  titleFontColor: '#6e707e',
			  titleFontSize: 14,
		      backgroundColor: 'rgb(255,255,255)',
		      bodyFontColor: '#858796',
		      borderColor: '#dddfeb',
		      borderWidth: 1,
		      xPadding: 15,
		      yPadding: 15,
		      displayColors: false,
		      mode:'index',	
		      callbacks: {
		        label: function(tooltipItem, chart) {
		          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
		          return datasetLabel + ': ' + $filter('number')(tooltipItem.yLabel, 2) + '$';
		        }
		      }
		    }
		  }
		});
	}, true);
	
	// 일별 종목 시드 소진률
	$scope.$watch("infiniteDashboard.runoutRate", function(runoutRate){
		if(!runoutRate)
			return;
		
		var stockList = runoutRate.stockList;
		var dateList = runoutRate.dateList;
		
		// dateList 크기 만큼 기본 data 리스트 만들기(빈값으로 채움)
		var commonList = Array.from({length: dateList.length}, () => 0);
		
		// 종목별로 거래일자 obj를 만듬
		var datas = {}
		stockList.forEach(function(stock){
			var key = stock.accountId + stock.symbol;
			if(datas[key] == undefined){
				datas[key] = {};
				datas[key].symbol = stock.symbol;
				datas[key].history = {};
				datas[key].priceList = [];
				datas[key].perList = [];
			}
			
			stock.averagePriceList.forEach(function(item){
				item.seed = stock.seed;
				datas[key].history[item.tradeDate] = item;
			});
		});

		// label
		var labels = [];
		
		// 일자별로 순회하면서 소진률 리스트를 만듬
		dateList.forEach(function(item, i){
			labels.push(item.stockDate);
			
			Object.keys(datas).forEach(function(key){
				
				if(datas[key].history[item.stockDate] != undefined){
					var d = datas[key].history[item.stockDate];
					
					// 매입금액
					var price = d.averagePrice * d.holdingQuantity;
					datas[key].priceList[i] = price;
					datas[key].perList[i] = price > 0 ? (price / d.seed) * 100 : 0;
				} else { // 해당 일자의 거래 내역이 없다면 전일 가격 그대로 push
					if(i == 0){
						datas[key].priceList.push(0);
						datas[key].perList.push(0);
					} else {
						datas[key].priceList.push(datas[key].priceList[i-1]);
						datas[key].perList.push(datas[key].perList[i-1]);
					}
				}
			});
		});
		
		// dataset 
		var datasets = [];
		
		// 전체
		var allPriceList = angular.copy(commonList);
		var allPerList = [];
		
		// symbol 들
		Object.keys(datas).forEach(function(key){
			datasets.push({
				label: datas[key].symbol,
				lineTension: 0.1,
				borderWidth: 2,
				borderDash: [10, 3],
				fill: false,
				borderColor: infiniteDashboard.colors[datasets.length % infiniteDashboard.colors.length],
				backgroundColor: infiniteDashboard.colors[datasets.length % infiniteDashboard.colors.length],
		        pointBackgroundColor: infiniteDashboard.colors[datasets.length % infiniteDashboard.colors.length],
		        pointRadius: 0,
		        pointHoverRadius: 4,
				data: datas[key].perList
			});
			
			// 전체 데이터 누적
			datas[key].priceList.forEach(function(o, i){
				allPriceList[i] = allPriceList[i] + o;
			});
		});
		
		allPriceList.forEach(function(price){
			allPerList.push((price / infiniteDashboard.state.sumAccountSeed) * 100); 
		});
		
		datasets.unshift({
			label: '전체',
			lineTension: 0.1,
			borderWidth: 3,
			pointRadius: 1,
			fill: false,
			borderColor: infiniteDashboard.colors[3],
			backgroundColor: infiniteDashboard.colors[3],
	        pointBackgroundColor: infiniteDashboard.colors[3],
	        pointHoverRadius: 4,
			data: allPerList
		});
		
		// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
		if(infiniteDashboard.chart.runoutRateChart)
			infiniteDashboard.chart.runoutRateChart.destroy();
		
		// 차트 그리기
		var ctx = document.getElementById("runoutRateChart");
		infiniteDashboard.chart.runoutRateChart = new Chart(ctx, {
		  type: 'line',
		  data: {
		    labels: labels.map(x => x.substring(5).replace("-", ".")),
		    datasets: datasets,
		  },
		  options: {
		    maintainAspectRatio: false,
		    layout: {
		      padding: {
		        left: 0,
		        right: 0,
		        top: 10,
		        bottom: 0
		      }
		    },
		    scales: {
		      xAxes: [{
		        time: {
		          unit: 'date'
		        },
		        gridLines: {
		          display: false,
		          drawBorder: false
		        },
		        ticks: {
		        }
		      }],
		      yAxes: [{
		        ticks: {
		          padding: 10,
		          // Include a dollar sign in the ticks
		          callback: function(value, index, values) {
		            return value + '%';
		          }
		        },
		        gridLines: {
		          color: "rgb(234, 236, 244)",
		          zeroLineColor: "rgb(234, 236, 244)",
		          drawBorder: false,
		          zeroLineBorderDash: [2]
		        }
		      }],
		    },
		    legend: {
		      display: true,
		      position: 'bottom'
		    },
		    tooltips: {
		      titleMarginBottom: 10,
			  titleFontColor: '#6e707e',
			  titleFontSize: 14,
		      backgroundColor: 'rgb(255,255,255)',
		      bodyFontColor: '#858796',
		      borderColor: '#dddfeb',
		      borderWidth: 1,
		      xPadding: 15,
		      yPadding: 15,
		      displayColors: false,
		      mode:'index',	
		      callbacks: {
		        label: function(tooltipItem, chart) {
		          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
		          return datasetLabel + ': ' + $filter('number')(tooltipItem.yLabel, 1) + '%';
		        }
		      }
		    }
		  }
		});
		
	}, true);
	
});