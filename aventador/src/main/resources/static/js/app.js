var app = angular.module("app", ["ngRoute"]);
app.config(["$routeProvider", "$httpProvider", 
		function($routeProvider, $httpProvider) {
	
	// http 응답오류 인터셉터 (eg. 세션 타임아웃)
	$httpProvider.interceptors.push("responseObserver");
	
	// app.js 로딩 위치를 기본 경로로 하여 작동함
	// $locationProvider.hashPrefix("!"); // 해시 뒤에 오는 접두어(기본값: "!")을 변경할 수 있음
	
	// trailing slash로 끝나는 경우 "index.html"를 붙여줌
	// public/, admin/ 등 최상위인 경우 index.html는 서버에서 로딩하므로 "_index.html"을 붙여줌
	$routeProvider
		.when("/", { // == "#!/index" 
			templateUrl : function(path){
				return "_index.html";
			}
		})
		.when("/:name*", {
			templateUrl : function(path){
				
				var pathName = path.name;
				
				if(/\/$/.test(path.name)){
					path.name += "index";
				}
				return path.name + ".html";
			},
		});
}]);
app.factory("responseObserver", ["$q", "$window", function($q, $window){
	return {
		"responseError": function(response){
			switch (response.status) {
			case 403:
				console.error("403 Forbidden.");
				$window.location = (meta && meta.baseurl) ? meta.baseurl : "/";
				break;
			}
		}
	}
}]);
app.run(["$rootScope", "$location", function($rootScope, $location){
//	$rootScope.meta = meta; 
//	
//	// USAGES:
//	// meta.path(): to return current path
//	// meta.path("/path/string"): to check whether current path is including(starting with) "/path/string"
//	$rootScope.path = function(){
//		// getter
//		// return current path
//		if(arguments.length == 0){
//			return $location.path();
//		}
//		
//		// checker
//		// check current path pattern
//		var path;
//		for(var i=0; i<arguments.length; i++){
//			path = arguments[i];
//			if(typeof path === "string"){
//				if($location.path().length >= path.length
//						&& $location.path().substring(0, path.length) == path){
//					return true;
//				}
//			}
//		}
//		
//		return false;
//	}
}]);
app.controller("MainController", function($scope, $http, $location){
	
	
});	