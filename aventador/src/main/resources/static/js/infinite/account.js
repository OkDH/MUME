app.controller("InfiniteAccountController", function($scope, $filter, httpService, stockService, infiniteService){

	var infiniteAccount = this;
	
	// ETF 기본 정보
	infiniteAccount.initData = {};
	stockService.getInitData().then(function(data){
		infiniteAccount.initData.etfs = [];
		Object.keys(data.etfs).forEach(function(key){
			infiniteAccount.initData.etfs.push(data.etfs[key]);
		});
		
		infiniteAccount.addStock.init();
	});
	
	infiniteAccount.account = {};
	infiniteAccount.account.query = {
		accountId: "ALL",  
		infiniteState: [],
		infiniteType: [],
		infiniteVersion: [],
		orderBy: "registered_date desc",
		offset: 0,
		limit: 8
	};
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteAccount.account.myAccounts = data;
		
		// 계좌 order map
		infiniteAccount.account.myAccountsOrder = {};
		data.forEach(function(d){
			infiniteAccount.account.myAccountsOrder[d.accountId] = {};
			infiniteAccount.account.myAccountsOrder[d.accountId].order = d.accountOrder;
			infiniteAccount.account.myAccountsOrder[d.accountId].alias = d.accountAlias;
		});
	});
	
	// fiter 와 query 변환용 변수
	infiniteAccount.account.filter = {
		infiniteState: {
			ing: { name: "진행중", value: true },
			stop: { name: "매수중지", value: true },
			done: { name: "매도완료", value: false },
			out: { name: "원금소진", value: true }
		},
		infiniteType: {
			infinite: { name: "INFINITE", value: true },
			tlp: { name: "TLP", value: true }
		},
		infiniteVersion: {
			v2_1: { name: "v2.1", value: true },
			v2_1_SH: { name: "v2.1후반", value: true },
			v2: { name: "v2", value: true },
			v1: { name: "v1", value: true }
		},
		order: { name: "registered", value: "registered_date desc"},
		orderValue: {
			registered: { name: "registered", value: "registered_date desc"},
			symbol: { name: "symbol", value: "symbol asc"}
		}
	};
	
	// 검색 필터
	$scope.$watch("infiniteAccount.account.filter", function(filter){
		if(!filter)
			return;
		
		infiniteAccount.account.query.offset = 0;
		
		// 무한매수 상태 필터
		infiniteAccount.account.query.infiniteState = [];
		Object.keys(filter.infiniteState).forEach(function(k){
			if(filter.infiniteState[k].value)
				infiniteAccount.account.query.infiniteState.push(filter.infiniteState[k].name);
		});
		// 무한매수/TLP 필터
		infiniteAccount.account.query.infiniteType = [];
		Object.keys(filter.infiniteType).forEach(function(k){
			if(filter.infiniteType[k].value)
				infiniteAccount.account.query.infiniteType.push(filter.infiniteType[k].name);
		});
		// 무한매수 버전 필터
		infiniteAccount.account.query.infiniteVersion = [];
		Object.keys(filter.infiniteVersion).forEach(function(k){
			if(filter.infiniteVersion[k].value)
				infiniteAccount.account.query.infiniteVersion.push(filter.infiniteVersion[k].name);
		});
		// 정렬
		infiniteAccount.account.query.orderBy = filter.order.value;
	}, true);
	
	// 필터 모달
	infiniteAccount.openFilterModal = function(){
		infiniteAccount.addStock.init();
		$('#filterModal').modal("show");
		// selectpicker
		$("#filterModal #accountSelect").selectpicker("refresh");
	}
	
	// 종목 리스트 조회
	infiniteAccount.stocks = [];
	infiniteAccount.getStocks = function(query){
		infiniteService.getStocks(query).then(function(data){
			if(query.offset == 0){
				infiniteAccount.isMore = true;
				infiniteAccount.stocks = data;
			} else {
				if(data.length > 0)
					Array.prototype.push.apply(infiniteAccount.stocks, data);
				else 
					infiniteAccount.isMore = false;
			}
		});
	}
	
	// 더보기
	infiniteAccount.isMore = true;
	infiniteAccount.more = function(){
		infiniteAccount.account.query.offset += infiniteAccount.account.query.limit;
	}
	
	// 계좌 내 종목 현황 조회
	infiniteAccount.getAccountState = function(accountId){
		infiniteService.getAccountState({accountId:accountId}).then(function(data){
			infiniteAccount.account.state = data;
		});
	}

	// 심플 주문 리스트 조회
	infiniteAccount.isSimpleOut = false;
	infiniteAccount.getSimpleOrders = function(accountId){
		var params = {
			accountId: accountId,
			infiniteState: ["진행중", "원금소진"],
			orderBy: "account_id asc, symbol asc"
		}
		infiniteService.getStocks(params).then(function(data){
			infiniteAccount.simpleOrders = [];
			data.forEach(function(d){
				if(d.infiniteState == "진행중"){
					d.buyTradeInfoList.forEach(function(info){
						infiniteAccount.simpleOrders.push({
							accountId: d.accountId,
							symbol: d.symbol,
							infiniteState: d.infiniteState,
							tradeName: info.tradeName,
							tradeType: "매수",
							concludeType: info.concludeType,
							price: info.price,
							quantity: info.quantity
						});
					});
				}
				
				d.sellTradeInfoList.forEach(function(info){
					infiniteAccount.simpleOrders.push({
						accountId: d.accountId,
						symbol: d.symbol,
						infiniteState: d.infiniteState,
						tradeName: info.tradeName,
						tradeType: "매도",
						concludeType: info.concludeType,
						price: info.price,
						quantity: info.quantity
					});
				});
				infiniteAccount.simpleOrders[infiniteAccount.simpleOrders.length-1].isLast = true;
			});
		});
	}
	
	// 계좌 선택 필터 변경 시 종목 조회
	$scope.$watch("infiniteAccount.account.query", function(query){
		if(!query)
			return;
		infiniteAccount.getStocks(query);
	}, true);
	
	// 계좌 선택 필터 변경 시 현황 조회
	$scope.$watch("infiniteAccount.account.query.accountId", function(accountId){
		infiniteAccount.getAccountState(accountId);
		infiniteAccount.getSimpleOrders(accountId);
	}, true);
	
	// 종목 추가 관련 변수 및 초기화
	infiniteAccount.addStock = {};
	infiniteAccount.addStock.init = function(){
		infiniteAccount.addStock.data = {
			symbol: null,
			infiniteVersion: "v2.1",
			infiniteType: "INFINITE",
			startedDate: null,
			seed: null,
			unitPrice: null,
			quantity: null,
			accountId: null,
			divisions: 40,
			isAutoTrade: true
		}
		if(infiniteAccount.account.query.accountId !== "ALL")
			infiniteAccount.addStock.data.accountId = infiniteAccount.account.query.accountId;
		
		// form validation 초기화
		if(infiniteAccount.addStockForm){
			infiniteAccount.addStockForm.$setPristine();
			infiniteAccount.addStockForm.$setUntouched();
		}
	}
	
	// 종목 추가
	infiniteAccount.addStock.openAddModal = function(){
		infiniteAccount.addStock.init();
		$('#addStockModal').modal("show");
		// selectpicker
		$("#addStockForm #symbolSelect").val('');
		$("#addStockForm #symbolSelect").selectpicker("refresh");
		$("#addStockForm #accountSelect").val('');
		$("#addStockForm #accountSelect").selectpicker("refresh");
		$('#addStockForm #autoTradeToggle').bootstrapToggle('on');
	}
	infiniteAccount.addStock.add = function(){
		// Date to StringDate
		infiniteAccount.addStock.data.startedDate = $filter("printDate")(infiniteAccount.addStock.data.startedDate);
		
		infiniteService.addStock(infiniteAccount.addStock.data).then(function(data){
			if(data == true){
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);
				infiniteAccount.addStock.init();
				$('#addStockModal').modal("hide");
				
				// TODO : 알림창 
				alert("추가되었습니다.");
			}
		})
	}
	
	// 종목 변경
	infiniteAccount.updateStock = {};
	infiniteAccount.updateStock.openUpdateModal = function(stock){
		infiniteAccount.updateStock.data = angular.copy(stock);
		
		// form validation 초기화
		if(infiniteAccount.updateStockForm){
			infiniteAccount.updateStockForm.$setPristine();
			infiniteAccount.updateStockForm.$setUntouched();
		}
		
		// StringDate to Date
		if(infiniteAccount.updateStock.data.startedDate)
			infiniteAccount.updateStock.data.startedDate = new Date(infiniteAccount.updateStock.data.startedDate);
		if(infiniteAccount.updateStock.data.doneDate)
			infiniteAccount.updateStock.data.doneDate = new Date(infiniteAccount.updateStock.data.doneDate);
		
		// 매매내역 자동 여부 토글
		if(infiniteAccount.updateStock.data.isAutoTrade)
			$('#updateStockForm #autoTradeToggle').bootstrapToggle('on');
		else
			$('#updateStockForm #autoTradeToggle').bootstrapToggle('off');
				
		$('#updateStockModal').modal("show");
	}
	infiniteAccount.updateStock.update = function(params){
		if(!params)
			return;
		
		var request = angular.copy(params); 
		
		// Date to StringDate
		request.startedDate = $filter("printDate")(request.startedDate);
		if(params.doneDate)
			request.doneDate = $filter("printDate")(request.doneDate);
		
		infiniteService.updateStock(request).then(function(data){
			if(data == true){
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);
				$('#updateStockModal').modal("hide");

				// TODO : 알림창 
				alert("변경되었습니다.");
			}
		})
	}
	
	// 종목 상태 변경
	infiniteAccount.changeState = function(accountId, infiniteId, infiniteState){
		var params = {
			accountId : accountId, infiniteId : infiniteId, infiniteState : infiniteState
		}
		infiniteService.updateStock(params).then(function(data){
			if(data == true){
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);

				// TODO : 알림창 
				alert("변경되었습니다.");
			}
		})
	}
	
	// 종목 삭제
	infiniteAccount.checkDeleteInfinite = function(accountId, infiniteId){
		var isDelete = prompt( "정말로 삭제하시겠습니까? \r한 번 삭제한 후에는 복구가 불가능합니다. \r삭제를 원하시면 '삭제'를 입력해주세요.", "" );
		if(isDelete == "삭제"){
			infiniteAccount.deleteIfinite(accountId, infiniteId);
		}
	}
	infiniteAccount.deleteIfinite = function(accountId, infiniteId){
		var params = {
			accountId : accountId, 
			infiniteId : infiniteId, 
			isDeleted : true
		}
		
		infiniteService.updateStock(params).then(function(data){
			if(data == true){
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);
				
				// TODO : 알림창 
				alert("삭제되었습니다.");
			}
		})
	}
	
	
	// -----------------------------------------------------
	
	// 매매내역
	infiniteAccount.viewStock = {}
	// modal open
	infiniteAccount.openHistoryModal = function(accountId, infiniteId, symbol){
		infiniteAccount.viewStock = {
			symbol: symbol,
			accountId: accountId,
			infiniteId: infiniteId
		}
		$("#historyModal").modal("show");
		
		infiniteAccount.getStockHistory();
		infiniteAccount.addHistory.init();
	}
	
	// 매매내역 조회
	infiniteAccount.getStockHistory = function(){
		if(!infiniteAccount.viewStock)
			return;
		
		infiniteService.getStockHistory(infiniteAccount.viewStock).then(function(data){
			if(!data)
				return;
			
			infiniteAccount.viewStock.history = data;
			
			// 원본값을 보존하고 edit 변수에 값을 넣어줌
			infiniteAccount.viewStock.history.forEach(function(item){
				item.edit = angular.copy(item);
				item.edit.tradeDate = new Date(item.edit.tradeDate);
			});
			
		});
	}
	
	// 매매내역 추가 관련 변수
	infiniteAccount.addHistory = {};
	infiniteAccount.addHistory.init = function(){
		infiniteAccount.addHistory.isShow = false;
		infiniteAccount.addHistory.data = {
			accountId: infiniteAccount.viewStock.accountId,
			infiniteId: infiniteAccount.viewStock.infiniteId,
			tradeType : "매수",
			tradeDate: null,
			unitPrice: null,
			quantity: null
		}
	}
	infiniteAccount.addHistory.add = function(){
		infiniteAccount.addHistory.data.tradeDate = $filter("printDate")(infiniteAccount.addHistory.data.tradeDate);
		
		infiniteService.addHistory(infiniteAccount.addHistory.data).then(function(data){
			if(data == true){
				infiniteAccount.getStockHistory();
				infiniteAccount.addHistory.init();
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);
				
				// TODO : 알림창 
				alert("추가되었습니다.");
			}
		})
	}
	// 매매내역 삭제
	infiniteAccount.checkDeleteHistory = function(infiniteHistoryId){
		var isDelete = confirm( '삭제하시겠습니까?' );
		if(isDelete){
			infiniteAccount.deleteHistory(infiniteHistoryId);
		}
	}
	infiniteAccount.deleteHistory = function(infiniteHistoryId){
		var params = {
			accountId : infiniteAccount.viewStock.accountId, 
			infiniteId : infiniteAccount.viewStock.infiniteId, 
			infiniteHistoryId : infiniteHistoryId,
			isDeleted : true
		}
		
		infiniteService.updateHistory(params).then(function(data){
			if(data == true){
				infiniteAccount.getStockHistory();
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);
				
				// TODO : 알림창 
				alert("삭제되었습니다.");
			}
		})
	}
	// 매매내역 업데이트
	infiniteAccount.updateHistory = function(trade){
		trade.editMode = false;
		trade.edit.accountId = infiniteAccount.viewStock.accountId;
		trade.edit.tradeDate = $filter("printDate")(trade.edit.tradeDate);
		
		infiniteService.updateHistory(trade.edit).then(function(data){
			if(data == true){
				infiniteAccount.getStockHistory();
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.getSimpleOrders(infiniteAccount.account.query.accountId);
				
				// TODO : 알림창 
				alert("변경되었습니다.");
			}
		})
	}
	
	// -------------------------------------
	// 보고서
	// modal open
	infiniteAccount.openReportModal = function(accountId, infiniteId, symbol){
		infiniteAccount.viewStock = {
			symbol: symbol,
			accountId: accountId,
			infiniteId: infiniteId
		}
		$("#reportModal").modal("show");
		
		infiniteAccount.getStock();
	}
	
	// 종목 상세 조회
	infiniteAccount.getStock = function(){
		if(!infiniteAccount.viewStock)
			return;
		
		infiniteService.getStock(infiniteAccount.viewStock).then(function(data){
			if(!data)
				return;
			infiniteAccount.viewStock.detail = data;
		});
	}
	
	infiniteAccount.report = {};
	
	// 보고서 차트
	$scope.$watch("infiniteAccount.viewStock.detail", function(detail){
		if(!detail)
			return;
		
		console.log(detail);
		
		var stockList = detail.stockList;
		
		// 일자별로 map 만들기
		var datas = {};
		detail.averagePriceList.forEach(function(item){
			datas[item.tradeDate] = item;
		});
		
		// label
		var labels = [];
		
		// 평단가 
		var averagePriceList = [];
		// 큰수매수
		var bigBuyList = [];
		// 종가
		var closeList = [];
		// 5% 매도
		var sellList1 = [];
		// 10% 매도
		var sellList2 = [];
		// 고가
		var highList = [];
		
		// 데이터 리스트 채우기
		detail.stockList.forEach(function(item){
			// label
			labels.push(item.stockDate);
			
			// 종가, 고가
			closeList.push(item.priceClose);
			highList.push(item.priceHigh);
			
			// 평단가
			var averagePrice;
			if(datas[item.stockDate])
				averagePrice = datas[item.stockDate].averagePrice;
			else
				averagePrice = averagePriceList[averagePriceList.length-1];
			
			averagePriceList.push(averagePrice);
			
			// 큰수 매수
			bigBuyList.push((averagePrice * 1.05).toFixed(2));
			
			// 5% 매도
			sellList1.push((averagePrice * 1.0517).toFixed(2));
			
			// 10% 매도
			sellList2.push((averagePrice * 1.1017).toFixed(2));
		});
		
		
		// 매수 흐름 차트
		{
			// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
			if(infiniteAccount.report.buyFlowChart)
				infiniteAccount.report.buyFlowChart.destroy();
			
			// 차트 그리기
			var ctx = document.getElementById("buyFlowChart");
			infiniteAccount.report.buyFlowChart = new Chart(ctx, {
			  type: 'line',
			  data: {
			    labels: labels.map(x => x.replaceAll("-", ".").substring(2)),
			    datasets: [
			    	{
						label: '종가',
						lineTension: 0.1,
						borderWidth: 2,
						pointRadius: 1,
						fill: false,
						borderColor: '#4e73df',
						backgroundColor: '#4e73df',
				        pointBackgroundColor: '#4e73df',
				        pointHoverRadius: 3,
						data: closeList
					},
			    	{
			    		label: '평단매수',
			    		lineTension: 0.1,
			    		borderWidth: 2,
			    		pointRadius: 0,
			    		fill: false,
			    		borderColor: '#f0ad4e',
			    		backgroundColor: '#f0ad4e',
			    		pointBackgroundColor: '#f0ad4e',
			    		pointHoverRadius: 3,
			    		data: averagePriceList
			    	},
			    	{
			    		label: '큰수매수',
			    		lineTension: 0.1,
			    		borderWidth: 2,
			    		pointRadius: 0,
			    		fill: false,
			    		borderColor: '#d9534f',
			    		backgroundColor: '#d9534f',
			    		pointBackgroundColor: '#d9534f',
			    		pointHoverRadius: 3,
			    		data: bigBuyList
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
		
		
		// 매도 흐름 차트
		{
			// 그려진 차트가 있다면 차트 삭제 후 다시 그리기
			if(infiniteAccount.report.sellFlowChart)
				infiniteAccount.report.sellFlowChart.destroy();
			
			// 차트 그리기
			var ctx = document.getElementById("sellFlowChart");
			infiniteAccount.report.sellFlowChart = new Chart(ctx, {
			  type: 'line',
			  data: {
			    labels: labels.map(x => x.replaceAll("-", ".").substring(2)),
			    datasets: [
			    	{
						label: '고가',
						lineTension: 0.1,
						borderWidth: 2,
						pointRadius: 1,
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
			    		pointRadius: 0,
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
			    		pointRadius: 0,
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
	
	
	// -------------------------------------
	// FCM
	if ($scope.$parent.isMobile()) {
		// 식별 토큰 가져오기
		// api/member/check-token
		httpService.get({
			url: meta.baseUrl + "api/member/check-token"
		}).then(function(response){
			// 웹뷰 메소드 호출
			if(response.status == 200)
				AppGetFcmToken.postMessage(response.data.token);
		});
	}
});

