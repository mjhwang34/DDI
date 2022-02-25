(function (window) {
	'use strict';

    angular
        .module('app')
        .factory('httpRequestInterceptor', ['$q', '$injector', '$rootScope', function ($q, $injector, $rootScope) {
			return {
				request: function (config) {
					// api 호출시 로딩 띄운다.
                    // header 값 셋팅한다.
					var url = config.url;

					if (url.startsWith("/api/")) {
                        config.headers['locale'] = locale;
						config.withCredentials = true;
					}
					return config;
				}
			};
		}])
		.factory('httpResponseInterceptor',['$q', '$injector', '$location', '$rootScope', function ($q, $injector, $location, $rootScope) {
			return {
				responseError: function (rejection) {
					return $q.reject(rejection);
				},
				response: function (response) {
					return response;
				}
			};
		}])
}(window));  