app.controller("SettingController", function($scope, httpService, infiniteService, settingService){

    var setting = this;
    
    // 무한매수 설정 관련
    setting.infinite = {};
    
    // 무한매수 계좌 설정 관련
    setting.infinite.account = {};
    
    setting.infinite.getMyAccounts = function(){
    	infiniteService.getMyAccounts().then(function(data){
	    	setting.infinite.account.list = data;
	    	
	    	// 원본값을 보존하고 edit 변수에 값을 넣어줌
	    	setting.infinite.account.list.forEach(function(item){
				item.edit = angular.copy(item);
			});
		});
    }
    setting.infinite.getMyAccounts();
    
    // 계좌 테이블 드래그 관련
    setting.infinite.account.sortableOptions = {
	    stop: function(e, ui) {
	    	var updateOrderList = [];
	    	setting.infinite.account.list.forEach(function(o, i){
	    		updateOrderList.push({
	    			accountId: o.accountId,
	    			accountOrder: (i+1)
	    		});
	    	});
	    	infiniteService.updateAccount({updateOrderList : updateOrderList});
	    }
    }
    
    // 계좌 추가 모달
    setting.infinite.account.openAddModal = function(){
    	setting.infinite.account.addData = {
			accountAlias: null,
			seed: null,
			feesPer: 0.07
    	}
    	// form validation 초기화
		if(setting.infinite.account.addForm){
			setting.infinite.account.addForm.$setPristine();
			setting.infinite.account.addForm.$setUntouched();
		}
		$('#addAccountModal').modal("show");
    };
    
    // 계좌 추가
    setting.infinite.account.add = function(){
    	setting.infinite.account.addData.accountOrder = setting.infinite.account.list.length + 1;
    	infiniteService.addAccount(setting.infinite.account.addData).then(function(data){
    		if(data == true){
    			setting.infinite.getMyAccounts();
    			$('#addAccountModal').modal("hide");
    			
				// TODO : 알림창 
				alert("추가되었습니다.");
			}
    	});
    }
    
    // 계좌 수정
    setting.infinite.account.update = function(account){
    	account.editMode = false;
    	
    	infiniteService.updateAccount(account.edit).then(function(data){
			if(data == true){
				setting.infinite.getMyAccounts();
				
				// TODO : 알림창 
				alert("변경되었습니다.");
			}
		})
    }
    
    // 계좌 삭제
    setting.infinite.account.checkDelete = function(accountId){
    	var isDelete = prompt( "정말로 삭제하시겠습니까? \r한 번 삭제한 후에는 복구가 불가능합니다. \r삭제를 원하시면 '삭제'를 입력해주세요.", "" );
		if(isDelete == "삭제"){
			setting.infinite.account.deleteAccount(accountId);
		}
    }
    setting.infinite.account.deleteAccount = function(accountId){
    	var params = {
			accountId : accountId, 
			isDeleted : true
		}
		
		infiniteService.updateAccount(params).then(function(data){
			if(data == true){
				setting.infinite.getMyAccounts();
				
				// TODO : 알림창 
				alert("삭제되었습니다.");
			}
		})
    }
    
});

app.service("settingService", function(){


});