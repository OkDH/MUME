app.controller("InfiniteDashboardController", function($scope, $filter, httpService, infiniteService){
	var infiniteDashboard = this;
	infiniteDashboard.query = {
		accountId: null,
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
	});
	
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
			infiniteService.getStatistics("buy-stock-daily", infiniteDashboard.query).then(function(data){
				infiniteDashboard.buyStockDaily = data;
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

		// Area Chart Example
		var ctx = document.getElementById("profitMonthlyChart");
		var myBarChart = new Chart(ctx, {
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
		
		// Area Chart Example
		var ctx = document.getElementById("profitStockChart");
		var myBarChart = new Chart(ctx, {
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
			// Pie Chart Example
			var ctx = document.getElementById("dallerGravityChart");
			var myPieChart = new Chart(ctx, {
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
			infiniteDashboard.gravityStocks = angular.copy(stocks);
			
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
			if(date.getDate() == curDate.getDate()){
				infiniteDashboard.state.beforeMonthSumBuyPrice = beforeMonthData[beforeMonthData.length - 1];
				infiniteDashboard.state.sumBuyPriceGap = infiniteDashboard.state.thisMonthSumBuyPrice - infiniteDashboard.state.beforeMonthSumBuyPrice;
			}
			
			// 다음일
			curDate.setDate(curDate.getDate() + 1);
		}
		
		if(beforeMonthData < thisMonthData) // 이번월이 전월보다 일수가 많을경우 전월 하루 추가
			beforeMonthData.push(beforeMonthData[beforeMonthData.length - 1]);
		
		var ctx = document.getElementById("buyDailyChart");
		var myLineChart = new Chart(ctx, {
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
	$scope.$watch("infiniteDashboard.buyStockDaily", function(buyStockDaily){
		if(!buyStockDaily)
			return;
		
		// label
		var labels = [];
		var labelMap = {};
		var labelIndex = 0;
		buyStockDaily.forEach(function(item){
			if(labels.length == 0 || labels[labels.length-1] != item.tradeDate){
				labels.push(item.tradeDate);
				labelMap[item.tradeDate] = labelIndex;
				labelIndex++;
			}
		});
		
		// label 크기 만큼 기본 data 리스트 만들기(빈값 0)
		var commonList = Array.from({length: labels.length}, () => 0);
		
		// data
		var datas = {};
		buyStockDaily.forEach(function(item){
			if(datas[item.symbol] == undefined){
				datas[item.symbol] = {symbol: item.symbol, priceList: angular.copy(commonList), perList: angular.copy(commonList)};
			}
			var index = labelMap[item.tradeDate];
			var price = 0;
			
			if(index == 0)
				price = item.tradePrice;
			else 
				price = datas[item.symbol].priceList[index-1] + item.tradePrice; // 누적을 위한 합산
			datas[item.symbol].priceList[index] = price > 0 ? price : 0; // 마이너스라면, 다 팔린거기 때문에 0;
			datas[item.symbol].perList[index] = price > 0 ? (price / item.seed) * 100 : 0;
		});
		
		// dataset 
		var datasets = [];
		
		// 전체
		var allPriceList = angular.copy(commonList);
		var allPerList = [];
		
		// symbol 들
		Object.keys(datas).forEach(function(symbol){
			datasets.push({
				label: symbol,
				lineTension: 0.1,
				borderWidth: 2,
				borderDash: [10, 3],
				fill: false,
				borderColor: infiniteDashboard.colors[datasets.length % infiniteDashboard.colors.length],
				backgroundColor: infiniteDashboard.colors[datasets.length % infiniteDashboard.colors.length],
		        pointBackgroundColor: infiniteDashboard.colors[datasets.length % infiniteDashboard.colors.length],
		        pointRadius: 0,
		        pointHoverRadius: 4,
				data: datas[symbol].perList
			});
			
			// 전체 데이터 누적
			datas[symbol].priceList.forEach(function(o, i){
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
			pointRadius: 2,
			fill: false,
			borderColor: infiniteDashboard.colors[3],
			backgroundColor: infiniteDashboard.colors[3],
	        pointBackgroundColor: infiniteDashboard.colors[3],
	        pointHoverRadius: 4,
			data: allPerList
		});
		
		var ctx = document.getElementById("buyDailyStockChart");
		var myLineChart = new Chart(ctx, {
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
		          return datasetLabel + ': ' + $filter('number')(tooltipItem.yLabel, 2) + '%';
		        }
		      }
		    }
		  }
		});
		
	}, true);
	
});