app.controller("InfiniteAccountController", function($scope, httpService, stockService, infiniteService){

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
		accountId: null,
		infiniteType: [],
		infiniteState: [],
		offset: 0,
		limit: 30
	};
	// fiter 와 query 변환용 변수
	infiniteAccount.account.filter = {
		infiniteState: {
			ing: { name: "진행중", value: true },
			stop: { name: "매수중지", value: true },
			done: { name: "매도완료", value: false },
			out: { name: "원금소진", value: false }
		},
		infiniteType: {
			v2_1: { name: "v2.1", value: true },
			v2: { name: "v2", value: true },
			v1: { name: "v1", value: true }
		}
	};
	// 검색 필터 : 무한매수 상태
	$scope.$watch("infiniteAccount.account.filter.infiniteState", function(infiniteState){
		if(!infiniteState){
			return;
		}
		infiniteAccount.account.query.infiniteState = [];
		Object.keys(infiniteState).forEach(function(k){
			if(infiniteState[k].value)
				infiniteAccount.account.query.infiniteState.push(infiniteState[k].name);
		});
	}, true);
	
	// 검색 필터 : 무한매수 버전
	$scope.$watch("infiniteAccount.account.filter.infiniteType", function(infiniteType){
		if(!infiniteType){
			return;
		}
		infiniteAccount.account.query.infiniteType = [];
		Object.keys(infiniteType).forEach(function(k){
			if(infiniteType[k].value)
				infiniteAccount.account.query.infiniteType.push(infiniteType[k].name);
		});
	}, true);
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteAccount.account.myAccounts = data;
		infiniteAccount.addStock.init();
	});
	
	// 종목 리스트 조회
	infiniteAccount.stocks = [];
	infiniteAccount.getStocks = function(query){
		infiniteService.getStocks(query).then(function(data){
			infiniteAccount.stocks = data;
		});
	}
	
	// 계좌 내 종목 현황 조회
	infiniteAccount.getAccountState = function(accountId){
		infiniteService.getAccountState({accountId:accountId}).then(function(data){
			infiniteAccount.account.state = data;
		});
	}
	
	// 심플 주문 리스트 조회
	infiniteAccount.getSimpleOrders = function(accountId){
		var params = {
			accountId: accountId,
			infiniteState: ["진행중"],
			orderBy: "symbol asc"
		}
		infiniteService.getStocks(params).then(function(data){
			if(!data)
				return null;
			infiniteAccount.simpleOrders = [];
			data.forEach(function(d){
				d.buyTradeInfoList.forEach(function(info){
					infiniteAccount.simpleOrders.push({
						symbol: d.symbol,
						tradeName: info.tradeName,
						tradeType: "매수",
						concludeType: info.concludeType,
						price: info.price,
						quantity: info.quantity
					});
				});
				d.sellTradeInfoList.forEach(function(info){
					infiniteAccount.simpleOrders.push({
						symbol: d.symbol,
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
		if(!query){
			return;
		}
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
			infiniteType: "v2.1",
			startedDate: null,
			seed: null,
			unitPrice: null,
			quantity: null,
			accountId: null
		}
		if(infiniteAccount.account.myAccounts)
			infiniteAccount.addStock.data.accountId = infiniteAccount.account.myAccounts[0].accountId;
	}
	
	// 종목 추가
	infiniteAccount.addStock.openAddModal = function(){
		infiniteAccount.addStock.init();
		$('#addStockModal').modal("show");
		// datepicker
		infiniteAccount.datepicker("#addStockDatePicker");
		// selectpicker
		$("#symbolSelect").selectpicker();
	}
	infiniteAccount.addStock.add = function(){
		// jquery에서 만든 날짜 값 가져오기
		infiniteAccount.addStock.data.startedDate = $('#addStockDatePicker').val();
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
		$('#updateStockModal').modal("show");
		// datepicker
		infiniteAccount.datepicker("#updateStockDatePicker");
	}
	infiniteAccount.updateStock.update = function(params){
		if(!params)
			return;
		params.startedDate = $('#updateStockDatePicker').val();
		infiniteService.updateStock(params).then(function(data){
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
		
		// datepicker
		infiniteAccount.datepicker("#addHistoryDatePicker");
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
		// jquery에서 만든 날짜 값 가져오기
		infiniteAccount.addHistory.data.tradeDate = $('#addHistoryDatePicker').val();
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
	
	// datepicker
	infiniteAccount.datepicker = function(selector){
		$(selector).datepicker({
			format: "yyyy-mm-dd",	// 데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
			endDate: '1d',
			autoclose : true,	// 사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
			templates : {
				leftArrow: '&laquo;',
				rightArrow: '&raquo;'
			}, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징 
			showWeekDays : true ,// 위에 요일 보여주는 옵션 기본값 : true
			todayHighlight : true ,	//오늘 날짜에 하이라이팅 기능 기본값 :false 
			language : "ko"	//달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
		});
	}
	
});

