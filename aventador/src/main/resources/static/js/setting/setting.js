app.controller("SettingController", function($scope, httpService, infiniteService, settingService){

    var setting = this;
    setting.infinite = {};
    
    setting.infinite.getMyAccounts = function(){
    	infiniteService.getMyAccounts().then(function(data){
	    	setting.infinite.account = {};
	    	setting.infinite.account.list = data;
		});
    }
    setting.infinite.getMyAccounts();
    
    // 계좌 편집모드
    $scope.$watch("setting.infinite.account.isEditMode", function(isEditMode){
    	if(!isEditMode)
    		return;
    	setting.infinite.account.editList = angular.copy(setting.infinite.account.list);
    });
    
    // 계좌 추가 모달
    setting.infinite.addAccount = {};
    setting.infinite.addAccount.openModal = function(){
    	setting.infinite.addAccount.data = {
			accountAlias: null,
			seed: null,
			feesPer: null
    	}
    	// form validation 초기화
		if(setting.infinite.addAccountForm){
			setting.infinite.addAccountForm.$setPristine();
			setting.infinite.addAccountForm.$setUntouched();
		}
		$('#addAccountModal').modal("show");
    };
    
    // 계좌 추가
    setting.infinite.addAccount.add = function(){
    	setting.infinite.addAccount.data.accountOrder = setting.infinite.account.list.length + 1;
    	infiniteService.addAccount(setting.infinite.addAccount.data).then(function(data){
    		if(data == true){
    			setting.infinite.getMyAccounts();
    			$('#addAccountModal').modal("hide");
				// TODO : 알림창 
				alert("추가되었습니다.");
			}
    	});
    }
    
    // 계좌 수정
    
    // 테이블 드래그 모드 on
    setting.tableDnD = function(id){
    	$(id).tableDnD({
    		onDragClass: "drag"
    	});
    }
});

app.service("settingService", function(){


});