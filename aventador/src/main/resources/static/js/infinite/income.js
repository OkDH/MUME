app.controller("InfiniteIncomeController", function($scope, $filter, httpService, infiniteService){
	var infiniteIncome = this;
	
	var now = new Date();
	infiniteIncome.query = {
		accountId: 'ALL',
		sellDateStart: new Date(new Date().setFullYear(now.getFullYear() - 1)).toISOString().split("T")[0], // 1년
		sellDateEnd: now.toISOString().split("T")[0],
		order: 'DESC'
	}
	infiniteIncome.filter = {
		date: '1년'
	}
	
	infiniteIncome.stats = {
		totalBuy: 0,
		totalSell: 0,
		totalIncome: 0
	}
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteIncome.myAccounts = data;
		
		// 계좌 order map
		infiniteIncome.myAccountsOrder = {};
		data.forEach(function(d){
			infiniteIncome.myAccountsOrder[d.accountId] = {};
			infiniteIncome.myAccountsOrder[d.accountId].order = d.accountOrder;
			infiniteIncome.myAccountsOrder[d.accountId].alias = d.accountAlias;
		});
	});
	
	// 필터 모달
	infiniteIncome.openFilterModal = function(){
		$('#filterModal').modal("show");
		// selectpicker
		$("#filterModal #accountSelect").selectpicker("refresh");
	}
	
	$scope.$watch("infiniteIncome.filter", function(filter){
		if(!filter)
			return;
		if(filter.date == "1개월"){
			infiniteIncome.query.sellDateStart = new Date(new Date().setMonth(now.getMonth() - 1)).toISOString().split("T")[0];
			infiniteIncome.query.sellDateEnd = now.toISOString().split("T")[0];
		} else if(filter.date == "3개월"){
			infiniteIncome.query.sellDateStart = new Date(new Date().setMonth(now.getMonth() - 3)).toISOString().split("T")[0];
			infiniteIncome.query.sellDateEnd = now.toISOString().split("T")[0];
		} else if(filter.date == "1년"){
			infiniteIncome.query.sellDateStart = new Date(new Date().setFullYear(now.getFullYear() - 1)).toISOString().split("T")[0];
			infiniteIncome.query.sellDateEnd = now.toISOString().split("T")[0];
		}
	}, true);
	
	$scope.$watch("infiniteIncome.query", function(query){
		
		// 손익현황 가져오기
		infiniteService.promiseGetIncome("income", query).then(function(data){
			infiniteIncome.income = data;
			infiniteIncome.stats.totalBuy = 0;
			infiniteIncome.stats.totalSell = 0;
			infiniteIncome.stats.totalIncome = 0;
			
			infiniteIncome.income.forEach(function(item){
				// 총매수금
				infiniteIncome.stats.totalBuy += item.buyPrice;
				// 총매도금
				infiniteIncome.stats.totalSell += item.sellPrice;
				// 총실현손익
				infiniteIncome.stats.totalIncome += item.income;
			});
		});
		
		// 종목별 손익현황 가져오기
		infiniteService.promiseGetIncome("stock", query).then(function(data){
			infiniteIncome.incomeByStock = data;
		});
		
		// 월별 손익현황 가져오기
		infiniteService.promiseGetIncome("monthly", query).then(function(data){
			infiniteIncome.incomeByMonthly = data;
		});
		
	}, true);
	
	
	infiniteIncome.detail = {};
	
	// 손익현황 상세 모달
	infiniteIncome.openDetailModal = function(item){
		$('#detailModal').modal("show");
		infiniteIncome.detail = angular.copy(item);
		
		infiniteService.getStock({accountId: item.accountId, infiniteId: item.infiniteId}).then(function(data){
			if(!data)
				return;
			
			infiniteIncome.detail.stock = data;
		});
	}
	
	// 매도 흐름 차트
	infiniteIncome.report = {};
	
	// 보고서 차트
	$scope.$watch("infiniteIncome.detail.stock", function(stock){
		if(!stock)
			return;
		
		var stockList = stock.stockList;
		
		// 일자별로 map 만들기
		var datas = {};
		stock.averagePriceList.forEach(function(item){
			datas[item.tradeDate] = item;
		});
		
		// label
		var labels = [];
		
		// 평단가 
		var averagePriceList = [];
		// 5% 매도
		var sellList1 = [];
		// 10% 매도
		var sellList2 = [];
		// 고가
		var highList = [];
		
		// 데이터 리스트 채우기
		stock.stockList.forEach(function(item){
			// label
			labels.push(item.stockDate);
			
			// 고가
			highList.push(item.priceHigh);
			
			// 평단가
			var averagePrice;
			if(datas[item.stockDate])
				averagePrice = datas[item.stockDate].averagePrice;
			else
				averagePrice = averagePriceList[averagePriceList.length-1];
			
			averagePriceList.push(averagePrice);
			
			// 5% 매도
			sellList1.push((averagePrice * 1.0517).toFixed(2));
			
			// 10% 매도
			sellList2.push((averagePrice * 1.1017).toFixed(2));
		});
		
		// 매도 흐름 차트
		{
			// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
			if(infiniteIncome.report.sellFlowChart)
				infiniteIncome.report.sellFlowChart.destroy();
			
			// 차트 그리기
			var ctx = document.getElementById("sellFlowChart");
			infiniteIncome.report.sellFlowChart = new Chart(ctx, {
			  type: 'line',
			  data: {
			    labels: labels.map(x => x.replaceAll("-", ".").substring(2)),
			    datasets: [
			    	{
						label: '고가',
						lineTension: 0.1,
						borderWidth: 2,
						pointRadius: 2,
						fill: false,
						borderColor: '#4e73df',
						backgroundColor: '#4e73df',
				        pointBackgroundColor: '#4e73df',
				        pointHoverRadius: 3,
						data: highList
					},
			    	{
			    		label: '5%매도',
			    		lineTension: 0.1,
			    		borderWidth: 2,
			    		pointRadius: 1,
			    		fill: false,
			    		borderColor: '#f0ad4e',
			    		backgroundColor: '#f0ad4e',
			    		pointBackgroundColor: '#f0ad4e',
			    		pointHoverRadius: 3,
			    		data: sellList1
			    	},
			    	{
			    		label: '10%매도',
			    		lineTension: 0.1,
			    		borderWidth: 2,
			    		pointRadius: 1,
			    		fill: false,
			    		borderColor: '#d9534f',
			    		backgroundColor: '#d9534f',
			    		pointBackgroundColor: '#d9534f',
			    		pointHoverRadius: 3,
			    		data: sellList2
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
			        }
			      }],
			      yAxes: [{
			        ticks: {
			          padding: 10,
			          // Include a dollar sign in the ticks
			          callback: function(value, index, values) {
			            return '$' + value;
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
			      position: 'top'
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
			          return datasetLabel + ': ' + $filter('number')(tooltipItem.yLabel, 1) + '$';
			        }
			      }
			    }
			  }
			});
		}
	}, true);
});