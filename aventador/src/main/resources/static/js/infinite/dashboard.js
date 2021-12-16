app.controller("InfiniteDashboardController", function($scope, httpService, infiniteService){
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
		'#4e73df'
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
			
			// TODO : 원금 가져오기
			infiniteDashboard.state.seed = 20000;
			
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
			data.push(item.profit);
			backgroundColors.push(item.profit > 0 ? 'rgb(255, 99, 132)': 'rgb(54, 162, 235)');
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
			        left: 10,
			        right: 25,
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
			          return datasetLabel + ': ' + tooltipItem.yLabel + '$';
			        }
			  	 }
			  },
			  plugins: {
				  labels: {
					  render: function (args) {
					      return args.value + '$';
					  },
					  fontColor: function (args) {
						  return args.value > 0 ? '#6f6f6f' : '#ffffff';
					  }
				  }
			  }
		  }
		});
	});
	
	// 종목별 누적 손익 차트
	$scope.$watch("infiniteDashboard.profitStock", function(profitStock){
		if(!profitStock)
			return;
		
		var labels = [];
		var data = [];
		var backgroundColors = [];

		profitStock.forEach(function(item){
			labels.push(item.symbol);
			data.push(item.profit);
			backgroundColors.push(item.profit > 0 ? 'rgb(255, 99, 132)': 'rgb(54, 162, 235)');
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
			        left: 10,
			        right: 25,
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
							return value + '$';
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
			          return datasetLabel + ': ' + tooltipItem.yLabel + '$';
			        }
			  	 }
			  },
			  plugins: {
				  labels: {
					  render: function (args) {
					      return args.value + '$';
					  },
					  fontColor: function (args) {
						  return args.value > 0 ? '#6f6f6f' : '#ffffff';
					  }
				  }
			  }
		  }
		});
	});
	
	// 비중 관련 차트
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
			      data: [infiniteDashboard.state.sumInfiniteBuyPrice, infiniteDashboard.state.seed-infiniteDashboard.state.sumInfiniteBuyPrice],
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
				        	return datasetLabel + ': ' + chart.datasets[0].data[tooltipItem.index] + '$';
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

	});
	
	// 최근 2개월 매수 손익현황 차트
	$scope.$watch("infiniteDashboard.buyDaily", function(buyDaily){
		if(!buyDaily)
			return;
		console.log("buyDaily :" , buyDaily);
		
		var labels = [];
		var dataBefore = [];
		var dataThis = [];
		// 전월
		buyDaily.beforeMonth.forEach(function(item){
			var buyPrice = dataBefore.length == 0 ? 0 : dataBefore[dataBefore.length-1];
			dataBefore.push(buyPrice + item.buyPrice);
		});
		
		// 이번달
		buyDaily.thisMonth.forEach(function(item){
			var buyPrice = dataThis.length == 0 ? 0 : dataThis[dataBefore.length-1];
			dataThis.push(buyPrice + item.buyPrice);
		});
		
		var ctx = document.getElementById("buyDailyChart");
		var myLineChart = new Chart(ctx, {
		  type: 'line',
		  data: {
		    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
		    datasets: [{
		      label: "Earnings",
		      lineTension: 0.1,
		      backgroundColor: "rgba(78, 115, 223, 0.05)",
		      borderColor: "rgba(78, 115, 223, 1)",
		      pointRadius: 3,
		      pointBackgroundColor: "rgba(78, 115, 223, 1)",
		      pointBorderColor: "rgba(78, 115, 223, 1)",
		      pointHoverRadius: 3,
		      pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
		      pointHoverBorderColor: "rgba(78, 115, 223, 1)",
		      pointHitRadius: 10,
		      pointBorderWidth: 0,
		      data: dataBefore,
		    },
		    {
		      label: "Earnings",
		      lineTension: 0.3,
		      backgroundColor: "rgba(78, 115, 223, 0.05)",
		      borderColor: "rgba(78, 115, 223, 1)",
		      pointRadius: 3,
		      pointBackgroundColor: "rgba(78, 115, 223, 1)",
		      pointBorderColor: "rgba(78, 115, 223, 1)",
		      pointHoverRadius: 3,
		      pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
		      pointHoverBorderColor: "rgba(78, 115, 223, 1)",
		      pointHitRadius: 10,
		      pointBorderWidth: 2,
		      data: dataThis,
		    }],
		  },
		  options: {
		    maintainAspectRatio: false,
		    layout: {
		      padding: {
		        left: 10,
		        right: 25,
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
		          maxTicksLimit: 7
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
		      display: false
		    },
		    tooltips: {
		      backgroundColor: "rgb(255,255,255)",
		      bodyFontColor: "#858796",
		      titleMarginBottom: 10,
		      titleFontColor: '#6e707e',
		      titleFontSize: 14,
		      borderColor: '#dddfeb',
		      borderWidth: 1,
		      xPadding: 15,
		      yPadding: 15,
		      displayColors: false,
		      intersect: false,
		      mode: 'index',
		      caretPadding: 10,
		      callbacks: {
		        label: function(tooltipItem, chart) {
		          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
		          return datasetLabel + ': ' + tooltipItem.yLabel + '$';
		        }
		      }
		    }
		  }
		});

	});
	
});