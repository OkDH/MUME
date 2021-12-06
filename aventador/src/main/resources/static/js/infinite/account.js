app.controller("InfiniteAccountController", function($scope, httpService, stockService, infiniteService){

	var infiniteAccount = this;
	
	// ETF 기본 정보
	infiniteAccount.initData = {};
	stockService.getInitData().then(function(data){
		infiniteAccount.initData = data;
		infiniteAccount.addStock.init();
	});
	
	infiniteAccount.account = {};
	infiniteAccount.account.query = {
		accountId: null,
		infiniteType: null,
		infiniteState: "진행중",
		offset: 0,
		limit: 30
	};
	
	// 계좌 정보
	infiniteService.getMyAccounts().then(function(data){
		infiniteAccount.account.myAccounts = data;
		infiniteAccount.addStock.init();
	});
	
	// 종목 리스트 조회
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
		if(infiniteAccount.initData.symbols)
			infiniteAccount.addStock.data.symbol = infiniteAccount.initData.symbols[0];
		if(infiniteAccount.account.myAccounts)
			infiniteAccount.addStock.data.accountId = infiniteAccount.account.myAccounts[0].accountId;
	}
	
	// 종목 추가
	infiniteAccount.addStock.add = function(){
		// jquery에서 만든 날짜 값 가져오기
		infiniteAccount.addStock.data.startedDate = $('#addStockDatePicker').val();
		infiniteService.addStock(infiniteAccount.addStock.data).then(function(data){
			if(data == true){
				infiniteAccount.getStocks(infiniteAccount.account.query);
				infiniteAccount.getAccountState(infiniteAccount.account.query.accountId);
				infiniteAccount.addStock.init();
				$('#addStockModal').modal("hide");
				
				// TODO : 알림창 
				alert("추가되었습니다.");
			}
		})
	}
	
	// datepicker
	$('#addStockDatePicker').datepicker({
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
	
});

