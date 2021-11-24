/**
 * meta initializer (non-lazy loader)
 * 식별: meta == window.meta
 * 결과: meta[name] 중복시 array로 처리
 */ 
(function(){
	meta = {};
	var metaElements = [];
	
	read();
	document.addEventListener("DOMContentLoaded", read);
	
	function read(){
		var newMetaElements = [];
		for (var i=0; i<document.getElementsByTagName("meta").length; i++){
			if(metaElements.indexOf(document.getElementsByTagName("meta")[i]) < 0){
				newMetaElements.push(document.getElementsByTagName("meta")[i]);
			}
		}
		
		newMetaElements.forEach(function(e){
			var name = e.getAttribute("name");
			var content = e.getAttribute("content");
			
			meta[name] = content;
		});
	}
	
})();

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
	$rootScope.meta = meta; 
	
	// USAGES:
	// meta.path(): to return current path
	// meta.path("/path/string"): to check whether current path is including(starting with) "/path/string"
	$rootScope.path = function(){
		// getter
		// return current path
		if(arguments.length == 0){
			return $location.path();
		}
		
		// checker
		// check current path pattern
		var path;
		for(var i=0; i<arguments.length; i++){
			path = arguments[i];
			if(typeof path === "string"){
				if($location.path().length >= path.length
						&& $location.path().substring(0, path.length) == path){
					return true;
				}
			}
		}
		
		return false;
	}
}]);
app.service("httpService", function($http, $q){
	var httpService = this;
	
	// request default
	var requestDefault = {
		method: "POST",
		headers: {
			"Accept": "application/json; charset=UTF-8",
			"Content-Type": "application/json; charset=UTF-8",
		}
	}
	
	if(meta._csrf_header)
		requestDefault.headers[meta._csrf_header] = meta._csrf;
	
	// promise repository
	// [ [promise1, timeout1], [promise2, timeout2], ... ]
	var arrayPromiseTimeout = [];
	
	// ----------------
	// public functions
	// ----------------
	this.run = fnRequest;
	this.request = fnRequest;
	
	this.post = function(request){
		request.method = "POST";
		return fnRequest(request);
	}
	this.get = function(request){
		request.method = "GET";
		delete request.data;
		return fnRequest(request);
	}
	this.promises = function(){
		return arrayPromiseTimeout.map(function(e){ return e[0]; });
	}
	
	this.stop = function(promise){
		if(!promise)
			return; 
		var idx = findPromiseIndex(promise);
		if(idx >= 0){
			arrayPromiseTimeout[idx][1].resolve();
			arrayPromiseTimeout.splice(idx, 1);
		}
	}
	
	this.status403 = null;
	
	
	// ----------------
	// core functions
	// ----------------
	
	function fnRequest(request){
		var requestOption = angular.extend({}, requestDefault, request);
		
		var timeoutDefer = $q.defer();
		requestOption.timeout = timeoutDefer.promise;
		
		var promise = $http(requestOption);
		arrayPromiseTimeout.push([ promise, timeoutDefer ]);
		
		promise.then(function(){
			var idx = findPromiseIndex(promise);
			if(idx >= 0){
				arrayPromiseTimeout.splice(idx, 1);
			}
		}, function(e){
			console.log(e);
			if(e.status == 403){
				if(httpService.status403){
					httpService.status403(e);
				}else if(meta && meta.baseurl){
					window.location.href = meta.baseurl;
				}
			}
		});
		
		return promise;
	}
	
	function findPromiseIndex(promise){
		for(var i=0; i < arrayPromiseTimeout.length; i++){
			if(arrayPromiseTimeout[i][0] === promise){
				return i;
			}
		}
		return -1;
	}
	
});
app.filter("appendUpDown", function(){
	return function(score){
		score = Number(score);
		if(score > 0)
			return "▲ " + score.toFixed(2);
		else if(score < 0)
			return ("▼ " + score.toFixed(2)).replace("-", "");
		else
			return "­ " + score.toFixed(2);
	}
});
app.filter("appendPulMa", function(){
	return function(score){
		score = Number(score);
		if(score > 0)
			return "+" + score.toFixed(2);
		else if(score < 0)
			return "-" + score.toFixed(2).replace("-", "");
		else
			return score.toFixed(2);
	}
});
app.controller("MainController", function($scope, $http, $location){
	
	
});	