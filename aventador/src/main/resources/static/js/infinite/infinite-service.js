app.service("infiniteService", function(httpService){
	// 기본값 세팅
	var promiseMyAcount = null;
	
	this.getMyAccount = function(){
		
		if(promiseMyAcount){
			httpService.stop(promiseMyAcount);
		}
		
		promiseMyAcount = httpService.get({
			url: meta.baseUrl + "api/infinite/my-account",
		}).then(function(response){
			return response.data;
		});
		
		return promiseMyAcount;
	}
});
