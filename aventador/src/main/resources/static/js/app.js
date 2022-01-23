/**
 * meta initializer (non-lazy loader) 식별: meta == window.meta 결과: meta[name] 중복시
 * array로 처리
 */
(function() {
	meta = {};
	var metaElements = [];

	read();
	document.addEventListener("DOMContentLoaded", read);

	function read() {
		var newMetaElements = [];
		for (var i = 0; i < document.getElementsByTagName("meta").length; i++) {
			if (metaElements.indexOf(document.getElementsByTagName("meta")[i]) < 0) {
				newMetaElements.push(document.getElementsByTagName("meta")[i]);
			}
		}

		newMetaElements.forEach(function(e) {
			var name = e.getAttribute("name");
			var content = e.getAttribute("content");

			meta[name] = content;
		});
	}

})();

var app = angular.module("app", [ "ngRoute", "ui.sortable" ]);
app.config([ "$routeProvider", "$httpProvider", function($routeProvider, $httpProvider) {

	// http 응답오류 인터셉터 (eg. 세션 타임아웃)
	$httpProvider.interceptors.push("responseObserver");

	// app.js 로딩 위치를 기본 경로로 하여 작동함
	// $locationProvider.hashPrefix("!"); // 해시 뒤에 오는 접두어(기본값: "!")을 변경할
	// 수 있음

	// trailing slash로 끝나는 경우 "index.html"를 붙여줌
	// public/, admin/ 등 최상위인 경우 index.html는 서버에서 로딩하므로 "_index.html"을
	// 붙여줌
	$routeProvider.when("/", { // == "#!/index"
		templateUrl : function(path) {
			return "_index.html";
		}
	}).when("/:name*", {
		templateUrl : function(path) {

			var pathName = path.name;

			if (/\/$/.test(path.name)) {
				path.name += "index";
			}
			return path.name + ".html";
		},
	});
} ]);
app.factory("responseObserver", [ "$q", "$window", function($q, $window) {
	return {
		"responseError" : function(response) {
			switch (response.status) {
			case 403:
				console.error("403 Forbidden.");
				$window.location = (meta && meta.baseurl) ? meta.baseurl : "/";
				break;
			}
		}
	}
} ]);
app.run(["$rootScope", "$location", "$window", function($rootScope, $location, $window) {
	$rootScope.meta = meta;

	// USAGES:
	// meta.path(): to return current path
	// meta.path("/path/string"): to check whether current path
	// is including(starting with) "/path/string"
	$rootScope.path = function() {
		// getter
		// return current path
		if (arguments.length == 0) {
			return $location.path();
		}

		// checker
		// check current path pattern
		var path;
		for (var i = 0; i < arguments.length; i++) {
			path = arguments[i];
			if (typeof path === "string") {
				if ($location.path().length >= path.length
						&& $location.path().substring(0,
								path.length) == path) {
					return true;
				}
			}
		}

		return false;
	}
	
	// Google Analytics with AngularJS
	$rootScope.$on('$viewContentLoaded', function (event) {
		$window.gtag('config', 'G-1BC0PP7F2N', {'page_path': $location.path()});
		$window.gtag('set', 'page_path', $location.path());
		$window.gtag('send', 'pageview', { page: $location.path() });
		$window.gtag('event', 'page_view');
	});
	
} ]);

