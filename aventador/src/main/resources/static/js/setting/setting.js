app.controller("SettingController", function($scope, httpService, infiniteService){

    var setting = this;
    setting.infinite = {};
    
    infiniteService.getMyAccounts().then(function(data){
    	setting.infinite.account = {};
    	setting.infinite.account.list = data;
	});
    
    $scope.$watch("setting.infinite.account.isEditMode", function(isEditMode){
    	if(!isEditMode)
    		return;
    	$("#accountTable").tableDnD();
    });
    
    //var initSetting = settingService.getInitSetting();      /*param값넘겨줘야한다.*/
});

app.service("settingService", function(){

    var initSetting = null;

    // 계좌 세팅값 불러오기
    this.getInitSetting = function(/*param*/){

        if(initSetting){
            httpService.stop(initSetting);
        }

        initSetting = httpService.get({
            url: meta.baseUrl + "api/setting/init",
            // data: param
        }).then(function(response){
            return response.data;
        });

        return initSetting;
    }

});