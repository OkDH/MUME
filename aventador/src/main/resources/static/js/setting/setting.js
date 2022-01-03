app.controller("SettingController", function($scope, httpService, stockService, infiniteService){

    var infiniteSetting = this;

    var initSetting = infiniteSettingService.getInitSetting();      /*param값넘겨줘야한다.*/

});

app.service("infiniteSettingService", function(){

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