app.service("httpService", function($http, $q) {
	var httpService = this;

	// request default
	var requestDefault = {
		method : "POST",
		headers : {
			"Accept" : "application/json; charset=UTF-8",
			"Content-Type" : "application/json; charset=UTF-8",
		}
	}

	if (meta._csrf_header)
		requestDefault.headers[meta._csrf_header] = meta._csrf;

	// promise repository
	// [ [promise1, timeout1], [promise2, timeout2], ... ]
	var arrayPromiseTimeout = [];

	// ----------------
	// public functions
	// ----------------
	this.run = fnRequest;
	this.request = fnRequest;

	this.post = function(request) {
		request.method = "POST";
		return fnRequest(request);
	}
	this.get = function(request) {
		request.method = "GET";
		delete request.data;
		return fnRequest(request);
	}
	this.promises = function() {
		return arrayPromiseTimeout.map(function(e) {
			return e[0];
		});
	}

	this.stop = function(promise) {
		if (!promise)
			return;
		var idx = findPromiseIndex(promise);
		if (idx >= 0) {
			arrayPromiseTimeout[idx][1].resolve();
			arrayPromiseTimeout.splice(idx, 1);
		}
	}

	this.status403 = null;

	// ----------------
	// core functions
	// ----------------

	function fnRequest(request) {
		var requestOption = angular.extend({}, requestDefault, request);

		var timeoutDefer = $q.defer();
		requestOption.timeout = timeoutDefer.promise;

		var promise = $http(requestOption);
		arrayPromiseTimeout.push([ promise, timeoutDefer ]);

		promise.then(function() {
			var idx = findPromiseIndex(promise);
			if (idx >= 0) {
				arrayPromiseTimeout.splice(idx, 1);
			}
		}, function(e) {
			console.log(e);
			if (e.status == 403) {
				if (httpService.status403) {
					httpService.status403(e);
				} else if (meta && meta.baseurl) {
					window.location.href = meta.baseurl;
				}
			}
		});

		return promise;
	}

	function findPromiseIndex(promise) {
		for (var i = 0; i < arrayPromiseTimeout.length; i++) {
			if (arrayPromiseTimeout[i][0] === promise) {
				return i;
			}
		}
		return -1;
	}

});
app.filter("appendUpDown", function() {
	return function(score) {
		score = Number(score);
		if (score > 0)
			return "▲ " + score.toFixed(2);
		else if (score < 0)
			return ("▼ " + score.toFixed(2)).replace("-", "");
		else
			return "­ " + score.toFixed(2);
	}
});
app.filter("appendPulMa", function() {
	return function(score) {
		score = Number(score);
		if (score > 0)
			return "+" + score.toFixed(2);
		else if (score < 0)
			return "-" + score.toFixed(2).replace("-", "");
		else
			return score.toFixed(2);
	}
});
app.filter('abs', function () {
	return function(val) {
		return Math.abs(val);
	}
});
app.filter('printDate', function(){
	return function(date) {
		if (typeof path === "string") 
			return date;
		return new Date(date.getTime() - (date.getTimezoneOffset() * 60000)).toISOString().split("T")[0]; // 한국기준으로는 9시간을 빼줘야함.
	}
});
app.directive('validationDoller', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attr, mCtrl) {
            function isValidation(value) {
            	var temp = value; 
            	temp += ''// 문자열로 변환
            	temp = temp.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
            	if (temp == '' || isNaN(temp)) {
            		mCtrl.$setValidity('doller', false);
            	} else {
            		mCtrl.$setValidity('doller', true);
            	}
            	return value;
            }
            mCtrl.$parsers.push(isValidation);
        }
    };
});
app.directive('googleAd', ['$timeout', function($timeout) {
		return {
			restrict: 'A',
			link: function(scope, element, attr) {
				return $timeout(function(type) {
					var adsbygoogle, html, rand;
					rand = Math.random();
					
					console.log("type ", type)
					// 인피드 텍스트
					// html = "<ins class='adsbygoogle' style='display:block' data-ad-format='fluid' data-ad-layout-key='-he-3+1f-3d+2z' data-ad-client='ca-pub-2618229544885366' data-ad-slot='2432361728'></ins>";
					// 자동
					html = "<ins class='adsbygoogle' style='display:block; text-align:center;' data-ad-layout='in-article' data-ad-format='fluid' data-ad-client='ca-pub-2618229544885366' data-ad-slot='5239198906'></ins>";
					// 카드
					// html = "<ins class='adsbygoogle' style='display:block' data-ad-client='ca-pub-2618229544885366' data-ad-slot='8633285480' data-ad-format='auto' data-full-width-responsive='true'></ins>";
					$(element).append(html);
					return (adsbygoogle = window.adsbygoogle || []).push({});
				}, 50);
			}
		};
	}
]);
app.controller("MainController", function($scope, $http, $location) {

	// Toggle the side navigation
	$("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
		$("body").toggleClass("sidebar-toggled");
		$(".sidebar").toggleClass("toggled");
		if ($(".sidebar").hasClass("toggled")) {
			$('.sidebar .collapse').collapse('hide');
		}
	});

	// Close any open menu accordions when window is resized below 768px
	$(window).resize(function() {
		if ($(window).width() < 768) {
			$('.sidebar .collapse').collapse('hide');
		}

		// Toggle the side navigation when window is resized below 480px
		if ($(window).width() < 480 && !$(".sidebar").hasClass("toggled")) {
			$("body").addClass("sidebar-toggled");
			$(".sidebar").addClass("toggled");
			$('.sidebar .collapse').collapse('hide');
		}
	});

	// Prevent the content wrapper from scrolling when the fixed side navigation
	// hovered over
	$('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
		if ($(window).width() > 768) {
			var e0 = e.originalEvent, delta = e0.wheelDelta
					|| -e0.detail;
			this.scrollTop += (delta < 0 ? 1 : -1) * 30;
			e.preventDefault();
		}
	});

	// Scroll to top button appear
	$(document).on('scroll', function() {
		var scrollDistance = $(this).scrollTop();
		if (scrollDistance > 100) {
			$('.scroll-to-top').fadeIn();
		} else {
			$('.scroll-to-top').fadeOut();
		}
	});

	// Smooth scrolling using jQuery easing
	$(document).on('click', 'a.scroll-to-top', function(e) {
		$('html, body').stop().animate({
			scrollTop : 0
		}, 1000, 'easeInOutExpo');
		e.preventDefault();
	});
	
	// 모바일 체크
	$scope.isMobile = function(){
		var mobile = (/iphone|ipad|ipod|android/i.test(navigator.userAgent.toLowerCase()));  
		if (mobile)
			return true;
		else 
			return false;
	}
	
	// 모바일에서 문의하기 화면 이동(앱 요청)
	$scope.moveContact = function(){
		AppGoAskPage.postMessage('mume');
	}
});