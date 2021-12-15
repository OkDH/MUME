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
		
		console.log("profitMonthly :" , profitMonthly);
		
		var labels = profitMonthly.map(function(item){
			return item.monthly;
		});
		var data = profitMonthly.map(function(item){
			return item.profit;
		});

		// Area Chart Example
		var ctx = document.getElementById("profitMonthlyChart");
		var myLineChart = new Chart(ctx, {
		  type: 'bar',
		  data: {
		  	labels: labels,
		    datasets: [{
		      label: "손익금",
		      backgroundColor: 'rgba(255, 99, 132, 0.2)', //  rgba(54, 162, 235, 0.2)
		      borderColor: 'rgb(255, 99, 132)', // rgb(54, 162, 235)
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
			        top: 25,
			        bottom: 0
			      }
			  },
			  scales: {
			      y: {
			        ticks: {
			          maxTicksLimit: 5,
			          padding: 10,
			          // Include a dollar sign in the ticks
			          callback: function(value, index, values) {
			            return '$' + value;
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
							  console.log(tooltipItem);
							  return tooltipItem.dataset.label + ': $' + tooltipItem.raw;
						  }
					  }
				  }
			  }
		  }
		});
	});
	
	// 종목별 손익현황 차트
	$scope.$watch("infiniteDashboard.profitStock", function(profitStock){
		if(!profitStock)
			return;
		
		console.log("profitStock :" , profitStock);
	});
	
	// 최근 2개월 매수 손익현황 차트
	$scope.$watch("infiniteDashboard.buyDaily", function(buyDaily){
		if(!buyDaily)
			return;
		console.log("buyDaily :" , buyDaily);
	});
	
});