app.controller("InfiniteDashboardController", function($scope, httpService, infiniteService){
	var infiniteDashboard = this;
	infiniteDashboard.query = {
		accountId: null,
		infiniteState: ["진행중","원금소진","매수중지"]
	}
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteDashboard.myAccounts = data;
	});
	
	// 통계 자료 가져오기
	$scope.$watch("infiniteDashboard.query.accountId", function(query){
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
	});
	
	$scope.$watch("infiniteDashboard.query.infiniteState", function(query){
		// 종목 진행률
		infiniteService.getStocks(infiniteDashboard.query).then(function(data){
			infiniteDashboard.stocks = data;
			
			console.log("stocks: " , data);
		});
	}, true);
	
	// 차트 그리기
	// 월별 손익현황 차트
	$scope.$watch("infiniteDashboard.profitMonthly", function(profitMonthly){
		if(!profitMonthly)
			return;
		
		var labels = [];
		var data = [];
		var backgroundColors = [];
		var borderColor = [];

		profitMonthly.forEach(function(item){
			labels.push(item.monthly);
			data.push(item.profit);
			backgroundColors.push(item.profit > 0 ? 'rgba(255, 99, 132, 0.2)': 'rgba(54, 162, 235, 0.2)');
			borderColor.push(item.profit > 0 ? 'rgb(255, 99, 132)': 'rgb(54, 162, 235)');
		});

		// Area Chart Example
		var ctx = document.getElementById("profitMonthlyChart");
		var myBarChart = new Chart(ctx, {
		  type: 'bar',
		  data: {
		  	labels: labels,
		    datasets: [{
		      label: "손익금",
		      backgroundColor: backgroundColors, //  rgba(54, 162, 235, 0.2)
		      borderColor: borderColor, // rgb(54, 162, 235)
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
			      y: {
			        ticks: {
			          maxTicksLimit: 7,
			          padding: 10,
			          // Include a dollar sign in the ticks
			          callback: function(value, index, values) {
			            return value + '$';
			          }
			        },
			        grid: {
			          color: "rgb(234, 236, 244)",
			          zeroLineColor: "rgb(234, 236, 244)",
			          drawBorder: false,
			          borderDash: [2],
			          zeroLineBorderDash: [2]
			        }
			      },
			  },
			  plugins:{
				  legend: {
					  display: false
				  },
				  tooltip: {
					  backgroundColor: "rgb(255,255,255)",
					  bodyColor: "#858796",
					  titleMarginBottom: 10,
					  titleColor: '#6e707e',
					  titleFontSize: 14,
					  borderColor: '#dddfeb',
					  borderWidth: 1,
					  padding: 12,
					  displayColors: false,
					  intersect: false,
					  mode: 'index',
					  caretPadding: 10,
					  callbacks: {
						  label: function(tooltipItem) {
							  return tooltipItem.dataset.label + ': ' + tooltipItem.raw + '$';
						  }
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
		var borderColor = [];

		profitStock.forEach(function(item){
			labels.push(item.symbol);
			data.push(item.profit);
			backgroundColors.push(item.profit > 0 ? 'rgba(255, 99, 132, 0.2)': 'rgba(54, 162, 235, 0.2)');
			borderColor.push(item.profit > 0 ? 'rgb(255, 99, 132)': 'rgb(54, 162, 235)');
		});
		
		// Area Chart Example
		var ctx = document.getElementById("profitStockChart");
		var myBarChart = new Chart(ctx, {
		  type: 'bar',
		  data: {
		  	labels: labels,
		    datasets: [{
		      label: "손익금",
		      backgroundColor: backgroundColors, //  rgba(54, 162, 235, 0.2)
		      borderColor: borderColor, // rgb(54, 162, 235)
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
			      y: {
			        ticks: {
			          maxTicksLimit: 7,
			          padding: 10,
			          // Include a dollar sign in the ticks
			          callback: function(value, index, values) {
			            return value + '$';
			          }
			        },
			        grid: {
			          color: "rgb(234, 236, 244)",
			          zeroLineColor: "rgb(234, 236, 244)",
			          drawBorder: false,
			          borderDash: [2],
			          zeroLineBorderDash: [2]
			        }
			      },
			  },
			  plugins:{
				  legend: {
					  display: false
				  },
				  tooltip: {
					  backgroundColor: "rgb(255,255,255)",
					  bodyColor: "#858796",
					  titleMarginBottom: 10,
					  titleColor: '#6e707e',
					  titleFontSize: 14,
					  borderColor: '#dddfeb',
					  borderWidth: 1,
					  padding: 12,
					  displayColors: false,
					  intersect: false,
					  mode: 'index',
					  caretPadding: 10,
					  callbacks: {
						  label: function(tooltipItem) {
							  return tooltipItem.dataset.label + ': ' + tooltipItem.raw + '$';
						  }
					  }
				  }
			  }
		  }
		});
	});
	
	// 최근 2개월 매수 손익현황 차트
	$scope.$watch("infiniteDashboard.buyDaily", function(buyDaily){
		if(!buyDaily)
			return;
		console.log("buyDaily :" , buyDaily);
	});
	
	// 비중 관련 차트
	$scope.$watch("infiniteDashboard.stocks", function(stocks){
		// TODO : 원금 가져오기
		var seed = 20000;
		var totalBuy = 0; // 전체 매입금
		stocks.forEach(function(item){
			totalBuy += item.buyPrice;
		});
		var totalPer = totalBuy / seed * 100;
		
		// Pie Chart Example
		var ctx = document.getElementById("dallerGravityChart");
		var myPieChart = new Chart(ctx, {
		  type: 'doughnut',
		  data: {
		    labels: ["주식", "현금"],
		    datasets: [{
		      data: [totalPer, 100-totalPer],
		      backgroundColor: ['#f6c23e', '#36b9cc'],
		      hoverBackgroundColor: ['#f5ba25', '#2c9faf'],
		      hoverBorderColor: "rgba(234, 236, 244, 1)",
		    }],
		  },
		  options: {
		    maintainAspectRatio: false,
		    plugins:{
			  legend: {
				  display: false
			  },
			  tooltip: {
			      backgroundColor: "rgb(255,255,255)",
			      bodyColor: "#858796",
			      borderColor: '#dddfeb',
			      borderWidth: 1,
			      padding: 15,
			      caretPadding: 10,
			      callbacks: {
					  label: function(tooltipItem) {
						  return tooltipItem.label + ': ' + tooltipItem.formattedValue + '%';
					  }
				  }
			    }
		    },
		    cutoutPercentage: 80,
		  },
		});

	});
	
});